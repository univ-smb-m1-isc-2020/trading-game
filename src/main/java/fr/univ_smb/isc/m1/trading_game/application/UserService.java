package fr.univ_smb.isc.m1.trading_game.application;

import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository repository) {
        this.passwordEncoder = passwordEncoder;
        this.repository = repository;
    }

    public TradingGameUser getCurrentUser(SecurityContext ctx){
        Authentication auth = ctx.getAuthentication();
        if(auth==null)return null;
        long uid = ((TradingGameUser) auth.getPrincipal()).getId();
        return repository.findById(uid).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        return repository.findTradingGameUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public boolean register(String name, String password){
        if (userExists(name)) {
            return false;
        }
        TradingGameUser user = new TradingGameUser(name, passwordEncoder.encode(password));
        repository.saveAndFlush(user);
        return true;
    }

    public boolean userExists(String username){
        return repository.findTradingGameUserByUsername(username).isPresent();
    }
}
