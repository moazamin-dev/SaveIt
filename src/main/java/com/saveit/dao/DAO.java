package com.saveit.dao;

import java.util.List;

public interface DAO<T> {

    void save(T entity);

    List<T> getAll(int id);

    void delete(int id);
}
