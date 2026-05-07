package com.saveit.controller;

import com.saveit.model.Category;
import com.saveit.model.Expense;
import com.saveit.service.BudgetManager;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.time.LocalDate;
import java.util.List;

/**
 * @brief Controller for the Expense Log view.
 *
 * This class manages the display of historical expenses in a tabular format. It provides
 * functionality for filtering expenses by category, deleting specific transactions,
 * calculating total historical spending, and navigating to the expense creation view.
 */
public class ExpenseLogController extends Controller {

    /** @var HBox Expense_log The root container for the expense log view */
    @FXML private HBox Expense_log;

    /** @var TableView<Expense> expenseTable Table UI component to display expense records */
    @FXML private TableView<Expense> expenseTable;

    /** @var TableColumn<Expense, String> colCategory Column displaying the category name */
    @FXML private TableColumn<Expense, String> colCategory;

    /** @var TableColumn<Expense, Double> colAmount Column displaying the expense amount */
    @FXML private TableColumn<Expense, Double> colAmount;

    /** @var TableColumn<Expense, LocalDate> colDate Column displaying the transaction date */
    @FXML private TableColumn<Expense, LocalDate> colDate;

    /** @var TableColumn<Expense, String> colDescription Column displaying the expense description */
    @FXML private TableColumn<Expense, String> colDescription;

    /** @var ComboBox<Category> categoryFilter Dropdown menu for filtering table data by category */
    @FXML private ComboBox<Category> categoryFilter;

    /** @var Label totalSpentLabel Label displaying the sum of amounts currently in the list */
    @FXML private Label totalSpentLabel;

    /** @var ObservableList<Expense> expenseList The master list of expenses retrieved from the database */
    private final ObservableList<Expense> expenseList = FXCollections.observableArrayList();

    /** @var ObservableList<Category> categoryList The list of categories available for filtering */
    private final ObservableList<Category> categoryList = FXCollections.observableArrayList();

    /** @var FilteredList<Expense> filteredData A wrapper around expenseList that allows real-time UI filtering */
    private FilteredList<Expense> filteredData;

    /** @var BudgetManager manager The business logic service for budget and expense operations */
    private BudgetManager  manager;

    /**
     * @brief Initializes the controller, sets up table columns, and populates data.
     *
     * Configures the cell value factories for the TableView and sets up listeners
     * for the category filter dropdown.
     */
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

    /**
     * @brief Configures the category filter ComboBox.
     *
     * Adds an "All Categories" option and populates the remaining items from the
     * user's saved categories.
     */
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

    /**
     * @brief Calculates and updates the UI label for the total amount spent.
     */
    private void updateTotalSpent() {
        double total = 0.0;

        for (Expense e : expenseList) {
            total += e.getAmount();
        }

        totalSpentLabel.setText(String.format("$%.2f", total));

    }

    /**
     * @brief Fetches all historical expenses for the user and populates the observable list.
     */
    private void populateTable() {
        expenseList.clear();
        List<Expense> data = manager.getAllExpenses();
        if (data != null) {
            expenseList.addAll(data);
        }
    }

    /**
     * @brief Helper method to fetch categories from the manager.
     */
    private void setupCategory() {
        List<Category> data = manager.getCategories();
        if (data != null) {
            categoryList.addAll(data);
        }
    }

    /**
     * @brief Filters the table data based on the selected category.
     *
     * @param category The Category object selected in the filter ComboBox.
     */
    public void onFilterByCategory(Category category) {
        filteredData.setPredicate(expense -> {
            if (category == null || category.getName().equals("All Categories") || category.getCategoryID() == -1) {
                return true;
            }

            return expense.getCategory().getName().equalsIgnoreCase(category.getName());
        });
        updateTotalSpent();
    }

    /**
     * @brief Navigates the UI to the "Add Expense" view.
     */
    public void goToAddExpense() {
        Parent nextView = SceneController.getInstance().loadScene(ViewType.ADD_EXPENSE);

        if (nextView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) totalSpentLabel.getScene().getWindow();
            stage.getScene().setRoot(nextView);
        }
    }

    /**
     * @brief Handles the deletion of a selected expense from the table.
     *
     * Prompts the user for confirmation, deletes the record from the database
     * via BudgetManager, and updates the UI lists.
     */
    @FXML
    private void handleDeleteExpense() {
        Expense selectedExpense = expenseTable.getSelectionModel().getSelectedItem();

        if (selectedExpense == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setContentText("Please select a transaction from the table to delete.");
            alert.showAndWait();
            return;
        }

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Delete this transaction?");

        if (confirm.showAndWait().get() == javafx.scene.control.ButtonType.OK) {
            manager.deleteExpense(selectedExpense);

            expenseList.remove(selectedExpense);

            updateTotalSpent();
        }
    }
}