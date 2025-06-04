package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

public class EmployeeUserController {

    @FXML
    private AnchorPane userEmployeeAnc;

    @FXML
    void btnEmployeeManagementOnAction(ActionEvent event) {
        navigateTo("/View/EmployeeView.fxml");

    }

    @FXML
    void btnUserManagementOnAction(ActionEvent event) {
        navigateTo("/View/UserPageView.fxml");

    }
    private void navigateTo(String path) {
        try {
            userEmployeeAnc.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(userEmployeeAnc.widthProperty());
            anchorPane.prefHeightProperty().bind(userEmployeeAnc.heightProperty());

            userEmployeeAnc.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong while loading the main container", ButtonType.OK).show();
            e.printStackTrace();
        }

    }

    public void btnEmployeeAttendanceOnAction(ActionEvent actionEvent) {
        navigateTo("/View/AttendanceManagement.fxml");
    }
}
