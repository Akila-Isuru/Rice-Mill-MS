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
import lk.ijse.gdse74.mytest2.responsive.dto.Employeedto;
import lk.ijse.gdse74.mytest2.responsive.model.EmployeeModel;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML private TableColumn<Employeedto, String> colEmployeeId;
    @FXML private TableColumn<Employeedto, String> colName;
    @FXML private TableColumn<Employeedto, String> colAddress;
    @FXML private TableColumn<Employeedto, String> colContactNumber;
    @FXML private TableColumn<Employeedto, String> colJobRole;
    @FXML private TableColumn<Employeedto, BigDecimal> colBasicSalary;

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;

    @FXML private TableView<Employeedto> tblEmployees;
    @FXML private TextField txtEmployeeId;
    @FXML private TextField txtName;
    @FXML private TextField txtAddress;
    @FXML private TextField txtContactNumber;
    @FXML private TextField txtJobRole;
    @FXML private TextField txtBasicSalary;
    @FXML private TextField txtSearch;
    @FXML private Label lblEmployeeCount;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";
    private final String salaryPattern = "^\\d+(\\.\\d{1,2})?$"; // Allows positive numbers with up to 2 decimal places

    private ObservableList<Employeedto> employeeMasterData = FXCollections.observableArrayList();
    EmployeeModel employeeModel = new EmployeeModel();

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
        FilteredList<Employeedto> filteredData = new FilteredList<>(employeeMasterData, p -> true);

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(employee -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (employee.getEmployeeId().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getContactNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getAddress().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (employee.getJobRole().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                // Optionally, you can search by salary if it's represented as a string
                // else if (employee.getBasicSalary().toString().toLowerCase().contains(lowerCaseFilter)) {
                //     return true;
                // }
                return false;
            });

            SortedList<Employeedto> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tblEmployees.comparatorProperty());
            tblEmployees.setItems(sortedData);
            updateEmployeeCount();
        });
    }

    private void updateEmployeeCount() {
        lblEmployeeCount.setText("Employees: " + tblEmployees.getItems().size());
    }

    private void disableButtons(boolean disable) {
        btnUpdate.setDisable(disable);
        btnDelete.setDisable(disable);
        btnSave.setDisable(!disable);
    }

    private void loadTable() throws SQLException {
        colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("jobRole"));
        colBasicSalary.setCellValueFactory(new PropertyValueFactory<>("basicSalary"));


        try {
            ArrayList<Employeedto> employeedtos = employeeModel.viewAllEmployees();
            if(employeedtos != null) {
                employeeMasterData = FXCollections.observableArrayList(employeedtos);
                tblEmployees.setItems(employeeMasterData);
                updateEmployeeCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String contactNumber = txtContactNumber.getText();
        String basicSalaryStr = txtBasicSalary.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);
        boolean isValidSalary = basicSalaryStr.matches(salaryPattern);

        if(isValidName && isValidContact && isValidSalary) {
            try {
                BigDecimal basicSalary = new BigDecimal(basicSalaryStr);
                Employeedto employeedto = new Employeedto(
                        txtEmployeeId.getText(),
                        txtName.getText(),
                        txtAddress.getText(),
                        txtContactNumber.getText(),
                        txtJobRole.getText(),
                        basicSalary
                );
                boolean isSave = employeeModel.saveEmployee(employeedto);
                if (isSave) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Employee saved successfully").show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Employee saved Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error saving employee").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter valid data for Name, Contact Number, and Basic Salary.").show();
        }
    }

    private void clearFields() throws SQLException {
        txtName.setText("");
        txtAddress.setText("");
        txtContactNumber.setText("");
        txtJobRole.setText("");
        txtBasicSalary.setText("");
        loadNextId();
        disableButtons(true);
        Platform.runLater(() -> {
            txtEmployeeId.setText(txtEmployeeId.getText());
            System.out.println("UI refreshed with ID: " + txtEmployeeId.getText());
        });
        loadTable();
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = employeeModel.getNextId();
            txtEmployeeId.setText(nextId);
            txtEmployeeId.setEditable(false);
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
        alert.setHeaderText("Update Employee");
        alert.setContentText("Are you sure you want to update this employee?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String name = txtName.getText();
            String contactNumber = txtContactNumber.getText();
            String basicSalaryStr = txtBasicSalary.getText();

            boolean isValidName = name.matches(namePattern);
            boolean isValidContact = contactNumber.matches(phonePattern);
            boolean isValidSalary = basicSalaryStr.matches(salaryPattern);

            if(isValidName && isValidContact && isValidSalary) {
                try {
                    BigDecimal basicSalary = new BigDecimal(basicSalaryStr);
                    Employeedto employeedto = new Employeedto(
                            txtEmployeeId.getText(),
                            txtName.getText(),
                            txtAddress.getText(),
                            txtContactNumber.getText(),
                            txtJobRole.getText(),
                            basicSalary
                    );
                    boolean isUpdate = employeeModel.updateEmployee(employeedto);
                    if (isUpdate) {
                        clearFields();
                        new Alert(Alert.AlertType.INFORMATION, "Employee updated successfully").show();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Employee update Failed").show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error updating employee").show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR, "Please enter valid data for Name, Contact Number, and Basic Salary.").show();
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Delete Employee");
        alert.setContentText("Are you sure you want to delete this employee?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String id = txtEmployeeId.getText();
            try {
                boolean isDelete = employeeModel.deleteEmployee(new Employeedto(id));
                if (isDelete) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION,"Employee deleted successfully").show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION,"Employee delete Failed").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR,"Error deleting employee").show();
            }
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) throws SQLException {
        clearFields();
    }

    @FXML
    void tableColumnOnClicked(MouseEvent event) {
        Employeedto employeedto = tblEmployees.getSelectionModel().getSelectedItem();
        btnSave.setDisable(true);
        if(employeedto != null) {
            txtEmployeeId.setText(employeedto.getEmployeeId());
            txtName.setText(employeedto.getName());
            txtAddress.setText(employeedto.getAddress());
            txtContactNumber.setText(employeedto.getContactNumber());
            txtJobRole.setText(employeedto.getJobRole());
            txtBasicSalary.setText(employeedto.getBasicSalary().toPlainString()); // Convert BigDecimal to String for TextField
            disableButtons(false);
        }
    }

    public void txtNameChange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        txtName.setStyle(isValidName ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContactNumber.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        txtContactNumber.setStyle(isValidContact ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    public void txtBasicSalaryChange(KeyEvent keyEvent) {
        String salary = txtBasicSalary.getText();
        boolean isValidSalary = salary.matches(salaryPattern);
        txtBasicSalary.setStyle(isValidSalary ? "-fx-border-color: blue" : "-fx-border-color: red");
    }

    @FXML
    void searchEmployee(KeyEvent event) {
        // The search logic is handled by setupSearchFilter and its listener
    }

    @FXML
    void clearSearch(ActionEvent event) {
        txtSearch.clear();
        tblEmployees.setItems(employeeMasterData);
        updateEmployeeCount();
    }
}