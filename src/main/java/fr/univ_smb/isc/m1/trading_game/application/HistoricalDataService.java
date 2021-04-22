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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class HistoricalDataService {
    private final TickerRepository tickerRepository;
    private final EODRepository eodRepository;

    public HistoricalDataService(TickerRepository tickerRepository, EODRepository eodRepository) {
        this.tickerRepository = tickerRepository;
        this.eodRepository = eodRepository;
    }

    @PostConstruct
    public void initialize() {
        if (tickers().isEmpty()) {
            try {
                String restResult = new RestTemplate().getForEntity(new URI("http://api.marketstack.com/v1/exchanges/XPAR/tickers?access_key=176434db214273ad3282785f999c7d42"), String.class).getBody();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode tickers = mapper.readTree(restResult).get("data").get("tickers");
                for (final JsonNode jTicker : tickers) {
                    Ticker ticker = mapper.treeToValue(jTicker, Ticker.class);
                    tickerRepository.saveAndFlush(ticker);
                }
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        if (eods().isEmpty()) {
            String uri;
            String restResult;
            ObjectMapper mapper = new ObjectMapper();
            //for (final Ticker ticker : tickers()) {
            Ticker ticker = tickers().get(1);
            uri = "http://api.marketstack.com/v1/eod?access_key=176434db214273ad3282785f999c7d42&symbols=";
            uri += ticker.getSymbol();
            try {
                restResult = new RestTemplate().getForEntity(new URI(uri), String.class).getBody();
                JsonNode eods = mapper.readTree(restResult).get("data");
                for (final JsonNode jEOD : eods) {
                    EOD eod = mapper.treeToValue(jEOD, EOD.class);
                    eod.setSymbol(ticker);
                    eodRepository.saveAndFlush(eod);
                }
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
            }
            //}
        }
    }

    public List<Ticker> tickers() {
        return tickerRepository.findAll();
    }

    public List<EOD> eods() {
        return eodRepository.findAll();
    }
}