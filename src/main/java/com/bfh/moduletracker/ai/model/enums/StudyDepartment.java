package com.bfh.moduletracker.ai.model.enums;

import lombok.Getter;

@Getter
public enum StudyDepartment {
    TECHNIK_UND_INFORMATIK("Technik und Informatik"),
    WIRTSCHAFT_UND_MANAGEMENT("Wirtschaft und Management");

    private final String displayName;

    StudyDepartment(String displayName) {
        this.displayName = displayName;
    }


    @Override
    public String toString() {
        return displayName;
    }
}
