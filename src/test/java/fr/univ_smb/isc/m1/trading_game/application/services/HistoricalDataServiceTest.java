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
import java.util.Optional;

import static org.mockito.Mockito.*;

public class HistoricalDataServiceTest {
    private static final String exampleTickerRequest = "{\"pagination\":{\"limit\":3,\"offset\":0,\"count\":3,\"total\":1181},\"data\":{\"name\":\"Euronext Paris\",\"acronym\":\"Euronext\",\"mic\":\"XPAR\",\"country\":\"France\",\"city\":\"Paris\",\"website\":\"WWW.EURONEXT.COM\",\"tickers\":[{\"name\":\"LYXOR ETF BEL 20\",\"symbol\":\"BEL.XPAR\",\"has_intraday\":false,\"has_eod\":false},{\"name\":\"LVMH\",\"symbol\":\"MC.XPAR\",\"has_intraday\":false,\"has_eod\":true},{\"name\":\"L\\u0027OREAL\",\"symbol\":\"OR.XPAR\",\"has_intraday\":false,\"has_eod\":true}]}}\n";
    private static final int exampleTickerRequestCount = 3;
    private static final String exampleEodRequest = "{\"pagination\":{\"limit\":10,\"offset\":0,\"count\":10,\"total\":464},\"data\":[{\"open\":628.0,\"high\":637.7,\"low\":626.7,\"close\":632.0,\"volume\":436028.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":632.0,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"MC.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-22T00:00:00+0000\"},{\"open\":342.7,\"high\":348.95,\"low\":342.15,\"close\":347.7,\"volume\":387005.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":347.7,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"OR.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-22T00:00:00+0000\"},{\"open\":337.35,\"high\":343.3,\"low\":337.3,\"close\":342.6,\"volume\":404622.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":342.6,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"OR.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-21T00:00:00+0000\"},{\"open\":605.0,\"high\":627.8,\"low\":603.3,\"close\":624.6,\"volume\":515257.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":624.6,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"MC.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-21T00:00:00+0000\"},{\"open\":339.05,\"high\":340.0,\"low\":337.55,\"close\":337.6,\"volume\":433745.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":337.6,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"OR.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-20T00:00:00+0000\"},{\"open\":621.3,\"high\":622.6,\"low\":605.1,\"close\":605.3,\"volume\":570894.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":605.3,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"MC.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-20T00:00:00+0000\"},{\"open\":336.5,\"high\":340.85,\"low\":336.0,\"close\":338.85,\"volume\":318467.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":338.85,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"OR.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-19T00:00:00+0000\"},{\"open\":632.2,\"high\":632.8,\"low\":622.6,\"close\":624.9,\"volume\":328492.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":624.9,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"MC.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-19T00:00:00+0000\"},{\"open\":336.0,\"high\":338.95,\"low\":331.9,\"close\":336.55,\"volume\":633124.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":336.55,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"OR.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-16T00:00:00+0000\"},{\"open\":617.1,\"high\":630.5,\"low\":614.8,\"close\":630.0,\"volume\":587186.0,\"adj_high\":null,\"adj_low\":null,\"adj_close\":630.0,\"adj_open\":null,\"adj_volume\":null,\"split_factor\":1.0,\"symbol\":\"MC.XPAR\",\"exchange\":\"XPAR\",\"date\":\"2021-04-16T00:00:00+0000\"}]}";
    private static final int exampleEODRequestCount = 10;

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
    public void initializeEOD(){
        when(mockEODRepository.findAll()).thenReturn(new ArrayList<>());
        when(mockHTTPService.getRequestResponse(any())).thenReturn(exampleEodRequest);
        when(mockTickerRepository.findById(any())).thenReturn(Optional.of(mock(Ticker.class)));
        List<Ticker> mockTickers = new ArrayList<>();
        for(int i=0;i<exampleTickerRequestCount;i++){
            mockTickers.add(mock(Ticker.class));
        }
        when(mockTickerRepository.findAll()).thenReturn(mockTickers);

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.initializeEODs();
        verify(mockEODRepository, times(exampleEODRequestCount)).saveAndFlush(any());
    }

    @Test
    public void initializeEODExistAlready(){
        EOD mockEOD = mock(EOD.class);
        List<EOD> mockEODs =new ArrayList<>();
        mockEODs.add(mockEOD);
        when(mockEODRepository.findAll()).thenReturn(mockEODs);

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.initializeEODs();
        verify(mockEODRepository, never()).saveAndFlush(any());
    }

    @Test
    public void fetchEOD(){
        when(mockEODRepository.findAll()).thenReturn(new ArrayList<>());
        when(mockHTTPService.getRequestResponse(any())).thenReturn(exampleEodRequest);
        when(mockTickerRepository.findById(any())).thenReturn(Optional.of(mock(Ticker.class)));
        List<Ticker> mockTickers = new ArrayList<>();
        for(int i=0;i<exampleTickerRequestCount;i++){
            mockTickers.add(mock(Ticker.class));
        }

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.fetchEODof(mockTickers);
        verify(mockEODRepository, times(exampleEODRequestCount)).saveAndFlush(any());
    }

    @Test
    public void fetchEODNotExistingTicker(){
        when(mockEODRepository.findAll()).thenReturn(new ArrayList<>());
        when(mockHTTPService.getRequestResponse(any())).thenReturn(exampleEodRequest);
        when(mockTickerRepository.findById(any())).thenReturn(Optional.empty());
        List<Ticker> mockTickers = new ArrayList<>();
        for(int i=0;i<exampleTickerRequestCount;i++){
            mockTickers.add(mock(Ticker.class));
        }

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.fetchEODof(mockTickers);
        verify(mockEODRepository, never()).saveAndFlush(any());
    }

    @Test
    public void initializeTickers(){
        when(mockTickerRepository.findAll()).thenReturn(new ArrayList<>());
        when(mockHTTPService.getRequestResponse(any())).thenReturn(exampleTickerRequest);

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.initializeTickers();
        verify(mockTickerRepository, times(exampleTickerRequestCount)).saveAndFlush(any());
    }

    @Test
    public void initializeTickersExistAlready(){
        Ticker mockTicker = mock(Ticker.class);
        List<Ticker> mockTickers =new ArrayList<>();
        mockTickers.add(mockTicker);
        when(mockTickerRepository.findAll()).thenReturn(mockTickers);

        HistoricalDataService service = new HistoricalDataService(mockTickerRepository, mockEODRepository, mockHTTPService);
        service.initializeTickers();
        verify(mockTickerRepository, never()).saveAndFlush(any());
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
