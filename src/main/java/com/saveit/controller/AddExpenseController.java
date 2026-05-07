package com.saveit.controller;

import com.saveit.service.BudgetManager;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import java.util.function.UnaryOperator;

/**
 * @brief Controller for the "Add Expense" view.
 *
 * This class handles user interactions for recording a new expense, including
 * input validation for amounts, categories, and dates. It utilizes a
 * TextFormatter to ensure only valid numeric data is entered into the amount field.
 */
public class AddExpenseController extends Controller {

    /** @var BudgetManager manager The business logic service for budget and expense operations */
    private BudgetManager manager;

    /** @var TextFormatter<Double> textFormatter Formatter to restrict and convert numeric input for the expense amount */
    private TextFormatter<Double> textFormatter;

    /** @var BorderPane addExpense The root layout pane for the add expense view */
    @FXML private BorderPane addExpense;

    /** @var TextField amountField Input field for the expense monetary value */
    @FXML private TextField amountField;

    /** @var TextField categoryField Input field for the expense category name */
    @FXML private TextField categoryField;

    /** @var DatePicker datePicker Date selection control for the expense date */
    @FXML private DatePicker datePicker;

    /**
     * @brief Processes the "Save" action triggered by the user.
     *
     * Validates that the amount, category, and date are provided. If valid,
     * it delegates the transaction storage to the BudgetManager and refreshes
     * the form.
     */
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

    /**
     * @brief Initializes the controller, setting up the service and resetting form fields.
     *
     * This method is called automatically after the FXML file has been loaded or
     * manually to reset the view state.
     */
    @Override
    public void initialize() {
        manager = new BudgetManager(getUser());
        textFormatter = setupFormatter();
        amountField.setTextFormatter(textFormatter);
        categoryField.clear();
        datePicker.setValue(null);
    }

    /**
     * @brief Configures a numeric TextFormatter for the amount field.
     *
     * Uses a regex filter to allow only valid double values (including decimals
     * and signs) and provides a converter for mapping the field text to a Double object.
     *
     * @return TextFormatter<Double> The configured text formatter.
     */
    public TextFormatter<Double> setupFormatter(){
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

}