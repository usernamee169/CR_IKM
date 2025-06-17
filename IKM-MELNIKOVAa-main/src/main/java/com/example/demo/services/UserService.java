package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * Сервис для работы с пользователями.
 * Реализует интерфейс UserDetailsService
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Конструктор сервиса пользователей.
     *
     * @param userRepository репозиторий пользователей (не может быть null)
     * @param passwordEncoder кодировщик паролей (не может быть null)
     * @throws IllegalArgumentException если любой из параметров равен null
     */
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        if (userRepository == null) {
            throw new IllegalArgumentException("UserRepository cannot be null");
        }

        if (passwordEncoder == null) {
            throw new IllegalArgumentException("PasswordEncoder cannot be null");
        }

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param username имя пользователя (не может быть null или пустым)
     * @param password пароль пользователя (не может быть null или пустым)
     * @param roles список ролей пользователя (не может быть null или пустым)
     * @return зарегистрированный пользователь
     * @throws IllegalArgumentException если любой из параметров невалиден
     * @throws UserRegistrationException если не удалось зарегистрировать пользователя
     */
    public User registerUser(String username, String password, List<String> roles) {
        validateUserCredentials(username, password, roles);

        try {
            User user = new User(username, passwordEncoder.encode(password), roles);
            return userRepository.save(user);
        }

        catch (Exception e) {
            throw new UserRegistrationException("Failed to register user: " + username, e);
        }
    }


    /**
     * Проверяет валидность учетных данных пользователя.
     *
     * @param username имя пользователя
     * @param password пароль
     * @param roles список ролей
     * @throws IllegalArgumentException если учетные данные невалидны
     */
    private void validateUserCredentials(String username, String password, List<String> roles) {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        if (!StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Пользователю должна быть присвоена хотя бы одна роль");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Пароль должен быть не короче 8 символоов");
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках регистрации пользователя.
     */
    public static class UserRegistrationException extends RuntimeException {
        public UserRegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Загружает пользователя по имени пользователя.
     *
     * @param username имя пользователя для поиска (не может быть null или пустым)
     * @return найденный пользователь
     * @throws IllegalArgumentException если username равен null или пуст
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!StringUtils.hasText(username)) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с именем '%s' не найден", username)));
    }
}