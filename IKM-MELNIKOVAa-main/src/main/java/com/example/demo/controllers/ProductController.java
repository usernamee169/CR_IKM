package com.example.demo.controllers;

import com.example.demo.models.Product;
import com.example.demo.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * Контроллер для управления продуктами.
 * Обрабатывает запросы, связанные с отображением, созданием, редактированием и удалением продуктов.
 */
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    /**
     * Отображает список продуктов с возможностью фильтрации по названию.
     *
     * @param title  фильтр по названию продукта (может быть null)
     * @param model  объект Model для передачи данных в представление
     * @return имя представления для отображения списка продуктов
     */
    @GetMapping("/")
    public String products(@RequestParam(name = "title", required = false) String title, Model model) {
        model.addAttribute("products", productService.listProducts(title));
        return "products";
    }

    /**
     * Отображает подробную информацию о продукте.
     *
     * @param id     идентификатор продукта
     * @param model  объект Model для передачи данных в представление
     * @return имя представления с информацией о продукте
     * @throws ProductNotFoundException если продукт с указанным id не найден
     */
    @GetMapping("/product/{id}")
    public String productInfo(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт со следующим id не найден: " + id));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        return "product-info";
    }

    /**
     * Создает новый продукт.
     *
     * @param file1    изображение продукта
     * @param product  данные продукта
     * @return перенаправление на главную страницу
     * @throws IOException              если произошла ошибка при обработке файла
     * @throws IllegalArgumentException если переданы невалидные параметры
     */
    @PostMapping("/product/create")
    public String createProduct(@RequestParam("file1") MultipartFile file1,
                                Product product) throws IOException {
        validateProduct(product);
        validateFile(file1);
        productService.saveProduct(product, file1);
        return "redirect:/";
    }

    /**
     * Удаляет продукт.
     *
     * @param id идентификатор продукта для удаления
     * @return перенаправление на главную страницу
     * @throws ProductNotFoundException если продукт с указанным id не найден
     */
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        if (!productService.productExists(id)) {
            throw new ProductNotFoundException("Продукт со следующим id не найден: " + id);
        }
        productService.deleteProduct(id);
        return "redirect:/";
    }

    /**
     * Отображает форму редактирования продукта.
     *
     * @param id     идентификатор продукта
     * @param model  объект Model для передачи данных в представление
     * @return имя представления для редактирования продукта
     * @throws ProductNotFoundException если продукт с указанным id не найден
     */
    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт со следующим id не найден: " + id));
        model.addAttribute("product", product);
        return "product-edit";
    }

    /**
     * Обновляет информацию о продукте.
     *
     * @param id           идентификатор продукта
     * @param title        новое название продукта
     * @param description  новое описание продукта
     * @param price        новая цена продукта
     * @param genre        новый жанр продукта
     * @param author       новый автор продукта
     * @return перенаправление на страницу продукта
     * @throws ProductNotFoundException если продукт с указанным id не найден
     * @throws IllegalArgumentException если переданы невалидные параметры
     */
    @PostMapping("/product/update/{id}")
    public String updateProduct(@PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String description,
                                @RequestParam double price,
                                @RequestParam String genre,
                                @RequestParam String author) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException("Продукт со следующим id не найден: " + id));

        validateProductParameters(title, description, price, genre, author);

        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setGenre(genre);
        product.setAuthor(author);
        productService.saveProductWithoutImages(product);

        return "redirect:/product/" + id;
    }

    /**
     * Проверяет валидность параметров продукта.
     */
    private void validateProduct(Product product) {
        if (product == null || product.getTitle() == null || product.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }

        if (product.getPrice() <= 0) {
            throw new IllegalArgumentException("Цена продукта должна быть положительной");
        }
    }

    /**
     * Проверяет валидность файла изображения.
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл изображения не может быть пустым");
        }

        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Загружаемый файл должен быть изображением");
        }
    }

    /**
     * Проверяет валидность параметров обновления продукта.
     */
    private void validateProductParameters(String title, String description,
                                           double price, String genre, String author) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Название продукта не может быть пустым");
        }

        if (price <= 0) {
            throw new IllegalArgumentException("Цена продукта должна быть положительной");
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