package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.gdse74.mytest2.responsive.model.UsersModel;
import lk.ijse.gdse74.mytest2.responsive.utill.EmailUtil;

import java.io.IOException;
import java.sql.SQLException;

public class ForgotPasswordController {

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnResetPassword;

    @FXML
    private Button btnBackToLogin;

    @FXML
    void resetPasswordOnAction(ActionEvent event) {
        String email = txtEmail.getText().trim();

        if (email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email field cannot be empty!");
            return;
        }

        try {
            UsersModel usersModel = new UsersModel();
            String password = usersModel.getPasswordByEmail(email);

            if (password != null) {

                boolean emailSent = EmailUtil.sendEmail(email, "Password Recovery",
                        "Your password is: " + password + "\n\nPlease change it after logging in.");

                if (emailSent) {
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Password has been sent to your email address.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error",
                            "Failed to send email. Please try again later.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Error",
                        "No account found with this email address.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "An error occurred while accessing the database: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error",
                    "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    void backToLoginOnAction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/View/FirstPageView.fxml"));
        Stage stage = (Stage) btnBackToLogin.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Login");
        stage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}