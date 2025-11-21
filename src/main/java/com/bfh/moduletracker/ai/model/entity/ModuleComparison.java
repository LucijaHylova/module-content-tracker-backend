package com.bfh.moduletracker.ai.model.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModuleComparison {

    private String code;

    private Name name;

    public void setDifferences_de(List<String> l) {
        this.differences_de = (l == null ? new ArrayList<>() : new ArrayList<>(l));
    }

    public void setDifferences_en(List<String> l) {
        this.differences_en = (l == null ? new ArrayList<>() : new ArrayList<>(l));
    }

    public void setSimilarities_de(List<String> l) {
        this.similarities_de = (l == null ? new ArrayList<>() : new ArrayList<>(l));
    }

    public void setSimilarities_en(List<String> l) {
        this.similarities_en = (l == null ? new ArrayList<>() : new ArrayList<>(l));
    }

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> differences_de = new ArrayList<>();

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> differences_en = new ArrayList<>();

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> similarities_de = new ArrayList<>();

    @Builder.Default
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> similarities_en = new ArrayList<>();

}
