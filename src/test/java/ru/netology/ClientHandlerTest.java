package ru.netology;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ClientHandlerTest {

    @Mock
    private Server mockServer;

    @Mock
    private Socket mockSocket;

    @Mock
    private BufferedReader mockReader;

    @Mock
    private PrintWriter mockWriter;

    private ClientHandler clientHandler;

    private Path tempLogFile;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this); // Инициализация моков
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());
        when(mockServer.getClients()).thenReturn(new HashSet<>());

        // временный файл для логирования
        tempLogFile = Files.createTempFile("test-log", ".log");

        // создание клиента с перенаправленным логированием в этот файл
        clientHandler = new ClientHandler(mockSocket, mockServer) {
            @Override
            protected void logMessage(String message) {
                try (BufferedWriter writer = Files.newBufferedWriter(tempLogFile)) {
                    writer.write(message);  // Печатаем только текст без даты
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        clientHandler.writer = mockWriter;
        clientHandler.reader = mockReader;
    }
    @AfterEach
    public void tearDown() throws IOException {
        // удаляем временный файл после каждого теста
        Files.deleteIfExists(tempLogFile);
    }

    @Test
    // тестирования метода логирования
    public void testLogMessage() throws IOException {
        // Печатаем тестовое сообщение в лог
        clientHandler.logMessage("Test log message");

        // Проверяем, что файл содержит нужное сообщение
        String content = new String(Files.readAllBytes(tempLogFile));
        assertTrue(content.contains("Test log message"), "Log message not found in log file");
    }

    @Test
    public void testBroadcastMessage() {
        // создаем нескольких клиентов
        ClientHandler client1 = mock(ClientHandler.class);
        ClientHandler client2 = mock(ClientHandler.class);
        Set<ClientHandler> clients = new HashSet<>();
        clients.add(client1);
        clients.add(client2);

        // подключаем их к серверу
        when(mockServer.getClients()).thenReturn(clients);

        // отправляем сообщение всем клиентам
        clientHandler.broadcastMessage("Hello");

        // проверяем, что метод sendMessage был вызван для каждого клиента
        verify(client1).sendMessage("Hello");
        verify(client2).sendMessage("Hello");
    }
}
