package com.mcutierlist.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Movie scores values.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 23, 2026
 * @since 17
 */
@AllArgsConstructor
@Getter
public enum Scores {

    TOP("Top", 4.5, 5.0),
    VERY_GOOD("Muy buena", 4.0, 4.4),
    GOOD("Buena", 3.0, 3.9),
    WEAK("Floja", 2.0, 2.9),
    BAD("Mala", 0.0, 1.9);

    private final String displayName;
    private final double minRange;
    private final double maxRange;

    public static Scores valueOfRange(Double range) throws IllegalArgumentException {
        for (Scores score : values()) {
            if ((score.getMinRange() <= range) && (range <= score.getMaxRange())) return score;
        }

        throw new IllegalArgumentException("No score found for range: " + range);
    }
}