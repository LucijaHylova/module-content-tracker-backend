package com.bfh.moduletracker.ai.model.enums;

import lombok.Getter;


@Getter
public enum Status {
    PASSED("Bestanden"),
    FAILED("Nicht bestanden"),
    PLANNED("Geplant"),
    RETAKE_IN_PROGRESS("Wiederholung läuft"),
    IN_PROGRESS("In Bearbeitung"),
    NONE("Keine Angabe");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
