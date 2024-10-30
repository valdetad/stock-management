package com.example.StockManagement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig
{
//    @Bean
//    public AuthenticationProvider authProvider(){
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
//        return provider;
//    }


    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> users = new ArrayList<>();
        users.add(User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build());

        return new InMemoryUserDetailsManager(users);
    }
}
