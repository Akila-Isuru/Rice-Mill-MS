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

import java.io.IOException;


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

        String username=userName.getText();
        String Password=password.getText();

        if(username.equals("admin")&&Password.equals("1234") || username.equals("akila") && Password.equals("1234"))
        {
            Parent parent = FXMLLoader.load(getClass().getResource("/View/SecondPageView.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

            /*ancPage1.getChildren().clear();
            ancPage2.getChildren().clear();
            Parent parent = FXMLLoader.load(getClass().getResource("/view/SecondPageView.fxml"));
            ancPage1.getChildren().add(parent);
            ancPage2.getChildren().add(parent);*/
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Username or password are incorrect");
            alert.showAndWait();
        }

    }

    public void forgotPasswordOnAction(ActionEvent actionEvent) {

    }
}
