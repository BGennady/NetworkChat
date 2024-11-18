package ru.netology;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class ClientHandler implements Runnable {
    private static final String LOG_FILE = "file.log"; // файл для записи
    final Socket clientSocket; // сокет клиента
    private final Server server; // ссылка на сервер
    BufferedReader reader; // чтение сообщений от клиента
    public PrintWriter writer; // отправка сообщений клиенту
    private String clientName; // имя клиента

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // инициализация потоков ввода/вывода
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // приветствие и запрос имени пользователя
            writer.println("Введите ваше имя для подключения:");
            clientName = reader.readLine();
            writer.println("Добро пожаловать, " + clientName + "!");

            // логируем подключение
            logMessage(clientName + " подключился к чату.");

            // оповещаем чат о подключении нового клиента
            broadcastMessage(clientName + " присоединился к чату.");

            // добавляем нового клиента в список
            server.addClient(this);

            String message;
            while ((message = reader.readLine()) != null) {
                // если клиент выходит
                if ("/exit".equalsIgnoreCase(message)) {
                    break;
                }

                // логируем каждое сообщение
                logMessage(clientName + ": " + message);

                // рассылка сообщения всем остальным клиентам
                broadcastMessage(clientName + ": " + message);
            }

        } catch (IOException e) {
            System.err.println("Ошибка при обработке клиента: " + e.getMessage());
        } finally {
            try {
                // удаление клиента из списка, если он отключился
                server.removeClient(this);

                // логируем выход клиента
                logMessage(clientName + " покинул чат.");

                // уведомление всех клиентов о выходе
                broadcastMessage(clientName + " покинул чат.");

                // закрытие соединения с клиентом
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии соединения с клиентом: " + e.getMessage());
            }
        }
    }

    // метод рассылки сообщений всем клиентам
    void broadcastMessage(String message) {
        Set<ClientHandler> clients = server.getClients(); // получаем текущий список клиентов
        for (ClientHandler clientHandler : clients) {
            if (clientHandler != this) { // не отправляем сообщение отправителю
                clientHandler.sendMessage(message);
            }
        }
    }

    // метод отправки сообщения конкретному клиенту
    public void sendMessage(String message) {
        writer.println(message);
    }

    // метод для логирования сообщений в файл
    void logMessage(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            bw.write("[" + timestamp + "] " + message);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Ошибка при записи в лог: " + e.getMessage());
        }
    }
}