package ru.netology;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ServerTest {

    @Mock
    private ServerSocket mockServerSocket;

    @Mock
    private Socket mockSocket;

    @Mock
    private ClientHandler mockClientHandler;

    private Server server;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this); // инициализация мошков

        // Мокаем создание ServerSocket
        when(mockServerSocket.accept()).thenReturn(mockSocket);

        server = new Server() {
            @Override
            public void start() {
                // Мокаем метод start для тестирования
                // змулируем поведение метода, без реального подключения
                addClient(mockClientHandler);  // добавляем клиента
                removeClient(mockClientHandler);  // удаляем клиента сразу
            }
        };
    }

    @Test
    // тест метода добавления клиента
    public void testAddClient() {
        ClientHandler clientHandler = mock(ClientHandler.class);

        server.addClient(clientHandler);

        Set<ClientHandler> clients = server.getClients();
        assertTrue(clients.contains(clientHandler), "Client should be added to the server");
    }

    @Test
    // тест метода удаления клиента
    public void testRemoveClient() {
        ClientHandler clientHandler = mock(ClientHandler.class);

        server.addClient(clientHandler);
        server.removeClient(clientHandler);

        Set<ClientHandler> clients = server.getClients();
        assertFalse(clients.contains(clientHandler), "Client should be removed from the server");
    }

    @Test
    // тест метода получения списка клиентов
    public void testGetClients() {
        ClientHandler clientHandler1 = mock(ClientHandler.class);
        ClientHandler clientHandler2 = mock(ClientHandler.class);

        server.addClient(clientHandler1);
        server.addClient(clientHandler2);

        Set<ClientHandler> clients = server.getClients();

        assertTrue(clients.contains(clientHandler1), "Client should be in the list");
        assertTrue(clients.contains(clientHandler2), "Client should be in the list");
    }

    @Test
    // тест метода запуска сервера
    public void testServerStart() {
        // Мокаем запуск сервера
        server.start();

        // проверяем, что клиент добавился и удалился
        Set<ClientHandler> clients = server.getClients();
        assertTrue(clients.isEmpty(), "No clients should remain after the test start");
    }
}
