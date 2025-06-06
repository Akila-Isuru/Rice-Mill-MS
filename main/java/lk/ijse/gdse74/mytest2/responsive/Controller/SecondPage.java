package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SecondPage implements Initializable {
    public AnchorPane getAncMainContainer(){
        return null;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Dashboard load,I'm initializer");
        navigateTo("/View/ThirdPageView.fxml");

    }


    @FXML
    private AnchorPane ancMainContainer;

    @FXML
    void btnCustomerOnAction(ActionEvent event) {
        navigateTo("/View/ThirdPageView.fxml");

    }

    @FXML
    void btnUserOnAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/AdminPasswordPrompt.fxml"));
            Parent root = loader.load();

            AdminPasswordController controller = loader.getController();
            controller.setSecondPageController(this);

            Stage passwordStage = new Stage();
            passwordStage.initModality(Modality.APPLICATION_MODAL);
            passwordStage.setTitle("Admin Authentication");
            passwordStage.setScene(new Scene(root));
            passwordStage.setResizable(false);
            passwordStage.showAndWait();

        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load admin password prompt: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }


    public void loadUserAndEmployeePage() {
        navigateTo("/View/User&Employee.fxml");
    }


    public void btnSupplierOnAction(ActionEvent actionEvent) {

        navigateTo("/View/Farmers&Suppliers.fxml");
    }
    public void btnCustomersOnAction(ActionEvent actionEvent) {
        navigateTo("/View/CustomersView.fxml");
    }
    public void btnMillingProccessOnAction(ActionEvent actionEvent) {
        navigateTo("/View/MillingProcessView.fxml");
    }
    public void btnInventoryOnAction(ActionEvent actionEvent) {
        navigateTo("/View/InventoryView.fxml");
    }
    public void btnOrdersOnAction(ActionEvent actionEvent) {
        navigateTo("/View/SalesOrederView.fxml");
    }
    public void btnExpensesOnAction(ActionEvent actionEvent) {
        navigateTo("/View/PaymentsView.fxml");
    }
    public void btnReportsOnAction(ActionEvent actionEvent) {
        navigateTo("/View/ReportsView.fxml");
    }
    public void btnQualityCheckOnActiion(ActionEvent actionEvent) {
        navigateTo("/View/QualityCheckView.fxml");
    }
    public void btnMaintenanceOnAction(ActionEvent actionEvent) {
        navigateTo("/View/MachineMaintenanceView.fxml");
    }
    public void btnRawPaddyOnAction(ActionEvent actionEvent) {
        navigateTo("/View/RawPaddyView.fxml");
    }
    public void btnFinishedProductOnAction(ActionEvent actionEvent) {
        navigateTo("/View/FinishedProductView.fxml");
    }
    public void btnWasteOnAction(ActionEvent actionEvent) {
        navigateTo("/View/WasteManagementView.fxml");
    }

    @FXML
    void btnOrderOnAction(ActionEvent event) {

    }
    private void navigateTo(String path) {
        try {
            ancMainContainer.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancMainContainer.widthProperty());
            anchorPane.prefHeightProperty().bind(ancMainContainer.heightProperty());

            ancMainContainer.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong while loading the main container", ButtonType.OK).show();
            e.printStackTrace();
        }

    }


    public void btnLogoutOnAction(ActionEvent actionEvent) {
        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText(null);
            alert.setTitle("Logout Confirmation");

            if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {

                Stage stage = (Stage) ancMainContainer.getScene().getWindow();


                Parent root = FXMLLoader.load(getClass().getResource("/View/FirstPageView.fxml"));
                Scene scene = new Scene(root);


                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to logout: " + e.getMessage(), ButtonType.OK).show();
            e.printStackTrace();
        }
    }
}