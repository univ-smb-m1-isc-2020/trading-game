package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<TradingGameUser, Long> {
    Optional<TradingGameUser> findTradingGameUserByUsername(String username);
}
