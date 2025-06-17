package com.example.demo.controllers;

import com.example.demo.models.Image;
import com.example.demo.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;

/**
 * Контроллер для работы с изображениями.
 * Предоставляет endpoint для получения изображений по их идентификаторам.
 */
@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepository imageRepository;

    /**
     * Возвращает изображение по его идентификатору.
     *
     * @param id идентификатор изображения
     * @return ResponseEntity с содержимым изображения и метаданными
     * @throws IllegalArgumentException если идентификатор равен null
     * @throws ImageNotFoundException если изображение с указанным id не найдено
     */
    @GetMapping("/images/{id}")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Идентификатор изображения не может быть null");
        }

        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException("Изображение с id " + id + " не найдено"));

        try {
            return ResponseEntity.ok()
                    .header("fileName", image.getOriginalFilename())
                    .contentType(MediaType.valueOf(image.getContentType()))
                    .contentLength(image.getSize())
                    .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
        }

        catch (Exception e) {
            throw new ImageProcessingException("Ошибка при обработке изображения", e);
        }
    }

    /**
     * Исключение, выбрасываемое когда изображение не найдено.
     */
    public static class ImageNotFoundException extends RuntimeException {
        public ImageNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках обработки изображения.
     */
    public static class ImageProcessingException extends RuntimeException {
        public ImageProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}