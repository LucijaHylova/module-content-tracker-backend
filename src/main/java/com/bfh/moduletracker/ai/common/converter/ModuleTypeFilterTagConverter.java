package com.bfh.moduletracker.ai.common.converter;

import com.bfh.moduletracker.ai.model.enums.ModuleTypeFilterTag;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ModuleTypeFilterTagConverter implements Converter<String, ModuleTypeFilterTag> {
    @Override
    public ModuleTypeFilterTag convert(String source) {
        return ModuleTypeFilterTag.valueOf(source.toUpperCase());
    }
}
