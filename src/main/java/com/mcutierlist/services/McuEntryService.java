package com.mcutierlist.services;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.User;
import com.mcutierlist.model.enums.Scores;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.UserRepository;
import com.mcutierlist.utils.adapters.McuEntryAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation for {@link MCUEntry} business logic service.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Service
@Transactional
@Slf4j
public class McuEntryService implements IMcuEntryService {

    private final MCUEntryRepository mcuEntryRepository;
    private final UserRepository userRepository;
    private final MovieScoreXUserRepository movieScoreXUserRepository;

    public McuEntryService(MCUEntryRepository mcuEntryRepository,
                           UserRepository userRepository,
                           MovieScoreXUserRepository movieScoreXUserRepository) {
        this.mcuEntryRepository = mcuEntryRepository;
        this.userRepository = userRepository;
        this.movieScoreXUserRepository = movieScoreXUserRepository;
    }

    /**
     * Returns a map of movies grouped by their phase for the given user.
     *
     * @param username Requesting username.
     * @return Map of user scored {@link McuEntryScoreDTO}, grouped by phase.
     */
    @Override
    public Map<Integer, List<McuEntryScoreDTO>> getMoviesByPhase(final String username) {
        return getMoviesWithScores(username)
                .stream()
                .collect(Collectors.groupingBy(dto -> dto.mcuEntryPhase(), LinkedHashMap::new, Collectors.toList()));
    }

    /**
     * Returns a map of score labels for display purposes.
     *
     * @return Map of score labels for display.
     */
    @Override
    public Map<String, String> getScoreLabelsForDisplay() {
        Map<String, String> map = new LinkedHashMap<>();

        for (int half = 1; half <= 10; half++) {
            Double score = half * 0.5;
            map.put(String.format(Locale.US, "%.1f", score), Scores.valueOfRange(score).getDisplayName());
        }

        return map;
    }

    /**
     * Updates the score for a movie by the given user.
     *
     * @param username Requesting username.
     * @param movieId  Movie ID to update score.
     * @param newScore New score value to set.
     * @throws IllegalArgumentException If the new score is invalid.
     * @throws NoSuchElementException   If the user or movie is not found.
     */
    @Override
    public void updateScore(final String username, final Long movieId, final Double newScore)
            throws IllegalArgumentException, NoSuchElementException {
        validate(newScore);

        MovieScoreXUser userMovieScore = getMovieScoreXUser(username, movieId);
        userMovieScore.setScore(newScore);
        userMovieScore.setUpdatedAt(ZonedDateTime.now());
        movieScoreXUserRepository.save(userMovieScore);
    }

    private List<McuEntryScoreDTO> getMoviesWithScores(final String username) {
        Map<Long, MovieScoreXUser> userScoresByMovieId = movieScoreXUserRepository.findByUserUsername(username)
                .stream()
                .collect(Collectors.toMap(msu -> msu.getMcuEntryId(), ums -> ums));

        return mcuEntryRepository.findAllByOrderByReleaseDateAsc()
                .stream()
                .map(movie -> McuEntryAdapter.transform(movie, userScoresByMovieId))
                .collect(Collectors.toList());
    }

    private void validate(final Double newScore) throws IllegalArgumentException {
        try {
            Scores.valueOfRange(newScore);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw ex;
        }
    }

    private MovieScoreXUser getMovieScoreXUser(final String username, final Long movieId) throws NoSuchElementException {
        Optional<MovieScoreXUser> pivot = movieScoreXUserRepository.findByUserUsernameAndMcuEntry_Id(username, movieId);
        if (pivot.isPresent()) return pivot.get();

        User user = userRepository.findById(username).orElseThrow();
        MCUEntry mcuEntry = mcuEntryRepository.findById(movieId).orElseThrow();

        return MovieScoreXUser.builder()
                .user(user)
                .mcuEntry(mcuEntry)
                .createdAt(ZonedDateTime.now())
                .build();
    }
}