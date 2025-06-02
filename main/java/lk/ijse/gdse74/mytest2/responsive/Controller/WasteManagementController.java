package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.dto.WasteManagementdto;
import lk.ijse.gdse74.mytest2.responsive.model.WasteManagementModel;
import lk.ijse.gdse74.mytest2.responsive.model.MillingProcessModel;

import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class WasteManagementController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private ComboBox<String> cmbMilling_id;

    @FXML
    private ComboBox<String> cmbWaste_type;

    @FXML
    private ComboBox<String> cmbDisposal_method;

    @FXML
    private TableColumn<WasteManagementdto,String> colDisposalMethod;

    @FXML
    private TableColumn<WasteManagementdto,String> colMilling_id;

    @FXML
    private TableColumn<WasteManagementdto,Integer> colQuantity;

    @FXML
    private TableColumn<WasteManagementdto, Date> colRecordedDate;

    @FXML
    private TableColumn<WasteManagementdto,String> colWaste_id;

    @FXML
    private TableColumn<WasteManagementdto,String> colWaste_type;

    @FXML
    private TableView<WasteManagementdto> table;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtWaste_id;

    @FXML
    private TextField txtrecorded_date;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String waste_id = txtWaste_id.getText();

        if (waste_id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a waste record to delete.").show();
            return;
        }

        // Confirmation Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Delete Waste Record");
        alert.setContentText("Are you sure you want to delete this waste record?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDelete = new WasteManagementModel().deleteWasteManagement(new WasteManagementdto(waste_id));
                if (isDelete) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Waste Deleted Successfully").show();
                    loadTable(); // Refresh table after delete
                } else {
                    new Alert(Alert.AlertType.ERROR, "Waste Deletion Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error occurred while deleting: " + e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            // Validate ComboBox selections and quantity input
            if (cmbMilling_id.getValue() == null || cmbWaste_type.getValue() == null || cmbDisposal_method.getValue() == null) {
                new Alert(Alert.AlertType.ERROR, "Please select a Milling ID, Waste Type, and Disposal Method.").show();
                return;
            }
            if (txtQuantity.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Quantity cannot be empty.").show();
                return;
            }
            if (txtrecorded_date.getText().isEmpty()) {
                new Alert(Alert.AlertType.ERROR, "Record Date cannot be empty.").show();
                return;
            }

            int quantity = Integer.parseInt(txtQuantity.getText());
            Date recordDate = Date.valueOf(txtrecorded_date.getText()); // Ensure date format is YYYY-MM-DD

            WasteManagementdto wasteManagementdto = new WasteManagementdto(
                    txtWaste_id.getText(),
                    cmbMilling_id.getValue(),
                    cmbWaste_type.getValue(),
                    quantity,
                    cmbDisposal_method.getValue(),
                    recordDate
            );

            boolean isSave = new WasteManagementModel().saveWasteManagement(wasteManagementdto);
            if (isSave) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Waste Management has been saved successfully").show();
                loadTable(); // Refresh table after save
            } else {
                new Alert(Alert.AlertType.ERROR, "Waste Management could not be saved").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid number format for Quantity. Please enter a valid number.").show();
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-DD.").show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String waste_id = txtWaste_id.getText();
        if (waste_id.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select a waste record to update.").show();
            return;
        }

        // Confirmation Alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Update Confirmation");
        alert.setHeaderText("Update Waste Record");
        alert.setContentText("Are you sure you want to update this waste record?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Validate ComboBox selections and quantity input
                if (cmbMilling_id.getValue() == null || cmbWaste_type.getValue() == null || cmbDisposal_method.getValue() == null) {
                    new Alert(Alert.AlertType.ERROR, "Please select a Milling ID, Waste Type, and Disposal Method.").show();
                    return;
                }
                if (txtQuantity.getText().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Quantity cannot be empty.").show();
                    return;
                }
                if (txtrecorded_date.getText().isEmpty()) {
                    new Alert(Alert.AlertType.ERROR, "Record Date cannot be empty.").show();
                    return;
                }

                int quantity = Integer.parseInt(txtQuantity.getText());
                Date recordDate = Date.valueOf(txtrecorded_date.getText());

                WasteManagementdto wasteManagementdto = new WasteManagementdto(
                        txtWaste_id.getText(),
                        cmbMilling_id.getValue(),
                        cmbWaste_type.getValue(),
                        quantity,
                        cmbDisposal_method.getValue(),
                        recordDate
                );

                boolean isUpdate = new WasteManagementModel().updateWasteManagement(wasteManagementdto);
                if (isUpdate) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Waste Management has been updated successfully").show();
                    loadTable(); // Refresh table after update
                } else {
                    new Alert(Alert.AlertType.ERROR, "Waste Management could not be updated").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid number format for Quantity. Please enter a valid number.").show();
            } catch (IllegalArgumentException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-DD.").show();
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "An error occurred while updating: " + e.getMessage()).show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadNextId();
        loadTable();
        setCellValueFactory();
        loadMillingIds();
        loadWasteTypes();
        loadDisposalMethods();
        setLocalCurrentDate();


        cmbWaste_type.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cmbMilling_id.getValue() != null) {
                fetchQuantityForWasteType();
            }
        });


        cmbMilling_id.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && cmbWaste_type.getValue() != null) {
                fetchQuantityForWasteType();
            }
        });
    }

    private void loadNextId() {
        try {
            String nextId = WasteManagementModel.getNextId();
            txtWaste_id.setText(nextId);
            txtWaste_id.setEditable(false);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate next ID: " + e.getMessage()).show();
        }
    }

    private void loadMillingIds() {
        try {
            ObservableList<String> millingIds = FXCollections.observableArrayList(MillingProcessModel.getAllMillingIds());
            cmbMilling_id.setItems(millingIds);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load Milling IDs: " + e.getMessage()).show();
        }
    }

    private void loadWasteTypes() {
        ObservableList<String> wasteTypes = FXCollections.observableArrayList(
                "Husk", "Bran", "Broken Rice", "Stones", "Dust", "Other"
        );
        cmbWaste_type.setItems(wasteTypes);
        cmbWaste_type.setEditable(true);
    }

    private void loadDisposalMethods() {
        ObservableList<String> disposalMethods = FXCollections.observableArrayList(
                "Landfill", "Recycling", "Composting", "Incineration", "Other"
        );
        cmbDisposal_method.setItems(disposalMethods);
        cmbDisposal_method.setEditable(true); // Allow custom entries
    }

    private void setLocalCurrentDate() {
        txtrecorded_date.setText(LocalDate.now().toString());
        txtrecorded_date.setEditable(false); // Make it non-editable if always auto-filled
    }


    private void fetchQuantityForWasteType() {
        String millingId = cmbMilling_id.getValue();
        String wasteType = cmbWaste_type.getValue();

        if (millingId == null || wasteType == null || millingId.isEmpty() || wasteType.isEmpty()) {
            txtQuantity.clear();
            return;
        }

        try {
            MillingProcessdto millingProcess = MillingProcessModel.getMillingProcessByMillingId(millingId);
            if (millingProcess != null) {
                double quantity = 0.0;
                switch (wasteType) {
                    case "Husk":
                        quantity = millingProcess.getHusk();
                        break;
                    case "Bran":
                        quantity = millingProcess.getBran();
                        break;
                    case "Broken Rice":
                        quantity = millingProcess.getBrokenRice();
                        break;
                    case "Stones":
                    case "Dust":
                    case "Other":
                        quantity = 0.0;
                        break;
                    default:
                        quantity = 0.0;
                        break;
                }
                txtQuantity.setText(String.valueOf((int) quantity));
            } else {
                txtQuantity.clear();
                new Alert(Alert.AlertType.WARNING, "Milling process details not found for selected Milling ID.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error fetching quantity: " + e.getMessage()).show();
            txtQuantity.clear(); // Clear on error
        }
    }


    private void setCellValueFactory() {
        colWaste_id.setCellValueFactory(new PropertyValueFactory<>("wasteId"));
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colWaste_type.setCellValueFactory(new PropertyValueFactory<>("wasteType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDisposalMethod.setCellValueFactory(new PropertyValueFactory<>("disposalMethod"));
        colRecordedDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));
    }

    private void loadTable() {
        try {
            ArrayList<WasteManagementdto> wasteManagementdtos = WasteManagementModel.viewAllWasteManagement();
            if (wasteManagementdtos != null) {
                ObservableList<WasteManagementdto> observableList = FXCollections.observableArrayList(wasteManagementdtos);
                table.setItems(observableList);
            } else {


                table.setItems(FXCollections.observableArrayList()); // Clear table if no data
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading waste management data: " + e.getMessage()).show();
        }
    }

    private void clearFields() {
        loadNextId(); // Generate new ID
        cmbMilling_id.getSelectionModel().clearSelection();
        cmbWaste_type.getSelectionModel().clearSelection();
        cmbDisposal_method.getSelectionModel().clearSelection();
        txtQuantity.clear();
        setLocalCurrentDate();
        loadTable();
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        WasteManagementdto wasteManagementdto = table.getSelectionModel().getSelectedItem();
        if (wasteManagementdto != null) {
            txtWaste_id.setText(wasteManagementdto.getWasteId());
            cmbMilling_id.setValue(wasteManagementdto.getMillingId());
            cmbWaste_type.setValue(wasteManagementdto.getWasteType());
            cmbDisposal_method.setValue(wasteManagementdto.getDisposalMethod());
            txtQuantity.setText(String.valueOf(wasteManagementdto.getQuantity()));
            txtrecorded_date.setText(String.valueOf(wasteManagementdto.getRecordDate()));
            txtWaste_id.setEditable(false);
        }
    }
}