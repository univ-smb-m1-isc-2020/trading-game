package fr.univ_smb.isc.m1.trading_game.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;

public class GameTest {
    private final static int PORT_COUNT = 3;
    private final static int BALANCE = 10000;
    private final static int FEE = 1;
    private final static Date START = new Date(50000);
    private final static int DURATION = 100;
    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(PORT_COUNT, BALANCE, FEE, START, DURATION);
    }

    @Test
    public void getCurrentDuration(){
        Assertions.assertEquals(0, game.getCurrentDuration());
    }

    @Test
    public void getTotalDuration(){
        Assertions.assertEquals(DURATION, game.getTotalDuration());
    }

    @Test
    public void getStartDate(){
        Assertions.assertEquals(START, game.getStartDate());
    }

    @Test
    public void getPortfolioCount(){
        Assertions.assertEquals(PORT_COUNT, game.getMaxPortfolios());
    }

    @Test
    public void getInitialBalance(){
        Assertions.assertEquals(BALANCE, game.getInitialBalance());
    }

    @Test
    public void getTransactionFee(){
        Assertions.assertEquals(FEE, game.getTransactionFee());
    }

    @Test
    public void getPlayerCount(){
        Assertions.assertEquals(0, game.getPlayerCount());
    }

    @Test
    public void getRankings(){
        //TODO
    }

    @Test
    public void createPlayer(){
        //TODO
    }

    @Test
    public void applyEod(){
        //TODO
    }

    @Test
    public void hasEnded(){
        Assertions.assertFalse(game.hasEnded());
    }

    @Test
    public void hasNotEnded(){
        Game endedGame = new Game(PORT_COUNT, BALANCE, FEE, START, 0);
        Assertions.assertTrue(endedGame.hasEnded());
    }
}
