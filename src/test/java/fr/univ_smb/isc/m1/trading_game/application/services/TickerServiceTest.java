package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.TickerService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;

public class TickerServiceTest {
    private static final int mockTickerCount = 5;
    private static final String mockTickerMic = "TEST";
    private TickerRepository mockRepository;
    private List<Ticker> mockTickers;
    private Ticker mainMockTicker;

    @BeforeEach
    public void setUp(){
        mainMockTicker = mock(Ticker.class);
        when(mainMockTicker.getSymbol()).thenReturn(mockTickerMic);

        mockTickers = new ArrayList<>();
        mockTickers.add(mainMockTicker);
        for(int i=1;i<mockTickerCount;i++){
            Ticker t = mock(Ticker.class);
            when(t.getSymbol()).thenReturn(mockTickerMic+(i+1));
            mockTickers.add(t);
        }

        mockRepository = mock(TickerRepository.class);
        when(mockRepository.findById(mockTickerMic)).thenReturn(Optional.of(mainMockTicker));
        when(mockRepository.findAll()).thenReturn(mockTickers);
    }

    @Test
    public void get(){
        TickerService service = new TickerService(mockRepository);
        Assertions.assertEquals(mainMockTicker, service.get(mockTickerMic));
    }

    @Test
    public void getAll(){
        TickerService service = new TickerService(mockRepository);
        List<Ticker> tickers = service.getTickers();
        Assertions.assertEquals(mockTickerCount, tickers.size());
        Assertions.assertEquals(mockTickers, tickers);
    }

    @Test
    public void getDoesntExist(){
        TickerService service = new TickerService(mockRepository);
        Assertions.assertNull(service.get("NOT_EXISTING_TICKER"));
    }
}
