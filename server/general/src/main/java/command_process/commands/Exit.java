package command_process.commands;

import command_process.data.LabWork;

// Завершить программу (без сохранения в файл).
public class Exit implements Command{
    @Override
    public String execute(String args, LabWork labwork) {
        return "Завершение работы программы.";
    }
}