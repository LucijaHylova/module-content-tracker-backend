package com.bfh.moduletracker.ai.service.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bfh.moduletracker.ai.model.dto.module.ModuleDto;
import com.bfh.moduletracker.ai.service.module.ModuleService;
import io.weaviate.client.WeaviateClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.BatchingStrategy;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

@Component
public class VectorStoreLoad {

    private final VectorStore vectorStore;
    private final ModuleService moduleService;
    private final WeaviateClient weaviateClient;
    private final BatchingStrategy batchingStrategy;
    private static final Logger log = LoggerFactory.getLogger(VectorStoreLoad.class);

    public VectorStoreLoad(VectorStore vectorStore, ModuleService moduleService, WeaviateClient weaviateClient, BatchingStrategy batchingStrategy) {
        this.vectorStore = vectorStore;
        this.moduleService = moduleService;
        this.weaviateClient = weaviateClient;
        this.batchingStrategy = batchingStrategy;
    }


    public void run() {

        weaviateClient.schema().classDeleter()
                .withClassName("Module")
                .run();

        List<Document> docs = new ArrayList<>();

        List<ModuleDto> modules = moduleService.getAllModules()
                .stream()
                .toList();

        for (ModuleDto module : modules) {
            String content =
                    "SECTION CODE: " + module.getCode() + " "
                            + "SECTION NAME: " + module.getName().getDe() + " "
                            + "SECTION DEPARTMENT: " + module.getDepartment().getDe() + " "
                            + "SECTION PROGRAM: " + module.getProgram().getDe() + " "
                            + "SECTION COMPETENCIES: " + module.getCompetencies().getDe() + " "
                            + "SECTION REQUIREMENTS: " + module.getRequirements().getDe() + " "
                            + "SECTION CONTENT: " + module.getContent().getDe() + " "
                            + "SECTION SHORT DESCRIPTION: " + module.getShortDescription().getDe() + " "
                            + "SECTION RESPONSIBILITY: " + module.getResponsibility().getDe() + " "
                            + "SECTION ECTS: " + module.getEcts() + " "
                            + "SECTION SELF_STUDY: " + module.getSelfStudy().getDe() + " "
                            + "SECTION MODULE_TYPE: " + module.getModuleType().getDe() + " ";

            Map<String, Object> metadata = Map.of(
                    "code", module.getCode(),
                    "name", module.getName().getDe(),
                    "department", module.getDepartment().getDe(),
                    "program", module.getProgram().getDe(),
                    "module_type", module.getModuleType().getDe()
            );

            var doc = Document.builder()
                    .text(content)
                    .metadata(metadata)
                    .build();

            docs.add(doc);
            log.atInfo().log("Document ready to be add: {}", module.getCode());
        }

        List<List<Document>> batches = batchingStrategy.batch(docs);

        for (List<Document> batch : batches) {
            vectorStore.add(batch);
            log.atInfo().log("Finished batch. Documents added: {}", batch.size());
        }

        log.atInfo().log("Vector store with {} modules loaded.", modules.size());
    }
}
