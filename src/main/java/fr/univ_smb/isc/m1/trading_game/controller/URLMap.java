package fr.univ_smb.isc.m1.trading_game.controller;

public class URLMap {
    public final static String USER_PREFIX = "/user";
    public final static String ADMIN_PREFIX = "/admin";

    public final static String LOGIN_PAGE = "/login";
    public final static String SIGNUP_PAGE = "/signup";
    public final static String PERFORM_LOGIN = "/perform_login";
    public final static String PERFORM_SIGNUP = "/perform_signup";
    public final static String PERFORM_LOGOUT = "/perform_logout";

    public final static String USER_HOMEPAGE = USER_PREFIX +"/home";
    public final static String JOIN_GAME = USER_PREFIX +"/join_game";
    public final static String PERFORM_JOIN_GAME = USER_PREFIX +"/perform_join_game";
    public final static String VIEW_GAME = USER_PREFIX +"/game_manager";
    public final static String CREATE_ORDER = USER_PREFIX +"/create_order";
    public final static String PERFORM_CREATE_ORDER = USER_PREFIX +"/perform_create_order";


    public final static String CREATE_GAME_PAGE = ADMIN_PREFIX +"/create_game";
    public final static String PERFORM_CREATE_GAME = ADMIN_PREFIX +"/perform_create_game";
    public final static String PERFORM_START_GAME = ADMIN_PREFIX +"/perform_start_game";

    private URLMap(){}
}
