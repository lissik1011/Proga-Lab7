package users;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeSet;

public class UserManager {

    public static Deque<User> users = new ArrayDeque<>();
    public static TreeSet<String> NikSet = new TreeSet<>();
    public static String login = "";

    public UserManager(String login){
        UserManager.login = login;
    }
    
    public static String getLogin(){
        return login;
    }
    public static void setLogin(String login){
        UserManager.login = login;
    }
    public void addUser(User user) {
        users.add(user);
    }

    public void addNikname(String nikname) {
        NikSet.add(nikname);
    }
}
