package fr.univ_smb.isc.m1.trading_game.controller.security;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AdminInitializer {
    private final UserService userService;

    public AdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void createDefaultAdmin(){
        if(!userService.userExists("admin")){
            userService.register("admin", "admin", true);
        }
    }

}
