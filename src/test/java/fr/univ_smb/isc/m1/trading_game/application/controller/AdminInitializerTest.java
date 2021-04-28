package fr.univ_smb.isc.m1.trading_game.application.controller;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.security.AdminInitializer;
import org.apache.catalina.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class AdminInitializerTest {
    private UserService mockUserService;

    @BeforeEach
    public void setUp(){
        mockUserService = mock(UserService.class);
    }
    
    @Test
    public void initialize(){
        when(mockUserService.userExists("admin")).thenReturn(false);
        AdminInitializer initializer = new AdminInitializer(mockUserService);
        initializer.createDefaultAdmin();
        verify(mockUserService, times(1)).register("admin","admin", true);
    }

    @Test
    public void initializeExist(){
        when(mockUserService.userExists("admin")).thenReturn(true);
        AdminInitializer initializer = new AdminInitializer(mockUserService);
        initializer.createDefaultAdmin();
        verify(mockUserService, never()).register(any(),any(),anyBoolean());
    }
}
