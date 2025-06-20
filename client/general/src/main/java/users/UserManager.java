package users;

import java.util.Scanner;

import command_process.commands.ExecuteScript;
public class UserManager {

    static String login;
    static String password;
    static boolean logIn = false;

    public static void setUser(String login, String password, boolean logStat) {
        UserManager.login = login;
        UserManager.password = password;
        UserManager.logIn = logStat;
    }
    public static void setLogin(String login) {
        UserManager.login = login;
    }
    public static void setPW(String password) {
        UserManager.password = password;
    }
    public static void setLogStat(boolean logStat) {
        UserManager.logIn = logStat;
    }
    public static String getLogin(){
        return login;
    }
    public static String getPW(){
        return password;
    }
    public static boolean getLogStat() {
        return logIn;
    }

    public static String getUserLogin(Scanner scan) {
        boolean bool = true;
        while (bool) {
            if (ExecuteScript.getScannerType()) {
                System.out.print("Введите логин: ");
            }
            if (scan.hasNextLine()) {
                return scan.nextLine();
            } else {
                try (scan) {
                }
            }
        }
        try (scan) {
            return "";
        }
    }

    public static String getUserPW(Scanner scan) {
        boolean bool = true;
        while (bool) {
            if (ExecuteScript.getScannerType()) {
                System.out.print("Введите пароль: ");
            }
            if (scan.hasNextLine()) {
                return scan.nextLine();
            } else {
                try (scan) {
                }
            }
        }
        try (scan) {
            return "";
        }
    }
}
