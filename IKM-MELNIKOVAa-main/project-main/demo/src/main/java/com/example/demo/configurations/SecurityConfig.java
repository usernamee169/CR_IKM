package com.example.demo.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Конфигурация безопасности приложения.
 * Настраивает аутентификацию, авторизацию.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Настраивает цепочку фильтров безопасности.
     *
     * @param http объект для настройки безопасности HTTP
     * @return сконфигурированная цепочка фильтров безопасности
     * @throws Exception если произошла ошибка при настройке
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        if (http == null) {
            throw new IllegalArgumentException("HttpSecurity не может быть null");
        }

        try {
            http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(authz -> authz
                            .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
                            .requestMatchers(new AntPathRequestMatcher("/login")).permitAll()
                            .anyRequest().authenticated()
                    )
                    .formLogin(form -> form
                            .loginPage("/login")
                            .defaultSuccessUrl("/", true)
                            .failureUrl("/login?error=true")
                    )
                    .logout(logout -> logout
                            .logoutUrl("/logout")
                            .logoutSuccessUrl("/login?logout=true")
                            .invalidateHttpSession(true)
                            .deleteCookies("JSESSIONID")
                    );
            return http.build();
        }

        catch (Exception e) {
            throw new SecurityConfigurationException("Ошибка при настройке SecurityFilterChain", e);
        }
    }

    /**
     * Создает кодировщик паролей.
     *
     * @return экземпляр BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Исключение, выбрасываемое при ошибках конфигурации безопасности.
     */
    public static class SecurityConfigurationException extends RuntimeException {
        public SecurityConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}