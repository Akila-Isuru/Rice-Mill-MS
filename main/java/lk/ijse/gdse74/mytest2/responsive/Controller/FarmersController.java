package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class FarmersController implements Initializable {

    @FXML private TableColumn<Farmersdto, String> coladdress;
    @FXML private TableColumn<Farmersdto, String> colcontatcnumber;
    @FXML private TableColumn<Farmersdto, String> colid;
    @FXML private TableColumn<Farmersdto, String> colname;
    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableView<Farmersdto> tfarmersTable;
    @FXML private TextField txtContact_number;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtaddress;
    @FXML private TextField txtSearch;
    @FXML private Label lblFarmerCount;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.lk$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";

    private ObservableList<Farmersdto> farmerMasterData = FXCollections.observableArrayList();
    FarmersModel farmersModel = new FarmersModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            loadNextId();
            disableButtons(true);
            setupSearchFilter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupSearchFilter() {
        FilteredList<Farmersdto> filteredData = new FilteredList<>(farmerMasterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(farmer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (farmer.getFarmerId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (farmer.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (farmer.getContactNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (farmer.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

            SortedList<Farmersdto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tfarmersTable.comparatorProperty());
            tfarmersTable.setItems(sortedData);
            updateFarmerCount();
        });
    }

    private void updateFarmerCount() {
        lblFarmerCount.setText("Farmers: " + tfarmersTable.getItems().size());
    }

    private void disableButtons(boolean disable) {
        btnUpdate.setDisable(disable);
        btnDelete.setDisable(disable);
        btnSave.setDisable(!disable);
    }

    private void loadTable() throws SQLException {
        colid.setCellValueFactory(new PropertyValueFactory<>("farmerId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));

        try {
            ArrayList<Farmersdto> farmersdtos = farmersModel.viewAllFarmers();
            if(farmersdtos != null) {
                farmerMasterData = FXCollections.observableArrayList(farmersdtos);
                tfarmersTable.setItems(farmerMasterData);
                updateFarmerCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);

        Farmersdto farmersdto = new Farmersdto(txtId.getText(), txtName.getText(), txtaddress.getText(), txtContact_number.getText());
        if(isValidName && isValidContact) {
            try {
                boolean isSave = farmersModel.saveFarmer(farmersdto);
                if (isSave) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Farmer saved successfully").show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Farmer saved Faild").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error saving").show();
            }
        }
    }

    private void clearFields() throws SQLException {
        txtName.setText("");
        txtContact_number.setText("");
        txtaddress.setText("");
        loadNextId();
        disableButtons(true);
        Platform.runLater(() -> {
            txtId.setText(txtId.getText());
            System.out.println("UI refreshed with ID: " + txtId.getText());
        });
        loadTable();
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = farmersModel.getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false);
            System.out.println("DEBUG: Next ID retrieved: " + nextId);
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage());
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Update Farmer");
        alert.setContentText("Are you sure you want to update this farmer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = txtName.getText();
            String contactNumber = txtContact_number.getText();

            boolean isValidName = name.matches(namePattern);
            boolean isValidContact = contactNumber.matches(phonePattern);

            Farmersdto farmersdto = new Farmersdto(txtId.getText(), txtName.getText(), txtaddress.getText(), txtContact_number.getText());
            if(isValidName && isValidContact) {
                try {
                    boolean isSave = farmersModel.updateFarmer(farmersdto);
                    if (isSave) {
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION, "Farmer updated successfully").show();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Farmer update Failed").show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error updating").show();
                }
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Farmer");
        alert.setContentText("Are you sure you want to delete this farmer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtId.getText();
            try {
                boolean isDelete = farmersModel.deleteFarmer(new Farmersdto(id));
                if (isDelete) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION,"Farmer deleted successfully").show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION,"Farmer delete Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error deleting").show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) throws SQLException {
        clearFields();
    }

    @FXML
    void tableColumnOnClicked(MouseEvent event) {
        Farmersdto farmersdto = tfarmersTable.getSelectionModel().getSelectedItem();
        btnSave.setDisable(true);
        if(farmersdto != null) {
            txtId.setText(farmersdto.getFarmerId());
            txtName.setText(farmersdto.getName());
            txtaddress.setText(farmersdto.getAddress());
            txtContact_number.setText(farmersdto.getContactNumber());
            disableButtons(false);
        }
    }

    public void txtNamehange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        txtName.setStyle(isValidName ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContact_number.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        txtContact_number.setStyle(isValidContact ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    void searchFarmer(KeyEvent event) {
        // Handled by the search filter setup
    }

    @FXML
    void clearSearch(ActionEvent event) {
        txtSearch.clear();
        tfarmersTable.setItems(farmerMasterData);
        updateFarmerCount();
    }
}