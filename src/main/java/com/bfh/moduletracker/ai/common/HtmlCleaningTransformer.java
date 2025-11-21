package com.bfh.moduletracker.ai.common;

import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HtmlCleaningTransformer {

    public static void clean(List<Document> documents) {

        for (Document document : documents) {
            Map<String, Object> raw = document.getMetadata();

            raw.forEach((key, value) -> {

                if (value instanceof String rawValue) {
                    if (rawValue.contains("<") && (rawValue.contains(">") || rawValue.contains("/>"))) {
                        String cleaned = rawValue.replaceAll("<.+?>", " ");
                        document.getMetadata().put(key, cleaned);
                    }
                }
            });
        }
    }
}

