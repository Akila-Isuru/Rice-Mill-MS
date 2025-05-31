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
import lk.ijse.gdse74.mytest2.responsive.model.MillingProcessModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MillingProcessController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnOverride;
    @FXML private TableColumn<MillingProcessdto, Double> colBran_rice;
    @FXML private TableColumn<MillingProcessdto, Double> colBroken_rice;
    @FXML private TableColumn<MillingProcessdto, Time> colEnd_time;
    @FXML private TableColumn<MillingProcessdto, Double> colHusk;
    @FXML private TableColumn<MillingProcessdto, String> colMilling_id;
    @FXML private TableColumn<MillingProcessdto, String> colPaddy_id;
    @FXML private TableColumn<MillingProcessdto, Time> colStart_time;
    @FXML private TableColumn<MillingProcessdto, Double> colmilled_Quantity;
    @FXML private TableView<MillingProcessdto> table;
    @FXML private TextField txtBran;
    @FXML private TextField txtBrokenRice;
    @FXML private TextField txtHusk;
    @FXML private TextField txtMilledQuantity;
    @FXML private TextField txtMilling_id;
    @FXML private ComboBox<String> cmbPaddyId;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;
    @FXML private Spinner<Integer> endSecondSpinner;
    @FXML private Label lblDuration;
    @FXML private Label lblStartTime;

    private static final double BROKEN_RICE_RATIO = 0.05;
    private static final double HUSK_RATIO = 0.20;
    private static final double BRAN_RATIO = 0.10;
    private boolean overrideEnabled = false;
    private Time currentStartTime;
    private ObservableList<String> paddyIdList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentTimeAsStartTime();
        initTimeSpinners();
        setupAutomaticCalculations();
        disableButtons(true);
        loadPaddyIds();

        try {
            loadNextId();
            loadTable();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Class Not Found: " + e.getMessage());
        }
    }

    private void loadPaddyIds() {
        try {
            ArrayList<String> ids = MillingProcessModel.getAllPaddyIds();
            paddyIdList.clear();
            paddyIdList.addAll(ids);
            cmbPaddyId.setItems(paddyIdList);
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Failed to load paddy IDs: " + e.getMessage());
        }
    }

    private void setCurrentTimeAsStartTime() {
        LocalTime now = LocalTime.now();
        currentStartTime = Time.valueOf(now);
        lblStartTime.setText(String.format("%02d:%02d:%02d",
                now.getHour(), now.getMinute(), now.getSecond()));
    }

    private void initTimeSpinners() {
        LocalTime now = LocalTime.now();
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, now.getHour()));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, now.getMinute()));
        endSecondSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, now.getSecond()));

        endHourSpinner.valueProperty().addListener((obs, oldVal, newVal) -> calculateDuration());
        endMinuteSpinner.valueProperty().addListener((obs, oldVal, newVal) -> calculateDuration());
        endSecondSpinner.valueProperty().addListener((obs, oldVal, newVal) -> calculateDuration());
    }

    private void setupAutomaticCalculations() {
        txtMilledQuantity.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !overrideEnabled) {
                try {
                    calculateByproducts(Double.parseDouble(newVal));
                } catch (NumberFormatException e) {
                    clearByproductFields();
                }
            }
        });

        txtBrokenRice.setEditable(false);
        txtHusk.setEditable(false);
        txtBran.setEditable(false);
    }

    private void calculateByproducts(double milledQuantity) {
        double brokenRice = milledQuantity * BROKEN_RICE_RATIO;
        double husk = milledQuantity * HUSK_RATIO;
        double bran = milledQuantity * BRAN_RATIO;

        txtBrokenRice.setText(String.format("%.2f", brokenRice));
        txtHusk.setText(String.format("%.2f", husk));
        txtBran.setText(String.format("%.2f", bran));
    }

    private void calculateDuration() {
        try {
            Time endTime = getEndTimeFromSpinners();

            if (currentStartTime != null && endTime != null) {
                if (endTime.before(currentStartTime)) {
                    showInvalidDuration("Invalid: End before Start");
                } else {
                    showValidDuration(currentStartTime, endTime);
                }
            }
        } catch (Exception e) {
            showInvalidDuration("Invalid Time");
        }
    }

    private Time getEndTimeFromSpinners() {
        try {
            int hour = endHourSpinner.getValue();
            int minute = endMinuteSpinner.getValue();
            int second = endSecondSpinner.getValue();
            return new Time(hour, minute, second);
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            MillingProcessdto dto = createMillingProcessDto();
            boolean isSaved = MillingProcessModel.saveMillingProcess(dto);

            if (isSaved) {
                showAlert(Alert.AlertType.INFORMATION, "Process Saved");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Save Failed");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error saving: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (!validateInputs()) return;

        try {
            MillingProcessdto dto = createMillingProcessDto();
            boolean isUpdated = MillingProcessModel.updateMillingProcess(dto);

            if (isUpdated) {
                showAlert(Alert.AlertType.INFORMATION, "Process Updated");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error updating: " + e.getMessage());
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Milling Process");
        alert.setContentText("Are you sure you want to delete this process?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = MillingProcessModel.deleteMillingProcess(new MillingProcessdto(txtMilling_id.getText()));
                if (isDeleted) {
                    showAlert(Alert.AlertType.INFORMATION, "Process Deleted");
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Deletion Failed");
                }
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error deleting: " + e.getMessage());
            }
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        try {
            clearFields();
        } catch (SQLException | ClassNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Error clearing fields: " + e.getMessage());
        }
    }

    @FXML
    void toggleOverride(ActionEvent event) {
        overrideEnabled = !overrideEnabled;
        txtBrokenRice.setEditable(overrideEnabled);
        txtHusk.setEditable(overrideEnabled);
        txtBran.setEditable(overrideEnabled);

        if (overrideEnabled) {
            btnOverride.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
            btnOverride.setText("Lock");
        } else {
            btnOverride.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
            btnOverride.setText("Override");
            recalculateByproducts();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        MillingProcessdto process = table.getSelectionModel().getSelectedItem();
        if (process != null) {
            txtMilling_id.setText(process.getMillingId());
            cmbPaddyId.setValue(process.getPaddyId());
            txtMilledQuantity.setText(String.valueOf(process.getMilledQuantity()));
            txtBrokenRice.setText(String.valueOf(process.getBrokenRice()));
            txtHusk.setText(String.valueOf(process.getHusk()));
            txtBran.setText(String.valueOf(process.getBran()));

            currentStartTime = process.getStartTime();
            lblStartTime.setText(String.format("%02d:%02d:%02d",
                    currentStartTime.getHours(),
                    currentStartTime.getMinutes(),
                    currentStartTime.getSeconds()));

            endHourSpinner.getValueFactory().setValue(process.getEndTime().getHours());
            endMinuteSpinner.getValueFactory().setValue(process.getEndTime().getMinutes());
            endSecondSpinner.getValueFactory().setValue(process.getEndTime().getSeconds());

            disableButtons(false);
            btnSave.setDisable(true);
            overrideEnabled = true;
            toggleOverride(null);
        }
    }

    private MillingProcessdto createMillingProcessDto() {
        return new MillingProcessdto(
                txtMilling_id.getText(),
                cmbPaddyId.getValue(),
                currentStartTime,
                getEndTimeFromSpinners(),
                Double.parseDouble(txtMilledQuantity.getText()),
                Double.parseDouble(txtBrokenRice.getText()),
                Double.parseDouble(txtHusk.getText()),
                Double.parseDouble(txtBran.getText())
        );
    }

    private boolean validateInputs() {
        if (cmbPaddyId.getValue() == null || cmbPaddyId.getValue().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Please select a Paddy ID");
            return false;
        }

        try {
            Time endTime = getEndTimeFromSpinners();
            if (endTime == null || endTime.before(currentStartTime)) {
                showAlert(Alert.AlertType.ERROR, "Invalid time values");
                return false;
            }

            double milledQty = Double.parseDouble(txtMilledQuantity.getText());
            if (milledQty <= 0) {
                showAlert(Alert.AlertType.ERROR, "Milled quantity must be positive");
                return false;
            }

            return true;
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid numeric values");
            return false;
        }
    }

    private void clearFields() throws SQLException, ClassNotFoundException {
        cmbPaddyId.setValue(null);
        txtMilledQuantity.clear();
        clearByproductFields();
        setCurrentTimeAsStartTime();

        LocalTime now = LocalTime.now();
        endHourSpinner.getValueFactory().setValue(now.getHour());
        endMinuteSpinner.getValueFactory().setValue(now.getMinute());
        endSecondSpinner.getValueFactory().setValue(now.getSecond());

        lblDuration.setText("Duration: 00:00:00");
        lblDuration.setStyle("-fx-text-fill: black;");

        overrideEnabled = false;
        btnOverride.setStyle("-fx-background-color: #9b59b6; -fx-text-fill: white;");
        btnOverride.setText("Override");

        loadNextId();
        loadTable();
        disableButtons(true);
    }

    private void clearByproductFields() {
        txtBrokenRice.clear();
        txtHusk.clear();
        txtBran.clear();
    }

    private void loadNextId() throws SQLException, ClassNotFoundException {
        txtMilling_id.setText(new MillingProcessModel().getNextId());
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
            ArrayList<MillingProcessdto> processes = MillingProcessModel.viewAllMillingProcess();
            table.setItems(FXCollections.observableArrayList(processes));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error loading data: " + e.getMessage());
        }
    }

    private void disableButtons(boolean disable) {
        btnUpdate.setDisable(disable);
        btnDelete.setDisable(disable);
    }

    private void showInvalidDuration(String message) {
        lblDuration.setText(message);
        lblDuration.setStyle("-fx-text-fill: red;");
        disableSaveButtons(true);
    }

    private void showValidDuration(Time startTime, Time endTime) {
        long diff = endTime.getTime() - startTime.getTime();
        long diffHours = diff / (60 * 60 * 1000);
        long diffMinutes = (diff / (60 * 1000)) % 60;
        long diffSeconds = (diff / 1000) % 60;

        lblDuration.setText(String.format("Duration: %02d:%02d:%02d", diffHours, diffMinutes, diffSeconds));
        lblDuration.setStyle("-fx-text-fill: green;");
        disableSaveButtons(false);
    }

    private void disableSaveButtons(boolean disable) {
        btnSave.setDisable(disable);
        btnUpdate.setDisable(disable);
    }

    private void recalculateByproducts() {
        if (!txtMilledQuantity.getText().isEmpty()) {
            try {
                calculateByproducts(Double.parseDouble(txtMilledQuantity.getText()));
            } catch (NumberFormatException e) {
                clearByproductFields();
            }
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        new Alert(type, message).show();
    }
}