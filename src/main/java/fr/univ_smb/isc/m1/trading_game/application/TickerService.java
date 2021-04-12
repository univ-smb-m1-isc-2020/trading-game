package fr.univ_smb.isc.m1.trading_game.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TickerRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class TickerService {
    private final TickerRepository repository;

    public TickerService(TickerRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initialize() {
        if (repository.findAll().isEmpty()) {
            try {
                String string = new RestTemplate().getForEntity(new URI("http://api.marketstack.com/v1/exchanges/XPAR/tickers?access_key=176434db214273ad3282785f999c7d42"), String.class).getBody();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode tickers = mapper.readTree(string).get("data").get("tickers");
                for (final JsonNode jTicker : tickers) {
                    Ticker ticker = mapper.treeToValue(jTicker, Ticker.class);
                    repository.saveAndFlush(ticker);
                }
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Ticker> tickers() {
        return repository.findAll();
    }
}