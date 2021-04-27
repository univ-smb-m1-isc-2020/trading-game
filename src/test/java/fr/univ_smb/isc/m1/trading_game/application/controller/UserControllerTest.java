package fr.univ_smb.isc.m1.trading_game.application.controller;

import fr.univ_smb.isc.m1.trading_game.application.GameService;
import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.controller.routers.UserController;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.ui.Model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private final String HOME_PAGE_COMMON = "homePageCommon";
    private final String HOME_PAGE_PLAYER = "redirect:"+ URLMap.userHomepage;
    private TradingGameUser mockUser;
    private Model mockModel;
    private GameService mockGameService;
    private UserService mockUserService;
    private SecurityContext mockSecurityContext;

    @BeforeEach
    public void setUp(){
        mockUser = mock(TradingGameUser.class);
        mockModel = mock(Model.class);
        mockGameService = mock(GameService.class);
        mockUserService = mock(UserService.class);
        mockSecurityContext = mock(SecurityContext.class);
    }

    @Test
    public void indexAnonymous(){
        when(mockUserService.getCurrentUser(mockSecurityContext)).thenReturn(null);
        UserController userController = new UserController(mockGameService, mockUserService);
        Assertions.assertEquals(HOME_PAGE_COMMON, userController.index(mockModel));
    }

    /*@Test
    public void indexLoggedPlayer(){ // TODO : voir comment mock une classe static si on a le temps
        when(mockUserService.getCurrentUser(mockSecurityContext)).thenReturn(mockUser);
        UserController userController = new UserController(mockGameService, mockUserService);
        Assertions.assertEquals(HOME_PAGE_PLAYER, userController.index(mockModel));
    }*/
}
