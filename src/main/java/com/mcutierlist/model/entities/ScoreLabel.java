package com.mcutierlist.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity for ScoreLabel table.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "score_labels")
public class ScoreLabel implements Serializable {

    private static final long serialVersionUID = 1928374650192837465L;

    @Id
    private BigDecimal score;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "display_name", nullable = false)
    private String displayName;
}
