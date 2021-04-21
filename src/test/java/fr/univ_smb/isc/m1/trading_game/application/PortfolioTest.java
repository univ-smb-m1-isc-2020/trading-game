package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Portfolio;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class PortfolioTest {

    private Portfolio portfolio;
    private int baseValue;

    @BeforeEach
    public void setup(){
        baseValue = 500;
        portfolio = new Portfolio(baseValue);
    }

    @Test
    public void getBalance(){
        Assertions.assertEquals(baseValue, portfolio.getBalance());
    }

    @Test
    public void getQuantity(){
        Ticker ticker = mock(Ticker.class);
        Assertions.assertEquals(0, portfolio.getQuantity(ticker));
    }

    @Test
    public void setQuantity(){
        int quantity = 10;
        Ticker ticker = mock(Ticker.class);
        portfolio.setQuantity(ticker, quantity);
        Assertions.assertEquals(quantity, portfolio.getQuantity(ticker));
    }

    @Test
    public void canAfford(){
        Assertions.assertTrue(portfolio.canAfford(baseValue));
    }

    @Test
    public void cantAfford(){
        int cantAffordValue = 501;
        Assertions.assertFalse(portfolio.canAfford(cantAffordValue));
    }

    @Test
    public void buy(){
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.buy(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(baseValue-quantity*unitValue, portfolio.getBalance());
    }

    @Test
    public void canSell(){
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        portfolio.setQuantity(ticker, quantity);
        Assertions.assertTrue(portfolio.canSell(ticker, quantity));
    }

    @Test
    public void cantSellQuantity(){
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        portfolio.setQuantity(ticker, quantity);
        Assertions.assertFalse(portfolio.canSell(ticker, quantity+1));
    }

    @Test
    public void cantSellNotBought(){
        Ticker ticker = mock(Ticker.class);
        Assertions.assertFalse(portfolio.canSell(ticker, 1));
    }

    @Test
    public void sell(){
        Ticker ticker = mock(Ticker.class);
        int quantity = 10;
        int unitValue = 5;
        portfolio.setQuantity(ticker, quantity);
        portfolio.sell(ticker, quantity, quantity*unitValue);
        Assertions.assertEquals(baseValue+quantity*unitValue,portfolio.getBalance());
    }
}
