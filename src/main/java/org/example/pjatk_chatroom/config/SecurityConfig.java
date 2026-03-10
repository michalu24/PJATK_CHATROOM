package org.example.pjatk_chatroom.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        /**
         * 1) Utwórz zmienną generatedPassword = passwordEncoder.encode("test")
         * 2) Utwórz nowy InMemoryUserDetailsManager
         * 3) Dodaj do niego kilku userów (np. Michal, Agnieszka, Krzysztof, Laura, Kate)
         * 4) Hasła użytkowników ustaw tak samo: generatedPassword
         * 5) role: "USER"
         * 6) zwróć utworzony InMemoryUserDetailsManager
         */
        String generatedPassword = passwordEncoder.encode("test");
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("Michal")
                .password(generatedPassword)
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Agnieszka")
                .password(generatedPassword)
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Krzysztof")
                .password(generatedPassword)
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Laura")
                .password(generatedPassword)
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("Kate")
                .password(generatedPassword)
                .roles("USER")
                .build());
        return manager;
    }

    @Bean
    @ConditionalOnMissingBean(AuthenticationEventPublisher.class)
    DefaultAuthenticationEventPublisher defaultAuthenticationEventPublisher(ApplicationEventPublisher delegate) {
        /**
         * 1) Utwórz i zwróć new DefaultAuthenticationEventPublisher(delegate)
         */
        return new DefaultAuthenticationEventPublisher(delegate);
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * 1) Skonfiguruj http.authorizeHttpRequests
         * 2) dla "/login" → permitAll
         * 3) dla "/images/image.png" → permitAll
         * 4) dla wszystkich innych requestów → authenticated()
         * 5) Skonfiguruj http.formLogin
         * 6) ustaw loginPage("/login")
         * 7) zbuduj i zwróć SecurityFilterChain
         */
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/login").permitAll()
                .requestMatchers("/images/image.png").permitAll()
                .anyRequest().authenticated()
        );
        http.formLogin(form -> form
                .loginPage("/login")
        );
        return http.build();
    }
}
