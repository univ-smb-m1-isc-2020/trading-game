package fr.univ_smb.isc.m1.trading_game.application;


public class ChuckFact {

    private Long id;

    private String name;

    public ChuckFact(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
