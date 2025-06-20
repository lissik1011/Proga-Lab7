package command_process.commands;

import java.util.Scanner;

import command_process.collection_manager.CollectionManager;
import command_process.data.LabWork;
import connection.UDPClient;
import read_queries.CreateSendableObject;
import read_queries.MakeRequest;
import read_queries.MakeResponse;
import users.UserManager;

// Добавить новый элемент в коллекцию.
public class Add implements Command{
    @Override
    public void execute(String args, Scanner scan) throws IllegalArgumentException{
        if (!args.isEmpty()) throw new IllegalArgumentException("Неизвестные аргументы. Введите help, чтобы узнать доступные команды.");
        if (UserManager.getLogStat()) {
            LabWork labwork = new CollectionManager().getLabWork(scan);
            String answ = MakeResponse.answer(UDPClient.sendAndReceive(MakeRequest.request(new CreateSendableObject("add", labwork))));
    
            if (!answ.isEmpty()) {
                System.out.println(answ);
            } else {
                System.out.println("Не удается подключиться к серверу.");
            }
        } else {
            System.out.println("Требуется авторизация.");
        }
    }
}
