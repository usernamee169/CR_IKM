package com.example.demo.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Сущность для хранения изображений в системе.
 * Содержит метаданные изображения и его бинарные данные.
 */
@Entity
@Table(name = "images")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    /**
     * Уникальный идентификатор изображения.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    /**
     * Внутреннее имя изображения.
     */
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Оригинальное имя файла при загрузке.
     */
    @Column(name = "originalFilename", nullable = false)
    private String originalFilename;

    /**
     * MIME-тип содержимого изображения.
     */
    @Column(name = "contentType", nullable = false)
    private String contentType;

    /**
     * Флаг, указывающий является ли изображение превью.
     */
    @Column(name = "isPreviewImage", nullable = false)
    private boolean isPreviewImage;

    /**
     * Размер изображения в байтах.
     */
    @Column(name = "size", nullable = false)
    private Long size;

    /**
     * Бинарные данные изображения.
     */
    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    /**
     * Продукт, к которому относится изображение.
     */
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * Проверяет валидность объекта изображения.
     *
     * @throws IllegalStateException если данные изображения невалидны
     */
    @PostLoad
    @PostPersist
    @PostUpdate
    public void validate() {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalStateException("Имя изображения не может быть пустым");
        }

        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalStateException("Оригинальное имя файла не может быть пустым");
        }

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalStateException("Недопустимый тип содержимого");
        }

        if (size == null || size <= 0) {
            throw new IllegalStateException("Размер файла должен быть положительным");
        }

        if (bytes == null || bytes.length == 0) {
            throw new IllegalStateException("Изображение не содержит данных");
        }

        if (product == null) {
            throw new IllegalStateException("Изображение должно быть связано с продуктом");
        }
    }
}