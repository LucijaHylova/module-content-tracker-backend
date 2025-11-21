package com.bfh.moduletracker.ai.common.converter;

import com.bfh.moduletracker.ai.model.enums.StudyDepartment;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudyDepartmentConverter implements Converter<String, StudyDepartment> {
    @Override
    public StudyDepartment convert(String source) {
        return StudyDepartment.valueOf(source.toUpperCase().replace(" ", "_"));
    }
}
