package com.bfh.moduletracker.ai.model.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "PROGRAM_CONTENTS")
public class ProgramContent {

    @Id
    @Column(name = "PROGRAM")
    private String program;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_CONTENT_COMPETENCIES", joinColumns = @JoinColumn(name = "PROGRAM"))
    private List<LocalizedVisualDataEntry> competencies;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_CONTENT_REQUIREMENTS", joinColumns = @JoinColumn(name = "PROGRAM"))
    private List<VisualDataEntry> requirements;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_CONTENT_CONTENT", joinColumns = @JoinColumn(name = "PROGRAM"))
    private List<VisualDataEntry> content;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "PROGRAM_CONTENT_SHORT_DESCRIPTION", joinColumns = @JoinColumn(name = "PROGRAM"))
    private List<VisualDataEntry> shortDescription;

    @Column(name = "RESPONSIBILITY")
    private String responsibility;
}
