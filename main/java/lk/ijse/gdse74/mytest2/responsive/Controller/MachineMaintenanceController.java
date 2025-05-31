package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.MachineMaintenancedto;
import lk.ijse.gdse74.mytest2.responsive.model.MachineMaintenanceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MachineMaintenanceController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<MachineMaintenancedto, String> colMain_id;
    @FXML private TableColumn<MachineMaintenancedto, Integer> col_cost;
    @FXML private TableColumn<MachineMaintenancedto, String> coldescription;
    @FXML private TableColumn<MachineMaintenancedto, String> colmachine_name;
    @FXML private TableColumn<MachineMaintenancedto, String> colmain_date;
    @FXML private TableView<MachineMaintenancedto> table;
    @FXML private TextField txtcost;
    @FXML private TextField txtdescription;
    @FXML private TextField txtmachine_name;
    @FXML private TextField txtmain_date;
    @FXML private TextField txtmain_id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        disableButtons();
        loadNextId();
        setupFieldListeners();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void setCellValueFactories() {
        colMain_id.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colmachine_name.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        colmain_date.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    private void loadTable() {
        try {
            ArrayList<MachineMaintenancedto> allMaintenance = MachineMaintenanceModel.viewAllMachineMaintenance();
            table.setItems(FXCollections.observableArrayList(allMaintenance));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
        }
    }

    private void disableButtons() {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnSave.setDisable(false);
    }

    private void enableButtons() {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
        btnSave.setDisable(true);
    }

    private void loadNextId() {
        try {
            String nextId = new MachineMaintenanceModel().getNextId();
            txtmain_id.setText(nextId);
            txtmain_id.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate next ID").show();
        }
    }

    private void setupFieldListeners() {
        // Disable save button if any field is empty
        txtmachine_name.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtmain_date.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtdescription.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtcost.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
    }

    private void updateSaveButtonState() {
        boolean anyFieldEmpty = txtmachine_name.getText().isEmpty() ||
                txtmain_date.getText().isEmpty() ||
                txtdescription.getText().isEmpty() ||
                txtcost.getText().isEmpty();

        // Only disable Save button if we're not in update mode
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
        alert.setHeaderText("Delete Maintenance Record");
        alert.setContentText("Are you sure you want to delete this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new MachineMaintenanceModel().deleteMachineMaintenance(
                        new MachineMaintenancedto(txtmain_id.getText())
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

            int cost = Integer.parseInt(txtcost.getText());

            MachineMaintenancedto dto = new MachineMaintenancedto(
                    txtmain_id.getText(),
                    txtmachine_name.getText(),
                    txtmain_date.getText(),
                    txtdescription.getText(),
                    cost
            );

            boolean isSaved = new MachineMaintenanceModel().saveMachineMaintenance(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                clearFields();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid cost value").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Maintenance Record");
        alert.setContentText("Are you sure you want to update this record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (!validateFields()) {
                    return;
                }

                int cost = Integer.parseInt(txtcost.getText());

                MachineMaintenancedto dto = new MachineMaintenancedto(
                        txtmain_id.getText(),
                        txtmachine_name.getText(),
                        txtmain_date.getText(),
                        txtdescription.getText(),
                        cost
                );

                boolean isUpdated = new MachineMaintenanceModel().updateMachineMaintenance(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid cost value").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tableColumnOnClicked(MouseEvent mouseEvent) {
        MachineMaintenancedto selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtmain_id.setText(selectedItem.getMaintenanceId());
            txtmachine_name.setText(selectedItem.getMachineName());
            txtmain_date.setText(selectedItem.getMaintenanceDate());
            txtdescription.setText(selectedItem.getDescription());
            txtcost.setText(String.valueOf(selectedItem.getCost()));

            enableButtons();
        }
    }

    private boolean validateFields() {
        if (txtmachine_name.getText().isEmpty() || txtmain_date.getText().isEmpty() ||
                txtdescription.getText().isEmpty() || txtcost.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }

        try {
            Integer.parseInt(txtcost.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Cost must be a number").show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtmachine_name.clear();
        txtmain_date.clear();
        txtdescription.clear();
        txtcost.clear();

        loadNextId();
        loadTable();
        disableButtons();
    }
}