package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService implements UserDetailsService {//TODO test
    private PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        return repository.findTradingGameUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void register(String name, String password){
        if (userExists(name)) {
            return;
        }
        TradingGameUser user = new TradingGameUser(name, passwordEncoder.encode(password));
        repository.save(user);
    }

    private boolean userExists(String username){
        return repository.findTradingGameUserByUsername(username).isPresent();
    }
}
