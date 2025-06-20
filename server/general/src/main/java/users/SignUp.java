package users;

import command_process.collection_manager.CollectionManager;


public class SignUp {
    public static String execute(User user) {
        if (!CollectionManager.getDB().checkLogin(user.getLogin())) { // Т.е такой логин уже есть
            return "Такой пользователь уже существует.";
        } else {
            CollectionManager.getDB().addUser(user);
            return "Вы зарегестрированы.";
        }
    }    
}
