package command_process.commands;

import java.util.Scanner;

import connection.UDPClient;
import read_queries.CreateSendableObject;
import read_queries.MakeRequest;
import read_queries.MakeResponse;
import users.UserManager;

// Очистить коллекцию.
public class Clear implements Command{
    @Override
    public void execute(String args, Scanner scan) throws IllegalArgumentException{
        if (!args.isEmpty()) throw new IllegalArgumentException("Неизвестные аргументы. Введите help, чтобы узнать доступные команды.");

        if (UserManager.getLogStat()) {
            String answ = MakeResponse.answer(UDPClient.sendAndReceive(MakeRequest.request(new CreateSendableObject("clear"))));
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