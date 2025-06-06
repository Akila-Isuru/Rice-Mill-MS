package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;


public class AdminPasswordController {

    @FXML
    private PasswordField txtAdminPassword;


    private SecondPage secondPageController;

    public void setSecondPageController(SecondPage controller) {
        this.secondPageController = controller;
    }

    @FXML
    void btnSubmitPasswordOnAction(ActionEvent event) {
        String enteredPassword = txtAdminPassword.getText();
        String correctPassword = "admin";

        if (enteredPassword.equals(correctPassword)) {

            Stage stage = (Stage) txtAdminPassword.getScene().getWindow();
            stage.close();
            if (secondPageController != null) {
                secondPageController.loadUserAndEmployeePage();
            }
        } else {

            new Alert(Alert.AlertType.ERROR, "Incorrect Admin Password!").show();
            txtAdminPassword.clear();
        }
    }
}