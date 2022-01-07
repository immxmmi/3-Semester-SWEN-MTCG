package at.technikum.database;

public interface IDBTable<T> {

    T insert(T item);

    T update(T item);

    boolean delete(T item);
}
