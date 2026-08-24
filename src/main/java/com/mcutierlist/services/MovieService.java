package com.mcutierlist.services;

import com.mcutierlist.enums.MovieOrder;
import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.ScoreRepository;
import com.mcutierlist.model.repositories.UserRepository;
import com.mcutierlist.utils.adapters.McuEntryAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service handling movie scoring, tiering and ranking for the current user.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Service
@Transactional
public class MovieService implements IMovieService {

    private static final List<String> TIER_ORDER = List.of("Excellent", "Very Good", "Good", "Weak", "Bad");

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

    public MovieService(MCUEntryRepository mcuEntryRepository,
                        UserRepository userRepository,
                        ScoreRepository scoreRepository,
                        MovieScoreXUserRepository movieScoreXUserRepository) {
        this.mcuEntryRepository = mcuEntryRepository;
        this.userRepository = userRepository;
        this.scoreRepository = scoreRepository;
        this.movieScoreXUserRepository = movieScoreXUserRepository;
    }

    public List<McuEntryScoreDTO> getMoviesWithScores(final String username, final MovieOrder orderBy) {
        List<MCUEntry> allMovies = mcuEntryRepository.findAllByOrderByReleaseDateAsc();

        Map<Long, MovieScoreXUser> userScoresByMovieId = movieScoreXUserRepository.findByUserUsername(username)
                .stream()
                .collect(Collectors.toMap(msu -> msu.getMcuEntry().getId(), ums -> ums));

        Comparator<McuEntryScoreDTO> comparator = MovieOrder.BY_ORIGINAL_ORDER.equals(orderBy) ?
                Comparator.comparing(dto -> dto.mcuEntry().releaseDate()) :
                Comparator.comparing(McuEntryScoreDTO::ranking, Comparator.nullsLast(Comparator.naturalOrder()));

        return allMovies.stream()
                .map(movie -> McuEntryAdapter.transform(movie, userScoresByMovieId))
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    public Map<Integer, List<McuEntryScoreDTO>> getMoviesByPhase(final String username) {
        return getMoviesWithScores(username, MovieOrder.BY_ORIGINAL_ORDER)
                .stream()
                .collect(Collectors.groupingBy(dto -> dto.mcuEntry().phase(), LinkedHashMap::new, Collectors.toList()));
    }

    public Map<String, String> getScoreLabelsForDisplay() {
        return scoreRepository.findAll()
                .stream()
                .collect(Collectors.toMap(score -> score.getScore().toString(), Score::getDisplayName, (a, b) -> b, LinkedHashMap::new));
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

}