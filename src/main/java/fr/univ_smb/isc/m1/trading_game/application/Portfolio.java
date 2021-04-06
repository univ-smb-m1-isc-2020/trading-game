package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.HashMap;

public class Portfolio {
    private int balance; //cents
    private HashMap<Ticker, Integer> parts;

    public Portfolio(int balance){
        this.balance = balance;
        parts = new HashMap<>();
    }

    public boolean canAfford(int i) {
        return i<=balance;
    }

    public void buy(Ticker symbol, int quantity, int totalCost){
        int newQuantity = quantity;
        if(parts.containsKey(symbol)){
            newQuantity+=parts.get(symbol);
        }
        parts.put(symbol, newQuantity);
        balance -= totalCost;
    }

    public boolean canSell(Ticker ticker, int quantity) {
        if(!parts.containsKey(ticker)) return false;
        else return parts.get(ticker)<=quantity;
    }

    public void sell(Ticker symbol, int quantity, int benefits) {
        int newQuantity=parts.get(symbol)-quantity;
        parts.put(symbol, newQuantity);
        balance+=benefits;
    }
}
