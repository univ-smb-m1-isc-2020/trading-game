package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, String> {
    @Query("delete from ticker where ticker.symbol not in (select distinct symbol from eod);")
    public void removeUnusedTickers();
}
