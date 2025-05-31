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
import java.util.Optional; // Import Optional for confirmation dialogs
import java.util.ResourceBundle;
import java.sql.Date;

public class InventoryController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Inventorydto,Integer> colCurrentStockBags;

    @FXML
    private TableColumn<Inventorydto,String> colInventory_id;

    @FXML
    private TableColumn<Inventorydto, Date> colLastupdated;

    @FXML
    private TableColumn<Inventorydto,String> colProduct_id;

    @FXML
    private TableView<Inventorydto> table;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLatUpdated;

    @FXML
    private TextField txtProductId; // This field is not used, as cmbProductId is used instead.

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private TextField txt_CurrentStock;

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();
        // After clearing, disable update and delete buttons
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Confirmation dialog for delete
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Inventory Item");
        alert.setContentText("Are you sure you want to delete this inventory item? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtId.getText();
            try {
                InventoryModel inventoryModel = new InventoryModel();
                boolean isDelete = inventoryModel.deleteInventory(new Inventorydto(id));
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION,"Deleted successfully").show();
                    clearFields(); // Clear fields and refresh table
                }else {
                    new Alert(Alert.AlertType.ERROR,"Delete failed").show();
                }

            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Failed to delete inventory item").show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        // Basic validation for CurrentStock
        int CurrentStock;
        try {
            CurrentStock = Integer.parseInt(txt_CurrentStock.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid input for Current Stock. Please enter a number.").show();
            return;
        }

        // Basic validation for Last Updated Date
        Date lastupdate;
        try {
            lastupdate = Date.valueOf(txtLatUpdated.getText());
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format for Last Updated. Please use YYYY-MM-DD.").show();
            return;
        }

        // Check if a product ID is selected from the ComboBox
        String selectedProductId = cmbProductId.getValue();
        if (selectedProductId == null || selectedProductId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please select a Product ID.").show();
            return;
        }

        Inventorydto inventorydto = new Inventorydto(txtId.getText(), selectedProductId, CurrentStock, lastupdate);
        try {
            InventoryModel inventoryModel = new InventoryModel();
            boolean isSave = inventoryModel.saveInventory(inventorydto);
            if (isSave) {
                new Alert(Alert.AlertType.INFORMATION, "Inventory Saved Successfully").show();
                clearFields(); // Clear fields and refresh table
            } else {
                new Alert(Alert.AlertType.ERROR, "Inventory Not Saved").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong while saving inventory").show();
        }
    }

    private void clearFields() throws SQLException {
        txtId.clear();
        txtLatUpdated.clear();
        txt_CurrentStock.clear();
        cmbProductId.getSelectionModel().clearSelection(); // Clear ComboBox selection
        loadNextId();
        txtLatUpdated.setText(LocalDate.now().toString()); // Set current date after clearing
        Platform.runLater(() -> {
            txtId.setText(txtId.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtId.getText());
        });

        // Refresh table
        loadTable();
        // Disable update and delete buttons after clearing fields
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        // Confirmation dialog for update
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Inventory Item");
        alert.setContentText("Are you sure you want to update this inventory item?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Basic validation for CurrentStock
            int CurrentStock;
            try {
                CurrentStock = Integer.parseInt(txt_CurrentStock.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid input for Current Stock. Please enter a number.").show();
                return;
            }

            // Basic validation for Last Updated Date
            Date lastupdate;
            try {
                lastupdate = Date.valueOf(txtLatUpdated.getText());
            } catch (IllegalArgumentException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format for Last Updated. Please use YYYY-MM-DD.").show();
                return;
            }

            // Check if a product ID is selected from the ComboBox
            String selectedProductId = cmbProductId.getValue();
            if (selectedProductId == null || selectedProductId.isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Please select a Product ID.").show();
                return;
            }

            Inventorydto inventorydto = new Inventorydto(txtId.getText(), selectedProductId, CurrentStock, lastupdate);
            try {
                InventoryModel inventoryModel = new InventoryModel();
                boolean isUpdate = inventoryModel.updateInventory(inventorydto);
                if (isUpdate) {
                    new Alert(Alert.AlertType.INFORMATION, "Inventory updated successfully").show();
                    clearFields(); // Clear fields and refresh table
                } else {
                    new Alert(Alert.AlertType.ERROR, "Inventory Not updated").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Something went wrong while updating inventory").show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable update and delete buttons initially
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

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
        // Add all product IDs to the ComboBox
        cmbProductId.setItems(productIds);
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new InventoryModel().getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false); // Make ID field non-editable
            System.out.println("DEBUG: Next ID retrieved: " + nextId); // Debug line
            if (nextId == null || nextId.isEmpty()) {
                System.out.println("DEBUG: Got empty or null ID"); // Debug line
            }
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage()); // Debug line
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
        }
    }

    private void loadTable() {
        colInventory_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProduct_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCurrentStockBags.setCellValueFactory(new PropertyValueFactory<>("currentStockBags"));
        colLastupdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        try {
            // No need to instantiate Inventorydto here, as viewAllInventory is static
            ArrayList<Inventorydto> inventorydtos = InventoryModel.viewAllInventory();
            if(inventorydtos != null) {
                ObservableList<Inventorydto> inventory = FXCollections.observableArrayList(inventorydtos);
                table.setItems(inventory);
            }else {
                new Alert(Alert.AlertType.INFORMATION, "No inventory data found.").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load inventory data").show();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        // Get selected item from table
        Inventorydto selectedItem = table.getSelectionModel().getSelectedItem();

        // Only proceed if something is selected
        if (selectedItem == null) return;

        // Update form fields
        txtId.setText(selectedItem.getId());
        cmbProductId.setValue(selectedItem.getProductId()); // Set selected value for ComboBox
        txt_CurrentStock.setText(String.valueOf(selectedItem.getCurrentStockBags())); // Convert int to String
        txtLatUpdated.setText(String.valueOf(selectedItem.getLastUpdated())); // Convert Date to String

        // Enable update and delete buttons when an item is selected
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnSave.setDisable(true);
    }

    public void cmbProductIdOnAction(ActionEvent actionEvent) {
        String selectedItem = cmbProductId.getSelectionModel().getSelectedItem();
        System.out.println("Selected Product ID: " + selectedItem);
    }
}