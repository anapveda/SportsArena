package com.example.Sports_Arena.Model;
/*
@OneToMany works only inside a single service + single database
When you use:

java
Copy
Edit
@OneToMany(...)
private List<Court> courtList;
JPA/Hibernate expects Court to be an entity inside the same microservice with:

The same database connection.

The ability to join tables at runtime.

This works fine in a monolithic application, but not when Court is in another microservice with a separate database.
Because @OneToMany assumes both entities are in the same database schema and Hibernate can join them.
In microservices, each service has its own DB, so instead we store IDs and fetch details over REST.

*/
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

    // Instead of @OneToMany with Court entity,
    // we store only court IDs (since Court lives in another microservice)
    @ElementCollection
    @CollectionTable(
            name = "arena_courts", // name of the table to store court IDs
            joinColumns = @JoinColumn(name = "arena_id") // FK to SportsArena table
    )
    @Column(name = "court_id")
    private List<Long> courtList;

}
