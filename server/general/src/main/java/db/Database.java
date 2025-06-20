package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;

import command_process.data.Coordinates;
import command_process.data.Difficulty;
import command_process.data.LabWork;
import command_process.data.Location;
import command_process.data.Person;
import users.HashUtil;
import users.User;

public class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/studs";
    private static final String dbLB = "LabWorks";
    private static final String dbPersons = "Persons";

    private final Connection connect;

    public Database(String user, String password) throws SQLException {
        this.connect = DriverManager.getConnection(URL, user, password);
    }

    private ResultSet executeSQL(PreparedStatement pst) {
        try {
            if (pst == null) {
                System.out.println("PreparedStatement равен null.");
                return null;
            }
            return pst.executeQuery();
        } catch (SQLException e) {
            System.out.println("Введен неверный запрос в исходном коде: executeSQL error.\n" + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private PreparedStatement getPst(String query) {
        try {
            return connect.prepareStatement(query);
        } catch (SQLException e) {
            System.out.println("Введен неверный запрос в исходном коде: executeSQL error.\n" + e.getMessage());
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    
    // Возвращает коллекцию с базы данных
    public Deque<LabWork> getLabWorks() {
        Deque<LabWork> labWorks = new ArrayDeque<>();
        String query = String.format("SELECT * FROM %s", dbLB);
        ResultSet resultSet = this.executeSQL(getPst(query));
        if (resultSet == null) return labWorks;

        try {
            while (resultSet.next()) {
                LabWork lab = getLabWork(resultSet);
                if (lab != null) labWorks.add(lab);
            }
            return labWorks;
        } catch (SQLException e) {
            System.out.println("Не удалось получить коллекцию с базы данных. Коллекция пустая.");
            return labWorks;
        }
    }


    // Возвраящает элемент (null) по id 
    public LabWork getLabWork(long id) {
        LabWork lab = null;
        String query = String.format("SELECT * FROM %s WHERE id = %s", dbLB, id);
        ResultSet resultSet = this.executeSQL(getPst(query));
        if (id == 0 || resultSet == null) return null;
        try {
            while (resultSet.next()) {
                lab = getLabWork(resultSet);
            }
            return lab;
        } catch (SQLException e) {
            System.out.println("");
            return lab;
        }
    }

    // Возвращает пароль по логину
    public byte[] getUserPW(String login) {
        byte[] pw = {0};
        String query = String.format("SELECT * FROM %s WHERE login = '%s'", dbPersons, login);
        ResultSet resultSet = this.executeSQL(getPst(query));
        try {
            while (resultSet.next()) {
                pw = resultSet.getBytes("password");
            }
            return pw;
        } catch (SQLException e) {
            System.out.println("");
            return pw;
        }
    }

    // Добавляет нового пользователя
    public void addUser(User user) {

        String query = "INSERT INTO " + dbPersons + " (login, password)" +
                        "VALUES (?, ?)";
    
        PreparedStatement pst = getPst(query);
        if (pst == null) {
            System.out.println("Не удалось создать PreparedStatement.");
        } else {
            try {
                // Заполняем основные поля
                pst.setString(1, user.getLogin());
                pst.setBytes(2, HashUtil.hashPassword(user.getPassword()));
        
                // Выполняем запрос и получаем ID
                pst.executeQuery();

            } catch (SQLException e) {
                if (!e.getMessage().equals("Запрос не вернул результатов.")) System.out.println("Ошбика добавления элемента в базу данных." + e.getMessage());
            }
        }
    }

    // Проверяет, есть ли такой логин
    public boolean checkLogin(String login) {
        byte[] pw = null;
        String query = String.format("SELECT * FROM %s WHERE login = '%s'", dbPersons, login);
        ResultSet resultSet = this.executeSQL(getPst(query));
        try {
            while (resultSet.next()) {
                pw = resultSet.getBytes("password");
            }
            if (pw != null) return false;
            return true;
        } catch (SQLException e) {
            System.out.println("");
            return true; // То есть log уже сущ-ет
        }
    }

    // Удаляет запись с id = arg
    public boolean removeId(long id, String sign, String login) {
        String query = String.format("DELETE FROM %s WHERE id %s %s AND \"user\" = '%s'", dbLB, sign, id, login);
        try {
            getPst(query).executeUpdate();
            return true;
        } catch (SQLException e) {  
            return false;
        }
    }


    // Удаляет все записи
    public boolean clear(String login) {
        String query = String.format("DELETE FROM %s WHERE \"user\" = '%s'", dbLB, login);
        try {
            getPst(query).executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // Обновляет элемент в базе данных и возвращает элемент (null).
    public LabWork update(LabWork labWork, long id, String login) {
        String query = String.format("UPDATE %s SET name = ?, coordinatesX = ?, coordinatesY = ?, minimalPoint = ?, difficulty = ?, authorName = ?, authorWeight = ?, authorPassportId = ?, authorLocationX = ?, authorLocationY = ?, authorLocationName = ? WHERE id = %s AND \"user\" = '%s'", dbLB, id, login);
    
        PreparedStatement pst = getPst(query);
        if (pst == null) {
            System.out.println("Не удалось создать PreparedStatement.");
            return null;
        }
    
        try {
            // Основные поля
            pst.setString(1, labWork.getName());
            pst.setInt(2, labWork.getCoordinates().getX());
            pst.setFloat(3, labWork.getCoordinates().getY());
            pst.setInt(4, labWork.getMinimalPoint());
            pst.setString(5, labWork.getDifficulty().toString());
    
            // Поля автора
            if (labWork.getAuthor() == null) {
                pst.setNull(6, Types.VARCHAR);  // authorName
                pst.setNull(7, Types.FLOAT);     // authorWeight
                pst.setNull(8, Types.VARCHAR);   // authorPassportId
                pst.setNull(9, Types.FLOAT);     // authorLocationX
                pst.setNull(10, Types.FLOAT);    // authorLocationY
                pst.setNull(11, Types.VARCHAR);  // authorLocationName
            } else {
                Person author = labWork.getAuthor();
                pst.setString(6, author.getName());
                pst.setObject(7, author.getWeight(), Types.FLOAT);
                pst.setString(8, author.getPassportID());
                pst.setObject(9, author.getLocation().getX(), Types.FLOAT);
                pst.setObject(10, author.getLocation().getY(), Types.FLOAT);
                pst.setString(11, author.getLocation().getName());
            }
            pst.executeUpdate();
            return getLabWork(id);

        } catch (SQLException e) {
            System.out.println("Ошбика обновления элемента в базе данных.");
            return null;
        }
    }


    // Добавляет элемент в базу данных и возвращает элемент (null).
    public LabWork add(LabWork labWork) {
        String query = "INSERT INTO " + dbLB + " (name, coordinatesX, coordinatesY, minimalPoint, difficulty, " +
                        "authorName, authorWeight, authorPassportId, authorLocationX, authorLocationY, authorLocationName, \"user\") " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
    
        PreparedStatement pst = getPst(query);
        if (pst == null) {
            System.out.println("Не удалось создать PreparedStatement.");
            return null;
        }
            
        try {
            // Заполняем основные поля
            pst.setString(1, labWork.getName());
            pst.setInt(2, labWork.getCoordinates().getX());
            pst.setFloat(3, labWork.getCoordinates().getY());
            pst.setInt(4, labWork.getMinimalPoint());
            pst.setString(5, labWork.getDifficulty().toString());
            
            // Поля автора
            if (labWork.getAuthor() == null) {
                pst.setNull(6, Types.VARCHAR);  // authorName
                pst.setNull(7, Types.FLOAT);   // authorWeight
                pst.setNull(8, Types.VARCHAR);   // authorPassportId
                pst.setNull(9, Types.FLOAT);     // authorLocationX
                pst.setNull(10, Types.FLOAT);    // authorLocationY
                pst.setNull(11, Types.VARCHAR);  // authorLocationName
            } else {
                Person author = labWork.getAuthor();
                pst.setString(6, author.getName());
                pst.setObject(7, author.getWeight(), Types.FLOAT);
                pst.setString(8, author.getPassportID());
                pst.setObject(9, author.getLocation().getX(), Types.FLOAT);
                pst.setObject(10, author.getLocation().getY(), Types.FLOAT);
                pst.setString(11, author.getLocation().getName());
            }
            pst.setString(12, labWork.getUser());
            
            // Выполняем запрос и получаем ID
            ResultSet resultSet = pst.executeQuery();
            long id = 0;
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }   
            return getLabWork(id);
        } catch (SQLException e) {
            System.out.println("Ошбика добавления элемента в базу данных.");
            System.out.println(e.getMessage());
            return null;
        }
    }


    // Возвращает элемент LabWork (null) с ResultSet
    public LabWork getLabWork(ResultSet resultSet) {
        LabWork labWork = null;
        try {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            int coordinatesX = resultSet.getInt("coordinatesX");
            float coordinatesY = resultSet.getFloat("coordinatesY");
            LocalDateTime creationDate = resultSet.getTimestamp("creationDate").toLocalDateTime();
            int minimalPoint = resultSet.getInt("minimalPoint");
            Difficulty difficulty = Difficulty.valueOf(resultSet.getString("difficulty"));
            String authorName = resultSet.getString("authorName");
            String user = resultSet.getString("user");
    
            if (authorName == null || authorName.isEmpty()) {
                labWork = new LabWork(id, name, new Coordinates(coordinatesX, coordinatesY),
                creationDate, minimalPoint, difficulty, null);
                return new LabWork(labWork, user);
            } else {
                Float authorWeight = resultSet.getFloat("authorWeight");
                String authorPassportId = resultSet.getString("authorPassportId");
                Float authorLocationX = resultSet.getFloat("authorLocationX");
                Double authorLocationY = resultSet.getDouble("authorLocationY");
                String authorLocationName = resultSet.getString("authorLocationName");
    
                labWork = new LabWork(id, name, new Coordinates(coordinatesX, coordinatesY),
                creationDate, minimalPoint, difficulty,
                new Person(authorName, authorWeight, authorPassportId,
                new Location(authorLocationX, authorLocationY, authorLocationName)));
            }
            return new LabWork(labWork, user);
        } catch (SQLException e) {
            System.out.println("Ошибка чтения элемента.");
        }
        return labWork;
    }
}

