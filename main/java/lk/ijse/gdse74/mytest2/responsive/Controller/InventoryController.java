package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Inventorydto;
import lk.ijse.gdse74.mytest2.responsive.model.InventoryModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.sql.Date;

public class InventoryController implements Initializable {

    // Threshold constants for stock levels
    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int CRITICAL_STOCK_THRESHOLD = 5;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Inventorydto, Integer> colCurrentStockBags;

    @FXML
    private TableColumn<Inventorydto, String> colInventory_id;

    @FXML
    private TableColumn<Inventorydto, Date> colLastupdated;

    @FXML
    private TableColumn<Inventorydto, String> colProduct_id;

    @FXML
    private TableView<Inventorydto> table;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLatUpdated;

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private TextField txt_CurrentStock;

    @FXML
    private Label lblStockStatus;

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStockStatus.setVisible(false);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Inventory Item");
        alert.setContentText("Are you sure you want to delete this inventory item?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtId.getText();
            try {
                InventoryModel inventoryModel = new InventoryModel();
                boolean isDelete = inventoryModel.deleteInventory(new Inventorydto(id));
                if (isDelete) {
                    showAlert("Deleted successfully", Alert.AlertType.INFORMATION);
                    clearFields();
                } else {
                    showAlert("Delete failed", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Failed to delete inventory item", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        int currentStock = Integer.parseInt(txt_CurrentStock.getText());
        Date lastUpdate = Date.valueOf(txtLatUpdated.getText());
        String selectedProductId = cmbProductId.getValue();

        Inventorydto inventorydto = new Inventorydto(
                txtId.getText(),
                selectedProductId,
                currentStock,
                lastUpdate
        );

        try {
            InventoryModel inventoryModel = new InventoryModel();
            boolean isSave = inventoryModel.saveInventory(inventorydto);
            if (isSave) {
                showAlert("Inventory Saved Successfully", Alert.AlertType.INFORMATION);
                clearFields();
            } else {
                showAlert("Inventory Not Saved", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Something went wrong while saving inventory", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Inventory Item");
        alert.setContentText("Are you sure you want to update this inventory item?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (!validateInputs()) return;

            int currentStock = Integer.parseInt(txt_CurrentStock.getText());
            Date lastUpdate = Date.valueOf(txtLatUpdated.getText());
            String selectedProductId = cmbProductId.getValue();

            Inventorydto inventorydto = new Inventorydto(
                    txtId.getText(),
                    selectedProductId,
                    currentStock,
                    lastUpdate
            );

            try {
                InventoryModel inventoryModel = new InventoryModel();
                boolean isUpdate = inventoryModel.updateInventory(inventorydto);
                if (isUpdate) {
                    showAlert("Inventory updated successfully", Alert.AlertType.INFORMATION);
                    clearFields();
                } else {
                    showAlert("Inventory Not updated", Alert.AlertType.ERROR);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Something went wrong while updating inventory", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    void validateStockInput() {
        try {
            int stock = Integer.parseInt(txt_CurrentStock.getText());
            if (stock < CRITICAL_STOCK_THRESHOLD) {
                lblStockStatus.setText("CRITICAL STOCK LEVEL!");
                lblStockStatus.setStyle("-fx-text-fill: #e74c3c;");
                lblStockStatus.setVisible(true);
            } else if (stock < LOW_STOCK_THRESHOLD) {
                lblStockStatus.setText("LOW STOCK WARNING");
                lblStockStatus.setStyle("-fx-text-fill: #f39c12;");
                lblStockStatus.setVisible(true);
            } else {
                lblStockStatus.setVisible(false);
            }
        } catch (NumberFormatException e) {
            lblStockStatus.setVisible(false);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        lblStockStatus.setVisible(false);

        loadTable();
        try {
            loadNextId();
            txtLatUpdated.setText(LocalDate.now().toString());
            txtLatUpdated.setEditable(false);
            loadProductIds();
        } catch (SQLException e) {
            throw new RuntimeException("Error during initialization: " + e.getMessage(), e);
        }
    }

    private void loadProductIds() throws SQLException {
        ArrayList<String> productIdList = InventoryModel.getAllproductIds();
        ObservableList<String> productIds = FXCollections.observableArrayList(productIdList);
        cmbProductId.setItems(productIds);
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new InventoryModel().getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading next ID", Alert.AlertType.ERROR);
        }
    }

    private void loadTable() {
        colInventory_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProduct_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCurrentStockBags.setCellValueFactory(new PropertyValueFactory<>("currentStockBags"));
        colLastupdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        try {
            ArrayList<Inventorydto> inventorydtos = InventoryModel.viewAllInventory();
            if (inventorydtos != null) {
                ObservableList<Inventorydto> inventory = FXCollections.observableArrayList(inventorydtos);
                table.setItems(inventory);

                // Add row factory for visual indicators
                table.setRowFactory(tv -> new TableRow<Inventorydto>() {
                    @Override
                    protected void updateItem(Inventorydto item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item == null || empty) {
                            setStyle("");
                        } else {
                            if (item.getCurrentStockBags() < CRITICAL_STOCK_THRESHOLD) {
                                setStyle("-fx-background-color: #ffdddd; -fx-font-weight: bold;");
                            } else if (item.getCurrentStockBags() < LOW_STOCK_THRESHOLD) {
                                setStyle("-fx-background-color: #fff3cd;");
                            } else {
                                setStyle("");
                            }
                        }
                    }
                });

                checkCriticalStock(inventorydtos);
            } else {
                showAlert("No inventory data found", Alert.AlertType.INFORMATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Failed to load inventory data", Alert.AlertType.ERROR);
        }
    }

    private void checkCriticalStock(ArrayList<Inventorydto> inventoryList) {
        List<String> criticalItems = new ArrayList<>();
        List<String> lowItems = new ArrayList<>();

        for (Inventorydto item : inventoryList) {
            if (item.getCurrentStockBags() < CRITICAL_STOCK_THRESHOLD) {
                criticalItems.add(item.getId() + " (" + item.getProductId() + ") - Stock: " + item.getCurrentStockBags());
            } else if (item.getCurrentStockBags() < LOW_STOCK_THRESHOLD) {
                lowItems.add(item.getId() + " (" + item.getProductId() + ") - Stock: " + item.getCurrentStockBags());
            }
        }

        if (!criticalItems.isEmpty()) {
            showStockAlert("CRITICAL STOCK ALERT",
                    "The following items have critically low stock levels:\n\n" +
                            String.join("\n", criticalItems),
                    Alert.AlertType.ERROR);
        }

        if (!lowItems.isEmpty()) {
            showStockAlert("Low Stock Warning",
                    "The following items have low stock levels:\n\n" +
                            String.join("\n", lowItems),
                    Alert.AlertType.WARNING);
        }
    }

    private void clearFields() throws SQLException {
        txtId.clear();
        txtLatUpdated.clear();
        txt_CurrentStock.clear();
        cmbProductId.getSelectionModel().clearSelection();
        loadNextId();
        txtLatUpdated.setText(LocalDate.now().toString());
        loadTable();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
        lblStockStatus.setVisible(false);
    }

    private boolean validateInputs() {
        try {
            Integer.parseInt(txt_CurrentStock.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid input for Current Stock", Alert.AlertType.ERROR);
            return false;
        }

        try {
            Date.valueOf(txtLatUpdated.getText());
        } catch (IllegalArgumentException e) {
            showAlert("Invalid date format for Last Updated", Alert.AlertType.ERROR);
            return false;
        }

        String selectedProductId = cmbProductId.getValue();
        if (selectedProductId == null || selectedProductId.isEmpty()) {
            showAlert("Please select a Product ID", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void showAlert(String message, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(alertType.name());
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
        });
    }

    private void showStockAlert(String title, String content, Alert.AlertType alertType) {
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(content);
            alert.initOwner(table.getScene().getWindow());
            alert.show();
        });
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Inventorydto selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;

        txtId.setText(selectedItem.getId());
        cmbProductId.setValue(selectedItem.getProductId());
        txt_CurrentStock.setText(String.valueOf(selectedItem.getCurrentStockBags()));
        txtLatUpdated.setText(String.valueOf(selectedItem.getLastUpdated()));

        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnSave.setDisable(true);

        validateStockInput();
    }

    public void cmbProductIdOnAction(ActionEvent actionEvent) {
        String selectedItem = cmbProductId.getSelectionModel().getSelectedItem();
        System.out.println("Selected Product ID: " + selectedItem);
    }
}