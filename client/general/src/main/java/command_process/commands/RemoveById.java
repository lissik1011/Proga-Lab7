package command_process.commands;

import java.util.Scanner;

import connection.UDPClient;
import read_queries.CreateSendableObject;
import read_queries.MakeRequest;
import read_queries.MakeResponse;
import users.UserManager;

// Удалить элемент из коллекции по его id.
public class RemoveById implements Command{
    @Override
    public void execute(String args, Scanner scan) throws IllegalArgumentException{
        if (args.isEmpty()) throw new IllegalArgumentException("Введите id работы, которую хотите удалить.");
        
        if (UserManager.getLogStat()) {
            String answFirst = MakeResponse.answer(UDPClient.sendAndReceive(MakeRequest.request(new CreateSendableObject("find_id", args))));
            if (answFirst.isEmpty()) {
                String answ = MakeResponse.answer(UDPClient.sendAndReceive(MakeRequest.request(new CreateSendableObject("remove_by_id", args))));
                if (!answ.isEmpty()) {
                    System.out.println(answ);
                } else {
                    System.out.println("Не удается подключиться к серверу.");
                }
            } else {
                System.out.println(answFirst);
            }
        } else {
            System.out.println("Требуется авторизация.");
        }
    }
}
