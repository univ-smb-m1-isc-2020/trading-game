package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.BuyOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Portfolio;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BuyOrderTest {

    private EOD data;
    private Ticker ticker;
    private Portfolio portfolio;

    @BeforeEach
    public void setup(){
        data = mock(EOD.class);
        ticker = mock(Ticker.class);
        portfolio = mock(Portfolio.class);
    }

    @Test
    public void dontApplyWrongTicker(){
        int quantity = 10;
        int closePrice = 500;
        EOD otherEod = mock(EOD.class);
        Ticker otherTicker = mock(Ticker.class);
        when(data.getClose()).thenReturn(closePrice);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(otherTicker.getSymbol()).thenReturn("WRONG");
        when(data.getSymbol()).thenReturn(ticker);
        when(otherEod.getSymbol()).thenReturn(otherTicker);
        when(portfolio.canAfford(anyInt())).thenReturn(true);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);

        buyOrder.apply(otherEod);

        verify(portfolio, never()).buy(any(), anyInt(), anyInt());
        Assertions.assertTrue(buyOrder.isPending());
    }

    @Test
    public void applyCanAfford(){
        int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(true);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);

        buyOrder.apply(data);

        verify(portfolio, times(1)).buy(data.getSymbol(), quantity, close_price*quantity);
        Assertions.assertFalse(buyOrder.isPending());
    }

    @Test
    public void applyAlreadyApplied(){
        int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(true);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);
        buyOrder.setPending(false);

        buyOrder.apply(data);

        verify(portfolio, never()).buy(any(), anyInt(), anyInt());
        Assertions.assertFalse(buyOrder.isPending());
    }

    @Test
    public void applyCantAfford(){
        int quantity = 10;
        int close_price = 500;
        when(data.getClose()).thenReturn(close_price);
        when(ticker.getSymbol()).thenReturn("TEST");
        when(data.getSymbol()).thenReturn(ticker);
        when(portfolio.canAfford(anyInt())).thenReturn(false);
        BuyOrder buyOrder = new BuyOrder(portfolio, ticker, quantity);

        buyOrder.apply(data);

        verify(portfolio, never()).buy(any(), anyInt(), anyInt());
        Assertions.assertTrue(buyOrder.isPending());
    }
}
