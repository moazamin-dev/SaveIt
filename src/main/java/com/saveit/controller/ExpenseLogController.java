package com.saveit.controller;

import com.saveit.model.Category;
import com.saveit.model.Expense;
import com.saveit.service.BudgetManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

public class ExpenseLogController extends Controller {

    @FXML private HBox Expense_log;
    @FXML private TableView<Expense> expenseTable;
    @FXML private TableColumn<Expense, String> colCategory;
    @FXML private TableColumn<Expense, Double> colAmount;
    @FXML private TableColumn<Expense, LocalDate> colDate;
    @FXML private TableColumn<Expense, String> colDescription;
    @FXML private ComboBox<Category> categoryFilter;
    @FXML private Label totalSpentLabel;
    private final ObservableList<Expense> expenseList = FXCollections.observableArrayList();
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private FilteredList<Expense> filteredData;
    private BudgetManager  manager;
    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());
        colCategory.setCellValueFactory(cell->(new SimpleObjectProperty<>(cell.getValue().getCategoryName())));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

        filteredData = new FilteredList<>(expenseList, p -> true);
        expenseTable.setItems(filteredData);

        if (getUser() != null) {
            populateTable();
            setupCategoryFilter();
        }

        expenseTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        categoryFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            onFilterByCategory(newVal);
        });
        updateTotalSpent();
    }

    private void setupCategoryFilter() {
        categoryList.clear();

        categoryList.add(new Category("All Categories", -1));

        List<Category> data = manager.getCategories();

        if (data != null && !data.isEmpty()) {
            categoryList.addAll(data);
        } else {
            System.out.println("DEBUG: manager.getCategories() returned null or empty for user: " + getUser().getId());
        }

        categoryFilter.setItems(categoryList);

        categoryFilter.setConverter(new javafx.util.StringConverter<Category>() {
            @Override
            public String toString(Category category) {
                return (category == null) ? "" : category.getName();
            }
            @Override
            public Category fromString(String string) { return null; }
        });

        categoryFilter.getSelectionModel().selectFirst();
    }

    private void updateTotalSpent() {
        double total = 0.0;

        for (Expense e : expenseList) {
            total += e.getAmount();
        }

        totalSpentLabel.setText(String.format("$%.2f", total));

    }

    private void populateTable() {
        expenseList.clear();
        List<Expense> data = manager.getAllExpenses();
        if (data != null) {
            expenseList.addAll(data);
        }
    }

    private void setupCategory() {
        List<Category> data = manager.getCategories();
        if (data != null) {
            categoryList.addAll(data);
        }
    }

    public void onFilterByCategory(Category category) {
        filteredData.setPredicate(expense -> {
            if (category == null || category.getName().equals("All Categories") || category.getCategoryID() == -1) {
                return true;
            }

            return expense.getCategory().getName().equalsIgnoreCase(category.getName());
        });
        updateTotalSpent();
    }

    public void goToAddExpense() {
        Parent nextView = SceneController.getInstance().loadScene(ViewType.ADD_EXPENSE);

        if (nextView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) totalSpentLabel.getScene().getWindow();
            stage.getScene().setRoot(nextView);
        }
    }

    @FXML
    private void handleDeleteExpense() {
        Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();

        if (selectedExpense == null) {
            // Show a simple warning if nothing is selected
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a transaction from the table to delete.");
            alert.showAndWait();
            return;
        }

        // Confirmation Dialog
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete this transaction?");

        if (confirm.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            // 1. Delete from DB via Manager
            manager.deleteExpense(selectedExpense);

            // 2. Remove from the ObservableList (Table updates automatically)
            expenseList.remove(selectedExpense);

            // 3. Update the dynamic total label
            updateTotalSpent();
        }
    }
}