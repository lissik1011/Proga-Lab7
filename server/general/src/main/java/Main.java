import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.concurrent.ForkJoinPool;

import accept_connections.UDPServer;
import command_process.collection_manager.CollectionManager;
import command_process.commands.Command;
import command_process.commands.CommandManager;
import command_process.commands.History;
import command_process.commands.MakeListOfCommands;
import db.Database;
import read_queries.CreateSendableObject;
import read_queries.MakeQueries;
import send_response.MakeResponse;
import send_response.SendResponse;
import users.LogIn;
import users.SignUp;


public class Main {
    static MakeListOfCommands list = new MakeListOfCommands();
    
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        if (args.length != 2 && args.length != 0) {
            System.out.println("Введите пользователя и пароль.");
            System.exit(0); 
        }
        
        CommandManager commandManager = new CommandManager();
        DatagramChannel channel = UDPServer.createChannel();
        if (channel == null) return;
        
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        ByteBuffer buffer = ByteBuffer.allocate(65500);
        
        Database db;
        int counter = 0;
        boolean connected = false;

        while (counter < 5) {
            try {
                if (args.length == 2) db = new Database(args[0], args[1]);
                else db = new Database("s466560", "er5DtMk5VuEXWO11");
                connected = true;
                new CollectionManager().setDB(db);
                break;
            } catch (SQLException e) {
                counter++;
                System.out.printf("Не удалось подключиться к базе данных. Попытка %d/5\n", counter);
            }
        }

        if (!connected) {
            if (args.length == 2) System.err.println("Все попытки подключения исчерпаны.\nПроверьте, что введены верные пользователь и пароль, а также не забудьте пробросить порт: `ssh -L 5432:localhost:5432 s******@helios.cs.ifmo.ru -p 2222` или иное.");
            else System.err.println("Все попытки подключения исчерпаны.\nНе забудьте пробросить порт: `ssh -L 5432:localhost:5432 s466560@helios.cs.ifmo.ru -p 2222`");
            System.exit(0);
        }
        
        System.out.println("Сервер запущен. Ожидание команд и подключений...");
        
        try {
            while (true) {
                // Обработка команд из консоли
                if (consoleReader.ready()) {
                    String command = consoleReader.readLine().trim();
                        commandManager.processCommand(command);
                }

                // Обработка UDP запросов
                SocketAddress clientAddress = channel.receive(buffer);
                if (clientAddress != null) {
                    buffer.flip();
                    byte[] receivedData = new byte[buffer.remaining()];
                    buffer.get(receivedData);
                    buffer.clear();

                    forkJoinPool.execute(() -> handleClientRequest(forkJoinPool, channel, clientAddress, receivedData));
                }
            }
        } catch (IOException  e) {
            System.out.println("Ошибка ввода/вывода. " + e.getMessage());
        }
        System.out.println("Конец программы. server/Main");
    }


    private static void handleClientRequest(ForkJoinPool pool, DatagramChannel channel, SocketAddress clientAddress, byte[] receivedData) {
        // Задача 1: десериализация запроса
        CreateSendableObject query;
        try {
            query = MakeQueries.answer(receivedData);
        } catch (Exception e) {
            System.err.println("Ошибка десериализации запроса.");
            return;
        }
    
        if (query == null) {
            pool.execute(() -> sendErrorResponse(channel, clientAddress, "Ошибка в обработке запроса."));
            return;
        }
    
        String commandName = query.getCommand();
        History.addHistory(commandName);
    
        // Задача 2: обработка команды
        pool.execute(() -> {
            try {
                String response;
                if (commandName.contains("log_in")) {
                    response = LogIn.execute(query.getUser());
                } else if (commandName.contains("sign_up")) {
                    response = SignUp.execute(query.getUser());
                } else {
                    Command command = list.takeList().get(commandName);
                    response = command.execute(query.getArgs(), query.getLabWork(), query.getLogin());
                }
    
                // Задача 3: отправка ответа
                pool.execute(() -> sendResponse(channel, clientAddress, response));
    
            } catch (Exception e) {
                pool.execute(() -> sendErrorResponse(channel, clientAddress, "Ошибка выполнения: " + e.getMessage()));
            }
        });
    }

    private static void sendResponse(DatagramChannel channel, SocketAddress clientAddress, String response) {
        byte[] responseData = MakeResponse.response(response);
        try {
            SendResponse.send(channel, clientAddress, responseData);
        } catch (IOException e) {
            System.err.println("Не удалось отправить ответ.");
        }
    }
    
    private static void sendErrorResponse(DatagramChannel channel, SocketAddress clientAddress, String message) {
        byte[] errorData = MakeResponse.response(message);
        try {
            SendResponse.send(channel, clientAddress, errorData);
        } catch (IOException e) {
            System.err.println("Не удалось отправить ошибку.");
        }
    }
}