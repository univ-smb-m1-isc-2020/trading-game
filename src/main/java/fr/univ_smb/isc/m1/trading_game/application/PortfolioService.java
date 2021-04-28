package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PortfolioService {
    private final PortfolioRepository repository;
    private final OrderService orderService;
    private final TickerService tickerService;

    public PortfolioService(PortfolioRepository repository, OrderService orderService, TickerService tickerService) {
        this.repository = repository;
        this.orderService = orderService;
        this.tickerService = tickerService;
    }

    @PostConstruct
    public void init(){
        orderService.setPortfolioService(this);
    }

    public Portfolio create(int initialBalance) {
        Portfolio portfolio = new Portfolio(initialBalance);
        repository.saveAndFlush(portfolio);
        return portfolio;
    }

    public void applyOrders(long portfolioId, EOD dayData) {
        Portfolio portfolio = repository.findById(portfolioId).orElse(null);
        if(portfolio==null) return;
        for(Order o : portfolio.getOrders().stream().sorted(Comparator.comparingLong(Order::getId)).collect(Collectors.toList())){
            orderService.apply(o, dayData, portfolioId);
        }
    }

    public List<Order> getOrders(long portfolioId){
        Portfolio port = repository.findById(portfolioId).orElse(null);
        if(port==null) return new ArrayList<>();
        return port.getOrders().stream().sorted(Comparator.comparingLong(Order::getId)).collect(Collectors.toList());
    }

    public boolean buy(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = repository.findById(portfolioId).orElse(null);
        if(port == null) return false;
        Ticker ticker = tickerService.get(tickerMic);
        if(ticker == null) return false;
        int totalCost = unitPrice*quantity;
        int funds = port.getBalance();

        if(funds < totalCost) return false;

        int newQuantity = quantity;
        newQuantity+=port.getQuantity(ticker.getSymbol());
        port.setQuantity(ticker, newQuantity);
        port.setBalance(funds-totalCost);
        repository.saveAndFlush(port);
        return true;
    }

    public boolean sell(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = repository.findById(portfolioId).orElse(null);
        if(port==null)return false;
        Ticker ticker = tickerService.get(tickerMic);
        if(ticker==null)return false;
        if(quantity > port.getQuantity(ticker.getSymbol())) return false;
        int totalBenefits = unitPrice*quantity;
        int newQuantity = port.getQuantity(ticker.getSymbol());
        newQuantity-= quantity;
        port.setQuantity(ticker, newQuantity);
        port.setBalance(port.getBalance()+totalBenefits);
        repository.saveAndFlush(port);
        return true;
    }

    public void addOrder(long portfolioId, Order order) {
        Portfolio port = repository.findById(portfolioId).orElse(null);
        if(port==null || order==null)return;
        port.getOrders().add(order);
        repository.saveAndFlush(port);
    }

    public int getBalance(long portfolioId) {
        Portfolio port = repository.findById(portfolioId).orElse(null);
        if(port==null)return 0;
        return port.getBalance();
    }
}
