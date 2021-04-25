package fr.univ_smb.isc.m1.trading_game.controller.security;

import fr.univ_smb.isc.m1.trading_game.application.UserService;
import fr.univ_smb.isc.m1.trading_game.controller.URLMap;
import fr.univ_smb.isc.m1.trading_game.controller.routers.UserController;
import fr.univ_smb.isc.m1.trading_game.infrastructure.persistence.TradingGameUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final EncodingService encodingService;

    public SecurityConfig(UserService userService, EncodingService encodingService) {
        this.userService = userService;
        this.encodingService = encodingService;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authenticationProvider(getProvider())
                .authorizeRequests()
                .antMatchers(URLMap.adminPrefix+"/**").hasRole(TradingGameUser.ADMIN_ROLE)
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage(URLMap.loginPage)
                .loginProcessingUrl(URLMap.performLogin)
                .defaultSuccessUrl(URLMap.userHomepage, false)
                .failureUrl(URLMap.loginPage)
                .and()
                .logout()
                .logoutUrl(URLMap.performLogout)
                .logoutSuccessUrl(URLMap.loginPage);
    }

    private AuthenticationProvider getProvider() {
        return new TradingGameAuthProvider(userService, encodingService.passwordEncoder(), encodingService);
    }
}
