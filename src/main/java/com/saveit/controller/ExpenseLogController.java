package com.saveit.controller;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Category;
import com.saveit.model.Expense;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ExpenseLogController extends Controller {

    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> colCategory;
    @FXML private TableColumn<Expense, Double> colAmount;
    @FXML private TableColumn<Expense, LocalDate> colDate;
    @FXML private TableColumn<Expense, String> colDescription;
    @FXML private ComboBox<Category> categoryFilter;
    @FXML private Label totalSpentLabel;
    private final ObservableList<Expense> expenseList = FXCollections.observableArrayList();
    private FilteredList<Expense> filteredData;
    private final ExpenseDAO expenseDAO = new ExpenseDAO();

    @Override
    public void initialize() {
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        filteredData = new FilteredList<>(expenseList, p -> true);
        expenseTable.setItems(filteredData);

        if (getUser() != null) {
            populateTable();
        }

        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        categoryFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            onFilterByCategory(newVal);
        });
    }

    private void updateTotalSpent() {
        double total = 0.0;

        for (Expense e : expenseList) {
            total += e.getAmount();
        }

        totalSpentLabel.setText(String.format("$%,.2Basef", total));

    }

    private void populateTable() {
        expenseList.clear();
        List<Expense> data = expenseDAO.getAll(getUser().getId());
        if (data != null) {
            expenseList.addAll(data);
        }
    }

    public void onFilterByCategory(Category category) {
        filteredData.setPredicate(expense -> {
            if (category == null) return true;

            return expense.getCategory().getName().equalsIgnoreCase(category.getName());
        });
    }

    @Override
    public Node getViewNodes() {
        return expenseTable;
    }
}