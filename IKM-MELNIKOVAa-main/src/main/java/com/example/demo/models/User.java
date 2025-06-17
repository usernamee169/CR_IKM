package com.example.demo.models;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сущность пользователя системы, реализующая интерфейс UserDetails для Spring Security.
 * Содержит информацию об учетных данных пользователя, его ролях и связанных продуктах.
 */
@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    /**
     * Уникальный идентификатор пользователя.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    /**
     * Уникальное имя пользователя для входа в систему.
     * Не может быть null или пустым.
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * Зашифрованный пароль пользователя.
     * Не может быть null или пустым.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Роли пользователя в системе.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    private Collection<String> roles = new ArrayList<>();

    /**
     * Список продуктов, созданных пользователем.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY
    )
    private List<Product> products = new ArrayList<>();

    /**
     * Дата и время создания пользователя.
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Конструктор по умолчанию.
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Создает нового пользователя с указанными учетными данными.
     *
     * @param username имя пользователя
     * @param password пароль
     * @param roles    роли пользователя
     * @throws IllegalArgumentException если имя пользователя или пароль пусты
     */
    public User(String username, String password, Collection<String> roles) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Имя пользователя не может быть пустым");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        this.username = username;
        this.password = password;
        this.roles = new ArrayList<>(roles);
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Возвращает список прав доступа пользователя.
     *
     * @return коллекция объектов GrantedAuthority
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());
    }

    /**
     * Проверяет, не истек ли срок действия учетной записи.
     *
     * @return true если учетная запись активна
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись.
     *
     * @return true если учетная запись не заблокирована
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных.
     *
     * @return true если учетные данные действительны
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, активен ли пользователь.
     *
     * @return true если пользователь активен
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Добавляет продукт к списку продуктов пользователя.
     *
     * @param product продукт для добавления
     * @throws IllegalArgumentException если продукт равен null
     */
    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не может быть null");
        }

        product.setUser(this);
        products.add(product);
    }

    /**
     * Проверяет валидность состояния пользователя.
     *
     * @throws IllegalStateException если пользователь невалиден
     */
    @PostLoad
    @PostPersist
    @PostUpdate
    public void validate() {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalStateException("Имя пользователя не может быть пустым");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalStateException("Пароль не может быть пустым");
        }

        if (roles == null || roles.isEmpty()) {
            throw new IllegalStateException("Пользователь должен иметь хотя бы одну роль");
        }
    }
}