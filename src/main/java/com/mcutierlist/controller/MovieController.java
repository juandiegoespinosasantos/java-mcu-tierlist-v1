package com.mcutierlist.controller;

import com.mcutierlist.service.MovieService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model,
                        @RequestParam(defaultValue = "movies") String tab) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("tab", tab);
        model.addAttribute("movies", movieService.getMoviesWithScores(username));
        model.addAttribute("tierList", movieService.getMoviesByTier(username));
        model.addAttribute("chartLabels", movieService.getChartLabels(username));
        model.addAttribute("chartScores", movieService.getChartScores(username));
        return "index";
    }

    @PostMapping("/movies/{id}/score")
    public String updateScore(@PathVariable Long id,
                              @RequestParam BigDecimal score,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.updateScore(username, id, score);
        return "redirect:/";
    }

    @PostMapping("/movies/reorder")
    @ResponseBody
    public void reorder(@RequestBody List<Long> movieIds, HttpSession session) {
        String username = (String) session.getAttribute("username");
        movieService.reorderWithinTier(username, movieIds);
    }
}
