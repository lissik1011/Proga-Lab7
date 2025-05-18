import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import accept_connections.UDPServer;
import command_process.commands.Command;
import command_process.commands.CommandManager;
import command_process.commands.History;
import command_process.commands.MakeListOfCommands;
import read_queries.CreateSendableObject;
import read_queries.MakeQueries;
import send_response.MakeResponse;
import send_response.SendResponse;


public class Main {
    static MakeListOfCommands list = new MakeListOfCommands();

    public static void main(String[] args) {

        CommandManager commandManager = new CommandManager();
        DatagramChannel channel = UDPServer.createChannel();
        if (channel == null) return;

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        ByteBuffer buffer = ByteBuffer.allocate(65500); 
        
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

                    CreateSendableObject query = MakeQueries.answer(receivedData);
                    Command command = list.takeList().get(query.getCommand());
                    System.out.println("Выполнение команды " + query.getCommand());
                    History.addHistory(query.getCommand());
                    String response = command.execute(query.getArgs(), query.getLabWork());

                    SendResponse.send(channel, clientAddress, MakeResponse.response(response));
                }
            }
        } catch (IOException  e) {
            System.out.println(e.getMessage());
        }
        System.out.println("GTHDSQ. server/Main");
    }
}