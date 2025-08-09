package com.example.Sports_Arena.Repository;

import com.example.Sports_Arena.Model.SportsArena;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SportsArenaRepo extends JpaRepository<SportsArena,Long> {


    @Modifying
    @Transactional
    @Query(value = "DELETE FROM sportsarena WHERE id = :sportsArenaId", nativeQuery = true)
    int deleteSportsArenaById(@Param("sportsArenaId")Long sportsArenaId);
}
