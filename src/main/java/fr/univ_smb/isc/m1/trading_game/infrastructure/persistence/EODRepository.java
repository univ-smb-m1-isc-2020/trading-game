package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EODRepository extends JpaRepository<EOD, Long> {
    public List<EOD> findAllByDate(Date date);
}
