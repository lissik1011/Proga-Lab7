package users;

import command_process.collection_manager.CollectionManager;


public class LogIn {
    public static String execute(User user) {
        if (!CollectionManager.getDB().checkLogin(user.getLogin())) { // Т.е логин сущ-ет
            if (HashUtil.isPasswordMatch(user.getPassword(), CollectionManager.getDB().getUserPW(user.getLogin()))) {
                return "Успешная авторизация";
            } else return "Неверный пароль.";
        } else {
            return "Пользователь не найден.";
        }
    }
}
