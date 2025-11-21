package com.bfh.moduletracker.ai.service.ai;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bfh.moduletracker.ai.model.dto.ai.ModuleComparisonResponseDto;
import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.service.module.ModuleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AiService {

    private final ModuleService moduleService;
    private final ChatClient.Builder chatClientBuilder;
    private static final Logger log = LoggerFactory.getLogger(AiService.class);


    @Transactional
    public List<ModuleComparisonResponseDto> compareModules(Set<String> codes) {

        ChatClient chatClient = chatClientBuilder.build();

        List<ModuleDto> modules = moduleService.getAllModules().stream()
                .filter(m -> codes.contains(m.getCode()))
                .toList();

        if (modules.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder moduleContents = new StringBuilder();
        for (ModuleDto dto : modules) {
            moduleContents.append(String.format("""
                            Module Number: %s
                            -------------------------------
                            code: %s,
                            name: %s,
                            competencies: %s,
                            requirements: %s,
                            content: %s,
                            short description: %s,
                            --------------------------
                            """,
                    modules.indexOf(dto) + 1,
                    dto.getCode(),
                    dto.getName().getDe(),
                    dto.getCompetencies().getDe(),
                    dto.getRequirements().getDe(),
                    dto.getContent().getDe(),
                    dto.getShortDescription().getDe()));
        }

        Prompt prompt = PromptProvider.MODULE_COMPARISON_ANALYSIS_PROMPT.create(
                Map.of("moduleContents", moduleContents.toString())
        );

        try {
            List<ModuleComparisonResponseDto> response = chatClient
                    .prompt(prompt)
                    .call()
                    .entity(new ParameterizedTypeReference<List<ModuleComparisonResponseDto>>() {
                    });

            log.info("Finished analyzing module comparison.");
            return response;

        } catch (Exception e) {
            log.error("Error in AI module comparison. Prompt content:\n{}", prompt.getContents(), e);
            throw new RuntimeException("AI comparison failed.", e);
        }
    }
}
