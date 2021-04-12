package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.Ticker;

import java.util.HashMap;

public class Portfolio {
    private int balance; //cents
    private final HashMap<Ticker, Integer> parts; // TODO Owned parts class

    public int getBalance() {
        return balance;
    }

    public int getQuantity(Ticker ticker){
        return parts.getOrDefault(ticker, 0);
    }

    public void setQuantity(Ticker ticker, int value){
        parts.put(ticker, value);
    }

    public Portfolio(int balance){
        this.balance = balance;
        parts = new HashMap<>();
    }

    public boolean canAfford(int i) {
        return i<=balance;
    }

    public void buy(Ticker symbol, int quantity, int totalCost){
        if(!canAfford(totalCost)) return;

        int newQuantity = quantity;
        newQuantity+=getQuantity(symbol);
        setQuantity(symbol, newQuantity);
        balance -= totalCost;
    }

    public boolean canSell(Ticker ticker, int quantity) {
        return getQuantity(ticker)>=quantity;
    }

    public void sell(Ticker symbol, int quantity, int benefits) {
        if(!canSell(symbol, quantity)) return;
        int newQuantity=getQuantity(symbol)-quantity;
        setQuantity(symbol, newQuantity);
        balance+=benefits;
    }
}
