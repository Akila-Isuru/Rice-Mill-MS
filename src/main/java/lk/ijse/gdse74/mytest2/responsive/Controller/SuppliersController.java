package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Suppliersdto;
import lk.ijse.gdse74.mytest2.responsive.model.SuppliersModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SuppliersController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Suppliersdto, String> coladdress;

    @FXML
    private TableColumn<Suppliersdto, String> colcontatcnumber;

    @FXML
    private TableColumn<Suppliersdto, String> colemail;

    @FXML
    private TableColumn<Suppliersdto, String> colid;

    @FXML
    private TableColumn<Suppliersdto, String> colname;

    @FXML
    private TableView<Suppliersdto> tSuppliersTable;

    @FXML
    private TextField txtContact_number;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtaddress;

    SuppliersModel suppliersModel = new SuppliersModel();

    @FXML
    private TextField txtemail;
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private final String phonePattern = "^(\\d+)||((\\d+\\.)(\\d){2})$";


    @FXML
    void btnClearOnAction(ActionEvent event) {

    }

    @FXML
   public void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        try {
            boolean isDelete = SuppliersModel.deleteSupplier(new Suppliersdto(id));
            if (isDelete) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION,"Farmer delete successfully").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Farmer delete Faild").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error deleting").show();
        }

    }

    @FXML

    private void clearFields() {
        txtId.clear();
        txtName.clear();
        txtContact_number.clear();
        txtaddress.clear();
        txtemail.clear();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        Suppliersdto suppliersdto = new Suppliersdto(txtId.getText(), txtName.getText(), txtContact_number.getText(), txtaddress.getText(), txtemail.getText());
        try {
            boolean isSave = suppliersModel.updateSupplier(suppliersdto);
            if (isSave) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION, "Supplier has been updated successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Supplier update failed").show();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Supplier could not be updated").show();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colid.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));

        try {
            SuppliersModel suppliersModel = new SuppliersModel();
            ArrayList<Suppliersdto> suppliersdtos = SuppliersModel.viewAllSuppliers();
            if (suppliersdtos != null) {
                ObservableList<Suppliersdto> suppliersdtoObservableList = FXCollections.observableArrayList(suppliersdtos);
                tSuppliersTable.setItems(suppliersdtoObservableList);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void btnSaveOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {

        String name = txtName.getText();
        String contactNumber = txtContact_number.getText();
        String email = txtemail.getText();

         boolean isValidName = name.matches(namePattern);
         boolean isValidContact = contactNumber.matches(phonePattern);
         boolean isValidEmail = email.matches(emailPattern);

        Suppliersdto suppliersdto = new Suppliersdto(txtId.getText(), txtName.getText(), txtContact_number.getText(), txtaddress.getText(), txtemail.getText());
       if(isValidName && isValidContact && isValidEmail) {
           try {
               boolean isSave = suppliersModel.saveSupplier(suppliersdto);
               if (isSave) {
                   clearFields();
                   loadTable();
                   new Alert(Alert.AlertType.INFORMATION, "Supplier has been saved successfully").show();
               } else {
                   new Alert(Alert.AlertType.ERROR, "Supplier could not be saved").show();
               }

           } catch (Exception e) {
               e.printStackTrace();
               new Alert(Alert.AlertType.ERROR, "Supplier could not be saved").show();
           }
       }

    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Suppliersdto suppliersdto = (Suppliersdto) tSuppliersTable.getSelectionModel().getSelectedItem();
        if (suppliersdto != null) {
            txtId.setText(suppliersdto.getSupplierId());
            txtName.setText(suppliersdto.getName());
            txtContact_number.setText(suppliersdto.getContactNumber());
            txtaddress.setText(suppliersdto.getAddress());
            txtemail.setText(suppliersdto.getEmail());

        }

    }

    public void txtNameChange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        if (!isValidName) {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red");
        } else {
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

