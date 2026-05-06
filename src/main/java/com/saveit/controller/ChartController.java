package com.saveit.controller;

import com.saveit.model.Category;
import com.saveit.service.ReportGenerator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.util.Map;

public class ChartController extends Controller {

    @FXML private PieChart categoryPieChart;
    @FXML private BarChart<String, Double> weeklyBarChart;
    @FXML private CategoryAxis xAxis;
    @FXML private NumberAxis yAxis;
    
    @FXML private Label totalSavingsLabel;
    @FXML private Label monthlySpendingLabel;
    @FXML private Label budgetProgressLabel;

    private ReportGenerator reportGen;

    @Override
    public void initialize() {
        reportGen = new ReportGenerator();
        refreshCharts();
    }

    public void refreshCharts() {
        if (getUser() == null) return;

        int userId = getUser().getId();

        // Pass the userId to the private update methods
        updateCategoryPieChart(userId);
        updateWeeklyTrendChart(userId);
    }

    // Fixed: Added 'int userId' parameter to match the call in refreshCharts
    private void updateCategoryPieChart(int userId) { 
        categoryPieChart.getData().clear();
        
        // Fixed: Passing userId to the ReportGenerator
        Map<Category, Double> distribution = reportGen.getCategoryDistribution(userId); 
        
        if (distribution != null) {
            distribution.forEach((category, amount) -> {
                categoryPieChart.getData().add(new PieChart.Data(category.getName(), amount));
            });
        }
    }

    // Fixed: Added 'int userId' parameter to match the call in refreshCharts
    private void updateWeeklyTrendChart(int userId) {
        weeklyBarChart.getData().clear();
        XYChart.Series<String, Double> currentWeekSeries = new XYChart.Series<>();
        currentWeekSeries.setName("Current Week");

        
        Map<LocalDate, Double> trends = reportGen.getWeeklySpendingTrend(userId); 
        
        if (trends != null) {
            trends.forEach((date, amount) -> {
                currentWeekSeries.getData().add(new XYChart.Data<>(date.getDayOfWeek().name(), amount));
            });
        }

        weeklyBarChart.getData().add(currentWeekSeries);
    }

    @Override
    public Node getViewNodes() {
        // Returns the parent container of the chart UI[cite: 4]
        return weeklyBarChart.getParent(); 
    }
}