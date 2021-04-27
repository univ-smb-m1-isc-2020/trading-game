package fr.univ_smb.isc.m1.trading_game.application.controller;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.controller.routers.AuthenticationController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {
    private final String USER_NAME = "TEST";
    private final String USER_PASSWORD = "12345";
    private final String REGISTER_NON_EXISTING_USER = "redirect:"+ URLMap.loginPage;
    private final String REGISTER_EXISTING_USER = "redirect:"+ URLMap.signupPage;
    private UserService mockUserService;
    private RedirectAttributes mockRedirectAttributes;

    @BeforeEach
    public void setUp(){
        mockUserService = mock(UserService.class);
        mockRedirectAttributes = mock(RedirectAttributes.class);
    }

    @Test
    public void performRegisterNonExistingUser(){
        when(mockUserService.register(USER_NAME, USER_PASSWORD)).thenReturn(true);
        AuthenticationController authenticationController = new AuthenticationController(mockUserService);
        Assertions.assertEquals(REGISTER_NON_EXISTING_USER,
                authenticationController.performRegister(USER_NAME, USER_PASSWORD, mockRedirectAttributes));
    }

    @Test
    public void performRegisterExistingUser(){
        when(mockUserService.register(USER_NAME, USER_PASSWORD)).thenReturn(false);
        AuthenticationController authenticationController = new AuthenticationController(mockUserService);
        Assertions.assertEquals(REGISTER_EXISTING_USER,
                authenticationController.performRegister(USER_NAME, USER_PASSWORD, mockRedirectAttributes));
    }
}
