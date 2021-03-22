package fr.univ_smb.isc.m1.trading_game.application;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChuckFactsService {

    public List<ChuckFact> facts() {
        return List.of(new ChuckFact("Chuck Norris once lost his wedding ring....since then it's been war in Middle Earth"));
    }

}
