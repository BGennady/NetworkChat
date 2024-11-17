package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket; //серверный сокет
    private static final int PORT = Port.getPort();

    // метод для запуска сервера
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.printf("Сервер запущен на порту: %d%n", PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept(); // в цикле ожидаем подключения клиента
                System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

                // создается новый обработчик для каждого клиента
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                // каждый клиент в своем потоке
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }
}
