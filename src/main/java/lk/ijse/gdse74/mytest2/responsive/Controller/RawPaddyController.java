package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.RawPaddydto;
import lk.ijse.gdse74.mytest2.responsive.model.RawPaddyModel;

import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

public class RawPaddyController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<RawPaddydto,String> colFarmer_id;

    @FXML
    private TableColumn<RawPaddydto,Double> colMoisture_level;

    @FXML
    private TableColumn<RawPaddydto,String> colPaddy_id;

    @FXML
    private TableColumn<RawPaddydto,Double> colPrice_per_kg;

    @FXML
    private TableColumn<RawPaddydto, Date> colPurchase_date;

    @FXML
    private TableColumn<RawPaddydto,Double> colQuantity_Kg;

    @FXML
    private TableColumn<RawPaddydto,String> colSupplier_id;

    @FXML
    private TableView<RawPaddydto> table;

    @FXML
    private TextField txtFarmer_id;

    @FXML
    private TextField txtMoisture_level;

    @FXML
    private TextField txtPaddy_id;

    @FXML
    private TextField txtPurchase_date;

    @FXML
    private TextField txtPurchase_price_per_kg;

    @FXML
    private TextField txtQuantity_kg;

    @FXML
    private TextField txtSupplier_id;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String paddy_id = txtPaddy_id.getText();
        try {
            boolean isDelete = new RawPaddyModel().deleteRawPaddy(new RawPaddydto(paddy_id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Deleted Succesfully").show();
            }else {
                new Alert(Alert.AlertType.INFORMATION, "Deletion Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.INFORMATION, "Deletion Failed").show();

        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        double quantity = Double.parseDouble(txtPurchase_price_per_kg.getText());
        double moisture = Double.parseDouble(txtMoisture_level.getText());
        double price = Double.parseDouble(txtPurchase_price_per_kg.getText());
        java.sql.Date purchasedDate = java.sql.Date.valueOf(txtPurchase_date.getText());

        RawPaddydto rawPaddydto = new RawPaddydto(txtPaddy_id.getText(),txtSupplier_id.getText(),txtFarmer_id.getText(),quantity,moisture,price,purchasedDate);
        try {
            boolean isSave = new RawPaddyModel().SaveRawPaddy(rawPaddydto);
            if (isSave) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Save Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Save Failed").show();
        }

    }

    private void clearFields() {
        loadTable();
        txtFarmer_id.setText("");
        txtMoisture_level.setText("");
        txtPaddy_id.setText("");
        txtPurchase_date.setText("");
        txtPurchase_price_per_kg.setText("");
        txtQuantity_kg.setText("");
        txtSupplier_id.setText("");

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        double quantity = Double.parseDouble(txtPurchase_price_per_kg.getText());
        double moisture = Double.parseDouble(txtMoisture_level.getText());
        double price = Double.parseDouble(txtPurchase_price_per_kg.getText());
        java.sql.Date purchasedDate = java.sql.Date.valueOf(txtPurchase_date.getText());

        RawPaddydto rawPaddydto = new RawPaddydto(txtPaddy_id.getText(),txtSupplier_id.getText(),txtFarmer_id.getText(),quantity,moisture,price,purchasedDate);
        try {
            boolean isUpdate = new RawPaddyModel().updateRawPaddy(rawPaddydto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "update Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "update Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "update Failed").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colSupplier_id.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colFarmer_id.setCellValueFactory(new PropertyValueFactory<>("farmerId"));
        colQuantity_Kg.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colMoisture_level.setCellValueFactory(new PropertyValueFactory<>("moisture"));
        colPrice_per_kg.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colPurchase_date.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));

        try {
            RawPaddyModel model = new RawPaddyModel();
            ArrayList<RawPaddydto> rawPaddydtos = RawPaddyModel.viewAllPaddy();
            if(rawPaddydtos!= null){
                ObservableList<RawPaddydto> observableList = FXCollections.observableArrayList(rawPaddydtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        RawPaddydto rawPaddydto = table.getSelectionModel().getSelectedItem();
        if(rawPaddydto!=null){
            txtPaddy_id.setText(rawPaddydto.getPaddyId());
            txtSupplier_id.setText(rawPaddydto.getSupplierId());
            txtFarmer_id.setText(rawPaddydto.getFarmerId());
            txtQuantity_kg.setText(String.valueOf(rawPaddydto.getQuantity()));
            txtMoisture_level.setText(String.valueOf(rawPaddydto.getMoisture()));
            txtPurchase_price_per_kg.setText(String.valueOf(rawPaddydto.getPurchasePrice()));
            txtPurchase_date.setText(String.valueOf(rawPaddydto.getPurchaseDate()));
        }
    }
}
