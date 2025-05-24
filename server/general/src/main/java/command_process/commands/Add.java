package command_process.commands;

import command_process.collection_manager.CollectionManager;
import command_process.data.LabWork;

// Добавить новый элемент в коллекцию.
public class Add implements Command{
    @Override
    public String execute(String args, LabWork labwork) {
        
        LabWork newLab = CollectionManager.getDB().add(labwork);

        CollectionManager.getLabWorks().add(newLab);
        
        if (newLab.getAuthor() != null) GroupCountingByAuthor.addAName(newLab.getAuthor().getName());
        CollectionManager.sort();
        CollectionManager.addIdInSet(newLab.getId());
        Info.saveLastDateOfMod();
        Info.counterOfMod();
        if (!Info.getDateOfColl()) Info.saveDateOfColl();   

        return "Элемент добавлен в коллекцию.";
    }
}