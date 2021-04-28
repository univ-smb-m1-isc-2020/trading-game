package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EODRepository extends JpaRepository<EOD, Long> {
    List<EOD> findAllByDate(Date date);
    Optional<EOD> findTopBySymbolOrderByDate(Ticker ticker);
}
