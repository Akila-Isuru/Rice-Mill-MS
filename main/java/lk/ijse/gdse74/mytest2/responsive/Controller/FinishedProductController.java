package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.FinishedProductdto;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;
import lk.ijse.gdse74.mytest2.responsive.model.FinishedProductModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FinishedProductController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<FinishedProductdto,String> colMilling_id;

    @FXML
    private TableColumn<FinishedProductdto, Double> colPackaging_size;

    @FXML
    private TableColumn<FinishedProductdto, Integer> colPricePer_bag;

    @FXML
    private TableColumn<FinishedProductdto,String> colProduct_id;

    @FXML
    private TableColumn<FinishedProductdto,String> colProduct_type;

    @FXML
    private TableColumn<FinishedProductdto,Integer> colTotal_quantity;

    @FXML
    private TableView<FinishedProductdto> table;

    @FXML
    private TextField txtMilling_id;

    @FXML
    private TextField txtPackaging_size;

    @FXML
    private TextField txtPricePer_bag;

    @FXML
    private TextField txtProduct_id;

    @FXML
    private TextField txtProduct_type;

    @FXML
    private TextField txtQuantity_bags;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String product_id = txtProduct_id.getText();
        try {
            boolean isaDlete = new FinishedProductModel().deleteFinishedProduct(new FinishedProductdto(product_id));
            if (isaDlete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Product deleted successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Product not deleted").show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        double packaging_size = Double.parseDouble(txtPackaging_size.getText());
        int quantity_bags = Integer.parseInt(txtQuantity_bags.getText());
        int pricePer_bag = Integer.parseInt(txtPricePer_bag.getText());

        FinishedProductdto finishedProductdto = new FinishedProductdto(txtProduct_id.getText(),txtMilling_id.getText(),txtProduct_type.getText(),packaging_size,quantity_bags,pricePer_bag);
          try {
              boolean isSave = new FinishedProductModel().saveFinishedProduct(finishedProductdto);
              if (isSave) {
                  clearFields();
                  new Alert(Alert.AlertType.INFORMATION,"Saved Successfully").show();
              }else {
                  new Alert(Alert.AlertType.ERROR,"Save Failed").show();
                  new Alert(Alert.AlertType.INFORMATION,"Save Failed").show();
              }

          }catch (Exception e){
              e.printStackTrace();
          }
    }

    private void clearFields() {
        loadTable();
        txtMilling_id.clear();
        txtPackaging_size.clear();
        txtPricePer_bag.clear();
        txtProduct_id.clear();
        txtProduct_type.clear();
        txtQuantity_bags.clear();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        double packaging_size = Double.parseDouble(txtPackaging_size.getText());
        int quantity_bags = Integer.parseInt(txtQuantity_bags.getText());
        int pricePer_bag = Integer.parseInt(txtPricePer_bag.getText());

        FinishedProductdto finishedProductdto = new FinishedProductdto(txtProduct_id.getText(),txtMilling_id.getText(),txtProduct_type.getText(),packaging_size,quantity_bags,pricePer_bag);
        try {
            boolean isUpdate = new FinishedProductModel().updateFinishedProduct(finishedProductdto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"update Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"update Failed").show();

            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"update Failed").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colProduct_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colProduct_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colPackaging_size.setCellValueFactory(new PropertyValueFactory<>("packageSize"));
        colTotal_quantity.setCellValueFactory(new PropertyValueFactory<>("quantityBags"));
        colPricePer_bag.setCellValueFactory(new PropertyValueFactory<>("pricePerBag"));

        try {
            FinishedProductModel finishedProductModel = new FinishedProductModel();
            ArrayList<FinishedProductdto> finishedProductDtos = FinishedProductModel.viewAllFinishedProduct();
            if(finishedProductDtos!= null){
                ObservableList<FinishedProductdto> observableList = FXCollections.observableArrayList(finishedProductDtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        FinishedProductdto finishedProductdto = (FinishedProductdto) table.getSelectionModel().getSelectedItem();
        if(finishedProductdto!=null) {
            txtProduct_id.setText(finishedProductdto.getProductId());
            txtMilling_id.setText(finishedProductdto.getMillingId());
            txtProduct_type.setText(finishedProductdto.getProductType());
            txtPackaging_size.setText(String.valueOf(finishedProductdto.getPackageSize()));
            txtQuantity_bags.setText(String.valueOf(finishedProductdto.getQuantityBags()));
            txtPricePer_bag.setText(String.valueOf(finishedProductdto.getPricePerBag()));
        }
    }
}
