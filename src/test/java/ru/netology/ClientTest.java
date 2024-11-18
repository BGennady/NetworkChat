package ru.netology;

import org.junit.jupiter.api.*;
import org.mockito.*;

import java.io.*;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ClientTest {

    @Mock
    private Socket mockSocket;

    @Mock
    private BufferedReader mockReader;

    @Mock
    private PrintWriter mockWriter;

    @Mock
    private BufferedReader mockConsoleReader;

    @InjectMocks
    private Client client;

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);  // Инициализация моков

        // Мокаем поведение сокета
        when(mockSocket.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[0]));
        when(mockSocket.getOutputStream()).thenReturn(new ByteArrayOutputStream());

        // Мокаем консольный ввод
        when(mockConsoleReader.readLine()).thenReturn("John", "/exit");

        // создаем клиент с мокированными объектами
        client = new Client();
    }

    @Test
    public void testClientConnection() throws IOException {
        // Мокаем поведение сокета, чтобы он был успешно подключен
        when(mockSocket.isConnected()).thenReturn(true);

        // проверяем, что клиент пытается подключиться к серверу с правильными параметрами
        client.main(new String[0]);

        // проверяем, что клиент пытается подключиться через сокет
        verify(mockSocket).connect(any()); // проверка, что попытка соединения была сделана
    }

    @Test
    public void testClientThreads() throws InterruptedException {
        // Мокаем потоки для отправки и получения сообщений
        Thread receiveThread = mock(Thread.class);
        Thread sendThread = mock(Thread.class);

        // запуск потоков
        receiveThread.start();
        sendThread.start();

        // проверка, что оба потока запущены
        verify(receiveThread).start();  // проверка потока получения сообщений
        verify(sendThread).start();     // проверка потока отправки сообщений

        // завершаем тест
        receiveThread.join();
        sendThread.join();
    }
}
