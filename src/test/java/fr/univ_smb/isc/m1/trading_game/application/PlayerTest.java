package fr.univ_smb.isc.m1.trading_game.application;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    private final static int INIT_BALANCE = 1000;
    private final static int PORTFOLIO_COUNT = 3;
    private Object user;
    private Player player;

    @BeforeEach
    public void setup(){
        user = null;//TODO use a real User class
        player = new Player(user, INIT_BALANCE, PORTFOLIO_COUNT);
    }

    @Test
    public void getTotalBalance(){
        Assertions.assertEquals(INIT_BALANCE*PORTFOLIO_COUNT, player.getTotalBalance());
    }

}
