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
import lk.ijse.gdse74.mytest2.responsive.dto.QualityCheckdto;
import lk.ijse.gdse74.mytest2.responsive.model.QualityCheckModel;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class QualityCheckController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<QualityCheckdto,String> colBrokent_precentage;
    @FXML private TableColumn<QualityCheckdto,String> colCheck_Id;
    @FXML private TableColumn<QualityCheckdto,String> colForeignMaterial;
    @FXML private TableColumn<QualityCheckdto,String> colMoisture_level;
    @FXML private TableColumn<QualityCheckdto,String> colPaddy_id;
    @FXML private TableColumn<QualityCheckdto, Date> col_inception_date;
    @FXML private TableView<QualityCheckdto> table;
    @FXML private TextField txtBroken_precentage;
    @FXML private TextField txtCheck_id;
    @FXML private TextField txtForeign_Material;
    @FXML private TextField txtInception_date;
    @FXML private TextField txtMoisture_level;
    @FXML private TextField txtPaddy_id;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Quality Check Record");
        alert.setContentText("Are you sure you want to delete this quality check record?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new QualityCheckModel().deleteQualityCheck(
                        new QualityCheckdto(txtCheck_id.getText())
                );

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                    clearFields();
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
            // Validate fields
            if (!validateFields()) {
                return;
            }

            Date inceptionDate = Date.valueOf(txtInception_date.getText());

            QualityCheckdto dto = new QualityCheckdto(
                    txtCheck_id.getText(),
                    txtPaddy_id.getText(),
                    txtMoisture_level.getText(),
                    txtForeign_Material.getText(),
                    txtBroken_precentage.getText(),
                    inceptionDate
            );

            boolean isSaved = new QualityCheckModel().saveQualityCheck(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!").show();
            }
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-DD").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            // Validate fields
            if (!validateFields()) {
                return;
            }

            Date inceptionDate = Date.valueOf(txtInception_date.getText());

            QualityCheckdto dto = new QualityCheckdto(
                    txtCheck_id.getText(),
                    txtPaddy_id.getText(),
                    txtMoisture_level.getText(),
                    txtForeign_Material.getText(),
                    txtBroken_precentage.getText(),
                    inceptionDate
            );

            boolean isUpdated = new QualityCheckModel().updateQualityCheck(dto);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
            }
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid date format. Please use YYYY-MM-DD").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        disableButtons();
        loadNextId();
    }

    private void setCellValueFactories() {
        colCheck_Id.setCellValueFactory(new PropertyValueFactory<>("checkId"));
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colMoisture_level.setCellValueFactory(new PropertyValueFactory<>("moistureLevel"));
        colForeignMaterial.setCellValueFactory(new PropertyValueFactory<>("foreignMaterial"));
        colBrokent_precentage.setCellValueFactory(new PropertyValueFactory<>("brokenPrecentage"));
        col_inception_date.setCellValueFactory(new PropertyValueFactory<>("inceptionDate"));
    }

    private void loadTable() {
        try {
            ArrayList<QualityCheckdto> allQualityChecks = QualityCheckModel.viewAllQualityCheck();
            table.setItems(FXCollections.observableArrayList(allQualityChecks));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
        }
    }

    private void disableButtons() {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    private void enableButtons() {
        btnUpdate.setDisable(false);
        btnDelete.setDisable(false);
    }

    private void loadNextId() {
        try {
            String nextId = new QualityCheckModel().getNextId();
            txtCheck_id.setText(nextId);
            txtCheck_id.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate next ID").show();
        }
    }

    private boolean validateFields() {
        if (txtPaddy_id.getText().isEmpty() || txtMoisture_level.getText().isEmpty() ||
                txtForeign_Material.getText().isEmpty() || txtBroken_precentage.getText().isEmpty() ||
                txtInception_date.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtPaddy_id.clear();
        txtMoisture_level.clear();
        txtForeign_Material.clear();
        txtBroken_precentage.clear();
        txtInception_date.clear();

        loadNextId();
        loadTable();
        disableButtons();
    }

    @FXML
    void tableColumnOnClicked(MouseEvent mouseEvent) {
        QualityCheckdto selectedItem = table.getSelectionModel().getSelectedItem();
        btnSave.setDisable(true);
        if (selectedItem != null) {
            txtCheck_id.setText(selectedItem.getCheckId());
            txtPaddy_id.setText(selectedItem.getPaddyId());
            txtMoisture_level.setText(selectedItem.getMoistureLevel());
            txtForeign_Material.setText(selectedItem.getForeignMaterial());
            txtBroken_precentage.setText(selectedItem.getBrokenPrecentage());
            txtInception_date.setText(selectedItem.getInceptionDate().toString());

            enableButtons();
        }
    }
}