# Сетевой чат на Java

Это приложение реализует сетевой чат с использованием сокетов на языке Java. Чат состоит из сервера и клиентов, которые могут подключаться к серверу, отправлять и получать сообщения, а также выходить из чата. Все сообщения сохраняются в файл `file.log`, который обновляется при каждом отправлении или получении сообщения.

## Описание

- **Сервер** запускается на указанном порту и принимает подключения от клиентов.
- **Клиент** подключается к серверу, отправляет своё имя и может обмениваться сообщениями с другими клиентами.
- Все сообщения, включая подключения и выходы пользователей, логируются в файл `file.log`.
- В чате поддерживается команда `/exit` для выхода из чата.

## Структура проекта

Проект состоит из нескольких классов, каждый из которых отвечает за свою часть функционала:

### `Server.java`

Класс для запуска и управления сервером. Он слушает подключения клиентов, создает для каждого клиента отдельный поток и передает сообщения между ними.

- Запуск сервера на указанном порту из файла `settings.txt`.
- Обработка подключений клиентов и создание нового потока для каждого клиента.
- Организация рассылки сообщений всем клиентам, кроме отправителя.

### `Client.java`

Класс для работы клиента. Клиент подключается к серверу, вводит своё имя и может отправлять и получать сообщения.

- Получение имени от клиента через консоль.
- Отправка сообщений на сервер и получение сообщений от сервера в отдельных потоках.
- Возможность выхода из чата с помощью команды `/exit`.

### `ClientHandler.java`

Обработчик для каждого клиента, который работает в отдельном потоке на сервере. Он отвечает за прием сообщений от клиента и их рассылку другим пользователям.

- Приветствие клиента и запрос имени.
- Логирование сообщений и событий (подключение, выход, отправка сообщений) в файл `file.log`.
- Оповещение других клиентов о новых сообщениях и подключениях/выходах пользователей.

### `Port.java`

Класс для чтения настроек порта из файла `settings.txt`. Порт должен быть указан в формате `port=<номер порта>`.

### `Main.java`

Основной класс для запуска сервера. Он просто создает объект сервера и запускает его.

## Файлы конфигурации

### `settings.txt`

Файл, содержащий порт, на котором должен работать сервер. Пример содержимого:

### `file.log`

Файл для логирования всех сообщений и событий в чате, таких как подключения, отправленные и полученные сообщения.
