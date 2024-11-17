package ru.netology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Port {
    private static final String SETTINGS_FILE = "settings.txt";  // путь к файлу с настройками
    private static int port;  // переменная для хранения номера порта

    // метод чтения порта из файла
    private static int readPortFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(SETTINGS_FILE))) {
            String line = reader.readLine();
            if (line != null && !line.isEmpty()) {
                String[] parts = line.split("=");
                return Integer.parseInt(parts[1].trim());
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла настроек: " + e.getMessage());
        }
        return -1;  // -1, если порт не найден или произошла ошибка
    }

    // метод для получения порта
    public static int getPort() {
        if (port == 0) {
            port = readPortFromFile();  // Возвращаем номер порта, если еще не инициализирован
        }
        return port;
    }
}