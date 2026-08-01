package com.mcutierlist.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Entity for UserMovieScore table.
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
@Table(
    name = "user_movie_scores",
    uniqueConstraints = @UniqueConstraint(columnNames = {"username", "movie_id"})
)
public class UserMovieScore implements Serializable {

    private static final long serialVersionUID = -5647382910473625181L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "ranking")
    private Integer ranking;
}
