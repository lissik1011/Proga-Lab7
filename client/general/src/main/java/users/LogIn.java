package users;

import java.util.Scanner;

import connection.UDPClient;
import read_queries.CreateSendableObject;
import read_queries.MakeRequest;
import read_queries.MakeResponse;

public class LogIn {
    public void execute(Scanner scan) {
        User user = new User(UserManager.getUserLogin(scan), UserManager.getUserPW(scan));
        String answ = MakeResponse.answer(UDPClient.sendAndReceive(MakeRequest.request(new CreateSendableObject("log_in", user))));

        if (!answ.isEmpty()) {
            if (answ.contains("Успешная авторизация")) {
                UserManager.setUser(user.getLogin(), user.getPassword(), true);
            } else System.out.println(answ);
        } else {
            System.out.println("Не удается подключиться к серверу.");
        }
    }
}

