package com.mcutierlist.service;

import com.mcutierlist.dto.MovieScoreDTO;
import com.mcutierlist.model.entities.Movie;
import com.mcutierlist.model.entities.ScoreLabel;
import com.mcutierlist.model.entities.UserMovieScore;
import com.mcutierlist.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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

    private static final Map<String, BigDecimal[]> TIER_RANGES = Map.of(
        "Excellent", new BigDecimal[]{new BigDecimal("4.5"), new BigDecimal("5.0")},
        "Very Good", new BigDecimal[]{new BigDecimal("4.0"), new BigDecimal("4.4")},
        "Good",      new BigDecimal[]{new BigDecimal("3.0"), new BigDecimal("3.9")},
        "Weak",      new BigDecimal[]{new BigDecimal("2.0"), new BigDecimal("2.9")},
        "Bad",       new BigDecimal[]{new BigDecimal("0.5"), new BigDecimal("1.9")}
    );

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ScoreLabelRepository scoreLabelRepository;
    private final UserMovieScoreRepository userMovieScoreRepository;

    public MovieService(MovieRepository movieRepository, UserRepository userRepository,
                        ScoreLabelRepository scoreLabelRepository,
                        UserMovieScoreRepository userMovieScoreRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.scoreLabelRepository = scoreLabelRepository;
        this.userMovieScoreRepository = userMovieScoreRepository;
    }

    public List<MovieScoreDTO> getMoviesWithScores(String username) {
        List<Movie> movies = movieRepository.findAllByOrderByReleaseDateAsc();

        Map<Long, UserMovieScore> scoreMap = userMovieScoreRepository.findByUserUsername(username)
            .stream()
            .collect(Collectors.toMap(ums -> ums.getMovie().getId(), ums -> ums));

        Map<BigDecimal, ScoreLabel> labelMap = scoreLabelRepository.findAll()
            .stream()
            .collect(Collectors.toMap(ScoreLabel::getScore, sl -> sl));

        return movies.stream()
            .map(movie -> {
                UserMovieScore ums = scoreMap.get(movie.getId());
                BigDecimal score = ums != null ? ums.getScore() : null;
                ScoreLabel label = score != null ? labelMap.get(score) : null;

                return new MovieScoreDTO(
                    movie,
                    score,
                    label != null ? label.getDisplayName() : null,
                    score != null ? resolveTier(score) : null,
                    ums != null ? ums.getRanking() : null
                );
            })
            .collect(Collectors.toList());
    }

    public LinkedHashMap<Integer, List<MovieScoreDTO>> getMoviesByPhase(String username) {
        LinkedHashMap<Integer, List<MovieScoreDTO>> byPhase = new LinkedHashMap<>();
        getMoviesWithScores(username).forEach(dto ->
            byPhase.computeIfAbsent(dto.getMovie().getPhase(), p -> new ArrayList<>()).add(dto));
        return byPhase;
    }

    public Map<String, String> getScoreLabelsForDisplay() {
        return scoreLabelRepository.findAll().stream()
            .collect(Collectors.toMap(
                sl -> sl.getScore().setScale(1).toPlainString(),
                ScoreLabel::getDisplayName,
                (a, b) -> a,
                LinkedHashMap::new
            ));
    }

    public LinkedHashMap<String, List<MovieScoreDTO>> getMoviesByTier(String username) {
        List<MovieScoreDTO> rated = getMoviesWithScores(username).stream()
            .filter(dto -> dto.getScore() != null)
            .collect(Collectors.toList());

        LinkedHashMap<String, List<MovieScoreDTO>> tierMap = new LinkedHashMap<>();
        TIER_ORDER.forEach(tier -> tierMap.put(tier, new ArrayList<>()));

        rated.forEach(dto -> tierMap.get(dto.getTier()).add(dto));

        tierMap.values().forEach(list ->
            list.sort(Comparator.comparingInt(dto -> dto.getRanking() != null ? dto.getRanking() : Integer.MAX_VALUE))
        );

        return tierMap;
    }

    @Transactional
    public void updateScore(String username, Long movieId, BigDecimal score) {
        UserMovieScore ums = userMovieScoreRepository
            .findByUserUsernameAndMovieId(username, movieId)
            .orElseGet(() -> {
                UserMovieScore newUms = new UserMovieScore();
                newUms.setUser(userRepository.findById(username).orElseThrow());
                newUms.setMovie(movieRepository.findById(movieId).orElseThrow());
                return newUms;
            });

        String oldTier = ums.getScore() != null ? resolveTier(ums.getScore()) : null;
        String newTier = resolveTier(score);

        ums.setScore(score);

        if (!newTier.equals(oldTier) || ums.getRanking() == null) {
            BigDecimal[] range = TIER_RANGES.get(newTier);
            int count = userMovieScoreRepository.countByUserUsernameAndScoreBetween(
                username, range[0], range[1]);
            ums.setRanking(count + 1);
        }

        userMovieScoreRepository.save(ums);
    }

    @Transactional
    public void reorderWithinTier(String username, List<Long> movieIds) {
        for (int i = 0; i < movieIds.size(); i++) {
            userMovieScoreRepository
                .findByUserUsernameAndMovieId(username, movieIds.get(i))
                .ifPresent(ums -> {});
        }
        for (int i = 0; i < movieIds.size(); i++) {
            final int rank = i + 1;
            userMovieScoreRepository
                .findByUserUsernameAndMovieId(username, movieIds.get(i))
                .ifPresent(ums -> {
                    ums.setRanking(rank);
                    userMovieScoreRepository.save(ums);
                });
        }
    }

    public List<String> getChartLabels(String username) {
        return getRatedSorted(username).stream()
            .map(dto -> dto.getMovie().getOriginalTitle())
            .collect(Collectors.toList());
    }

    public List<BigDecimal> getChartScores(String username) {
        return getRatedSorted(username).stream()
            .map(MovieScoreDTO::getScore)
            .collect(Collectors.toList());
    }

    private List<MovieScoreDTO> getRatedSorted(String username) {
        return getMoviesWithScores(username).stream()
            .filter(dto -> dto.getScore() != null)
            .sorted(Comparator
                .comparingInt((MovieScoreDTO dto) -> TIER_ORDER.indexOf(dto.getTier()))
                .thenComparingInt(dto -> dto.getRanking() != null ? dto.getRanking() : Integer.MAX_VALUE))
            .collect(Collectors.toList());
    }

    public static String resolveTier(BigDecimal score) {
        if (score.compareTo(new BigDecimal("4.5")) >= 0) return "Excellent";
        if (score.compareTo(new BigDecimal("4.0")) >= 0) return "Very Good";
        if (score.compareTo(new BigDecimal("3.0")) >= 0) return "Good";
        if (score.compareTo(new BigDecimal("2.0")) >= 0) return "Weak";
        return "Bad";
    }
}
