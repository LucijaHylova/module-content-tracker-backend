package com.bfh.moduletracker.ai.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.bfh.moduletracker.ai.common.mapper.GeneralMapper;
import com.bfh.moduletracker.ai.model.dto.ai.ChatResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.DepartmentContentCompetencyDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonResponseDto;
import com.bfh.moduletracker.ai.model.dto.ai.ModuleContentDto;
import com.bfh.moduletracker.ai.model.dto.ai.ProgramContentCompetencyDto;
import com.bfh.moduletracker.ai.model.dto.ai.ProgramContentShortDescriptionDto;
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.model.entity.LocalizedVisualDataEntry;
import com.bfh.moduletracker.ai.model.entity.ModuleComparison;
import com.bfh.moduletracker.ai.model.entity.ModuleContent;
import com.bfh.moduletracker.ai.model.entity.User;
import com.bfh.moduletracker.ai.model.entity.VisualDataEntry;
import com.bfh.moduletracker.ai.model.enums.ModuleContentCategory;
import com.bfh.moduletracker.ai.model.enums.ModuleTypeFilterTag;
import com.bfh.moduletracker.ai.model.enums.StudyDepartment;
import com.bfh.moduletracker.ai.model.enums.StudyProgram;
import com.bfh.moduletracker.ai.repository.UserRepository;
import com.bfh.moduletracker.ai.service.ai.AiService;
import com.bfh.moduletracker.ai.service.ai.PromptProvider;
import com.bfh.moduletracker.ai.service.department_content.DepartmentContentService;
import com.bfh.moduletracker.ai.service.department_content.DepartmentContentServiceImp;
import com.bfh.moduletracker.ai.service.module.ModuleService;
import com.bfh.moduletracker.ai.service.module_content.ModuleContentService;
import com.bfh.moduletracker.ai.service.program_content.ProgramContentServiceImp;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/ai")
@OpenAPIDefinition(info = @Info(title = "AI Service", version = "1.0"))
public class AIController {

    private static final Logger log = LoggerFactory.getLogger(AIController.class);

    private final VectorStore vectorStore;
    private final ChatClient.Builder chatClientBuilder;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.84;
    private static final int DEFAULT_TOP_K = 10;
    private final ModuleService moduleService;
    private final ModuleContentService moduleContentService;
    private final DepartmentContentService departmentContentService;
    private final ProgramContentServiceImp programContentService;
    private final AiService aiService;
    private final UserRepository userRepository;

    private final ChatMemory memory;

    public AIController(
            VectorStore vectorStore,
            ChatClient.Builder chatClientBuilder,
            ModuleService moduleService,
            ModuleContentService moduleContentService,
            DepartmentContentServiceImp departmentContentService,
            ProgramContentServiceImp programContentService, AiService aiService, UserRepository userRepository,
            ChatMemory memory

    ) {
        this.vectorStore = vectorStore;
        this.chatClientBuilder = chatClientBuilder;
        this.moduleService = moduleService;
        this.moduleContentService = moduleContentService;
        this.departmentContentService = departmentContentService;
        this.programContentService = programContentService;
        this.aiService = aiService;
        this.userRepository = userRepository;

        this.memory = memory;
    }


    @GetMapping("/chat")
    @ApiResponse(responseCode = "200", description = "Answers a student's query about study modules")
    @ApiResponse(responseCode = "400", description = "Bad request")
    public ResponseEntity<ChatResponseDto> answerUserQuery(
            @RequestParam String query,
            @RequestParam(defaultValue = "default") String sessionId
    ) {

        List<Message> history = memory.get(sessionId, 2);


        ChatClient chatClient = chatClientBuilder
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(memory),
                        new SimpleLoggerAdvisor()
                )
                .build();

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .similarityThreshold(DEFAULT_SIMILARITY_THRESHOLD)
                .topK(5)
                .build();

        List<Document> relevantDocs = this.vectorStore.similaritySearch(searchRequest);

        String context = relevantDocs.stream()
                .filter(Objects::nonNull)
                .map(Document::getFormattedContent)
                .collect(Collectors.joining("\n---\n"));


        Prompt prompt = PromptProvider.CHATBOT_PROMPT.create(
                Map.of(
                        "query", query,
                        "modules", context,
                        "history", history
                ),
                MistralAiChatOptions.builder()
                        .responseFormat(new MistralAiApi.ChatCompletionRequest.ResponseFormat("text"))
                        .build()
        );

        log.atInfo().log("Prompt: {}", prompt.getContents());

        String answer = chatClient

                .prompt(prompt)
                .call()
                .content();

