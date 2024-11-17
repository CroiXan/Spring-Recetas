package com.duoc.recetas.security;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomAuthenticationProvider authProvider;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .headers(headers -> headers
                .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", 
                    "default-src 'self'; " +
                    "script-src 'self' cdn.jsdelivr.net; " +
                    "style-src 'self' cdn.jsdelivr.net; " +
                    "img-src 'self' data:; " +
                    "connect-src 'self'; " +
                    "font-src 'self'; " +
                    "object-src 'none'; " +
                    "frame-ancestors 'none';" +
                    "form-action 'self';"
                ))
            )
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/","/recetas","busqueda","/error").permitAll()
                .requestMatchers("/gestor",
                                "/editar/**",
                                "/agregarreceta",
                                "/saveedit",
                                "/savefile",
                                "/addingrediente/**",
                                "/editingrediente/**",
                                "/editinstruccion/**",
                                "/addinstruccion/**").hasRole("ADMIN")
                .requestMatchers("/**.css").permitAll()
                .requestMatchers("/**/.darcs/**", "/**/.bzr/**", "/**/.hg/**", "/**/BitKeeper/**").denyAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .permitAll());

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
