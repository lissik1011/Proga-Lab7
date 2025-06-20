package command_process.commands;

import java.util.ArrayDeque;
import java.util.Deque;

import command_process.data.LabWork;

// Вывести последние 12 команд (без их аргументов).
public class History implements Command {
    static Deque<String> history = new ArrayDeque<>();

    @Override
    public String execute(String args, LabWork labWork, String login) {
        String answ = "";
        if (!history.isEmpty()) {
            for (String e : history) {
                answ += e + "\n";
            }
            return answ;
        }
        return "Нет использованных команд";
    }

    public static void addHistory(String com){
        history.addLast(com);
        if (history.size() > 12) {
            history.pollFirst();
        }
    }
}