package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import lk.ijse.gdse74.mytest2.responsive.model.MillingProcessModel;
import lk.ijse.gdse74.mytest2.responsive.model.InventoryModel;
import lk.ijse.gdse74.mytest2.responsive.model.SalesOrderModel;
import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.dto.Inventorydto;
import lk.ijse.gdse74.mytest2.responsive.dto.SalesOrderdto;

import java.sql.SQLException;
import java.util.ArrayList;

public class ThirdPage {
    @FXML
    private Label todayMillingLabel;

    @FXML
    private Label inventoryLabel;

    @FXML
    private Text inventoryBagsText;

    @FXML
    private Label OrderLabel;

    @FXML
    private Text orderCountText;

    @FXML
    private Label revenueLabel; // Changed from revenuTable to revenueLabel for consistency

    @FXML
    private Text revenueAmountText; // Text element for the revenue amount

    @FXML
    private void initialize() {
        loadMillingData();
        loadInventoryData();
        loadOrderData();
        loadRevenueData(); // Call the method to load revenue data
    }

    private void loadMillingData() {
        try {
            ArrayList<MillingProcessdto> allProcesses = MillingProcessModel.viewAllMillingProcess();
            double totalMilled = calculateTotalMilled(allProcesses);
            updateMillingLabel(totalMilled);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            todayMillingLabel.setText("Error loading data");
        }
    }

    private double calculateTotalMilled(ArrayList<MillingProcessdto> processes) {
        return processes.stream()
                .mapToDouble(MillingProcessdto::getMilledQuantity)
                .sum();
    }

    private void updateMillingLabel(double quantity) {
        todayMillingLabel.setText(String.format("%.2f Kg", quantity));
    }

    private void loadInventoryData() {
        try {
            ArrayList<Inventorydto> allInventory = InventoryModel.viewAllInventory();
            int totalStockBags = calculateTotalStockBags(allInventory);
            updateInventoryBagsText(totalStockBags);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            inventoryBagsText.setText("Error loading inventory");
        }
    }

    private int calculateTotalStockBags(ArrayList<Inventorydto> inventoryList) {
        return inventoryList.stream()
                .mapToInt(Inventorydto::getCurrentStockBags)
                .sum();
    }

    private void updateInventoryBagsText(int totalBags) {
        inventoryBagsText.setText(String.format("%,d Bags in stock", totalBags));
    }

    private void loadOrderData() {
        try {
            ArrayList<SalesOrderdto> allOrders = SalesOrderModel.viewAllSalesOrder();
            int orderCount = allOrders.size();
            updateOrderLabel(orderCount);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            orderCountText.setText("Error loading orders");
        }
    }

    private void updateOrderLabel(int count) {
        orderCountText.setText(String.valueOf(count));
    }

    private void loadRevenueData() {
        try {
            ArrayList<SalesOrderdto> allOrders = SalesOrderModel.viewAllSalesOrder();
            double totalRevenue = calculateTotalRevenue(allOrders);
            updateRevenueAmountText(totalRevenue);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            revenueAmountText.setText("Error loading revenue");
        }
    }

    private double calculateTotalRevenue(ArrayList<SalesOrderdto> orders) {
        return orders.stream()
                .mapToDouble(SalesOrderdto::getOrderAmount)
                .sum();
    }

    private void updateRevenueAmountText(double amount) {
        revenueAmountText.setText(String.format("%,.2f LKR", amount));
    }
}