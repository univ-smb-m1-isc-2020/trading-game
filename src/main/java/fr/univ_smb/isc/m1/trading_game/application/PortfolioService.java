package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.*;
import org.springframework.stereotype.Service;

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

    public void applyOrders(long portfolioId, EOD dayData) {
        Portfolio portfolio = repository.getOne(portfolioId);
        for(Order o : portfolio.getOrders()){
            orderService.apply(o, dayData, portfolioId);
        }
    }

    public boolean buy(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = repository.getOne(portfolioId);
        Ticker ticker = tickerService.get(tickerMic);
        int totalCost = unitPrice*quantity;
        int funds = port.getBalance();

        if(funds < totalCost) return false;

        int newQuantity = quantity;
        newQuantity+=port.getQuantity(ticker);
        port.setQuantity(ticker, newQuantity);
        port.setBalance(funds-totalCost);
        repository.save(port);
        return true;
    }

    public boolean sell(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = repository.getOne(portfolioId);
        Ticker ticker = tickerService.get(tickerMic);

        if(quantity > port.getQuantity(ticker)) return false;

        int totalBenefits = unitPrice*quantity;
        int newQuantity = quantity;
        newQuantity-=port.getQuantity(ticker);
        port.setQuantity(ticker, newQuantity);
        port.setBalance(port.getBalance()+totalBenefits);
        repository.save(port);
        return true;
    }
}
