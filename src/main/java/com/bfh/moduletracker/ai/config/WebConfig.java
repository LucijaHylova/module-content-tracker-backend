package com.bfh.moduletracker.ai.config;

import com.bfh.moduletracker.ai.common.converter.ModuleTypeFilterTagConverter;
import com.bfh.moduletracker.ai.common.converter.StudyDepartmentConverter;
import com.bfh.moduletracker.ai.common.converter.StudyProgramConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.filter.ForwardedHeaderFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private StudyProgramConverter studyProgramConverter;

    @Autowired
    private ModuleTypeFilterTagConverter moduleTypeFilterTagConverter;

    @Autowired
    private StudyDepartmentConverter studyDepartmentConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(studyProgramConverter);
        registry.addConverter(moduleTypeFilterTagConverter);
        registry.addConverter(studyDepartmentConverter);
    }
//
//    @Bean
//    public ForwardedHeaderFilter forwardedHeaderFilter() {
//        return new ForwardedHeaderFilter();
//    }
}
