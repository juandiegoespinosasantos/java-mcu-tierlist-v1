package com.mcutierlist.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "score_labels")
public class ScoreLabel {

    @Id
    private BigDecimal score;

    @Column(nullable = false)
    private String description;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    public BigDecimal getScore() { return score; }
    public void setScore(BigDecimal score) { this.score = score; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
