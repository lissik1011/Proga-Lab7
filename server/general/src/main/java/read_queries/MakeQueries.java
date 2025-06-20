package read_queries;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class MakeQueries {

    // Создает запрос на выполнение команды
    public static CreateSendableObject answer(byte[] receiveData) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(receiveData))) {
            CreateSendableObject object =  (CreateSendableObject) ois.readObject();
            return object;
        } catch (ClassNotFoundException e) {
            System.out.println("Проблема десериализации");
            return null;
        } catch (IOException e) {
            System.out.println("Неверный формат данных. ПЕРВЫЙ Find me! server/read_queries/MakeQueries");
            return null;
        } catch (NullPointerException e) {
            System.out.println("Вернулся null объект с клиента");
            return null;
        }
    }
}
