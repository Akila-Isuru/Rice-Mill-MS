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
import lk.ijse.gdse74.mytest2.responsive.db.DBConnection;
import lk.ijse.gdse74.mytest2.responsive.dto.Usersdto;
import lk.ijse.gdse74.mytest2.responsive.model.CustomerModel;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;
import lk.ijse.gdse74.mytest2.responsive.model.UsersModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UsersController implements Initializable {
    Connection connection = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableView<Usersdto> table;

    @FXML
    private TextField txtContactNumber;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtRole;

    @FXML
    private TableColumn<Usersdto,String> colcontact_number;

    @FXML
    private TableColumn<Usersdto,String> colemail;

    @FXML
    private TableColumn<Usersdto,String> colid;

    @FXML
    private TableColumn<Usersdto,String> colname;

    @FXML
    private TableColumn<Usersdto,String> colpassword;

    @FXML
    private TableColumn<Usersdto,String> colrole;

    private final String namePattern = "^[A-Za-z ]+$";
    private final String nicPattern = "^[0-9]{9}[vVxX]||[0-9]{12}$";
    private final String rolePattern = "^[A-Za-z ]+$";
    private final String emailPattern = "^[a-zA-Z0-9._%+-]+@ricemill\\.lk$";
    private final String phonePattern = "^(?:0|\\+94|0094)?(?:07\\d{8})$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadTable();
            loadNextId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new UsersModel().getNextId();
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

    public void loadTable() throws SQLException {
        colid.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colname.setCellValueFactory(new PropertyValueFactory<>("name"));
        colemail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colpassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colrole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colcontact_number.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
        try {
            UsersModel usersModel = new UsersModel();
            ArrayList<Usersdto> usersdtos = new UsersModel().viewAllUsers();
            if (usersdtos != null) {
                ObservableList<Usersdto> users = FXCollections.observableArrayList(usersdtos);
                table.setItems(users);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        try {
            boolean isDlete = new UsersModel().deleteUser(new Usersdto(id));
            if (isDlete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "User deleted successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "User delete not successfull").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "User delete not successfull").show();
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtContactNumber.getText();
        String role = txtRole.getText();
        String password = txtPassword.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);
        boolean isValidRole = role.matches(rolePattern);

        Usersdto usersdto = new Usersdto(txtId.getText(), txtName.getText(), txtEmail.getText(),
                password, txtRole.getText(), txtContactNumber.getText());

        if(isValidName && isValidEmail && isValidPhone && isValidRole) {
            try {
                boolean isSave = new UsersModel().saveUser(usersdto);
                if (isSave) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "User saved successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "User could not be saved").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "User could not be saved").show();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR,"Invalid Input Fields");
            alert.show();
        }
    }

    private void clearFields() throws SQLException {
        loadTable();
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtRole.setText("");
        txtContactNumber.setText("");
        loadNextId();
        Platform.runLater(() -> {
            txtId.setText(txtId.getText());
            System.out.println("UI refreshed with ID: " + txtId.getText());
        });
        loadTable();
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String name = txtName.getText();
        String email = txtEmail.getText();
        String phone = txtContactNumber.getText();
        String role = txtRole.getText();
        String password = txtPassword.getText();

        boolean isValidName = name.matches(namePattern);
        boolean isValidEmail = email.matches(emailPattern);
        boolean isValidPhone = phone.matches(phonePattern);
        boolean isValidRole = role.matches(rolePattern);

        Usersdto usersdto = new Usersdto(txtId.getText(), txtName.getText(), txtEmail.getText(),
                password, txtRole.getText(), txtContactNumber.getText());

        if(isValidName && isValidEmail && isValidPhone && isValidRole) {
            try {
                boolean isUpdate = new UsersModel().updateUser(usersdto);
                if (isUpdate) {
                    clearFields();
                    new Alert(Alert.AlertType.INFORMATION, "User updated successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "User could not be updated").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "User could not be updated").show();
            }
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Usersdto usersdto = (Usersdto) table.getSelectionModel().getSelectedItem();
        if (usersdto != null) {
            txtId.setText(usersdto.getUser_id());
            txtName.setText(usersdto.getName());
            txtEmail.setText(usersdto.getEmail());
            txtPassword.setText(usersdto.getPassword());
            txtRole.setText(usersdto.getRole());
            txtContactNumber.setText(usersdto.getContact_number());
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

    public void txtEmailChange(KeyEvent keyEvent) {
        String email = txtEmail.getText();
        boolean isValidEmail = email.matches(emailPattern);
        if(!isValidEmail) {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: red");
        } else {
            txtEmail.setStyle(txtEmail.getStyle() + ";-fx-border-color: blue");
        }
    }

    public void txtRoleChange(KeyEvent keyEvent) {
        String role = txtRole.getText();
        boolean isValidRole = role.matches(rolePattern);
        if(!isValidRole) {
            txtRole.setStyle(txtRole.getStyle() + ";-fx-border-color: red");
        } else {
            txtRole.setStyle(txtRole.getStyle() + ";-fx-border-color: blue");
        }
    }

    public void txtContactChange(KeyEvent keyEvent) {
        String contactNumber = txtContactNumber.getText();
        boolean isValidPhone = contactNumber.matches(phonePattern);
        if(!isValidPhone) {
            txtContactNumber.setStyle(txtContactNumber.getStyle() + ";-fx-border-color: red");
        } else {
            txtContactNumber.setStyle(txtContactNumber.getStyle() + ";-fx-border-color: blue");
        }
    }
}