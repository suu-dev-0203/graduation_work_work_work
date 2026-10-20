package com.example.entity; // 💡正しいentityの住所に修正

import jakarta.persistence.*;

@Entity
@Table(name = "stage_definitions")
public class StageDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stageDefinitionId;

    public Long getStageDefinitionId() { return stageDefinitionId; }
    public void setStageDefinitionId(Long stageDefinitionId) { this.stageDefinitionId = stageDefinitionId; }
}
