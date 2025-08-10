package com.example.Sports_Arena.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.engine.internal.Cascade;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Sportsarena")
public class SportsArena {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double latitude;
    private double longitude;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER,orphanRemoval = true)
    @JoinColumn(name="court_fk",referencedColumnName = "id")
    private List<Court> courtList;

}
