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
import javafx.scene.paint.Color;
import lk.ijse.gdse74.mytest2.responsive.dto.QualityCheckdto;
import lk.ijse.gdse74.mytest2.responsive.model.QualityCheckModel;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class QualityCheckController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<QualityCheckdto, String> colBrokent_precentage;
    @FXML private TableColumn<QualityCheckdto, String> colCheck_Id;
    @FXML private TableColumn<QualityCheckdto, String> colForeignMaterial;
    @FXML private TableColumn<QualityCheckdto, String> colGrade;
    @FXML private TableColumn<QualityCheckdto, String> colMoisture_level;
    @FXML private TableColumn<QualityCheckdto, String> colPaddy_id;
    @FXML private TableColumn<QualityCheckdto, Date> col_inception_date;
    @FXML private DatePicker datePicker;
    @FXML private TableView<QualityCheckdto> table;
    @FXML private TextField txtBroken_precentage;
    @FXML private TextField txtCheck_id;
    @FXML private TextField txtForeign_Material;
    @FXML private TextField txtMoisture_level;
    @FXML private TextField txtPaddy_id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        disableButtons();
        loadNextId();
        setupDatePicker();
        setupValidation();
    }

    private void setCellValueFactories() {
        colCheck_Id.setCellValueFactory(new PropertyValueFactory<>("checkId"));
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colMoisture_level.setCellValueFactory(new PropertyValueFactory<>("moistureLevel"));
        colForeignMaterial.setCellValueFactory(new PropertyValueFactory<>("foreignMaterial"));
        colBrokent_precentage.setCellValueFactory(new PropertyValueFactory<>("brokenPrecentage"));
        col_inception_date.setCellValueFactory(new PropertyValueFactory<>("inceptionDate"));

        // Grade column with dynamic calculation and coloring
        colGrade.setCellFactory(column -> new TableCell<QualityCheckdto, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setText(null);
                    setStyle("");
                } else {
                    QualityCheckdto dto = getTableRow().getItem();
                    String grade = calculateGrade(dto);
                    setText(grade);

                    // Color coding
                    switch (grade) {
                        case "A":
                            setTextFill(Color.GREEN);
                            break;
                        case "B":
                            setTextFill(Color.ORANGE);
                            break;
                        case "C":
                            setTextFill(Color.RED);
                            break;
                        default:
                            setTextFill(Color.BLACK);
                    }
                }
            }

            private String calculateGrade(QualityCheckdto dto) {
                try {
                    double moisture = Double.parseDouble(dto.getMoistureLevel());
                    double foreign = Double.parseDouble(dto.getForeignMaterial());
                    double broken = Double.parseDouble(dto.getBrokenPrecentage());

                    if (moisture < 14 && foreign < 1 && broken < 5) {
                        return "A";
                    } else if (moisture < 16 && foreign < 2 && broken < 10) {
                        return "B";
                    } else {
                        return "C";
                    }
                } catch (NumberFormatException e) {
                    return "N/A";
                }
            }
        });
    }

    private void setupValidation() {
        txtMoisture_level.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePercentageField(txtMoisture_level, newValue);
        });

        txtForeign_Material.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePercentageField(txtForeign_Material, newValue);
        });

        txtBroken_precentage.textProperty().addListener((observable, oldValue, newValue) -> {
            validatePercentageField(txtBroken_precentage, newValue);
        });
    }

    private void validatePercentageField(TextField field, String value) {
        try {
            if (!value.isEmpty()) {
                double num = Double.parseDouble(value);
                if (num < 0 || num > 100) {
                    field.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
                } else {
                    field.setStyle("");
                }
            } else {
                field.setStyle("");
            }
        } catch (NumberFormatException e) {
            field.setStyle("-fx-border-color: red; -fx-border-width: 1px;");
        }
    }

    private void setupDatePicker() {
        datePicker.setValue(LocalDate.now());
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
                txtForeign_Material.getText().isEmpty() || txtBroken_precentage.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }

        // Check if any field has validation error (red border)
        if (txtMoisture_level.getStyle().contains("red") ||
                txtForeign_Material.getStyle().contains("red") ||
                txtBroken_precentage.getStyle().contains("red")) {
            new Alert(Alert.AlertType.ERROR, "Please correct the invalid fields").show();
            return false;
        }

        return true;
    }

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
            if (!validateFields()) {
                return;
            }

            Date inceptionDate = Date.valueOf(datePicker.getValue());

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
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        try {
            if (!validateFields()) {
                return;
            }

            Date inceptionDate = Date.valueOf(datePicker.getValue());

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
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
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
            datePicker.setValue(LocalDate.parse(selectedItem.getInceptionDate().toString()));

            enableButtons();
        }
    }

    private void clearFields() {
        txtPaddy_id.clear();
        txtMoisture_level.clear();
        txtForeign_Material.clear();
        txtBroken_precentage.clear();
        datePicker.setValue(LocalDate.now());

        loadNextId();
        loadTable();
        disableButtons();
        btnSave.setDisable(false);
    }
}