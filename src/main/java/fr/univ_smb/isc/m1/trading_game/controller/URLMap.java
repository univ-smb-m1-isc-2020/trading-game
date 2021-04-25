package fr.univ_smb.isc.m1.trading_game.controller;

public class URLMap {
    public final static String userPrefix = "/user";
    public final static String adminPrefix = "/admin";

    public final static String loginPage = "/login";
    public final static String signupPage = "/signup";
    public final static String performLogin = "/perform_login";
    public final static String performSignup = "/perform_login";
    public final static String performLogout = "/perform_logout";

    public final static String userHomepage = userPrefix+"/home";
    public final static String joinGame = userPrefix+"/join_game";
    public final static String gameManager = userPrefix+"/game_manager";
    public final static String gameSelected = userPrefix+"/game_selected";
    public final static String createOrder = userPrefix+"/create_order";


    public final static String createGamePage = adminPrefix+"/create_game";
    public final static String performCreateGame = adminPrefix+"/perform_create_game";

    private URLMap(){};
}
