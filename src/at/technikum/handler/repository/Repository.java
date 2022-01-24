package at.technikum.handler.repository;

public interface Repository<T> {

    T getItemById(String itemID);

    T insert(T item);

    T update(T item);

    boolean delete(T item);
}
