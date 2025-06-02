package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class LoadingScreenController {
    @FXML
    private AnchorPane rootPane;

    public void initialize() {

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), rootPane);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();


        fadeIn.setOnFinished(e -> {
            try {

                Parent firstPage = FXMLLoader.load(getClass().getResource("/View/FirstPageView.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(firstPage));
                stage.setTitle("Login");
                stage.show();


                ((Stage) rootPane.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}