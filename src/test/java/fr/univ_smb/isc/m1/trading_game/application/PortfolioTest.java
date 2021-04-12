package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class PortfolioTest {

    @BeforeEach
    public void setup(){

    }

    @Test
    public void getBalance(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        Assertions.assertEquals(baseValue, portfolio.getBalance());
    }

    @Test
    public void getQuantity(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        Ticker ticker = mock(Ticker.class);
        Assertions.assertEquals(0, portfolio.getQuantity(ticker));
    }

    @Test
    public void setQuantity(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        int quantity = 10;
        Ticker ticker = mock(Ticker.class);
        portfolio.setQuantity(ticker, quantity);
        Assertions.assertEquals(quantity, portfolio.getQuantity(ticker));
    }

    @Test
    public void canAfford(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        Assertions.assertTrue(portfolio.canAfford(baseValue));
    }

    @Test
    public void cantAfford(){
        int baseValue = 500;
        int cantAffordValue = 501;
        Portfolio portfolio = new Portfolio(baseValue);
        Assertions.assertFalse(portfolio.canAfford(cantAffordValue));
    }

    @Test
    public void buy(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.buy(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(portfolio.getBalance(), baseValue-quantity*unitValue);
    }

    @Test
    public void sell(){
        int baseValue = 500;
        Portfolio portfolio = new Portfolio(baseValue);
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.buy(ticker, quantity, 0); // TODO shouldn't use buy ? use a mock portfolio inheriting portfolio
        portfolio.sell(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(baseValue+quantity*unitValue,portfolio.getBalance());
    }
}
