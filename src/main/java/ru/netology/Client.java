package ru.netology;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String SERVER_ADDRESS = "localhost"; // адрес сервера
        final int PORT = Port.getPort(); // получаем порт

        try (Socket socket = new Socket(SERVER_ADDRESS, PORT);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {

            // Сначала ждем от сервера запрос на имя
            System.out.println(reader.readLine());  // сервер запросит имя
            String name = consoleReader.readLine();  // клиент вводит имя
            writer.println(name);  // отправляем имя на сервер

            // Поток для получения сообщений от сервера
            Thread receiveThread = new Thread(() -> {
                try {
                    String messageFromServer;
                    while ((messageFromServer = reader.readLine()) != null) {
                        System.out.println(messageFromServer);  // выводим сообщения от сервера
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при получении сообщений от сервера: " + e.getMessage());
                }
            });

            // Поток для отправки сообщений на сервер
            Thread sendThread = new Thread(() -> {
                try {
                    String messageToServer;
                    while (true) {
                        messageToServer = consoleReader.readLine();  // читаем сообщение с консоли

                        if ("/exit".equalsIgnoreCase(messageToServer)) {  // если команда выхода
                            writer.println("/exit");
                            break;  // выходим из цикла
                        }
                        writer.println(messageToServer);  // отправляем сообщение на сервер
                    }
                } catch (IOException e) {
                    System.err.println("Ошибка при отправке сообщения на сервер: " + e.getMessage());
                }
            });

            // Запускаем оба потока
            receiveThread.start();
            sendThread.start();

            // Ждем завершения обоих потоков
            receiveThread.join();
            sendThread.join();

        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка при подключении к серверу: " + e.getMessage());
        }
    }
}
