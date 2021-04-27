package fr.univ_smb.isc.m1.trading_game.application.services;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private final static long mockUserId = 0;
    private final static String encryptedPw = "encrypted";
    private TradingGameUser mockUser;
    private UserRepository mockRepository;
    private PasswordEncoder mockEncoder;

    @BeforeEach
    public void setUp(){
        mockUser = mock(TradingGameUser.class);
        mockRepository = mock(UserRepository.class);
        mockEncoder = mock(PasswordEncoder.class);
        when(mockUser.getId()).thenReturn(mockUserId);
        when(mockRepository.findById(mockUserId)).thenReturn(Optional.of(mockUser));
        when(mockRepository.findTradingGameUserByUsername(any())).thenReturn(Optional.empty());
        when(mockEncoder.encode(any())).thenReturn(encryptedPw);
    }

    @Test
    public void getCurrentUserNotLogged(){
        SecurityContext ctx = mock(SecurityContext.class);
        when(ctx.getAuthentication()).thenReturn(mock(Authentication.class));
        UserService service = new UserService(mockEncoder, mockRepository);
        TradingGameUser user = service.getCurrentUser(ctx);
        Assertions.assertNull(user);
    }

    @Test
    public void loadUserByName(){
        String mockName = "test";
        when(mockRepository.findTradingGameUserByUsername(mockName)).thenReturn(Optional.of(mockUser));
        UserService service = new UserService(mockEncoder, mockRepository);
        UserDetails user = service.loadUserByUsername(mockName);
        Assertions.assertEquals(mockUser, user);
    }

    @Test
    public void register(){
        String name = "test_name";
        String pw = "test_pw";
        UserService service = new UserService(mockEncoder, mockRepository);
        Assertions.assertTrue(service.register(name, pw));
        verify(mockRepository, times(1))
                .saveAndFlush(argThat(user -> user.getUsername().equals(name)
                        && user.getPassword().equals(encryptedPw)));
    }

    @Test
    public void registerExists(){
        String name = "test_name";
        String pw = "test_pw";
        when(mockRepository.findTradingGameUserByUsername(name)).thenReturn(Optional.of(mockUser));
        UserService service = new UserService(mockEncoder, mockRepository);
        Assertions.assertFalse(service.register(name, pw));
        verify(mockRepository, never()).saveAndFlush(any());
    }

    @Test
    public void userExists(){
        String name = "test_name";
        when(mockRepository.findTradingGameUserByUsername(name)).thenReturn(Optional.of(mockUser));
        UserService service = new UserService(mockEncoder, mockRepository);
        Assertions.assertTrue(service.userExists(name));
    }

    @Test
    public void userDoesntExists(){
        String name = "test_name";
        when(mockRepository.findTradingGameUserByUsername(name)).thenReturn(Optional.empty());
        UserService service = new UserService(mockEncoder, mockRepository);
        Assertions.assertFalse(service.userExists(name));
    }
}
