package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.stage.Stage;
import lk.ijse.gdse74.mytest2.responsive.model.UsersModel; // Import the UsersModel
import lk.ijse.gdse74.mytest2.responsive.dto.Usersdto; // Import the Usersdto

import java.io.IOException;
import java.sql.SQLException;

public class FirstPage {

    @FXML
    private AnchorPane ancPage1;

    @FXML
    private AnchorPane ancPage2;

    @FXML
    private Button btnSecond;

    @FXML
    private PasswordField password;

    @FXML
    private TextField userName;

    @FXML
    void btnSecondPageOnAction(ActionEvent event) throws IOException {

        String enteredUsername = userName.getText();
        String enteredPassword = password.getText();

        UsersModel usersModel = new UsersModel(); // Create an instance of UsersModel

        try {
            // Attempt to find the user by username and password
            Usersdto user = usersModel.getUserByUsernameAndPassword(enteredUsername, enteredPassword);

            if (user != null) {
                // If a user is found, credentials are correct
                Parent parent = FXMLLoader.load(getClass().getResource("/View/SecondPageView.fxml"));
                Scene scene = new Scene(parent);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Dashboard"); // Changed title to Dashboard
                stage.show();

                // Close the current login window if needed
                Stage currentStage = (Stage) btnSecond.getScene().getWindow();
                currentStage.close();

            } else {
                // If no user is found, credentials are incorrect
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText(null);
                alert.setContentText("Username or password incorrect. Please try again.");
                alert.showAndWait();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while connecting to the database: " + e.getMessage());
            alert.showAndWait();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) {
        // You can implement forgotten password functionality here
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Forgot Password");
        alert.setHeaderText(null);
        alert.setContentText("Please contact administration to reset your password.");
        alert.showAndWait();
    }
}