package ru.netology;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final String TXT = "settings.txt";
    private ServerSocket serverSocket; // серверный сокет
    private static final int PORT = Port.getPort(TXT);
    private final Set<ClientHandler> clients = new HashSet<>(); // коллекция всех клиентов

    // метод для запуска сервера
    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.printf("Сервер запущен на порту: %d%n", PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept(); // в цикле ожидаем подключения клиента
                System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

                // создается новый обработчик для каждого клиента
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);

                // каждый клиент в своем потоке
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при запуске сервера: " + e.getMessage());
        }
    }

    // метод для добавления клиента в список
    public synchronized void addClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }

    // метод для удаления клиента из списка
    public synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    // метод для получения текущего списка клиентов
    public synchronized Set<ClientHandler> getClients() {
        return new HashSet<>(clients); // возвращаем копию для безопасности
    }
}