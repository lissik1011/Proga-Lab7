package read_queries;

import java.io.File;
import java.io.Serializable;

import command_process.data.LabWork;
import users.User;
import users.UserManager;

public class CreateSendableObject implements Serializable{
    String command;
    String args = "";
    LabWork labWork = null;
    String login = "";
    User user = null;
    File file = null;

    public CreateSendableObject(String command){
        this.command = command;
        this.login = UserManager.getLogin();
    }

    public CreateSendableObject(String command, User user){
        this.command = command;
        this.user = user;
    }

    public CreateSendableObject(String command, String args){
        this.args = args;
        this.command = command;
    }

    public CreateSendableObject(String command, LabWork labWork){
        this.command = command;
        this.labWork = labWork;
    }

    public CreateSendableObject(String command, String args, LabWork labWork){
        this.command = command;
        this.args = args;
        this.labWork = labWork;
    }

    public CreateSendableObject(String command, File file){
        this.command = command;
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public String getCommand() {
        return command;
    }

    public String getArgs() {
        return args;
    }

    public LabWork getLabWork() {
        return labWork;
    }

    public User getUser() {
        return user;
    }

    public  String getLogin() {
        return login;
    }
}
