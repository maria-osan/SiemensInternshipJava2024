package app.repository;

import java.util.List;

public interface IRepository<E> {
    void save(E entity);

    E update(E entity);

    List<E> findAll();
}
