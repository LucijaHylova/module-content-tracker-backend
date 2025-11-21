package com.bfh.moduletracker.ai.common.converter;


import com.bfh.moduletracker.ai.model.enums.StudyProgram;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StudyProgramConverter implements Converter<String, StudyProgram> {
    @Override
    public StudyProgram convert(String source) {
        return StudyProgram.valueOf(source.toUpperCase().replace(" ", "_"));
    }
}
