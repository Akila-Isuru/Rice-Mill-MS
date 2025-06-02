package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.RawPaddydto;
import lk.ijse.gdse74.mytest2.responsive.dto.Suppliersdto;
import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.model.RawPaddyModel;
import lk.ijse.gdse74.mytest2.responsive.model.SuppliersModel;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class RawPaddyController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<RawPaddydto,String> colFarmer_id;
    @FXML private TableColumn<RawPaddydto,Double> colMoisture_level;
    @FXML private TableColumn<RawPaddydto,String> colPaddy_id;
    @FXML private TableColumn<RawPaddydto,Double> colPrice_per_kg;
    @FXML private TableColumn<RawPaddydto, Date> colPurchase_date;
    @FXML private TableColumn<RawPaddydto,Double> colQuantity_Kg;
    @FXML private TableColumn<RawPaddydto,String> colSupplier_id;
    @FXML private TableView<RawPaddydto> table;
    @FXML private ComboBox<String> cmbFarmer_id;
    @FXML private TextField txtMoisture_level;
    @FXML private TextField txtPaddy_id;
    @FXML private TextField txtPurchase_date;
    @FXML private TextField txtPurchase_price_per_kg;
    @FXML private TextField txtQuantity_kg;
    @FXML private ComboBox<String> cmbSupplier_id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        disableButtons();
        loadNextId();
        loadSupplierIds();
        loadFarmerIds();
        setupFieldListeners();
        setupQuantityListener();
        fillCurrentDate();
    }

    private void setCellValueFactories() {
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colSupplier_id.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colFarmer_id.setCellValueFactory(new PropertyValueFactory<>("farmerId"));
        colQuantity_Kg.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colMoisture_level.setCellValueFactory(new PropertyValueFactory<>("moisture"));
        colPrice_per_kg.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colPurchase_date.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
    }

    private void loadTable() {
        try {
            ArrayList<RawPaddydto> allPaddy = RawPaddyModel.viewAllPaddy();
            table.setItems(FXCollections.observableArrayList(allPaddy));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
        }
    }

    private void loadSupplierIds() {
        try {
            ArrayList<Suppliersdto> allSuppliers = SuppliersModel.viewAllSuppliers();
            ObservableList<String> supplierIds = FXCollections.observableArrayList();

            for (Suppliersdto supplier : allSuppliers) {
                if (!supplierIds.contains(supplier.getSupplierId())) {
                    supplierIds.add(supplier.getSupplierId());
                }
            }

            cmbSupplier_id.setItems(supplierIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load supplier IDs").show();
        }
    }

    private void loadFarmerIds() {
        try {
            ArrayList<Farmersdto> allFarmers = new FarmersModel().viewAllFarmers();
            ObservableList<String> farmerIds = FXCollections.observableArrayList();

            for (Farmersdto farmer : allFarmers) {
                if (!farmerIds.contains(farmer.getFarmerId())) {
                    farmerIds.add(farmer.getFarmerId());
                }
            }

            cmbFarmer_id.setItems(farmerIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load farmer IDs").show();
        }
    }

    private void disableButtons() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
        btnSave.setDisable(false);
    }

    private void enableButtons() {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnSave.setDisable(true);
    }

    private void loadNextId() {
        try {
            String nextId = new RawPaddyModel().getNextId();
            txtPaddy_id.setText(nextId);
            txtPaddy_id.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate next ID").show();
        }
    }

    private void fillCurrentDate() {
        LocalDate today = LocalDate.now();
        txtPurchase_date.setText(today.toString());
    }

    private void setupFieldListeners() {
        txtMoisture_level.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPurchase_date.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPurchase_price_per_kg.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtQuantity_kg.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        cmbSupplier_id.valueProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        cmbFarmer_id.valueProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
    }

    private void setupQuantityListener() {
        txtQuantity_kg.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && newValue.matches("\\d*\\.?\\d+")) {
                calculateMoistureLevel(Double.parseDouble(newValue));
                updateSaveButtonState();
            } else if (newValue.isEmpty()) { // Clear moisture if quantity is cleared
                txtMoisture_level.clear();
                updateSaveButtonState();
            }
        });
    }

    private void calculateMoistureLevel(double quantity) {
        double moistureLevel;

        if (quantity < 50) {
            moistureLevel = 12.5;
        } else if (quantity < 100) {
            moistureLevel = 12.0;
        } else if (quantity < 200) {
            moistureLevel = 11.5;
        } else {
            moistureLevel = 11.0;
        }

        txtMoisture_level.setText(String.format("%.1f", moistureLevel));
    }

    private void updateSaveButtonState() {
        boolean isQuantityValid = !txtQuantity_kg.getText().trim().isEmpty() && txtQuantity_kg.getText().matches("\\d*\\.?\\d+");
        boolean isPriceValid = !txtPurchase_price_per_kg.getText().trim().isEmpty() && txtPurchase_price_per_kg.getText().matches("\\d*\\.?\\d+");
        boolean isDateValid = !txtPurchase_date.getText().trim().isEmpty(); // Further date format validation in validateFields()


        boolean isEitherIdSelected = cmbSupplier_id.getValue() != null || cmbFarmer_id.getValue() != null;

        btnSave.setDisable(!(isQuantityValid && isPriceValid && isDateValid && isEitherIdSelected));
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Paddy Record");
        alert.setContentText("Are you sure you want to delete this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new RawPaddyModel().deleteRawPaddy(
                        new RawPaddydto(txtPaddy_id.getText())
                );

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Delete Failed!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            if (!validateFields()) {
                return;
            }

            double quantity = Double.parseDouble(txtQuantity_kg.getText());
            double moisture = Double.parseDouble(txtMoisture_level.getText());
            double price = Double.parseDouble(txtPurchase_price_per_kg.getText());
            java.sql.Date purchasedDate = java.sql.Date.valueOf(txtPurchase_date.getText());


            String selectedSupplierId = cmbSupplier_id.getValue();
            String finalSupplierId = (selectedSupplierId != null && !selectedSupplierId.trim().isEmpty()) ? selectedSupplierId.trim() : null;

            String selectedFarmerId = cmbFarmer_id.getValue();
            String finalFarmerId = (selectedFarmerId != null && !selectedFarmerId.trim().isEmpty()) ? selectedFarmerId.trim() : null;

            RawPaddydto dto = new RawPaddydto(
                    txtPaddy_id.getText(),
                    finalSupplierId,
                    finalFarmerId,
                    quantity,
                    moisture,
                    price,
                    purchasedDate
            );

            RawPaddyModel model = new RawPaddyModel();
            boolean isSaved = model.SaveRawPaddy(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                clearFields();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric value for quantity, moisture, or price.").show();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format (use YYYY-MM-dd).").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Paddy Record");
        alert.setContentText("Are you sure you want to update this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (!validateFields()) {
                    return;
                }

                double quantity = Double.parseDouble(txtQuantity_kg.getText());
                double moisture = Double.parseDouble(txtMoisture_level.getText());
                double price = Double.parseDouble(txtPurchase_price_per_kg.getText());
                java.sql.Date purchasedDate = java.sql.Date.valueOf(txtPurchase_date.getText());


                String selectedSupplierId = cmbSupplier_id.getValue();
                String finalSupplierId = (selectedSupplierId != null && !selectedSupplierId.trim().isEmpty()) ? selectedSupplierId.trim() : null;

                String selectedFarmerId = cmbFarmer_id.getValue();
                String finalFarmerId = (selectedFarmerId != null && !selectedFarmerId.trim().isEmpty()) ? selectedFarmerId.trim() : null;

                RawPaddydto dto = new RawPaddydto(
                        txtPaddy_id.getText(),
                        finalSupplierId,
                        finalFarmerId,
                        quantity,
                        moisture,
                        price,
                        purchasedDate
                );

                RawPaddyModel model = new RawPaddyModel();
                boolean isUpdated = model.updateRawPaddy(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric value for quantity, moisture, or price.").show();
            } catch (IllegalArgumentException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format (use YYYY-MM-dd).").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong: " + e.getMessage()).show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tableColumnOnClicked(MouseEvent mouseEvent) {
        RawPaddydto selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtPaddy_id.setText(selectedItem.getPaddyId());
            // Set combo box values, allowing for null
            cmbSupplier_id.setValue(selectedItem.getSupplierId());
            cmbFarmer_id.setValue(selectedItem.getFarmerId());
            txtQuantity_kg.setText(String.valueOf(selectedItem.getQuantity()));
            txtMoisture_level.setText(String.valueOf(selectedItem.getMoisture()));
            txtPurchase_price_per_kg.setText(String.valueOf(selectedItem.getPurchasePrice()));
            txtPurchase_date.setText(String.valueOf(selectedItem.getPurchaseDate()));

            enableButtons();
        }
    }

    private boolean validateFields() {

        if (txtQuantity_kg.getText().isEmpty() ||
                txtPurchase_price_per_kg.getText().isEmpty() ||
                txtPurchase_date.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill in Quantity, Purchase Price, and Purchase Date.").show();
            return false;
        }


        boolean isSupplierSelected = cmbSupplier_id.getValue() != null && !cmbSupplier_id.getValue().trim().isEmpty();
        boolean isFarmerSelected = cmbFarmer_id.getValue() != null && !cmbFarmer_id.getValue().trim().isEmpty();

        if (!isSupplierSelected && !isFarmerSelected) {
            new Alert(Alert.AlertType.ERROR, "Either a Supplier ID or a Farmer ID must be selected.").show();
            return false;
        }


        try {
            Double.parseDouble(txtQuantity_kg.getText());

            if (!txtMoisture_level.getText().isEmpty()) {
                Double.parseDouble(txtMoisture_level.getText());
            }
            Double.parseDouble(txtPurchase_price_per_kg.getText());
            java.sql.Date.valueOf(txtPurchase_date.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Numeric fields (Quantity, Moisture, Price) must contain valid numbers.").show();
            return false;
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-dd.").show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtMoisture_level.clear();
        txtPurchase_price_per_kg.clear();
        txtQuantity_kg.clear();
        cmbSupplier_id.getSelectionModel().clearSelection();
        cmbSupplier_id.setValue(null);
        cmbFarmer_id.getSelectionModel().clearSelection();
        cmbFarmer_id.setValue(null);

        loadNextId();
        fillCurrentDate();
        loadTable();
        disableButtons();
    }
}