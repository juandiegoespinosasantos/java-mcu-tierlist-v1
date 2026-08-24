package com.mcutierlist.services;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.User;
import com.mcutierlist.model.enums.Scores;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

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
    private MovieScoreXUserRepository mockMovieScoreXUserRepository;

    private McuEntryService service;

    @BeforeEach
    void setUp() {
        service = new McuEntryService(mockMcuEntryRepository, mockUserRepository, mockMovieScoreXUserRepository);
    }

    @Test
    @DisplayName("given username with scored movies, when get movies by phase, then return map of movies grouped by phase")
    void givenUsernameWithScoredMovies_whenGetMoviesByPhase_thenReturnMapOfMoviesGroupedByPhase() {
        // given
        MCUEntry movie1 = buildMovie(1L, "The Avengers", 1);
        MCUEntry movie2 = buildMovie(2L, "Avengers: Age of Ultron", 2);
        Mockito.when(mockMcuEntryRepository.findAllByOrderByReleaseDateAsc()).thenReturn(List.of(movie1, movie2));

        MovieScoreXUser userScore = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie1)
                .score(Scores.TOP.getMaxRange())
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

    @Test
    @DisplayName("given the half-point score range, when get score labels for display, then return map of score value to tier display name")
    void givenHalfPointScoreRange_whenGetScoreLabelsForDisplay_thenReturnMapOfScoreValueToTierDisplayName() {
        // when
        Map<String, String> actualLabels = service.getScoreLabelsForDisplay();

        // then
        Assertions.assertNotNull(actualLabels);
        Assertions.assertEquals(10, actualLabels.size());

        for (int half = 1; half <= 10; half++) {
            Double score = half * 0.5;
            String key = String.format(Locale.US, "%.1f", score);
            Assertions.assertEquals(Scores.valueOfRange(score).getDisplayName(), actualLabels.get(key));
        }
    }

    @Test
    @DisplayName("given no existing entry and a valid score, when update score, then create and save a new entry")
    void givenNoExistingEntryAndValidScore_whenUpdateScore_thenCreateAndSaveNewEntry() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        User user = new User(USERNAME, "Tony Stark");
        Double newScore = Scores.TOP.getMaxRange();

        Mockito.when(mockMovieScoreXUserRepository.findByUserUsernameAndMcuEntry_Id(USERNAME, 1L)).thenReturn(Optional.empty());
        Mockito.when(mockUserRepository.findById(USERNAME)).thenReturn(Optional.of(user));
        Mockito.when(mockMcuEntryRepository.findById(1L)).thenReturn(Optional.of(movie));

        // when
        service.updateScore(USERNAME, 1L, newScore);

        // then
        ArgumentCaptor<MovieScoreXUser> captor = ArgumentCaptor.forClass(MovieScoreXUser.class);
        Mockito.verify(mockMovieScoreXUserRepository).save(captor.capture());

        MovieScoreXUser saved = captor.getValue();
        Assertions.assertEquals(user, saved.getUser());
        Assertions.assertEquals(movie, saved.getMcuEntry());
        Assertions.assertEquals(newScore, saved.getScore());
        Assertions.assertNotNull(saved.getUpdatedAt());
    }

    @Test
    @DisplayName("given an existing entry and a valid score, when update score, then update and save the existing entry")
    void givenExistingEntryAndValidScore_whenUpdateScore_thenUpdateAndSaveExistingEntry() {
        // given
        MCUEntry movie = buildMovie(1L, "Iron Man", 1);
        MovieScoreXUser existing = MovieScoreXUser.builder()
                .id(10L)
                .mcuEntry(movie)
                .score(Scores.WEAK.getMinRange())
                .ranking(3)
                .createdAt(ZonedDateTime.now())
                .build();
        Double newScore = Scores.TOP.getMaxRange();

        Mockito.when(mockMovieScoreXUserRepository.findByUserUsernameAndMcuEntry_Id(USERNAME, 1L)).thenReturn(Optional.of(existing));

        // when
        service.updateScore(USERNAME, 1L, newScore);

        // then
        ArgumentCaptor<MovieScoreXUser> captor = ArgumentCaptor.forClass(MovieScoreXUser.class);
        Mockito.verify(mockMovieScoreXUserRepository).save(captor.capture());

        MovieScoreXUser saved = captor.getValue();
        Assertions.assertEquals(10L, saved.getId());
        Assertions.assertEquals(newScore, saved.getScore());
        Assertions.assertEquals(3, saved.getRanking());
        Assertions.assertNotNull(saved.getUpdatedAt());

        Mockito.verifyNoInteractions(mockUserRepository, mockMcuEntryRepository);
    }

    @Test
    @DisplayName("given a score outside every defined tier, when update score, then throw IllegalArgumentException and save nothing")
    void givenScoreOutsideEveryTier_whenUpdateScore_thenThrowIllegalArgumentExceptionAndSaveNothing() {
        // given, when & then
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.updateScore(USERNAME, 1L, -1.0));

        Mockito.verifyNoInteractions(mockMovieScoreXUserRepository, mockUserRepository, mockMcuEntryRepository);
    }

    @Test
    @DisplayName("given no existing entry and an unknown user, when update score, then throw NoSuchElementException and save nothing")
    void givenNoExistingEntryAndUnknownUser_whenUpdateScore_thenThrowNoSuchElementExceptionAndSaveNothing() {
        // given
        Mockito.when(mockMovieScoreXUserRepository.findByUserUsernameAndMcuEntry_Id(USERNAME, 1L)).thenReturn(Optional.empty());
        Mockito.when(mockUserRepository.findById(USERNAME)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(NoSuchElementException.class, () -> service.updateScore(USERNAME, 1L, Scores.TOP.getMaxRange()));

        Mockito.verify(mockMovieScoreXUserRepository, Mockito.never()).save(Mockito.any(MovieScoreXUser.class));
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