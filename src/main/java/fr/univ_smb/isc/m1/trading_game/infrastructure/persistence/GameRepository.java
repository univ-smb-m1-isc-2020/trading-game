package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    public List<Game> findAllByPlayersContains(Player player);
    public List<Game> findAllByCurrentDurationIsAndPlayersNotContaining(int value, Player player);
}
