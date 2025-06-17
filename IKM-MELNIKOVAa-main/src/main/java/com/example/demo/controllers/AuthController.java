package com.example.demo.controllers;

import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;
import java.util.Collections;

/**
 * Контроллер для обработки запросов аутентификации и регистрации пользователей.
 */
@Controller
public class AuthController {
    private final UserService userService;

    /**
     * Конструктор с внедрением зависимости UserService.
     *
     * @param userService сервис для работы с пользователями
     * @throws IllegalArgumentException если userService равен null
     */
    @Autowired
    public AuthController(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService не может быть null");
        }

        this.userService = userService;
    }

    /**
     * Отображает страницу входа в систему.
     *
     * @return имя представления для страницы входа
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    /**
     * Отображает страницу регистрации нового пользователя.
     *
     * @return имя представления для страницы регистрации
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * Обрабатывает запрос на регистрацию нового пользователя.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return ModelAndView для перенаправления на страницу входа
     * @throws IllegalArgumentException если username или password пусты или содержат только пробелы
     * @throws RegistrationException если произошла ошибка при регистрации пользователя
     */
    @PostMapping("/register")
    public ModelAndView registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        validateCredentials(username, password);

        try {
            userService.registerUser(username, password, Collections.singletonList("ROLE_USER"));
            return new ModelAndView("redirect:/login");
        }

        catch (Exception e) {
            throw new RegistrationException("Ошибка при регистрации пользователя", e);
        }
    }

    /**
     * Проверяет валидность учетных данных.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @throws IllegalArgumentException если учетные данные невалидны
     */
    private void validateCredentials(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Пароль должен содержать не менее 8 символов");
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках регистрации пользователя.
     */
    public static class RegistrationException extends RuntimeException {
        public RegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}