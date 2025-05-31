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
import lk.ijse.gdse74.mytest2.responsive.model.RawPaddyModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
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
    @FXML private TextField txtFarmer_id;
    @FXML private TextField txtMoisture_level;
    @FXML private TextField txtPaddy_id;
    @FXML private TextField txtPurchase_date;
    @FXML private TextField txtPurchase_price_per_kg;
    @FXML private TextField txtQuantity_kg;
    @FXML private TextField txtSupplier_id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        disableButtons();
        loadNextId();
        setupFieldListeners();
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

    private void setupFieldListeners() {
        txtFarmer_id.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtMoisture_level.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPurchase_date.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPurchase_price_per_kg.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtQuantity_kg.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtSupplier_id.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
    }

    private void updateSaveButtonState() {
        boolean anyFieldEmpty = txtFarmer_id.getText().isEmpty() ||
                txtMoisture_level.getText().isEmpty() ||
                txtPurchase_date.getText().isEmpty() ||
                txtPurchase_price_per_kg.getText().isEmpty() ||
                txtQuantity_kg.getText().isEmpty() ||
                txtSupplier_id.getText().isEmpty();

        if (btnSave.isDisabled()) return;
        btnSave.setDisable(anyFieldEmpty);
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

            RawPaddydto dto = new RawPaddydto(
                    txtPaddy_id.getText(),
                    txtSupplier_id.getText(),
                    txtFarmer_id.getText(),
                    quantity,
                    moisture,
                    price,
                    purchasedDate
            );

            boolean isSaved = new RawPaddyModel().SaveRawPaddy(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                clearFields();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric value").show();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format (use yyyy-MM-dd)").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
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

                RawPaddydto dto = new RawPaddydto(
                        txtPaddy_id.getText(),
                        txtSupplier_id.getText(),
                        txtFarmer_id.getText(),
                        quantity,
                        moisture,
                        price,
                        purchasedDate
                );

                boolean isUpdated = new RawPaddyModel().updateRawPaddy(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric value").show();
            } catch (IllegalArgumentException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format (use yyyy-MM-dd)").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tableColumnOnClicked(MouseEvent mouseEvent) {
        RawPaddydto selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtPaddy_id.setText(selectedItem.getPaddyId());
            txtSupplier_id.setText(selectedItem.getSupplierId());
            txtFarmer_id.setText(selectedItem.getFarmerId());
            txtQuantity_kg.setText(String.valueOf(selectedItem.getQuantity()));
            txtMoisture_level.setText(String.valueOf(selectedItem.getMoisture()));
            txtPurchase_price_per_kg.setText(String.valueOf(selectedItem.getPurchasePrice()));
            txtPurchase_date.setText(String.valueOf(selectedItem.getPurchaseDate()));

            enableButtons();
        }
    }

    private boolean validateFields() {
        if (txtSupplier_id.getText().isEmpty() || txtFarmer_id.getText().isEmpty() ||
                txtQuantity_kg.getText().isEmpty() || txtMoisture_level.getText().isEmpty() ||
                txtPurchase_price_per_kg.getText().isEmpty() || txtPurchase_date.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }

        try {
            Double.parseDouble(txtQuantity_kg.getText());
            Double.parseDouble(txtMoisture_level.getText());
            Double.parseDouble(txtPurchase_price_per_kg.getText());
            java.sql.Date.valueOf(txtPurchase_date.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Numeric fields must contain valid numbers").show();
            return false;
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format (use yyyy-MM-dd)").show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtFarmer_id.clear();
        txtMoisture_level.clear();
        txtPurchase_date.clear();
        txtPurchase_price_per_kg.clear();
        txtQuantity_kg.clear();
        txtSupplier_id.clear();

        loadNextId();
        loadTable();
        disableButtons();
    }
}