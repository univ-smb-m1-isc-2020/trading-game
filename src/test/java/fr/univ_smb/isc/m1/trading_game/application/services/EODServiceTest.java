package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.EODService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EODServiceTest {

    private EODRepository mockRepository;
    private EOD mockEod;
    private List<EOD> mockEodList;

    @BeforeEach
    public void setUp(){
        int eodCount = 5;
        mockRepository = mock(EODRepository.class);
        mockEod = mock(EOD.class);
        mockEodList = new ArrayList<>();
        mockEodList.add(mockEod);
        for(int i=0;i<eodCount;i++){
            mockEodList.add(mock(EOD.class));
        }
        when(mockRepository.findTopBySymbolOrderByDate(any())).thenReturn(Optional.of(mockEod));
        when(mockRepository.findAllByDate(any())).thenReturn(mockEodList);
    }

    @Test
    public void GetLast(){
        Ticker mockTicker = mock(Ticker.class);
        EODService service = new EODService(mockRepository);
        Assertions.assertEquals(mockEod, service.getLast(mockTicker));
    }

    @Test
    public void GetEods(){
        Date mockDate = mock(Date.class);
        EODService service = new EODService(mockRepository);
        Assertions.assertEquals(mockEodList, service.getEODs(mockDate));
    }

}
