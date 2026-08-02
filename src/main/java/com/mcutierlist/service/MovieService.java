package com.mcutierlist.service;

import com.mcutierlist.model.dto.MCUEntryDTO;
import com.mcutierlist.model.dto.McuEntryScoreDTO;
import com.mcutierlist.model.entities.MCUEntry;
import com.mcutierlist.model.entities.MovieScoreXUser;
import com.mcutierlist.model.entities.Score;
import com.mcutierlist.model.repositories.MCUEntryRepository;
import com.mcutierlist.model.repositories.MovieScoreXUserRepository;
import com.mcutierlist.model.repositories.ScoreRepository;
import com.mcutierlist.model.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
public class MovieService {

    private static final List<String> TIER_ORDER = List.of("Excellent", "Very Good", "Good", "Weak", "Bad");

    private static final Map<String, Double[]> TIER_RANGES = Map.of(
            "Excellent", new Double[]{4.5, 5.0},
            "Very Good", new Double[]{4.0, 4.4},
            "Good", new Double[]{3.0, 3.9},
            "Weak", new Double[]{2.0, 2.9},
            "Bad", new Double[]{0.5, 1.9}
    );

    private final MCUEntryRepository movieRepository;
    private final UserRepository userRepository;
    private final ScoreRepository scoreLabelRepository;
    private final MovieScoreXUserRepository userMovieScoreRepository;

    public MovieService(MCUEntryRepository movieRepository, UserRepository userRepository,
                        ScoreRepository scoreLabelRepository,
                        MovieScoreXUserRepository userMovieScoreRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.scoreLabelRepository = scoreLabelRepository;
        this.userMovieScoreRepository = userMovieScoreRepository;
    }

    public static String resolveTier(Double score) {
        if (score >= 4.5) return "Excellent";
        if (score >= 4.0) return "Very Good";
        if (score >= 3.0) return "Good";
        if (score >= 2.0) return "Weak";
        return "Bad";
    }

    public List<McuEntryScoreDTO> getMoviesWithScores(String username) {
        List<MCUEntry> movies = movieRepository.findAllByOrderByReleaseDateAsc();

        Map<Long, MovieScoreXUser> scoreMap = userMovieScoreRepository.findByUserUsername(username)
                .stream()
                .collect(Collectors.toMap(ums -> ums.getMcuEntry().getId(), ums -> ums));

        Map<Double, Score> labelMap = scoreLabelRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Score::getScore, sl -> sl));

        return movies.stream()
                .map(movie -> {
                    MovieScoreXUser ums = scoreMap.get(movie.getId());
                    Double score = ums != null ? ums.getScore() : null;
                    Score label = score != null ? labelMap.get(score) : null;
                    MCUEntryDTO mcuEntry = new MCUEntryDTO(
                            movie.getId(),
                            movie.getOriginalTitle(),
                            movie.getAlternativeTitle(),
                            movie.getPhase(),
                            movie.getReleaseDate(),
                            movie.getPosterUrl(),
                            movie.getCreatedAt(),
                            movie.getUpdatedAt());

                    return new McuEntryScoreDTO(
                            mcuEntry,
                            score != null ? score : null,
                            label != null ? label.getDisplayName() : null,
                            score != null ? resolveTier(score) : null,
                            ums != null ? ums.getRanking() : null
                    );
                })
                .collect(Collectors.toList());
    }

    public LinkedHashMap<Integer, List<McuEntryScoreDTO>> getMoviesByPhase(String username) {
        LinkedHashMap<Integer, List<McuEntryScoreDTO>> byPhase = new LinkedHashMap<>();
        getMoviesWithScores(username).forEach(dto ->
                byPhase.computeIfAbsent(dto.mcuEntry().phase(), p -> new ArrayList<>()).add(dto));
        return byPhase;
    }

    public Map<String, String> getScoreLabelsForDisplay() {
        return scoreLabelRepository.findAll().stream()
                .collect(Collectors.toMap(
                        sl -> BigDecimal.valueOf(sl.getScore()).setScale(1).toPlainString(),
                        Score::getDisplayName,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    public LinkedHashMap<String, List<McuEntryScoreDTO>> getMoviesByTier(String username) {
        List<McuEntryScoreDTO> rated = getMoviesWithScores(username).stream()
                .filter(dto -> dto.score() != null)
                .collect(Collectors.toList());

        LinkedHashMap<String, List<McuEntryScoreDTO>> tierMap = new LinkedHashMap<>();
        TIER_ORDER.forEach(tier -> tierMap.put(tier, new ArrayList<>()));

        rated.forEach(dto -> tierMap.get(dto.tier()).add(dto));

        tierMap.values().forEach(list ->
                list.sort(Comparator.comparingInt(dto -> dto.ranking() != null ? dto.ranking() : Integer.MAX_VALUE))
        );

        return tierMap;
    }

    @Transactional
    public void updateScore(String username, Long movieId, Double score) {
        MovieScoreXUser ums = userMovieScoreRepository
                .findByUserUsernameAndMcuEntryId(username, movieId)
                .orElseGet(() -> {
                    MovieScoreXUser newUms = new MovieScoreXUser();
                    newUms.setUser(userRepository.findById(username).orElseThrow());
                    newUms.setMcuEntry(movieRepository.findById(movieId).orElseThrow());
                    newUms.setCreatedAt(ZonedDateTime.now());
                    return newUms;
                });

        String oldTier = ums.getScore() != null ? resolveTier(ums.getScore()) : null;
        String newTier = resolveTier(score);

        ums.setScore(score);

        if (!newTier.equals(oldTier) || ums.getRanking() == null) {
            Double[] range = TIER_RANGES.get(newTier);
            int count = userMovieScoreRepository.countByUserUsernameAndScoreBetween(username, range[0], range[1]);
            ums.setRanking(count + 1);
        }

        ums.setUpdatedAt(ZonedDateTime.now());
        userMovieScoreRepository.save(ums);
    }

    @Transactional
    public void reorderWithinTier(String username, List<Long> movieIds) {
        for (int i = 0; i < movieIds.size(); i++) {
            userMovieScoreRepository
                    .findByUserUsernameAndMcuEntryId(username, movieIds.get(i))
                    .ifPresent(ums -> {
                    });
        }

        for (int i = 0; i < movieIds.size(); i++) {
            final int rank = i + 1;
            userMovieScoreRepository
                    .findByUserUsernameAndMcuEntryId(username, movieIds.get(i))
                    .ifPresent(ums -> {
                        ums.setRanking(rank);
                        userMovieScoreRepository.save(ums);
                    });
        }
    }

    public List<String> getChartLabels(String username) {
        return getRatedSorted(username).stream()
                .map(dto -> dto.mcuEntry().originalTitle())
                .collect(Collectors.toList());
    }

    public List<Double> getChartScores(String username) {
        return getRatedSorted(username).stream()
                .map(McuEntryScoreDTO::score)
                .collect(Collectors.toList());
    }

    private List<McuEntryScoreDTO> getRatedSorted(String username) {
        return getMoviesWithScores(username).stream()
                .filter(dto -> dto.score() != null)
                .sorted(Comparator
                        .comparingInt((McuEntryScoreDTO dto) -> TIER_ORDER.indexOf(dto.tier()))
                        .thenComparingInt(dto -> dto.ranking() != null ? dto.ranking() : Integer.MAX_VALUE))
                .collect(Collectors.toList());
    }
}