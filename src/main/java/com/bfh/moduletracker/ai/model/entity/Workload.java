package com.bfh.moduletracker.ai.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Workload {

    @Column(name = "workload_de")
    String workloadDe;

    @Column(name = "workload_en")
    String workloadEn;

    @Column(name = "workload_fr")
    String workloadFr;
}
