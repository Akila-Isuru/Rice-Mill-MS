package lk.ijse.gdse74.mytest2.responsive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AppInitializer extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent loadingScreen = FXMLLoader.load(getClass().getResource("/View/LoadingScreen.fxml"));
        Scene scene = new Scene(loadingScreen);

       
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}   