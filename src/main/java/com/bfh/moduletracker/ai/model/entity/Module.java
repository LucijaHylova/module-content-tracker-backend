package com.bfh.moduletracker.ai.model.entity;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "MODULES")
public class Module {

    @Id
    @Column(name = "CODE")
    private String code;
//
//    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Builder.Default
//    private Set<UserModule> userModules = new HashSet<>();

    @Embedded
    private Department department;

    @Embedded
    private Program program;

    @Embedded
    private Name name;

    @Column(name = "ECTS")
    private String ects;

    @Embedded
    private ModuleType moduleType;

    @Embedded
    private Responsibility responsibility;

    @Embedded
    private ShortDescription shortDescription;

    @Embedded
    private Content content;

    @ElementCollection(fetch = jakarta.persistence.FetchType.EAGER)
    @CollectionTable(name = "SEMESTERS", joinColumns = @JoinColumn(name = "CODE"))
    private List<Integer> semesters;

    @Column(name = "MODULE_YEAR", length = 25500)
    private String year;

    @Column(name = "YEAR_OF_MODULE", length = 25500)
    private String yearOfModule;

    @Column(name = "QUERY_MODE")
    private String queryMode;

    @Embedded
    private Specialization specialization;

    @Embedded
    private TeachingForm teachingForm;

    @Embedded
    private TeachingMethod teachingMethod;

    @Embedded
    private Workload workload;


    @Column(name = "CONTACT_LESSONS")
    private ContactLessons contactLessons;

    @Embedded
    private SelfStudy selfStudy;

    @Embedded
    private Requirements requirements;

    @Embedded
    private ProofCompetence proofCompetence;

    @Column(name = "ASSESSMENT_PROPORTION", length = 25500)
    private AssessmentProportion assessmentProportion;

    @Embedded
    private Competencies competencies;

    @Column(name = "LANGUAGES", length = 25500)
    private String languages;

    @Column(name = "CREATED_AT", length = 25500)
    private String createdAt;

    @Column(name = "UPDATED_AT", length = 25500)
    private String updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Module module = (Module) o;
        return code != null && code.equals(module.code);
    }

    @Override
    public int hashCode() {
        return 31;
    }

}
