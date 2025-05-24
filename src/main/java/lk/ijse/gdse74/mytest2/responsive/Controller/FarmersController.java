package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.model.CustomerModel;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class FarmersController implements Initializable {

    @FXML
    private TableColumn<Farmersdto, String> coladdress;

    @FXML
    private TableColumn<Farmersdto, String> colcontatcnumber;

    @FXML
    private TableColumn<Farmersdto, String> colid;

    @FXML
    private TableColumn<Farmersdto, String> colname;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<Farmersdto> tfarmersTable;

    @FXML
    private TextField txtContact_number;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtaddress;
    private TextField txtemail;
    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.lk$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";


    FarmersModel farmersModel = new FarmersModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            loadNextId();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTable() throws SQLException {

        colid.setCellValueFactory(new PropertyValueFactory<>("farmerId"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colcontatcnumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        coladdress.setCellValueFactory(new PropertyValueFactory<>("address"));
        try {
            FarmersModel farmersModel = new FarmersModel();
            ArrayList<Farmersdto> farmersdtos = farmersModel.viewAllFarmers();
            if(farmersdtos != null){
                ObservableList<Farmersdto> observableList = FXCollections.observableList(farmersdtos);
                tfarmersTable.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }
        }catch (Exception e ){
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
        txtId.setText("");
        txtContact_number.setText("");
        txtaddress.setText("");
        loadNextId();

        Platform.runLater(() -> {
            txtId.setText(txtId.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtId.getText());
        });
          loadTable();
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new FarmersModel().getNextId();
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

        boolean isValidName = name.matches(namePattern);
        boolean isValidContact = contactNumber.matches(phonePattern);

        Farmersdto farmersdto = new Farmersdto(txtId.getText(), txtName.getText(), txtaddress.getText(), txtContact_number.getText());
        if(isValidName && isValidContact) {
            try {
                boolean isSave = farmersModel.updateFarmer(farmersdto);
                if (isSave) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "Farmer update successfully").show();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Farmer update Faild").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Error updating").show();
            }
        }

    }


    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String id = txtId.getText();
        try {
            boolean isDelete = new FarmersModel().deleteFarmer(new Farmersdto(id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Farmer delete successfully").show();
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Farmer delete Faild").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Error deleting").show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {

    }

    @FXML
    void tableColumnOnClicked(MouseEvent event) {
Farmersdto farmersdto = (Farmersdto) tfarmersTable.getSelectionModel().getSelectedItem();
if(farmersdto != null){
    txtId.setText(farmersdto.getFarmerId());
    txtName.setText(farmersdto.getName());
    txtaddress.setText(farmersdto.getAddress());
    txtContact_number.setText(farmersdto.getContactNumber());
}
    }

    public void txtNamehange(KeyEvent keyEvent) {
        String name = txtName.getText();
        boolean isValidName = name.matches(namePattern);
        if(!isValidName){
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: red");

        }else {
            txtName.setStyle(txtName.getStyle() + ";-fx-border-color: blue");
        }
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContact_number.getText();
        boolean isValidContact = contactNumber.matches(phonePattern);
        if(!isValidContact){
            txtContact_number.setStyle(txtContact_number.getStyle() + ";-fx-border-color: red");
        }else {
            txtContact_number.setStyle(txtContact_number.getStyle() + ";-fx-border-color: blue");
        }
    }
}
