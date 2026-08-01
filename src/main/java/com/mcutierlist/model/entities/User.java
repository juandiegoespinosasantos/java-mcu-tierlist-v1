package com.mcutierlist.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity for User table.
 *
 * @author jdespinosa0014@outlook.com
 * @version Aug 1, 2026
 * @since 25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 7024891736451028461L;

    @Id
    private String username;

    @Column(name = "name", nullable = false)
    private String name;
}
