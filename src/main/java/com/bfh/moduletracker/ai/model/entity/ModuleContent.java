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
@Table(name = "MODULE_CONTENTS")
public class ModuleContent {

    @Id
    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_CONTENT_COMPETENCIES", joinColumns = @JoinColumn(name = "CODE"))
    private List<LocalizedVisualDataEntry> competencies;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_CONTENT_REQUIREMENTS", joinColumns = @JoinColumn(name = "CODE"))
    private List<VisualDataEntry> requirements;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_CONTENT_CONTENT", joinColumns = @JoinColumn(name = "CODE"))
    private List<LocalizedVisualDataEntry> content;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_CONTENT_SHORT_DESCRIPTION", joinColumns = @JoinColumn(name = "CODE"))
    private List<VisualDataEntry> shortDescription;


    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "MODULE_CONTENT_RESPONSIBILITY", joinColumns = @JoinColumn(name = "CODE"))
    private List<String> responsibility;

    @Column(name = "ECTS")
    private String ects;

    @Column(name = "SELF_STUDY")
    private String selfStudy;

}
