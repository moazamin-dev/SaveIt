package com.saveit.controller;

import com.saveit.model.Expense;
import com.saveit.service.BudgetManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @brief Controller responsible for managing the main dashboard view.
 *
 * This class coordinates the display of high-level budget metrics, including
 * remaining funds, daily limits, and cycle dates. It also manages the population
 * of tables showing recent transactions and spending summarized by category.
 */
public class MainController extends Controller {

    /** @var BorderPane DashBoard The root container for the dashboard UI */
    @FXML
    private BorderPane DashBoard;

    /** @var Label overviewLabel Displays the user's name or a greeting */
    @FXML private Label overviewLabel;

    /** @var Label remainingLabel Displays the total remaining budget for the cycle */
    @FXML private Label remainingLabel;

    /** @var Label dailyLimitLabel Displays the calculated amount allowed per day */
    @FXML private Label dailyLimitLabel;

    /** @var Label cycleLimitLabel Displays the total budget limit set for the cycle */
    @FXML private Label cycleLimitLabel;

    /** @var Label startDate Displays the start date of the current cycle */
    @FXML private Label startDate;

    /** @var Label endDate Displays the end date of the current cycle */
    @FXML private Label endDate;

    /** @var TableView categorySpendingBox Table showing spending totals grouped by category */
    @FXML private TableView<Map.Entry<String, Double>> categorySpendingBox;

    /** @var TableColumn categorySpendingColumn Column for the category name */
    @FXML private TableColumn<Map.Entry<String, Double>, String> categorySpendingColumn;

    /** @var TableColumn amountSpendingColumn Column for the total amount spent in a category */
    @FXML private TableColumn<Map.Entry<String, Double>, Double> amountSpendingColumn;

    /** @var TableView recentTransactions Table showing the list of individual recent expenses */
    @FXML private TableView<Expense> recentTransactions;

    /** @var TableColumn EidCol Column for the expense ID */
    @FXML private TableColumn<Expense, Integer> EidCol;

    /** @var TableColumn AmountCol Column for the transaction amount */
    @FXML private TableColumn<Expense, Double> AmountCol;

    /** @var TableColumn CategoryCol Column for the category associated with the expense */
    @FXML private TableColumn<Expense, String> CategoryCol;

    /** @var TableColumn DateCol Column for the transaction date */
    @FXML private TableColumn<Expense, LocalDate> DateCol;

    /** @var BudgetManager manager The business logic service providing budget data */
    private BudgetManager manager;

    /** @var ObservableList expenseList Backing list for the recent transactions table */
    private ObservableList<Expense> expenseList;

    /** @var Map spendingMap Map of category names to their respective spending totals */
    private Map<String,Double> spendingMap;

    /** @var ObservableList categoryList Backing list for the category spending table */
    private ObservableList<Map.Entry<String, Double>> categoryList;

    /**
     * @brief Initializes the controller and sets up data bindings.
     *
     * Configures table columns, initializes observable lists, and performs
     * the initial data fetch and refresh for the dashboard UI.
     */
    @FXML
    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());
        overviewLabel.setText(getUser().getUname());

        setupExpenseTable();
        setupCategorySpendingTable();

        expenseList = FXCollections.observableArrayList();
        spendingMap = manager.getSpendingByCategory();
        categoryList = FXCollections.observableArrayList(spendingMap.entrySet());

        bindData();
        refresh();
    }

    /**
     * @brief Configures the cell value factories for the Recent Transactions table.
     */
    private void setupExpenseTable(){
        EidCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getId()));
        AmountCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getAmount()));
        CategoryCol.setCellValueFactory(cell->(new SimpleObjectProperty<>(cell.getValue().getCategoryName())));
        DateCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getDate()));
    }

    /**
     * @brief Configures the cell value factories for the Category Spending table.
     */
    private void setupCategorySpendingTable(){
        categorySpendingColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getKey()));

        amountSpendingColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getValue()));
    }

    /**
     * @brief Binds observable lists to their respective UI TableView components.
     */
    private void bindData(){
        recentTransactions.setItems(expenseList);
        categorySpendingBox.setItems(categoryList);
    }

    /**
     * @brief Fetches fresh data from the service layer and updates all UI components.
     *
     * Updates numeric labels (remaining, daily limit, cycle limit), dates, and
     * refreshes both the transaction list and category breakdown map.
     */
    public void refresh() {
        remainingLabel.setText(String.format("$%.2f", manager.getRemainingLimit()));
        dailyLimitLabel.setText(String.format("$%.2f", manager.calculateDailyLimit()));
        cycleLimitLabel.setText(String.format("$%.2f", manager.getCycleLimit()));
        startDate.setText(String.valueOf(manager.getCycleStartDate()));
        endDate.setText(String.valueOf(manager.getCycleEndDate()));

        List<Expense> list = manager.getExpenseList();
        expenseList.setAll(list != null ? list : new ArrayList<>());

        spendingMap = manager.getSpendingByCategory();
        categoryList.setAll(spendingMap.entrySet());
    }

    /**
     * @brief Navigates the application view to the Budget Setup/Cycle screen.
     */
    public void goToSetCycle() {
        Parent nextView = SceneController.getInstance().loadScene(ViewType.BUDGET_SETUP);

        if (nextView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) overviewLabel.getScene().getWindow();
            stage.getScene().setRoot(nextView);
        }
    }
}