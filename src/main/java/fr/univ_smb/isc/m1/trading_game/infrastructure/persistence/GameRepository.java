package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("select g from Game g where g.currentDuration<g.totalDuration")
    public List<Game> findAllActive();

    @Query("select g from Game g where g.currentDuration>=g.totalDuration")
    List<Game> findAllInactive();
}