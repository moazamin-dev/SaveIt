package com.budget.controller;

import com.budget.model.Category;
import com.budget.model.Expense;
import com.budget.service.ReportGenerator;
import javafx.scene.Node;
import javafx.scene.control.TableView;

public class ExpenseLogController extends Controller {

    private TableView<Expense> expenseTable;
    private ReportGenerator reportGen;

    @Override
    public void initialize() {
        // TODO: implement
    }

    public void onFilterByCategory(Category category) {
        // TODO: implement
    }

    private void populateTable() {
        // TODO: implement
    }

    @Override
    public Node getViewNodes() {
        // TODO: implement
        return null;
    }
}
