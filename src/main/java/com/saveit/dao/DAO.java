package com.budget.dao;

import java.util.List;

public interface DAO<T> {

    void save(T entity);

    List<T> getAll();

    void delete(int id);
}
