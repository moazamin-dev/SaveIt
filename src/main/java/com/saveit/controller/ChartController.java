package com.saveit.controller;

import com.saveit.service.BudgetManager;
import com.saveit.service.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class ChartController extends Controller {

    @FXML private PieChart categoryPieChart;
    @FXML private BarChart<String, Double> weeklyBarChart;
    @FXML private LineChart<String, Double> monthlyLineChart;

    @FXML private Label totalSavingsLabel;
    @FXML private Label monthlySpendingLabel;
    @FXML private Label budgetProgressLabel;

    private ReportGenerator reportGen;

    @Override
    public void initialize() {
        // Use the getUser() method from your base Controller to create the manager
        if (getUser() != null) {
            BudgetManager manager = new BudgetManager(getUser());
            reportGen = new ReportGenerator(manager);
            refreshCharts();
        }
    }

    public void refreshCharts() {
        if (reportGen == null) return;

        updateSummaryLabels();
        updateCategoryPieChart();
        updateWeeklyTrendChart();
        updateMonthlyTrendChart();
    }

    private void updateSummaryLabels() {
        totalSavingsLabel.setText(String.format("$%.2f", reportGen.getSavings()));
        monthlySpendingLabel.setText(String.format("$%.2f", reportGen.getTotalSpending()));
        budgetProgressLabel.setText(String.format("%.1f%%", reportGen.getBudgetUsagePercentage()));
    }

    private void updateCategoryPieChart() {
        categoryPieChart.getData().clear();
        Map<String, Double> distribution = reportGen.getCategoryDistribution();

        distribution.forEach((categoryName, amount) -> {
            categoryPieChart.getData().add(new PieChart.Data(categoryName, amount));
        });
    }

    private void updateWeeklyTrendChart() {
        weeklyBarChart.getData().clear();
        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setName("Spending Trend (Past 7 Days)");

        Map<LocalDate, Double> trends = reportGen.getWeeklySpendingTrend();

        for (int i = 6; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);

            String dayName = date.getDayOfWeek().name();

            StringBuilder uniqueLabel = new StringBuilder(dayName);
            for(int j = 0; j < i; j++) {
                uniqueLabel.append(" ");
            }

            Double amount = trends.getOrDefault(date, 0.0);

            series.getData().add(new XYChart.Data<>(uniqueLabel.toString(), amount));
        }

        weeklyBarChart.getData().add(series);
    }

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