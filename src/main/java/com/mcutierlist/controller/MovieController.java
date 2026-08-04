package com.mcutierlist.controller;

import com.mcutierlist.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Renders the rate view and handles scoring.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("moviesByPhase", movieService.getMoviesByPhase(username));
        model.addAttribute("scoreLabels", movieService.getScoreLabelsForDisplay());
        return "index";
    }

    @PostMapping("/movies/{id}/score")
    public String updateScore(@PathVariable Long id,
                              @RequestParam Double score,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.updateScore(username, id, score);
        return "redirect:/";
    }
}