package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;


public class CustomersController implements Initializable {

    @FXML
    private TableColumn<Customersdto, String> coladdress;

    @FXML
    private TableColumn<Customersdto,String> colcontatcnumber;

    @FXML
    private TableColumn<Customersdto, String> colemail;

    @FXML
    private TableColumn<Customersdto, String> colid;

    @FXML
    private TableColumn<Customersdto, String> colname;

    @FXML
    private TableView<Customersdto> table;

    @FXML
    private TextField txtContact_number;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtaddress;

    @FXML
    private TextField txtemail;
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.lk$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";

    private final CustomerModel customerModel = new CustomerModel();
    @SneakyThrows
    @Override

    public void initialize(URL url, ResourceBundle resourceBundle) {
 loadTableData();
loadNextId();

    }

    private void loadTableData() throws Exception {
        colid.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            ArrayList<Customersdto> customers = customerModel.getAllCustomers();
            if (customers != null) {
                ObservableList<Customersdto> observableList = FXCollections.observableArrayList(customers);
                table.setItems(observableList);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Customer data is null");
                alert.setContentText("No customer data was retrieved.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load customer data", ButtonType.OK).show();
        }
    }



    public TableView<CustomerModel> tblCustomers;



    public void btnSaveOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();
        String email = txtemail.getText();
        String address = txtaddress.getText();


        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);
        boolean isValidEmail = email.matches(emailPattern);



      Customersdto customersdto = new Customersdto(txtId.getText(),txtName.getText(),txtContact_number.getText(),txtaddress.getText(),txtemail.getText());
      if(isValidName && isValidContact && isValidEmail) {
          try {
              CustomerModel customerModel = new CustomerModel();
              boolean isSave = customerModel.saveCustomer(customersdto);
              if (isSave) {
                  new Alert(Alert.AlertType.INFORMATION, "Customer saved successfully").show();
                  clearFields();
              } else {
                  new Alert(Alert.AlertType.INFORMATION, "Customer saved Faild").show();
              }

          } catch (Exception e) {
              e.printStackTrace();
              new Alert(Alert.AlertType.ERROR, "Failed to save customer data").show();
          }
      }


    }
    private void clearFields() throws Exception {
        try {
            // Clear fields first
            txtName.clear();
            txtContact_number.clear();
            txtaddress.clear();
            txtemail.clear();

            // Load new ID
            loadNextId();

            // Force UI update
            Platform.runLater(() -> {
                txtId.setText(txtId.getText()); // Forces refresh
                System.out.println("UI refreshed with ID: " + txtId.getText());
            });

            // Refresh table
            loadTableData();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error refreshing form").show();
        }


    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new CustomerModel().getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false);
            System.out.println("DEBUG: Next ID retrieved: " + nextId); // Debug line
            if (nextId == null || nextId.isEmpty()) {
                System.out.println("DEBUG: Got empty or null ID"); // Debug line
            }
            txtId.setText(nextId);
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage()); // Debug line
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
        }


    }

    public void btnUpdateOnAction(ActionEvent actionEvent) {
        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();
        String email = txtemail.getText();
        String address = txtaddress.getText();


        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);
        boolean isValidEmail = email.matches(emailPattern);

        Customersdto customersdto = new Customersdto(txtId.getText(),txtName.getText(),txtContact_number.getText(),txtaddress.getText(),txtemail.getText());
         if(isValidName && isValidContact && isValidEmail) {
             try {
                 CustomerModel customerModel = new CustomerModel();
                 boolean isUpdate = customerModel.updateCustomer(customersdto);
                 if (isUpdate) {
                     clearFields();
                     new Alert(Alert.AlertType.INFORMATION, "Customer updated successfully").show();
                 } else {
                     new Alert(Alert.AlertType.INFORMATION, "Customer update Faild").show();
                 }

             } catch (Exception e) {
                 e.printStackTrace();
                 new Alert(Alert.AlertType.ERROR, "Failed to update customer data").show();
             }
         }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        try {
            boolean isDelete = new CustomerModel().deleteCustomer(new Customersdto(id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Customer delete successfully").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Customer delete Faild").show();
            }

        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete customer data").show();
        }



    }

    public void btnClearOnAction(ActionEvent actionEvent) throws Exception {
        clearFields();

    }


    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Customersdto customersdto = (Customersdto) table.getSelectionModel().getSelectedItem();
        if (customersdto != null) {
            txtId.setText(customersdto.getCustomerId());
            txtName.setText(customersdto.getName());
            txtContact_number.setText(customersdto.getContactNumber());
            txtaddress.setText(customersdto.getAddress());
            txtemail.setText(customersdto.getEmail());

        }

    }

    public void txtNameChange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red");
        }else {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: blue");
        }
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContact_number.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        if (!isValidContact) {
            txtContact_number.setStyle(txtContact_number.getStyle() + ";-fx-border-color: red");
        }else {
            txtContact_number.setStyle(txtContact_number.getStyle() + ";-fx-border-color: blue");
        }
    }

    public void txtEmailChange(KeyEvent keyEvent) {
        String email = txtemail.getText();
        boolean isValidEmail = email.matches(emailPattern);
        if (!isValidEmail) {
            txtemail.setStyle(txtemail.getStyle() + ";-fx-border-color: red");
        }else {
            txtemail.setStyle(txtemail.getStyle() + ";-fx-border-color: blue");
        }
    }
}
