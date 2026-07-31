package com.mcutierlist.dto;

import com.mcutierlist.entity.Movie;
import java.math.BigDecimal;

public record MovieScoreDTO(
    Movie movie,
    BigDecimal score,
    String scoreLabel,
    String tier,
    Integer ranking
) {}