        ChatResponseDto chatResponseDto = ChatResponseDto.builder()
                .query(query)
                .answer(answer)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(chatResponseDto);
    }

    @GetMapping("/findByKeywords")
    @ApiResponse(responseCode = "200", description = "Returns a list of similar modules by keywords")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<List<ModuleDto>> suggestModulesByKeywords(
            @RequestParam String query,
            @RequestParam(required = false) StudyDepartment department,
            @RequestParam(required = false) StudyProgram program) {



        boolean hasDepartment = department != null;
        boolean hasProgram = program != null;

        FilterExpressionBuilder filterExpressionBuilder = new FilterExpressionBuilder();

        Filter.Expression expression = null;

        if (query.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        var wahlmodulExpr = filterExpressionBuilder.eq("module_type", "Wahlmodul (anrechenbar)");

        if (hasDepartment && hasProgram) {
            var deptProgExpr = filterExpressionBuilder.and(
                    filterExpressionBuilder.eq("department", department.getDisplayName()),
                    filterExpressionBuilder.eq("program", program.getDisplayName())
            );

            expression = filterExpressionBuilder
                    .or(wahlmodulExpr, deptProgExpr)
                    .build();

        } else if (hasDepartment) {
            expression = filterExpressionBuilder
                    .or(
                            wahlmodulExpr,
                            filterExpressionBuilder.eq("department", department.getDisplayName())
                    )
                    .build();

        } else if (hasProgram) {
            expression = filterExpressionBuilder
                    .or(
                            wahlmodulExpr,
                            filterExpressionBuilder.eq("program", program.getDisplayName())
                    )
                    .build();
        }

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .similarityThreshold(DEFAULT_SIMILARITY_THRESHOLD)
                .topK(DEFAULT_TOP_K)
                .filterExpression(expression)
                .build();

        List<Document> docs = vectorStore.similaritySearch(searchRequest);

        log.atInfo().log("Found {} similar documents for search request: {}", docs.size(), searchRequest);

        List<ModuleDto> modules = docs.stream()
                .filter(Objects::nonNull)
                .map(doc -> {
                    String code = (String) doc.getMetadata().get("code");
                    Double score = doc.getScore();

                    List<ModuleDto> response = moduleService.getModulesByCode(code);

                    for (ModuleDto dto : response) {
                        dto.setScore(score != null ? score : 0.0);
                    }

                    return response;
                })

                .flatMap(List::stream)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(modules);
    }

    @GetMapping("/getModuleContentData")
    @ApiResponse(responseCode = "200", description = "Get visualizable Data by Module Content Category")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<Set<ModuleContentDto>> getModuleContentData(
    ) {


        ChatClient chatClient = chatClientBuilder.build();

        Set<ModuleContentDto> result = new HashSet<>();

        List<ModuleDto> modules = this.moduleService.getAllModules();

        if (modules == null || modules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        for (ModuleDto dto : modules) {

            String module = String.format("""
                            code: %s, \s
                            name: %s, \s
                            """,
                    dto.getCode(),
                    dto.getName().getDe());

            String moduleCompetencies = String.format("""
                            competencies (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getCompetencies().getDe(),
                    ModuleContentCategory.COMPETENCIES.getSubPrompt());

            String moduleRequirements = String.format("""
                            requirements (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getRequirements().getDe(),
                    ModuleContentCategory.REQUIREMENTS.getSubPrompt());

            String moduleContent = String.format("""
                            content (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getContent().getDe(),
                    ModuleContentCategory.CONTENT.getSubPrompt());

            String moduleShortDescription = String.format("""
                            short description (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getShortDescription().getDe(),
                    ModuleContentCategory.SHORT_DESCRIPTION.getSubPrompt());

            String moduleResponsibility = String.format("""
                            responsibility (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getResponsibility().getDe(),
                    ModuleContentCategory.RESPONSIBILITY.getSubPrompt());

            String moduleEcts = String.format("""
                            ects (original text to analyse): %s, \s
                            analysis instruction %s \s
                            """,
                    dto.getEcts(),
                    ModuleContentCategory.ECTS.getSubPrompt());

            Prompt prompt = PromptProvider.MODULE_CONTENT_VISUALISATION_PROMPT.create(
                    Map.of(
                            "module", module,
                            "competencies", moduleCompetencies,
                            "requirements", moduleRequirements,
                            "content", moduleContent,
                            "shortDescription", moduleShortDescription,
                            "responsibility", moduleResponsibility,
                            "ects", moduleEcts
                    )
            );

            try {
                ModuleContentDto answer = chatClient
                        .prompt(prompt)
                        .call()
                        .entity(new ParameterizedTypeReference<>() {
                        });

                assert answer != null;
                ModuleContent entity =
                        ModuleContent.builder()
                                .code(answer.getCode())
                                .name(answer.getName())
                                .competencies(GeneralMapper.mapToLocalizedVisualDataEntries(answer.getCompetencies()))
                                .requirements(GeneralMapper.mapToVisualDataEntries(answer.getRequirements()))
                                .content(GeneralMapper.mapToLocalizedVisualDataEntries(answer.getContent()))
                                .shortDescription(GeneralMapper.mapToVisualDataEntries(answer.getShortDescription()))
                                .responsibility(answer.getResponsibility())
                                .ects(answer.getEcts())
                                .selfStudy(answer.getSelfStudy())
                                .build();

                this.moduleContentService.upsertModuleContent(entity);
                result.add(answer);
                log.atInfo().log("Finished analyzing module: {}", dto.getCode());


            } catch (Exception e) {
                log.atError().setCause(e).log("Error in the prompt processing. \n Prompt-Content: {}", prompt.getContents());
            }

        }
        log.atInfo().log("Total result size: {}", result.size());
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }


    @GetMapping("/compare/analysis")
    @ApiResponse(responseCode = "200", description = "Module Comparison result returned successfully")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<List<ModuleComparisonResponseDto>> getModuleComparisonUser(
            @RequestParam Set<String> codes) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = this.userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        List<ModuleComparisonResponseDto> result = new ArrayList<>(this.aiService.compareModules(codes));

        List<ModuleComparison> moduleComparisons = result.stream()
                .map(GeneralMapper::mapToModuleComparison)
                .toList();

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        user.setModuleComparisons(moduleComparisons);
        userRepository.save(user);

        return ResponseEntity.ok(result);
    }


    @GetMapping("/getProgramContentCompetenciesData")
    @ApiResponse(responseCode = "200", description = "Get visualizable Data by Program Content Category: Competencies")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<Set<ProgramContentCompetencyDto>> getProgramContentCompetenciesData(
            // @RequestParam(required = false) ModuleTypeFilterTag moduleType
            @RequestParam(required = false) String moduleType
    ) {
        boolean hasModuleType = moduleType != null;

        ChatClient chatClient = chatClientBuilder.build();

        Set<ProgramContentCompetencyDto> result = new HashSet<>();

        List<ModuleDto> modules = this.moduleService.getAllModules();


        if (hasModuleType) {
            modules = modules.stream()
                    .filter(m -> (m.getModuleType() != null && m.getModuleType().getDe().equalsIgnoreCase(moduleType)))
                    .toList();
        }

        List<String> programs = modules.stream()
                .map(m ->
                        m.getProgram().getDe())
                .distinct()
                .toList();

        for (String program : programs) {
            List<String> programCompetencies = modules.stream()
                    .filter(m -> m.getProgram().getDe().equals(program))
                    .map(m ->
                            m.getCompetencies().getDe()
                    )
                    .toList();

            if (programCompetencies == null || programCompetencies.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String competencies = programCompetencies.stream()
                    .distinct()
                    .collect(Collectors.joining(", "));

            Prompt prompt = PromptProvider.PROGRAM_CONTENT_COMPETENCIES.create(
                    Map.of(
                            "program", program,
                            "competencies", competencies
                    )
            );

            try {
                List<ProgramContentCompetencyDto> answer = chatClient
                        .prompt(prompt)
                        .call()
                        .entity(new ParameterizedTypeReference<>() {
                        });

                if (answer != null) {
                    result.addAll(answer);
                    answer.forEach(a -> log.atInfo().log("answer: {}", a.competencies));
                }

            } catch (Exception e) {
                log.atError().setCause(e).log("Error in the prompt processing. Prompt-Content: {}", prompt.getContents());
            }

            List<LocalizedVisualDataEntry> programContent = result.stream()
                    .filter(dto -> program.equals(dto.getProgram()))
                    .flatMap(dto -> GeneralMapper.mapToLocalizedVisualDataEntries(dto.getCompetencies()).stream())
                    .toList();

            if (hasModuleType) {
                this.programContentService.upsertCompetenciesForProgramWithModuleTypeFilterOption(program, programContent, moduleType);
            } else {
                this.programContentService.upsertCompetenciesForProgram(program, programContent);
            }
            log.atInfo().log("Finished analyzing program: {}", program);
        }

        log.atInfo().log("Total result size: {}", result.size());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/getProgramContentShortDescriptionData")
    @ApiResponse(responseCode = "200", description = "Get visualizable Data by Program Content Category: Short Description")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<Set<ProgramContentShortDescriptionDto>> getProgramContentShortDescriptionData(
            @RequestParam(required = false) ModuleTypeFilterTag moduleType
    ) {
        boolean hasModuleType = moduleType != null;

        ChatClient chatClient = chatClientBuilder.build();

        Set<ProgramContentShortDescriptionDto> result = new HashSet<>();

        List<ModuleDto> modules = this.moduleService.getAllModules();

        if (hasModuleType) {
            modules = modules.stream()
                    .filter(m -> (m.getModuleType() != null && m.getModuleType().getDe().equalsIgnoreCase(moduleType.getTag())))
                    .toList();
        }

        List<String> programs = modules.stream()
                .map(m ->
                        m.getProgram().getDe())
                .distinct()
                .toList();

        for (String program : programs) {

            List<String> programShortDescription = modules.stream()
                    .filter(m -> m.getProgram().getDe().equals(program))
                    .map(m ->
                            m.getShortDescription().getDe()
                    )
                    .toList();

            if (programShortDescription == null || programShortDescription.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String shortDescription = programShortDescription.stream()
                    .distinct()
                    .collect(Collectors.joining(", "));

            Prompt prompt = PromptProvider.PROGRAM_CONTENT_SHORT_DESCRIPTION.create(
                    Map.of(
                            "program", program,
                            "shortDescription", shortDescription
                    )
            );

            try {
                List<ProgramContentShortDescriptionDto> answer = chatClient
                        .prompt(prompt)
                        .call()
                        .entity(new ParameterizedTypeReference<>() {
                        });


                if (answer != null) {
                    result.addAll(answer);
                    answer.forEach(a -> log.atInfo().log("answer: {}", a.shortDescription));
                }

            } catch (Exception e) {
                log.atError().setCause(e).log("Error in the prompt processing. Prompt-Content: {}", prompt.getContents());
            }

            List<VisualDataEntry> programContent = result.stream()
                    .filter(dto -> program.equals(dto.getProgram()))
                    .flatMap(dto -> GeneralMapper.mapToVisualDataEntries(dto.getShortDescription()).stream())
                    .toList();

            if (hasModuleType) {
                this.programContentService.upsertShortDescriptionForProgramWithModuleTypeFilterOption(program, programContent, moduleType.getTag());
            } else {
                this.programContentService.upsertShortDescriptionForProgram(program, programContent);
            }

            log.atInfo().log("Finished analyzing program: {}", program);
        }

        log.atInfo().log("Total result size: {}", result.size());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @GetMapping("/getDepartmentContentCompetenciesData")
    @ApiResponse(responseCode = "200", description = "Get visualizable Data by Department Content Category: Competencies")
    @ApiResponse(responseCode = "404", description = "No modules found")
    public ResponseEntity<Set<DepartmentContentCompetencyDto>> getDepartmentContentCompetenciesData() {

        ChatClient chatClient = chatClientBuilder.build();

        Set<DepartmentContentCompetencyDto> result = new HashSet<>();

        List<ModuleDto> modules = this.moduleService.getAllModules();

        List<String> departments = modules.stream()
                .map(m ->
                        m.getDepartment().getDe())
                .distinct()
                .toList();


        for (String department : departments) {

            List<String> departmentCompetencies = modules.stream()
                    .filter(m -> m.getDepartment().getDe().equals(department))
                    .map(m ->
                            m.getCompetencies().getDe()
                    )
                    .toList();

            if (departmentCompetencies == null || departmentCompetencies.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            String competencies = departmentCompetencies.stream()
                    .distinct()
                    .collect(Collectors.joining(", "));

            Prompt prompt = PromptProvider.DEPARTMENT_CONTENT_COMPETENCIES.create(
                    Map.of(
                            "department", department,
                            "competencies", competencies
                    )
            );

            try {
                List<DepartmentContentCompetencyDto> answer = chatClient
                        .prompt(prompt)
                        .call()
                        .entity(new ParameterizedTypeReference<>() {
                        });


                if (answer != null) {
                    result.addAll(answer);
                    answer.forEach(a -> log.atInfo().log("answer: {}", a.competencies));
                }


            } catch (Exception e) {
                log.atError().setCause(e).log("Error in the prompt processing. Prompt-Content: {}", prompt.getContents());
            }
            List<LocalizedVisualDataEntry> departmentContent = result.stream()
                    .filter(dto -> department.equals(dto.getDepartment()))
                    .flatMap(dto -> GeneralMapper.mapToLocalizedVisualDataEntries(dto.getCompetencies()).stream())
                    .toList();

            this.departmentContentService.upsertCompetenciesForDepartment(department, departmentContent);
            log.atInfo().log("Finished analyzing department: {}", department);
        }

        log.atInfo().log("Total result size: {}", result.size());


        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}


