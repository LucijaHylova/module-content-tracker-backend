package com.bfh.moduletracker.ai.model.enums;

import lombok.Getter;

@Getter
public enum StudyProgram {
    BSC_INFORMATIK("BSc Informatik"),
    DIGITAL_BUSINESS("Digital Business"),
    WIRTSCHAFTSINFORMATIK("Wirtschaftsinformatik");

    private final String displayName;

    StudyProgram(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
