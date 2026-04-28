package com.saveit.dao;

import com.saveit.model.Expense;
import java.sql.Connection;
import java.util.List;

public class ExpenseDAO implements DAO<Expense> {

    private Connection connection;

    @Override
    public void save(Expense e) {
        // TODO: implement
    }

    @Override
    public List<Expense> getAll() {
        // TODO: implement
        return null;
    }

    @Override
    public void delete(int id) {
        // TODO: implement
    }
}
