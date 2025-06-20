package command_process.collection_manager;

import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import command_process.data.LabWork;
import db.Database;

public class CollectionManager {

    // Коллекции должны быть инициализированы заранее
    public static Deque<LabWork> labWorks = new ArrayDeque<>();
    public static TreeSet<Long> idSet = new TreeSet<>();
    public static Database db;

    /**
     * Сортирует коллекцию по координатам X и Y
     */
    public static void sort() {
        Comparator<LabWork> byCoordinates = Comparator
            .comparingDouble((LabWork lw) -> lw.getCoordinates().getX())
            .thenComparingDouble(lw -> lw.getCoordinates().getY());

        List<LabWork> labs = labWorks.stream()
                .sorted(byCoordinates)
                .collect(Collectors.toList());

        labWorks.clear();
        labWorks.addAll(labs);
    }

    /**
     * Возвращает коллекцию LabWork
     */
    public static Deque<LabWork> getLabWorks() {
        return labWorks;
    }

    /**
     * Возвращает множество ID элементов
     */
    public static TreeSet<Long> getIdSet() {
        return idSet;
    }

    /**
     * Пройтись по полю id элементов LabWorks и загрузить эти элементы в TreeSet<Long>
     * @param labWorks коллекция LabWork для извлечения ID
     * @return TreeSet<Long> — отсортированное множество ID
     */
    public static TreeSet<Long> getIdSet(Deque<LabWork> labWorks) {
        return labWorks.stream()
                .map(LabWork::getId)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Добавляет ID в множество
     */
    public static void addIdInSet(long id) {
        idSet.add(id);
    }

    /**
     * Удаляет ID из множества
     */
    public static void removeId(long id) {
        idSet.remove(id);
    }

    // Удаляет все id
    public static void clearIdSet(){
        idSet.clear();
    }

    /**
     * Находит LabWork по ID
     * @param id — искомый ID
     * @return LabWork или null, если не найден
     */
    public static LabWork findLabWorkById(long id) {
        return labWorks.stream()
                .filter(lab -> lab.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Проверяет наличие ID в множестве
     */
    public static boolean findId(long id) {
        System.out.println(idSet.toString());
        return idSet.contains(id);
    }

    public static void setLabWorks(Deque<LabWork> labWorks) {
        CollectionManager.labWorks = labWorks;
    }

    public void setDB(Database db){
        CollectionManager.db = db;
        labWorks.addAll(db.getLabWorks());
        idSet.addAll(getIdSet(labWorks));
    }

    public static Database getDB() {
        return db;
    }
}