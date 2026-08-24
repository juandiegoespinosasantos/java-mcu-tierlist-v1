package com.mcutierlist.controller;

import com.mcutierlist.services.McuEntryService;
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

    private final McuEntryService service;

    public MovieController(McuEntryService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        model.addAttribute("name", session.getAttribute("name"));
        model.addAttribute("moviesByPhase", service.getMoviesByPhase(username));
        model.addAttribute("scoreLabels", service.getScoreLabelsForDisplay());
        return "index";
    }

    @PostMapping("/movies/{id}/score")
    public String updateScore(@PathVariable Long id,
                              @RequestParam Double score,
                              HttpSession session) {
        String username = (String) session.getAttribute("username");
        service.updateScore(username, id, score);
        return "redirect:/";
    }
}