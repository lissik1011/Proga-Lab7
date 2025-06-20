package command_process.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import command_process.helpfull.Validation;

public class LabWork implements Validation, Serializable{
    private static final long serialVersionUID = 7255001375888618425L;
    
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле не может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле может быть null
    private String user;

    // Для создания объектов пользователем
    public LabWork(String name, Coordinates coordinates,
        Integer minimalPoint, Difficulty difficulty, Person author){
        this.name = name;
        this.coordinates = coordinates;
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }

    // Для чтения из файла
    public LabWork(long id, String name, Coordinates coordinates, LocalDateTime creationDate,
        Integer minimalPoint, Difficulty difficulty, Person author){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.minimalPoint = minimalPoint;   
        this.difficulty = difficulty;
        this.author = author;
    }

    // Для обновления
    public LabWork(long id, LabWork labWork, LocalDateTime creationDate){
        this.id = id;
        this.name = labWork.getName();
        this.coordinates = labWork.getCoordinates();
        this.creationDate = creationDate;
        this.minimalPoint = labWork.getMinimalPoint();   
        this.difficulty = labWork.getDifficulty();
        this.author = labWork.getAuthor();
    }

    public LabWork(LabWork lab, String user) {
        this.id = lab.id;
        this.name = lab.name;
        this.coordinates = lab.coordinates;
        this.creationDate = lab.creationDate;
        this.minimalPoint = lab.minimalPoint;
        this.difficulty = lab.difficulty;
        this.author = lab.author;
        this.user = user;
    }
    
    @Override
    public String toString(){
        return String.format("LabWork{%n id: %d, %n name: %s,%n coordinates: %s,%n creationDate: %s,%n minimalPoint: %s,%n difficulty: %s,%n Person: {%s} %n User: %s}%n\n", 
            this.getId(), name, coordinates, 
            creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")),
            minimalPoint, difficulty,
            (author == null ? "null" : author),
            user);
    }

    @Override
    public boolean validate(){
        if (this.getId() <= 0) return false;
        else if (name == null || name.isEmpty()) return false;
        else if (coordinates == null || !coordinates.validate()) return false;
        else if (creationDate == null) return false;
        else if (minimalPoint <= 0) return false;
        else if (difficulty == null) return false;
        else if (author == null) return true;
        else if (!author.validate()) return false;
        return true;
    }

    // get
    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public Coordinates getCoordinates(){
        return coordinates;
    }
    public LocalDateTime getCreationDate(){
        return creationDate;
    }
    public Integer getMinimalPoint(){
        return minimalPoint;
    }
    public Difficulty getDifficulty(){
        return difficulty;
    }
    public Person getAuthor(){
        return author;
    }
    public String getUser() {
        return user;
    }

    // set
    public void setId(long id){
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void setMinimalPoint(Integer minimalPoint) {
        this.minimalPoint = minimalPoint;
    }
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
    public void setAuthor(Person author) {
        this.author = author;
    }
    public void setUser(String user) {
        this.user = user;
    }

}