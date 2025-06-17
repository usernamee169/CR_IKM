package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * Главный класс приложения, содержащий точку входа.
 * Использует Spring Boot для автоматической настройки и запуска приложения.
 */
@SpringBootApplication
public class SoftaApplication {

	/**
	 * Точка входа в приложение.
	 *
	 * @param args аргументы командной строки (могут быть null или пустыми)
	 * @throws RuntimeException если произошла ошибка при запуске приложения
	 * @throws IllegalArgumentException если переданные аргументы недопустимы
	 */
	public static void main(String[] args) {
		try {
			validateArguments(args);
			SpringApplication.run(SoftaApplication.class, args);
		}

		catch (Exception e) {
			throw new RuntimeException("Ошибка при запуске приложения", e);
		}
	}

	/**
	 * Проверяет аргументы командной строки.
	 *
	 * @param args аргументы командной строки
	 * @throws IllegalArgumentException если аргументы недопустимы
	 */
	private static void validateArguments(String[] args) {
		if (args != null) {
			for (String arg : args) {
				if (arg != null && arg.contains("--spring.config.name=")) {
					if (arg.length() <= "--spring.config.name=".length()) {
						throw new IllegalArgumentException("Не указано имя конфигурационного файла");
					}
				}
			}
		}
	}
}