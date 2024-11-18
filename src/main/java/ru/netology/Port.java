package ru.netology;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Port {

    // метод чтения порта из файла
   public static int getPort(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
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
}