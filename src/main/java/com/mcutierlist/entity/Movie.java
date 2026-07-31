package com.mcutierlist.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @Column(name = "alternative_title")
    private String alternativeTitle;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    private Integer phase;

    @Column(name = "poster_url")
    private String posterUrl;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getAlternativeTitle() { return alternativeTitle; }
    public void setAlternativeTitle(String alternativeTitle) { this.alternativeTitle = alternativeTitle; }

    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public Integer getPhase() { return phase; }
    public void setPhase(Integer phase) { this.phase = phase; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
}
