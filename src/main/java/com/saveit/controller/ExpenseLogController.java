package com.saveit.controller;

import com.saveit.model.Category;
import com.saveit.model.Expense;
import com.saveit.service.ReportGenerator;
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
