package com.example.demo.services;

import com.example.demo.models.Image;
import com.example.demo.models.Product;
import com.example.demo.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для работы с продуктами.
 * Обеспечивает бизнес-логику для операций с продуктами и их изображениями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    /**
     * Возвращает список продуктов, отфильтрованный по названию (если указано).
     *
     * @param title часть названия для поиска (может быть null)
     * @return список продуктов, удовлетворяющих критериям поиска
     */
    public List<Product> listProducts(String title) {
        if (title != null && !title.trim().isEmpty()) {
            return productRepository.findByTitle(title);
        }

        return productRepository.findAll();
    }

    /**
     * Сохраняет продукт с прикрепленным изображением.
     *
     * @param product продукт для сохранения (не может быть null)
     * @param file1 файл изображения (может быть null)
     * @throws IOException если произошла ошибка при чтении файла
     * @throws IllegalArgumentException если продукт невалиден
     */
    public void saveProduct(Product product, MultipartFile file1) throws IOException {
        validateProduct(product);

        if (file1 != null && !file1.isEmpty()) {
            validateImageFile(file1);
            Image image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            product.addImageToProduct(image1);
        }

        log.info("Сохраняем новый продукт. Название: {}; Автор: {}", product.getTitle(), product.getAuthor());

        Product savedProduct = productRepository.save(product);
        setPreviewImageId(savedProduct);
    }

    /**
     * Устанавливает ID превью-изображения для продукта.
     *
     * @param product продукт для обновления
     * @throws ProductImageException если у продукта нет изображений
     */
    private void setPreviewImageId(Product product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            log.warn("У продукта нет изображения. Название: {}", product.getTitle());
            throw new ProductImageException("Продукт должен иметь хотя бы одно изображение");
        }

        product.setPreviewImageId(product.getImages().get(0).getId());
        productRepository.save(product);
    }

    /**
     * Преобразует файл в сущность Image.
     *
     * @param file файл изображения (не может быть null)
     * @return сущность Image
     * @throws IOException если произошла ошибка при чтении файла
     */
    private Image toImageEntity(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFilename(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }

    /**
     * Удаляет продукт по ID.
     *
     * @param id ID продукта для удаления (не может быть null)
     * @throws ProductNotFoundException если продукт не найден
     */
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Продукт со следующим id не найден: " + id);
        }

        productRepository.deleteById(id);
    }

    /**
     * Находит продукт по ID.
     *
     * @param id ID продукта (не может быть null)
     * @return найденный продукт
     * @throws ProductNotFoundException если продукт не найден
     */
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт со следующим id не найден: " + id));
    }

    /**
     * Сохраняет продукт без изображений.
     *
     * @param product продукт для сохранения (не может быть null)
     * @throws IllegalArgumentException если продукт невалиден
     */
    public void saveProductWithoutImages(Product product) {
        validateProduct(product);
        productRepository.save(product);
    }

    /**
     * Проверяет валидность продукта.
     *
     * @param product продукт для проверки
     * @throws IllegalArgumentException если продукт невалиден
     */
    private void validateProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не может быть пустым значением");
        }

        if (product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Именование продукта не может быть пустым значением");
        }

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена продукта должна быть положительным значением");
        }
    }

    /**
     * Проверяет валидность файла изображения.
     *
     * @param file файл для проверки
     * @throws IllegalArgumentException если файл невалиден
     */
    private void validateImageFile(MultipartFile file) {
        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Разрешены только изображения");
        }
    }

    /**
     * Исключение, выбрасываемое при ошибках работы с изображениями продукта.
     */
    public static class ProductImageException extends RuntimeException {
        public ProductImageException(String message) {
            super(message);
        }
    }

    /**
     * Исключение, выбрасываемое когда продукт не найден.
     */
    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}