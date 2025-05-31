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
import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.model.MillingProcessModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Optional; // Import Optional for confirmation dialogs
import java.util.ResourceBundle;

public class MillingProcessController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<MillingProcessdto,Double> colBran_rice;

    @FXML
    private TableColumn<MillingProcessdto,Double> colBroken_rice;

    @FXML
    private TableColumn<MillingProcessdto, Time> colEnd_time;

    @FXML
    private TableColumn<MillingProcessdto,Double> colHusk;

    @FXML
    private TableColumn<MillingProcessdto,String> colMilling_id;

    @FXML
    private TableColumn<MillingProcessdto,String> colPaddy_id;

    @FXML
    private TableColumn<MillingProcessdto,Time> colStart_time;

    @FXML
    private TableColumn<MillingProcessdto,Double> colmilled_Quantity;

    @FXML
    private TableView<MillingProcessdto> table;

    @FXML
    private TextField txtBran;

    @FXML
    private TextField txtBrokenRice;

    @FXML
    private TextField txtEnd_time;

    @FXML
    private TextField txtHusk;

    @FXML
    private TextField txtMilledQuantity;

    @FXML
    private TextField txtMilling_id;

    @FXML
    private TextField txtPaddyid;

    @FXML
    private TextField txtStart_time;

    private final String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$"; // This pattern is defined but not used for validation in the provided code.

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
        alert.setHeaderText("Delete Milling Process");
        alert.setContentText("Are you sure you want to delete this milling process? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtMilling_id.getText();
            try {
                boolean isDelete = MillingProcessModel.deleteMillingProcess(new MillingProcessdto(id));
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION, "Milling Process Deleted Successfully").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Milling Process Not Deleted").show();
                }

            } catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete milling process data").show();
            }
        }
    }

    @FXML
    public  void btnSaveOnAction(ActionEvent event) {

        String millingId = txtMilling_id.getText();
        String paddy = txtPaddyid.getText();
        // Basic validation for time format before parsing
        if (!txtStart_time.getText().matches(timePattern) || !txtEnd_time.getText().matches(timePattern)) {
            new Alert(Alert.AlertType.ERROR, "Invalid Time Format. Please use HH:MM:SS.").show();
            return;
        }

        Time starTime = Time.valueOf(txtStart_time.getText());
        Time endTime = Time.valueOf(txtEnd_time.getText());

        // Basic validation for numeric fields
        double milledQuantity, broken, husk, bran;
        try {
            milledQuantity = Double.parseDouble(txtMilledQuantity.getText());
            broken = Double.parseDouble(txtBrokenRice.getText());
            husk = Double.parseDouble(txtHusk.getText());
            bran = Double.parseDouble(txtBran.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric input. Please enter valid numbers for quantities.").show();
            return;
        }


        MillingProcessdto millingProcessdto = new MillingProcessdto(millingId,paddy,starTime,endTime,milledQuantity,broken,husk,bran);
        try {
            // No need to instantiate MillingProcessModel here, as saveMillingProcess is static
            boolean isSave = MillingProcessModel.saveMillingProcess(millingProcessdto);
            if(isSave){
                new Alert(Alert.AlertType.INFORMATION,"Milling Process Saved Successfully").show();
                clearFields();
            }else {
                new Alert(Alert.AlertType.ERROR,"Milling Process Not Saved").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong while saving milling process").show();
        }
    }

    private void clearFields() throws SQLException {
        txtMilling_id.clear();
        txtPaddyid.clear();
        txtStart_time.clear();
        txtEnd_time.clear();
        txtMilledQuantity.clear();
        txtBrokenRice.clear();
        txtHusk.clear();
        txtBran.clear();
        loadNextId();
        Platform.runLater(() -> {
            txtMilling_id.setText(txtMilling_id.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtMilling_id.getText());
        });
        loadTable(); // Refresh table data after clearing fields
        // Disable update and delete buttons after clearing fields
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent event) {
        // Confirmation dialog for update
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Milling Process");
        alert.setContentText("Are you sure you want to update this milling process?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String millingId = txtMilling_id.getText();
            String paddy = txtPaddyid.getText();

            // Basic validation for time format before parsing
            if (!txtStart_time.getText().matches(timePattern) || !txtEnd_time.getText().matches(timePattern)) {
                new Alert(Alert.AlertType.ERROR, "Invalid Time Format. Please use HH:MM:SS.").show();
                return;
            }

            Time starTime = Time.valueOf(txtStart_time.getText());
            Time endTime = Time.valueOf(txtEnd_time.getText());

            // Basic validation for numeric fields
            double milledQuantity, broken, husk, bran;
            try {
                milledQuantity = Double.parseDouble(txtMilledQuantity.getText());
                broken = Double.parseDouble(txtBrokenRice.getText());
                husk = Double.parseDouble(txtHusk.getText());
                bran = Double.parseDouble(txtBran.getText());
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric input. Please enter valid numbers for quantities.").show();
                return;
            }

            MillingProcessdto millingProcessdto = new MillingProcessdto(millingId,paddy,starTime,endTime,milledQuantity,broken,husk,bran);
            try {
                // No need to instantiate MillingProcessModel here, as updateMillingProcess is static
                boolean isUpdate = MillingProcessModel.updateMillingProcess(millingProcessdto);
                if(isUpdate){
                    new Alert(Alert.AlertType.INFORMATION,"Milling Process Updated Successfully").show();
                    clearFields();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Milling Process Not Updated").show();
                }

            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Something went wrong while updating milling process").show();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Disable update and delete buttons initially
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        try {
            loadNextId();
        } catch (SQLException e) {
            throw new RuntimeException("Error loading next ID on initialization", e);
        }
        loadTable();
    }

    private void loadNextId() throws SQLException {
        String id = new MillingProcessModel().getNextId();
        txtMilling_id.setText(id);
        txtMilling_id.setEditable(false); // Make ID field non-editable
    }

    private void loadTable() {
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colStart_time.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd_time.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colmilled_Quantity.setCellValueFactory(new PropertyValueFactory<>("milledQuantity"));
        colBroken_rice.setCellValueFactory(new PropertyValueFactory<>("brokenRice"));
        colHusk.setCellValueFactory(new PropertyValueFactory<>("husk"));
        colBran_rice.setCellValueFactory(new PropertyValueFactory<>("bran"));

        try {
            // No need to instantiate MillingProcessModel here, as viewAllMillingProcess is static
            ArrayList<MillingProcessdto> millingProcessdtos = MillingProcessModel.viewAllMillingProcess();
            if(millingProcessdtos != null) {
                ObservableList<MillingProcessdto> observableList = FXCollections.observableList(millingProcessdtos);
                table.setItems(observableList);
            } else {
                new Alert(Alert.AlertType.ERROR, "No milling process data was retrieved.").show();
            }

        } catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load milling process data").show();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        MillingProcessdto millingProcessdto = table.getSelectionModel().getSelectedItem();
        if (millingProcessdto != null) {
            txtMilling_id.setText(millingProcessdto.getMillingId());
            txtPaddyid.setText(millingProcessdto.getPaddyId());
            txtStart_time.setText(String.valueOf(millingProcessdto.getStartTime()));
            txtEnd_time.setText(String.valueOf(millingProcessdto.getEndTime()));
            txtMilledQuantity.setText(String.valueOf(millingProcessdto.getMilledQuantity()));
            txtBrokenRice.setText(String.valueOf(millingProcessdto.getBrokenRice()));
            txtHusk.setText(String.valueOf(millingProcessdto.getHusk()));
            txtBran.setText(String.valueOf(millingProcessdto.getBran()));

            // Enable update and delete buttons when a milling process is selected
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
            btnSave.setDisable(true);
        }
    }
}