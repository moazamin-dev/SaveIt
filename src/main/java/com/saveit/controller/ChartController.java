package com.saveit.controller;

import com.saveit.service.BudgetManager;
import com.saveit.service.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.util.Map;

/**
 * @brief Controller responsible for managing and displaying financial data visualizations.
 *
 * This class interacts with the ReportGenerator to populate various JavaFX charts,
 * including a PieChart for categories, a BarChart for weekly trends, and a LineChart
 * for monthly spending analysis. It also manages summary statistics like total savings
 * and budget usage percentage.
 */
public class ChartController extends Controller {

    /** @var PieChart categoryPieChart Chart displaying spending distribution across categories */
    @FXML private PieChart categoryPieChart;

    /** @var BarChart weeklyBarChart Chart displaying daily spending over the last 7 days */
    @FXML private BarChart<String, Double> weeklyBarChart;

    /** @var LineChart monthlyLineChart Chart displaying spending trends over the last 30 days */
    @FXML private LineChart<String, Double> monthlyLineChart;

    /** @var Label totalSavingsLabel Label displaying the calculated total savings */
    @FXML private Label totalSavingsLabel;

    /** @var Label monthlySpendingLabel Label displaying the total spent in the current cycle */
    @FXML private Label monthlySpendingLabel;

    /** @var Label budgetProgressLabel Label displaying the percentage of budget consumed */
    @FXML private Label budgetProgressLabel;

    /** @var ReportGenerator reportGen Service used to fetch calculated reporting data */
    private ReportGenerator reportGen;

    /**
     * @brief Initializes the controller and triggers the initial chart rendering.
     *
     * Sets up the ReportGenerator dependency using the currently authenticated user
     * and refreshes all visual components.
     */
    @Override
    public void initialize() {
        if (getUser() != null) {
            BudgetManager manager = new BudgetManager(getUser());
            reportGen = new ReportGenerator(manager);
            refreshCharts();
        }
    }

    /**
     * @brief Redraws all charts and updates summary labels with fresh data.
     */
    public void refreshCharts() {
        if (reportGen == null) return;

        updateSummaryLabels();
        updateCategoryPieChart();
        updateWeeklyTrendChart();
        updateMonthlyTrendChart();
    }

    /**
     * @brief Updates the textual summary metrics displayed in the view.
     */
    private void updateSummaryLabels() {
        totalSavingsLabel.setText(String.format("$%.2f", reportGen.getSavings()));
        monthlySpendingLabel.setText(String.format("$%.2f", reportGen.getTotalSpending()));
        budgetProgressLabel.setText(String.format("%.1f%%", reportGen.getBudgetUsagePercentage()));
    }

    /**
     * @brief Populates the category distribution PieChart.
     */
    private void updateCategoryPieChart() {
        categoryPieChart.getData().clear();
        Map<String, Double> distribution = reportGen.getCategoryDistribution();

        distribution.forEach((categoryName, amount) -> {
            categoryPieChart.getData().add(new PieChart.Data(categoryName, amount));
        });
    }

    /**
     * @brief Populates the weekly spending BarChart.
     *
     * Iterates through the last 7 days to create a chronological series.
     * Uses a unique label approach to ensure identical day names (e.g., two Mondays)
     * are treated as distinct entries by the JavaFX Axis.
     */
    private void updateWeeklyTrendChart() {
        weeklyBarChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Spending Trend (Past 7 Days)");

        Map<LocalDate, Double> trends = reportGen.getWeeklySpendingTrend();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            String dayName = date.getDayOfWeek().name();

            // Append spaces to create unique labels for the axis to prevent grouping
            StringBuilder uniqueLabel = new StringBuilder(dayName);
            for(int j = 0; j < i; j++) {
                uniqueLabel.append(" ");
            }

            Double amount = trends.getOrDefault(date, 0.0);

            series.getData().add(new XYChart.Data<>(uniqueLabel.toString(), amount));
        }

        weeklyBarChart.getData().add(series);
    }

    /**
     * @brief Populates the monthly spending LineChart.
     *
     * Maps spending data for the past 30 days onto a line graph, formatted by month and day.
     */
    private void updateMonthlyTrendChart() {
        monthlyLineChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Last 30 Days");

        Map<LocalDate, Double> trends = reportGen.getMonthlySpendingTrend();

        for (int i = 29; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            String label = String.format("%02d/%02d", date.getMonthValue(), date.getDayOfMonth());
            Double amount = trends.getOrDefault(date, 0.0);

            series.getData().add(new XYChart.Data<>(label, amount));
        }

        monthlyLineChart.getData().add(series);
    }
}