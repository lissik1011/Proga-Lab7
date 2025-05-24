package command_process.commands;

import command_process.collection_manager.CollectionManager;
import command_process.data.LabWork;

// Очистить коллекцию.
public class Clear implements Command{
    @Override
    public String execute(String args, LabWork labwork) {

        if (CollectionManager.getDB().clear()) {
            CollectionManager.getLabWorks().clear();
            CollectionManager.clearIdSet();
            Info.counterOfMod();
    
            return "Коллекция очищена.";
        } else {
            return "Не удалось удалить элементы.";
        }
    }
}