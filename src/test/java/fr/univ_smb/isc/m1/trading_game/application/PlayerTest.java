package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class PlayerTest {

    private final static int INIT_BALANCE = 1000;
    private final static int PORTFOLIO_COUNT = 3;
    private Object user;
    private TestablePlayer player;

    @BeforeEach
    public void setup(){
        user = null;//TODO use a real User class
        player = new TestablePlayer(user, INIT_BALANCE, PORTFOLIO_COUNT);
    }

    @Test
    public void createBuyOrder(){
        int quantity = 5;
        int portNumber = 1;
        Ticker ticker = mock(Ticker.class);
        player.createBuyOrder(portNumber, ticker, quantity);

        Assertions.assertTrue(
                player.getOrders().stream().anyMatch(order ->
                        order.getClass() == BuyOrder.class
                                && order.portfolio == player.getPortfolios().get(portNumber)
                                && order.quantity == quantity
                                && order.ticker == ticker
                )
        );
    }

    @Test
    public void createSellOrder(){
        int quantity = 5;
        int portNumber = 1;
        Ticker ticker = mock(Ticker.class);
        player.createSellOrder(portNumber, ticker, quantity);

        Assertions.assertTrue(
                player.getOrders().stream().anyMatch(order ->
                        order.getClass() == SellOrder.class
                                && order.portfolio == player.getPortfolios().get(portNumber)
                                && order.quantity == quantity
                                && order.ticker == ticker
                )
        );
    }

    @Test
    public void applyOrder(){
        Order o = mock(Order.class);
        Order o2 = mock(Order.class);
        EOD eod = mock(EOD.class);
        player.addOrder(o);
        player.addOrder(o2);

        player.applyOrders(eod);

        verify(o, times(1)).apply(eod);
        verify(o2, times(1)).apply(eod);
    }

    @Test
    public void getTotalBalance(){
        Assertions.assertEquals(INIT_BALANCE*PORTFOLIO_COUNT, player.getTotalBalance());
    }

}
