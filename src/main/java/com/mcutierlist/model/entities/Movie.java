package com.mcutierlist.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Entity for Movie table.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    private static final long serialVersionUID = -3826194057382910447L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_title", nullable = false)
    private String originalTitle;

    @Column(name = "alternative_title")
    private String alternativeTitle;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @Column(name = "phase")
    private Integer phase;

    @Column(name = "poster_url")
    private String posterUrl;
}