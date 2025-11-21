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
@Table(name = "DEPARTMENT_CONTENTS")
public class DepartmentContent {

    @Id
    @Column(name = "DEPARTMENT")
    private String department;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "DEPARTMENT_CONTENT_COMPETENCIES", joinColumns = @JoinColumn(name = "DEPARTMENT"))
    private List<LocalizedVisualDataEntry> competencies;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "DEPARTMENT_CONTENT_REQUIREMENTS", joinColumns = @JoinColumn(name = "DEPARTMENT"))
    private List<VisualDataEntry> requirements;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "DEPARTMENT_CONTENT_CONTENT", joinColumns = @JoinColumn(name = "DEPARTMENT"))
    private List<VisualDataEntry> content;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "DEPARMTENT_CONTENT_SHORT_DESCRIPTION", joinColumns = @JoinColumn(name = "DEPARTMENT"))
    private List<VisualDataEntry> shortDescription;

    @Column(name = "RESPONSIBILITY")
    private String responsibility;
}
