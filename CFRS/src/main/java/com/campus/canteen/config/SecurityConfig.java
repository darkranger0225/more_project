package com.campus.canteen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .antMatchers("/", "/login", "/register", "/profile", "/password", "/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .antMatchers("/api/auth/**", "/api/user/**", "/api/dishes/**", "/api/windows/**", "/api/reviews/**").permitAll()
                .antMatchers("/dishes", "/dish/**", "/windows", "/window/**").permitAll()
                .antMatchers("/favorite/**", "/tag/**", "/notification/**").permitAll()
                .antMatchers("/admin/**", "/api/admin/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            .and()
            .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll();
        
        return http.build();
    }
}
