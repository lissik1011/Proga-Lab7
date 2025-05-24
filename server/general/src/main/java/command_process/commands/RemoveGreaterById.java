package command_process.commands;

import command_process.collection_manager.CollectionManager;
import command_process.data.LabWork;

// Удалить из коллекции все элементы, превышающие заданный.
public class RemoveGreaterById implements Command{
    @Override
    public String execute(String args, LabWork labWork) {   

        try {
            long id = Long.parseLong(args);
            if (CollectionManager.getLabWorks().isEmpty()) {
                return "Коллекция пуста.";
            } else {
                if (CollectionManager.getDB().removeId(id, ">")) {
                    boolean removed = CollectionManager.getLabWorks().removeIf(lab -> lab.getId() > id);
                    CollectionManager.getIdSet().stream().toList()
                        .forEach(CollectionManager::removeId);                    
                    return removed 
                        ? "Элементы с Id больше " + id + " успешно удалены."
                        : "Лабораторных работ с Id, превышающими " + id + ", не найдено.";
                } else {
                    return "Не удалось удалить элементы.";
                }
                    
            }   
        } catch (NumberFormatException e) {
            return "Введен неверный id.";
        }
    }
}
