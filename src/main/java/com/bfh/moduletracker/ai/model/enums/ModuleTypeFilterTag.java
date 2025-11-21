package com.bfh.moduletracker.ai.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Module type filter options")
public enum ModuleTypeFilterTag {
    PFLICHTMODUL("Pflichtmodul"),
    WAHLMODUL("Wahlmodul (anrechenbar)"),
    WAHLPFLICHTMODUL("Wahlpflichtmodul");

    private final String tag;

    ModuleTypeFilterTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return getTag();
    }
}

