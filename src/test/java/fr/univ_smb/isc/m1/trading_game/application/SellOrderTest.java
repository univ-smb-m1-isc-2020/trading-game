package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Portfolio;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.SellOrder;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

public class SellOrderTest {
    /*private EOD data;
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

        when(portfolio.canSell(eq(ticker),anyInt())).thenReturn(false);
        SellOrder sellOrder = new SellOrder(portfolio, ticker, quantity);

        sellOrder.apply(otherEod);

        verify(portfolio, never()).sell(any(), anyInt(), anyInt());
    }

    @Test
    public void applyCanSell(){
        int quantity = 10;
        int close_price = 500;

        when(data.getClose()).thenReturn(close_price);

        when(ticker.getSymbol()).thenReturn("TEST");

        when(data.getSymbol()).thenReturn(ticker);

        when(portfolio.canSell(eq(ticker),anyInt())).thenReturn(true);

        SellOrder sellOrder = new SellOrder(portfolio, ticker, quantity);

        sellOrder.apply(data);

        verify(portfolio, times(1)).sell(data.getSymbol(), quantity, close_price*quantity);
    }

    @Test
    public void applyCantSell(){
        int quantity = 10;
        int close_price = 500;

        when(data.getClose()).thenReturn(close_price);

        when(ticker.getSymbol()).thenReturn("TEST");

        when(data.getSymbol()).thenReturn(ticker);

        when(portfolio.canSell(eq(ticker),anyInt())).thenReturn(false);
        SellOrder sellOrder = new SellOrder(portfolio, ticker, quantity);

        sellOrder.apply(data);

        verify(portfolio, never()).sell(any(), anyInt(), anyInt());
    }*/
}
