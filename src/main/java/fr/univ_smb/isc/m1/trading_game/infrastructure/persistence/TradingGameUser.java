package fr.univ_smb.isc.m1.trading_game.infrastructure.persistence;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class TradingGameUser implements UserDetails {
    public final static String USER_ROLE = "USER";
    public final static String ADMIN_ROLE = "ADMIN";

    @Id
    @GeneratedValue
    private long id;
    private String username;
    private String password;
    private boolean isAdmin;

    public TradingGameUser(){
        // JPA
    }

    public TradingGameUser(String username, String password) {
        this.username = username;
        this.password = password;
        this.isAdmin = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long userId) {
        this.id = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority(isAdmin?ADMIN_ROLE:USER_ROLE));
        return list;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
