package fr.univ_smb.isc.m1.trading_game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EODRepository;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@Service
public class HistoricalDataService {//TODO test
    private final static String API_TICKERS_URL = "http://api.marketstack.com/v1/exchanges/XPAR/tickers?";
    private final static String API_KEY = "95f0474bf969b42aad6612791285e16b";
    private final static int TICKER_LIMIT = 60;

    private final static String API_EOD_URL = "http://api.marketstack.com/v1/eod?";
    private final static String START_DATE="2020-05-27";
    private final static String END_DATE="2021-04-22";
    private final static int EOD_LIMIT = 1000;


    private final TickerRepository tickerRepository;
    private final EODRepository eodRepository;

    public HistoricalDataService(TickerRepository tickerRepository, EODRepository eodRepository) {
        this.tickerRepository = tickerRepository;
        this.eodRepository = eodRepository;
    }

    @PostConstruct
    public void initialize() {
        String restResult;
        Ticker currentTicker;
        StringBuilder uri;
        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        // Tickers initialization
        if (tickers().isEmpty()) {
            JsonNode jTickers;
            uri = new StringBuilder(API_TICKERS_URL +
                    "access_key="+API_KEY +
                    "&limit="+TICKER_LIMIT);

            try {
                // Getting 60 tickers
                restResult = restTemplate.getForEntity(new URI(uri.toString()), String.class).getBody();
                jTickers = mapper.readTree(restResult).get("data").get("tickers");

                // Adding the tickers to db
                for (final JsonNode jTicker : jTickers) {
                    currentTicker = mapper.treeToValue(jTicker, Ticker.class);
                    tickerRepository.saveAndFlush(currentTicker);
                }
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        // EODs initialization
        if (eods().isEmpty()) {
            Map<String, Ticker> currentTickers = new HashMap<>();
            List<Ticker> tickers = tickers();
            JsonNode jEODs;
            EOD eod;

            for (int i=0; i < tickers.size(); i+=3) {
                uri = new StringBuilder(API_EOD_URL +
                        "access_key="+API_KEY +
                        "&limit="+ EOD_LIMIT +
                        "&date_from=" + START_DATE+
                        "&date_to=" + END_DATE +
                        "&symbols=");
                //Working on three tickers at the same time to limit the number of queries
                currentTickers.clear();
               for (int j = i; j < i+3; j++) {
                    try {
                        currentTicker = tickers.get(j);
                        currentTickers.put(currentTicker.getSymbol(), currentTicker);
                        uri.append(currentTicker.getSymbol());
                        uri.append(",");
                    } catch (IndexOutOfBoundsException ignored){} // Number of tickers wasn't a multiple of 3, that doesn't matter in the next
                }
                uri.deleteCharAt(uri.length()-1);
                // Getting 330 latest eods for the 3 current tickers
                try {
                    restResult = restTemplate.getForEntity(new URI(uri.toString()), String.class).getBody();
                    jEODs = mapper.readTree(restResult).get("data");
                    for (final JsonNode jEOD : jEODs) {
                        eod = mapper.treeToValue(jEOD, EOD.class);
                        eod.setSymbol(currentTickers.get(jEOD.get("symbol").textValue()));
                        eodRepository.saveAndFlush(eod);
                    }
                } catch (URISyntaxException | JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Ticker> tickers() {
        return tickerRepository.findAll();
    }

    public List<EOD> eods() {
        return eodRepository.findAll();
    }
}