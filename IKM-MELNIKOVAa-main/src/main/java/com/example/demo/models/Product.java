package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая продукт в системе.
 * Содержит информацию о продукте, его изображениях и владельце.
 */
@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    /**
     * Уникальный идентификатор продукта.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * Название продукта. Не может быть пустым.
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * Подробное описание продукта.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * Цена продукта. Должна быть положительной.
     */
    @Column(name = "price", nullable = false)
    private double price;

    /**
     * Жанр продукта.
     */
    @Column(name = "genre", length = 50)
    private String genre;

    /**
     * Автор продукта.
     */
    @Column(name = "author", length = 100)
    private String author;

    /**
     * Список изображений продукта.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
            mappedBy = "product", orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    /**
     * Идентификатор превью-изображения.
     */
    @Column(name = "preview_image_id")
    private Long previewImageId;

    /**
     * Дата и время создания продукта.
     */
    @Column(name = "date_of_created", nullable = false, updatable = false)
    private LocalDateTime dateOfCreated;

    /**
     * Пользователь, создавший продукт.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Инициализирует дату создания перед сохранением в БД.
     */
    @PrePersist
    private void init() {
        if (dateOfCreated == null) {
            dateOfCreated = LocalDateTime.now();
        }
    }

    /**
     * Добавляет изображение к продукту.
     *
     * @param image изображение для добавления
     * @throws IllegalArgumentException если изображение равно null
     */
    public void addImageToProduct(Image image) {
        if (image == null) {
            throw new IllegalArgumentException("Изображение не может быть null");
        }

        image.setProduct(this);
        images.add(image);
    }

    /**
     * Проверяет валидность состояния продукта.
     *
     * @throws IllegalStateException если продукт невалиден
     */
    @PostLoad
    @PostPersist
    @PostUpdate
    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalStateException("Название продукта не может быть пустым");
        }

        if (price <= 0) {
            throw new IllegalStateException("Цена продукта должна быть положительной");
        }

        if (user == null) {
            throw new IllegalStateException("Продукт должен быть связан с пользователем");
        }
    }

    /**
     * Устанавливает пользователя для продукта.
     *
     * @param user пользователь-владелец
     * @throws IllegalArgumentException если пользователь равен null
     */
    public void setUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не может быть null");
        }

        this.user = user;
    }
}