package com.mcutierlist.utils.adapters;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Map;

/**
 * Unit tests for {@link McuEntryAdapter}.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 23, 2026
 * @since 25
 */
class McuEntryAdapterTest {

    @Test
    @DisplayName("given a movie without a recorded user score, when transform, then return a DTO with null score and ranking")
    void givenMovieWithoutRecordedScore_whenTransform_thenReturnDtoWithNullScoreAndRanking() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        Map<Long, MovieScoreXUser> scoredMoviesById = Collections.emptyMap();

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.mcuEntry());
        Assertions.assertEquals(movie.getId(), actual.mcuEntry().id());
        Assertions.assertEquals(movie.getOriginalTitle(), actual.mcuEntry().originalTitle());
        Assertions.assertEquals(movie.getPhase(), actual.mcuEntry().phase());
        Assertions.assertNull(actual.score());
        Assertions.assertNull(actual.ranking());
    }

    @Test
    @DisplayName("given a movie with a recorded user score, when transform, then return a DTO with the mapped score and ranking")
    void givenMovieWithRecordedScore_whenTransform_thenReturnDtoWithMappedScoreAndRanking() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        Score score = new Score(4.5, "Excellent", "Excellent!");
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(score)
                .ranking(2)
                .build();
        Map<Long, MovieScoreXUser> scoredMoviesById = Map.of(movie.getId(), userScore);

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(movie.getId(), actual.mcuEntry().id());
        Assertions.assertNotNull(actual.score());
        Assertions.assertEquals(score.getScore(), actual.score().score());
        Assertions.assertEquals(score.getDescription(), actual.score().description());
        Assertions.assertEquals(score.getDisplayName(), actual.score().displayName());
        Assertions.assertEquals(2, actual.ranking());
    }

    @Test
    @DisplayName("given a movie with a recorded entry but no score, when transform, then return a DTO with null score")
    void givenMovieWithRecordedEntryButNoScore_whenTransform_thenReturnDtoWithNullScore() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(null)
                .ranking(null)
                .build();
        Map<Long, MovieScoreXUser> scoredMoviesById = Map.of(movie.getId(), userScore);

        // when
        McuEntryScoreDTO actual = McuEntryAdapter.transform(movie, scoredMoviesById);

        // then
        Assertions.assertNotNull(actual);
        Assertions.assertNull(actual.score());
        Assertions.assertNull(actual.ranking());
    }

    @ParameterizedTest(name = "given a score of {0}, when resolveTier, then return \"{1}\"")
    @CsvSource({
            "5.0, Excellent",
            "4.5, Excellent",
            "4.4, Very Good",
            "4.0, Very Good",
            "3.9, Good",
            "3.0, Good",
            "2.9, Weak",
            "2.0, Weak",
            "1.9, Bad",
            "0.5, Bad"
    })
    @DisplayName("resolveTier maps a score to its tier")
    void givenScore_whenResolveTier_thenReturnMatchingTier(final Double score, final String expectedTier) {
        // when
        String actualTier = McuEntryAdapter.resolveTier(score);

        // then
        Assertions.assertEquals(expectedTier, actualTier);
    }

    private MCUEntry buildMovie(Long id, String title, Integer phase) {
        return MCUEntry.builder()
                .id(id)
                .originalTitle(title)
                .phase(phase)
                .releaseDate(ZonedDateTime.now())
                .createdAt(ZonedDateTime.now())
                .updatedAt(ZonedDateTime.now())
                .build();
    }
}
