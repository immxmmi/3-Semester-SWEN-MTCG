package at.technikum.repository;

public interface Repository<T> { // TODO: 07.01.2022 fertig

    T insert(T item);

    T update(T item);

    boolean delete(T item);
}
