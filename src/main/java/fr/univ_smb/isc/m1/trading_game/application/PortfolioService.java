package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.EOD;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Portfolio;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;
import org.springframework.stereotype.Service;

@Service
public class PortfolioService {

    public boolean buy(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = new Portfolio();//TODO get it from repository
        Ticker ticker = null;//TODO get from service
        int totalCost = unitPrice*quantity;
        int funds = port.getBalance();

        if(funds < totalCost) return false;

        int newQuantity = quantity;
        newQuantity+=port.getQuantity(ticker);
        port.setQuantity(ticker, newQuantity);
        port.setBalance(funds-totalCost);
        return true;
    }

    public boolean sell(long portfolioId, String tickerMic, int unitPrice, int quantity)
    {
        Portfolio port = new Portfolio();//TODO get it from repository
        Ticker ticker = null;//TODO get from service

        if(quantity > port.getQuantity(ticker)) return false;

        int totalBenefits = unitPrice*quantity;
        int newQuantity = quantity;
        newQuantity-=port.getQuantity(ticker);
        port.setQuantity(ticker, newQuantity);
        port.setBalance(port.getBalance()+totalBenefits);
        return true;
    }

}
