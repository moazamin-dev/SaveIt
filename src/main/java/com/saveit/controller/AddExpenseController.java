package com.saveit.controller;

import com.saveit.dao.ExpenseDAO;
import com.saveit.model.Expense;
import com.saveit.service.BudgetManager;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;

import java.util.Map;
import java.util.function.UnaryOperator;

public class AddExpenseController extends Controller {

    private BudgetManager manager;

    private TextFormatter<Double> textFormatter;

    @FXML private BorderPane addExpense;
    @FXML private TextField amountField;
    @FXML private TextField categoryField;
    @FXML private DatePicker datePicker;

    @FXML
    public void handleSave() {
        Double amount = textFormatter.getValue();
        String category = categoryField.getText();
        java.time.LocalDate date = datePicker.getValue();
        if (amount == null) {
            System.out.println("Invalid amount input");
            return;
        }
        if (category == null || category.isBlank()) {
            System.out.println("Invalid category input");
            return;
        }
        if (date == null) {
            System.out.println("Invalid date input");
            return;
        }
        manager.addTransaction(amount,category,date);

        initialize();
    }

    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());
        textFormatter = setupFormatter();
        amountField.setTextFormatter(textFormatter);
        categoryField.clear();
        datePicker.setValue(null);
    }

    public TextFormatter<Double> setupFormatter(){
        // Use a converter that doesn't crash or reset on empty/partial input
        StringConverter<Double> converter = new StringConverter<Double>() {
            @Override
            public String toString(Double object) {
                return (object == null) ? "" : object.toString();
            }

            @Override
            public Double fromString(String string) {
                if (string == null || string.isEmpty() || string.equals("-") || string.equals(".")) {
                    return 0.00;
                }
                try {
                    return Double.parseDouble(string);
                } catch (NumberFormatException e) {
                    return 0.00;
                }
            }
        };

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();
            if(text.matches("-?([0-9]*(\\.[0-9]*)?)?")){
                return change;
            }
            return null;
        };
        return new TextFormatter<>(new DoubleStringConverter(),null,filter);
    }

    @Override
    public Node getViewNodes() {
        return addExpense;
    }
}