/*package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class FarmersSuppliersController {

    @FXML
    private AnchorPane ancFarmerSupplier;
    @FXML
    void onFarmer(MouseEvent event) {
        navigateTo("/View/FarmersView.fxml");

    }

    @FXML
    void onSupplier(MouseEvent event) {

        navigateTo("/View/SuppliersView.fxml");
    }
    private void navigateTo(String path) {
        try {
            ancFarmerSupplier.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(ancFarmerSupplier.widthProperty());
            anchorPane.prefHeightProperty().bind(ancFarmerSupplier.heightProperty());

            ancFarmerSupplier.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong while loading the main container", ButtonType.OK).show();
            e.printStackTrace();
        }

    }

}*/
package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

public class FarmersSuppliersController {

    @FXML
    private AnchorPane suppliersFarmersAnc;

    @FXML
    void btnFarmerManagementOnAction(ActionEvent event) {
        navigateTo("/View/FarmersView.fxml");

    }

    @FXML
    void btnSupplierManagementOnAction(ActionEvent event) {
        navigateTo("/View/SuppliersView.fxml");

    }

    private void navigateTo(String path) {
        try {
            suppliersFarmersAnc.getChildren().clear();
            AnchorPane anchorPane = FXMLLoader.load(getClass().getResource(path));
            anchorPane.prefWidthProperty().bind(suppliersFarmersAnc.widthProperty());
            anchorPane.prefHeightProperty().bind(suppliersFarmersAnc.heightProperty());

            suppliersFarmersAnc.getChildren().add(anchorPane);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong while loading the main container", ButtonType.OK).show();
            e.printStackTrace();
        }

    }
}

