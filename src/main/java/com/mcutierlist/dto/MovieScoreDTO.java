package com.mcutierlist.dto;

import com.mcutierlist.model.entities.MCUEntry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO joining a movie with the current user's score, label, tier and ranking.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieScoreDTO implements Serializable {

    private static final long serialVersionUID = 4816273950162738495L;

    private MCUEntry movie;

    private BigDecimal score;

    private String scoreLabel;

    private String tier;

    private Integer ranking;
}
