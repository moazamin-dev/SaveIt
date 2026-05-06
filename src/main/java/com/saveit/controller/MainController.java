package com.saveit.controller;

import com.saveit.model.Expense;
import com.saveit.service.BudgetManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainController extends Controller {

    @FXML
    private BorderPane DashBoard;

    @FXML private Label overviewLabel;
    @FXML private Label remainingLabel;
    @FXML private Label dailyLimitLabel;
    @FXML private Label cycleLimitLabel;
    @FXML private Label startDate;
    @FXML private Label endDate;

    @FXML private TableView<Map.Entry<String, Double>> categorySpendingBox;
    @FXML private TableColumn<Map.Entry<String, Double>, String> categorySpendingColumn;
    @FXML private TableColumn<Map.Entry<String, Double>, Double> amountSpendingColumn;

    @FXML private TableView<Expense> recentTransactions;
    @FXML private TableColumn<Expense, Integer> EidCol;
    @FXML private TableColumn<Expense, Double> AmountCol;
    @FXML private TableColumn<Expense, String> CategoryCol;
    @FXML private TableColumn<Expense, LocalDate> DateCol;

    private BudgetManager manager;

    private ObservableList<Expense> expenseList;
    private Map<String,Double> spendingMap;
    private ObservableList<Map.Entry<String, Double>> categoryList;

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
    private void setupExpenseTable(){
        EidCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getId()));
        AmountCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getAmount()));
        CategoryCol.setCellValueFactory(cell->(new SimpleObjectProperty<>(cell.getValue().getCategoryName())));
        DateCol.setCellValueFactory(cell->new SimpleObjectProperty<>(cell.getValue().getDate()));
    }
    private void setupCategorySpendingTable(){
        categorySpendingColumn.setCellValueFactory(data ->
                new ReadOnlyStringWrapper(data.getValue().getKey()));

        amountSpendingColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(data.getValue().getValue()));
    }

    private void bindData(){
        recentTransactions.setItems(expenseList);


        categorySpendingBox.setItems(categoryList);
    }

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

    public void goToSetCycle() {
        Parent nextView = SceneController.getInstance().loadScene(ViewType.BUDGET_SETUP);

        if (nextView != null) {
            javafx.stage.Stage stage = (javafx.stage.Stage) overviewLabel.getScene().getWindow();
            stage.getScene().setRoot(nextView);
        }
    }
}
