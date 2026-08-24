package com.mcutierlist.services;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.ScoreRepository;
import com.mcutierlist.model.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Unit tests for {@link McuEntryService}.
 *
 * @author juandiegoespinosasantos@outlook.com
 * @version Aug 23, 2026
 * @since 25
 */
@ExtendWith(MockitoExtension.class)
class McuEntryServiceTest {

    private static final String USERNAME = "tonystark";

    @Mock
    private MCUEntryRepository mockMcuEntryRepository;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private ScoreRepository mockScoreRepository;

    @Mock
    private MovieScoreXUserRepository mockMovieScoreXUserRepository;

    private McuEntryService service;

    @BeforeEach
    void setUp() {
        service = new McuEntryService(mockMcuEntryRepository, mockUserRepository, mockScoreRepository,
                mockMovieScoreXUserRepository);
    }

    @Test
    @DisplayName("given username with scored movies, when get movies by phase, then return map of movies grouped by phase")
    void givenUsernameWithScoredMovies_whenGetMoviesByPhase_thenReturnMapOfMoviesGroupedByPhase() {
        // given
        MCUEntry movie1 = buildMovie(1L, "The Avengers", 1);
        MCUEntry movie2 = buildMovie(2L, "Avengers: Age of Ultron", 2);
        Mockito.when(mockMcuEntryRepository.findAllByOrderByReleaseDateAsc()).thenReturn(List.of(movie1, movie2));

        Score score = new Score(5.0, "Top", "Top");
        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie1)
                .score(score)
                .ranking(1)
                .build();
        Mockito.when(mockMovieScoreXUserRepository.findByUserUsername(USERNAME)).thenReturn(List.of(userScore));

        // when
        Map<Integer, List<McuEntryScoreDTO>> actualMap = service.getMoviesByPhase(USERNAME);

        // then
        Assertions.assertNotNull(actualMap);
        Assertions.assertFalse(actualMap.isEmpty());
        Assertions.assertEquals(2, actualMap.size());

        List<McuEntryScoreDTO> actual1Movies = actualMap.get(movie1.getPhase());
        Assertions.assertNotNull(actual1Movies);
        Assertions.assertFalse(actual1Movies.isEmpty());
        Assertions.assertEquals(1, actual1Movies.size());

        McuEntryScoreDTO actual1 = actual1Movies.get(0);
        Assertions.assertNotNull(actual1);
        Assertions.assertEquals(movie1.getId(), actual1.mcuEntryId());
        Assertions.assertNotNull(actual1.score());

        List<McuEntryScoreDTO> actual2Movies = actualMap.get(movie2.getPhase());
        Assertions.assertNotNull(actual2Movies);
        Assertions.assertFalse(actual2Movies.isEmpty());
        Assertions.assertEquals(1, actual2Movies.size());

        McuEntryScoreDTO actual2 = actual2Movies.get(0);
        Assertions.assertNotNull(actual2);
        Assertions.assertEquals(movie2.getId(), actual2.mcuEntryId());
        Assertions.assertNull(actual2.score());

        Mockito.verify(mockMcuEntryRepository).findAllByOrderByReleaseDateAsc();
        Mockito.verify(mockMovieScoreXUserRepository).findByUserUsername(USERNAME);
    }

    @Test
    @DisplayName("given username without scored movies, when get movies by phase, then return empty map")
    void givenUsernameWithoutScoredMovies_whenGetMoviesByPhase_thenReturnEmptyMap() {
        // given
        Mockito.when(mockMcuEntryRepository.findAllByOrderByReleaseDateAsc()).thenReturn(Collections.emptyList());
        Mockito.when(mockMovieScoreXUserRepository.findByUserUsername(USERNAME)).thenReturn(Collections.emptyList());

        // when
        Map<Integer, List<McuEntryScoreDTO>> actualMap = service.getMoviesByPhase(USERNAME);

        // then
        Assertions.assertNotNull(actualMap);
        Assertions.assertTrue(actualMap.isEmpty());

        Mockito.verify(mockMcuEntryRepository).findAllByOrderByReleaseDateAsc();
        Mockito.verify(mockMovieScoreXUserRepository).findByUserUsername(USERNAME);
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