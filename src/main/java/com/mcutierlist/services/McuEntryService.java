package com.mcutierlist.services;

import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import com.mcutierlist.model.enums.Scores;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.ScoreRepository;
import com.mcutierlist.model.repositories.UserRepository;
import com.mcutierlist.utils.adapters.McuEntryAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation for {@link MCUEntry} business logic service. *
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Service
@Transactional
public class McuEntryService implements IMcuEntryService {

    private static final Map<String, Double[]> TIER_RANGES = Map.of(
            "Excellent", new Double[]{4.5, 5.0},
            "Very Good", new Double[]{4.0, 4.4},
            "Good", new Double[]{3.0, 3.9},
            "Weak", new Double[]{2.0, 2.9},
            "Bad", new Double[]{0.5, 1.9}
    );

    private final MCUEntryRepository mcuEntryRepository;
    private final UserRepository userRepository;
    private final ScoreRepository scoreRepository;
    private final MovieScoreXUserRepository movieScoreXUserRepository;

    public McuEntryService(MCUEntryRepository mcuEntryRepository,
                           UserRepository userRepository,
                           ScoreRepository scoreRepository,
                           MovieScoreXUserRepository movieScoreXUserRepository) {
        this.mcuEntryRepository = mcuEntryRepository;
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
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

        for (Scores score : Scores.values()) {
            map.put(score.name(), score.getDisplayName());
        }

        return map;
    }

    public void updateScore(String username, Long movieId, Double newScoreValue) {
        MovieScoreXUser ums = movieScoreXUserRepository
                .findByUserUsernameAndMcuEntryId(username, movieId)
                .orElseGet(() -> {
                    MovieScoreXUser newUms = new MovieScoreXUser();
                    newUms.setUser(userRepository.findById(username).orElseThrow());
                    newUms.setMcuEntry(mcuEntryRepository.findById(movieId).orElseThrow());
                    newUms.setCreatedAt(ZonedDateTime.now());
                    return newUms;
                });

        String oldTier = ums.getScore() != null ? ums.getScore().getDescription() : null;
        String newTier = McuEntryAdapter.resolveTier(newScoreValue);

        Score newScore = scoreRepository.findById(newScoreValue).orElseThrow();
        ums.setScore(newScore);

        if (!newTier.equals(oldTier) || ums.getRanking() == null) {
            Double[] range = TIER_RANGES.get(newTier);
            int count = movieScoreXUserRepository.countByUserUsernameAndScore_ScoreBetween(username, range[0], range[1]);
            ums.setRanking(count + 1);
        }

        ums.setUpdatedAt(ZonedDateTime.now());
        movieScoreXUserRepository.save(ums);
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
}