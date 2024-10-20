package com.duoc.recetas;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/","recetas").permitAll()
                .requestMatchers("/**.css").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService users(){
        UserDetails usuario1 = User.builder()
            .username("usuario1")
            .password(passwordEncoder().encode("password1"))
            .roles("USER")
            .build();
        UserDetails usuario2 = User.builder()
            .username("usuario2")
            .password(passwordEncoder().encode("password2"))
            .roles("USER")
            .build();
        UserDetails admin = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin1"))
            .roles("ADMIN","USER")
            .build();
        return new InMemoryUserDetailsManager(usuario1,usuario2,admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
