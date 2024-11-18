package ru.netology;

import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PortTest {

    @Test
        // тест, что метод getPort возвращает нужное значение
    void testGetPor() throws IOException {
        String testFileName = "test_settings.txt";
        try (FileWriter writer = new FileWriter(testFileName);
        ) {
            writer.write("port = 1010");
        }
        assertEquals(1010, Port.getPort(testFileName));
        new File(testFileName).delete();
    }

    @Test
        // тест ошибки если файл не найден
    void testGetPortNoFile() {
        assertEquals(-1, Port.getPort("non_existent_file.txt"));
    }


    @Test
        // тест на срабатывание ошибки чтения файла
    void testGetPortReadError() throws IOException {
        // МОКаем FileReader
        FileReader mockReader = mock(FileReader.class);

        // при вызове read() выбрасываем IOException
        when(mockReader.read()).thenThrow(new IOException("Test exception"));

        // проверяем, что метод выбрасывает IOException
        assertThrows(IOException.class, mockReader::read);
    }
}