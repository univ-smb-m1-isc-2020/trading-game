package fr.univ_smb.isc.m1.trading_game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;

@Transactional
@Service
public class HistoricalDataService {
    private final static String API_TICKERS_URL = "http://api.marketstack.com/v1/exchanges/XPAR/tickers?";
    private final static String API_KEY = "b12558fc063d7b8d1245b972fe227316";
    private final static int TICKER_LIMIT = 3;

    private final static String API_EOD_URL = "http://api.marketstack.com/v1/eod?";
    private final static String START_DATE="2020-05-27";
    private final static String END_DATE="2021-04-22";
    private final static int EOD_LIMIT = 10;

    private final TickerRepository tickerRepository;
    private final EODRepository eodRepository;
    private final HTTPRequestService httpService;

    public HistoricalDataService(TickerRepository tickerRepository, EODRepository eodRepository, HTTPRequestService httpService) {
        this.tickerRepository = tickerRepository;
        this.eodRepository = eodRepository;
        this.httpService = httpService;
    }

    @PostConstruct
    public void initialize() {
        initializeTickers();
        initializeEODs();
        deleteNonEODUsers();
    }

    public void initializeEODs(){
        List<Ticker> tickers = tickers();
        int tickersPerRequest = 3;
        if(eods().isEmpty()){
            for(int i=0; i<tickers().size(); i+=tickersPerRequest){
                List<Ticker> requestTickers = tickers.subList(i*tickersPerRequest,
                        Math.min((i+1)*tickersPerRequest, tickers.size()-1));
                fetchEODof(requestTickers);
            }
        }
    }

    public void fetchEODof(List<Ticker> requestTickers){
        ObjectMapper mapper = new ObjectMapper();
        String symbols = getTickerRequestParameter(requestTickers);
        URI uri = getEODsURI(symbols);
        String restResult = httpService.getRequestResponse(uri);
        try{
            JsonNode jEODs = mapper.readTree(restResult).get("data");
            for (final JsonNode jEOD : jEODs) {
                Ticker ticker = tickerRepository.findById(jEOD.get("symbol").textValue()).orElse(null);
                if(ticker != null){
                    EOD eod = mapper.treeToValue(jEOD, EOD.class);
                    eod.setSymbol(ticker);
                    eodRepository.saveAndFlush(eod);
                }
            }
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
    }

    private String getTickerRequestParameter(List<Ticker> tickers){
        StringBuilder res = new StringBuilder();
        for (Ticker ticker : tickers) {
            res.append(ticker.getSymbol()).append(",");
        }
        res.deleteCharAt(res.length()-1);
        return res.toString();
    }

    public void initializeTickers(){
        // Tickers initialization
        ObjectMapper mapper = new ObjectMapper();
        if (tickers().isEmpty()) {
            try {
                // Getting tickers
                String restResult = httpService.getRequestResponse(getTickerURI());
                JsonNode jTickers = mapper.readTree(restResult).get("data").get("tickers");
                // Adding the tickers to db
                for (final JsonNode jTicker : jTickers) {
                    tickerRepository.saveAndFlush(mapper.treeToValue(jTicker, Ticker.class));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private URI getEODsURI(String symbols){
        return UriComponentsBuilder.fromUriString(API_EOD_URL)
                .queryParam("access_key", API_KEY)
                .queryParam("limit", EOD_LIMIT)
                .queryParam("date_from", START_DATE)
                .queryParam("date_to", END_DATE)
                .queryParam("symbols", symbols).build().toUri();
    }

    private URI getTickerURI(){
        return UriComponentsBuilder.fromUriString(API_TICKERS_URL)
                .queryParam("access_key", API_KEY)
                .queryParam("limit", TICKER_LIMIT).build().toUri();
    }

    public List<Ticker> tickers() {
        return tickerRepository.findAll();
    }

    public List<EOD> eods() {
        return eodRepository.findAll();
    }

    public void deleteNonEODUsers(){
        for (Ticker ticker : tickers()){
            if(eodRepository.findAllBySymbol(ticker).isEmpty()){
                tickerRepository.delete(ticker);
            }
        }
    }
}