package at.technikum.repository.util;

public interface Repository<T> { // TODO: 07.01.2022 fertig

    T getItemById(String itemID);

    T insert(T item);

    T update(T item);

    boolean delete(T item);
}
