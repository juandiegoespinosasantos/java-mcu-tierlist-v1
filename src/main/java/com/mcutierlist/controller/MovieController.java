package com.mcutierlist.controller;

import com.mcutierlist.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Renders the main movies/rate/tierlist view and handles scoring and reordering.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Controller
public class MovieController {

    private static final Set<String> VALID_TABS = Set.of("movies", "rate", "tierlist");

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model,
                        @RequestParam(defaultValue = "rate") String tab) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("tab", tab);
        model.addAttribute("movies", movieService.getMoviesWithScores(username));
        model.addAttribute("moviesByPhase", movieService.getMoviesByPhase(username));
        model.addAttribute("scoreLabels", movieService.getScoreLabelsForDisplay());
        model.addAttribute("tierList", movieService.getMoviesByTier(username));
        model.addAttribute("chartLabels", movieService.getChartLabels(username));
        model.addAttribute("chartScores", movieService.getChartScores(username));
        return "index";
    }

    @PostMapping("/movies/{id}/score")
    public String updateScore(@PathVariable Long id,
                              @RequestParam Double score,
                              @RequestParam(defaultValue = "rate") String tab,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.updateScore(username, id, score);
        String safeTab = VALID_TABS.contains(tab) ? tab : "rate";
        return "redirect:/?tab=" + safeTab;
    }

    @PostMapping("/movies/reorder")
    @ResponseBody
    public void reorder(@RequestBody List<Long> movieIds, HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.reorderWithinTier(username, movieIds);
    }
}