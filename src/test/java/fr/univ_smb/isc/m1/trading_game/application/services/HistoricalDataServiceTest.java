package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.HTTPRequestService;
import fr.univ_smb.isc.m1.trading_game.application.HistoricalDataService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HistoricalDataServiceTest {
    private TickerRepository mockTickerRepository;
    private EODRepository mockEODRepository;
    private HTTPRequestService mockHTTPService;

    @BeforeEach
    public void setUp(){
        mockTickerRepository = mock(TickerRepository.class);
        mockEODRepository = mock(EODRepository.class);
        mockHTTPService = mock(HTTPRequestService.class);
    }

    @Test
    public void tickers(){
        int mockTickerCount = 5;
        List<Ticker> mockTickers = new ArrayList<>();
        for(int i=0;i<mockTickerCount;i++){
            mockTickers.add(mock(Ticker.class));
        }
        when(mockTickerRepository.findAll()).thenReturn(mockTickers);
        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        Assertions.assertEquals(service.tickers(), mockTickers);
    }

    @Test
    public void eods(){
        int mockEODCount = 5;
        List<EOD> mockEODs = new ArrayList<>();
        for(int i=0;i<mockEODCount;i++){
            mockEODs.add(mock(EOD.class));
        }
        when(mockEODRepository.findAll()).thenReturn(mockEODs);
        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        Assertions.assertEquals(service.eods(), mockEODs);
    }

}
