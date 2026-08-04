package com.mcutierlist.controller;

import com.mcutierlist.services.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

/**
 * Renders the movies list/tier-list view and handles reordering.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 3, 2026
 * @since 25
 */
@Controller
public class MoviesController {

    private static final Set<String> VALID_VIEWS = Set.of("list", "tierlist");

    private final MovieService movieService;

    public MoviesController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String movies(HttpSession session, Model model,
                         @RequestParam(defaultValue = "list") String view) {
        String username = (String) session.getAttribute("username");
        String safeView = VALID_VIEWS.contains(view) ? view : "list";
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("view", safeView);
        if (safeView.equals("tierlist")) {
            model.addAttribute("tierList", movieService.getMoviesByTier(username));
        } else {
            model.addAttribute("movies", movieService.getMoviesWithScores(username));
            model.addAttribute("chartLabels", movieService.getChartLabels(username));
            model.addAttribute("chartScores", movieService.getChartScores(username));
        }
        return "movies";
    }

    @PostMapping("/movies/reorder")
    @ResponseBody
    public void reorder(@RequestBody List<Long> movieIds, HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.reorderWithinTier(username, movieIds);
    }
}
