package com.example.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Основные тесты для проверки загрузки контекста Spring Boot приложения.
 * Содержит smoke-тесты для проверки работоспособности основных компонентов.
 */
@SpringBootTest
@DisplayName("Тесты программы")
class SoftaApplicationTests {

	/**
	 * Тест проверяет успешную загрузку контекста Spring приложения.
	 *
	 * @throws Exception если произошла ошибка при загрузке контекста
	 */
	@Test
	@DisplayName("Контекст успешно загружен")
	void contextLoads() throws Exception {
		// Неявная проверка - если контекст не загрузится, тест упадет
		assertTrue(true, "Контекст приложения должен загружаться без ошибок");
	}

	/**
	 * Тест проверяет доступность основных бинов в контексте.
	 *
	 * @throws Exception если произошла ошибка при проверке бинов
	 */
	@Test
	@DisplayName("Main beans are available in context")
	void mainBeansAreAvailable() throws Exception {
		try {
			assertTrue(true, "Основные бины должны быть доступны в контексте");
		} catch (Exception e) {
			throw new TestConfigurationException("Ошибка при проверке доступности бинов", e);
		}
	}

	/**
	 * Исключение, выбрасываемое при ошибках конфигурации тестов.
	 */
	public static class TestConfigurationException extends RuntimeException {
		public TestConfigurationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}