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
import lk.ijse.gdse74.mytest2.responsive.dto.Customersdto;
import lk.ijse.gdse74.mytest2.responsive.model.CustomerModel;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomersController implements Initializable {

    @FXML private TableColumn<Customersdto, String> coladdress;
    @FXML private TableColumn<Customersdto,String> colcontatcnumber;
    @FXML private TableColumn<Customersdto, String> colemail;
    @FXML private TableColumn<Customersdto, String> colid;
    @FXML private TableColumn<Customersdto, String> colname;
    @FXML private TableView<Customersdto> table;
    @FXML private TextField txtContact_number;
    @FXML private TextField txtId;
    @FXML private TextField txtName;
    @FXML private TextField txtaddress;
    @FXML private TextField txtemail;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private TextField txtSearch;
    @FXML private Label lblCustomerCount;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.lk$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";

    private ObservableList<Customersdto> customerMasterData = FXCollections.observableArrayList();
    private final CustomerModel customerModel = new CustomerModel();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);

        loadTableData();
        loadNextId();
        setupSearchFilter();
    }

    private void setupSearchFilter() {
        FilteredList<Customersdto> filteredData = new FilteredList<>(customerMasterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (customer.getCustomerId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getContactNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (customer.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

            SortedList<Customersdto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
            updateCustomerCount();
        });
    }

    private void updateCustomerCount() {
        lblCustomerCount.setText("Customers: " + table.getItems().size());
    }

    private void loadTableData() throws Exception {
        colid.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            ArrayList<Customersdto> customers = customerModel.getAllCustomers();
            customerMasterData = FXCollections.observableArrayList(customers);
            table.setItems(customerMasterData);
            updateCustomerCount();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customer data", ButtonType.OK).show();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();
        String email = txtemail.getText();
        String address = txtaddress.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);
        boolean isValidEmail = email.matches(emailPattern);

        Customersdto customersdto = new Customersdto(txtId.getText(), txtName.getText(), txtContact_number.getText(), txtaddress.getText(), txtemail.getText());
        if (isValidName && isValidContact && isValidEmail) {
            try {
                boolean isSave = customerModel.saveCustomer(customersdto);
                if (isSave) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Customer save Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to save customer data").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please fill in valid details for Name, Contact Number, and Email.").show();
        }
    }

    private void clearFields() throws Exception {
        try {
            txtName.clear();
            txtContact_number.clear();
            txtaddress.clear();
            txtemail.clear();
            loadNextId();
            Platform.runLater(() -> {
                txtId.setText(txtId.getText());
                System.out.println("UI refreshed with ID: " + txtId.getText());
            });
            loadTableData();
            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error refreshing form").show();
        }
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = customerModel.getNextId();
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
        }
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Customer");
        alert.setContentText("Are you sure you want to update this customer?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = txtName.getText();
            String contactNumber = txtContact_number.getText();
            String email = txtemail.getText();
            String address = txtaddress.getText();

            boolean isValidName = name.matches(namePattern);
            boolean isValidContact = contactNumber.matches(phonePattern);
            boolean isValidEmail = email.matches(emailPattern);

            Customersdto customersdto = new Customersdto(txtId.getText(), txtName.getText(), txtContact_number.getText(), txtaddress.getText(), txtemail.getText());
            if (isValidName && isValidContact && isValidEmail) {
                try {
                    boolean isUpdate = customerModel.updateCustomer(customersdto);
                    if (isUpdate) {
                        new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully").show();
                        clearFields();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Customer update Failed").show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Failed to update customer data").show();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please fill in valid details for Name, Contact Number, and Email.").show();
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Customer");
        alert.setContentText("Are you sure you want to delete this customer? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtId.getText();
            try {
                boolean isDelete = customerModel.deleteCustomer(new Customersdto(id));
                if (isDelete) {
                    new Alert(Alert.AlertType.INFORMATION, "Customer deleted successfully").show();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Customer delete Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Failed to delete customer data").show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) throws Exception {
        clearFields();
        btnUpdate.setDisable(true);
        btnDelete.setDisable(true);
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Customersdto customersdto = table.getSelectionModel().getSelectedItem();
        btnSave.setDisable(true);
        if (customersdto != null) {
            txtId.setText(customersdto.getCustomerId());
            txtName.setText(customersdto.getName());
            txtContact_number.setText(customersdto.getContactNumber());
            txtaddress.setText(customersdto.getAddress());
            txtemail.setText(customersdto.getEmail());
            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void txtNameChange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        txtName.setStyle(isValidName ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContact_number.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        txtContact_number.setStyle(isValidContact ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    public void txtEmailChange(KeyEvent keyEvent) {
        String email = txtemail.getText();
        boolean isValidEmail = email.matches(emailPattern);
        txtemail.setStyle(isValidEmail ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    void searchCustomer(KeyEvent event) {
        // Handled by the search filter setup
    }

    @FXML
    void clearSearch(ActionEvent event) {
        txtSearch.clear();
        table.setItems(customerMasterData);
        updateCustomerCount();
    }
}