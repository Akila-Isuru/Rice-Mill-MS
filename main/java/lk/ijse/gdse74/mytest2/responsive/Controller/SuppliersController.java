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
import lk.ijse.gdse74.mytest2.responsive.dto.Suppliersdto;
import lk.ijse.gdse74.mytest2.responsive.model.SuppliersModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {
    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<Suppliersdto, String> coladdress;
    @FXML private TableColumn<Suppliersdto, String> colcontatcnumber;
    @FXML private TableColumn<Suppliersdto, String> colemail;
    @FXML private TableColumn<Suppliersdto, String> colid;
    @FXML private TableColumn<Suppliersdto, String> colname;
    @FXML private TableView<Suppliersdto> tSuppliersTable;
    @FXML private TextField txtContact_number;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtaddress;
    @FXML private TextField txtemail;
    @FXML private TextField txtSearch;
    @FXML private Label lblSupplierCount;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^0\\d{9}$";

    private ObservableList<Suppliersdto> supplierMasterData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            disableButtons(true);
            loadNextId();
            setupSearchFilter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTable() {
        colid.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            ArrayList<Suppliersdto> suppliersdtos = SuppliersModel.viewAllSuppliers();
            supplierMasterData = FXCollections.observableArrayList(suppliersdtos);
            tSuppliersTable.setItems(supplierMasterData);
            updateSupplierCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupSearchFilter() {
        FilteredList<Suppliersdto> filteredData = new FilteredList<>(supplierMasterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (supplier.getSupplierId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (supplier.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (supplier.getContactNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (supplier.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (supplier.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

            SortedList<Suppliersdto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tSuppliersTable.comparatorProperty());
            tSuppliersTable.setItems(sortedData);
            updateSupplierCount();
        });
    }

    private void updateSupplierCount() {
        lblSupplierCount.setText("Suppliers: " + tSuppliersTable.getItems().size());
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new SuppliersModel().getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false);
            System.out.println("DEBUG: Next ID retrieved: " + nextId);
            if (nextId == null || nextId.isEmpty()) {
                System.out.println("DEBUG: Got empty or null ID");
            }
            txtId.setText(nextId);
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage());
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
            txtId.setText("S001");
            txtId.setEditable(false);
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();
        disableButtons(true);
        loadNextId();
    }

    @FXML
    public void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Supplier");
        alert.setContentText("Are you sure you want to delete this supplier?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtId.getText();
            try {
                boolean isDelete = SuppliersModel.deleteSupplier(new Suppliersdto(id));
                if (isDelete) {
                    clearFields();
                    loadTable();
                    disableButtons(true);
                    loadNextId();
                    new Alert(Alert.AlertType.INFORMATION,"Supplier deleted successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR,"Supplier delete failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error deleting supplier").show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Update Supplier");
        alert.setContentText("Are you sure you want to update this supplier?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Suppliersdto suppliersdto = new Suppliersdto(
                    txtId.getText(),
                    txtName.getText(),
                    txtContact_number.getText(),
                    txtaddress.getText(),
                    txtemail.getText()
            );

            try {
                boolean isSave = new SuppliersModel().updateSupplier(suppliersdto);
                if (isSave) {
                    clearFields();
                    loadTable();
                    disableButtons(true);
                    loadNextId();
                    new Alert(Alert.AlertType.INFORMATION, "Supplier updated successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Supplier update failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating supplier").show();
            }
        }
    }

    @FXML
    public void btnSaveOnAction(ActionEvent event) {
        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();
        String email = txtemail.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);
        boolean isValidEmail = email.matches(emailPattern);

        if(!isValidName || !isValidContact || !isValidEmail) {
            new Alert(Alert.AlertType.ERROR, "Invalid data. Please check fields").show();
            return;
        }

        Suppliersdto suppliersdto = new Suppliersdto(
                txtId.getText(),
                txtName.getText(),
                txtContact_number.getText(),
                txtaddress.getText(),
                txtemail.getText()
        );

        try {
            boolean isSave = new SuppliersModel().saveSupplier(suppliersdto);
            if (isSave) {
                clearFields();
                loadTable();
                loadNextId();
                new Alert(Alert.AlertType.INFORMATION, "Supplier saved successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Supplier save failed").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error saving supplier").show();
        }
    }

    private void clearFields() {
        txtName.clear();
        txtContact_number.clear();
        txtaddress.clear();
        txtemail.clear();
    }

    private void disableButtons(boolean disable) {
        btnUpdate.setDisable(disable);
        btnDelete.setDisable(disable);
        btnSave.setDisable(!disable);
    }

    @FXML
    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Suppliersdto suppliersdto = tSuppliersTable.getSelectionModel().getSelectedItem();
        if (suppliersdto != null) {
            txtId.setText(suppliersdto.getSupplierId());
            txtName.setText(suppliersdto.getName());
            txtContact_number.setText(suppliersdto.getContactNumber());
            txtaddress.setText(suppliersdto.getAddress());
            txtemail.setText(suppliersdto.getEmail());
            disableButtons(false);
        }
    }

    @FXML
    public void txtNameChange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        txtName.setStyle(isValidName ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContact_number.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        txtContact_number.setStyle(isValidContact ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    public void txtEmailChange(KeyEvent keyEvent) {
        String email = txtemail.getText();
        boolean isValidEmail = email.matches(emailPattern);
        txtemail.setStyle(isValidEmail ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    void searchSupplier(KeyEvent event) {
        // Handled by the search filter setup
    }

    @FXML
    void clearSearch(ActionEvent event) {
        txtSearch.clear();
        tSuppliersTable.setItems(supplierMasterData);
        updateSupplierCount();
    }
}