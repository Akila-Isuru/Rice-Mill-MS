package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Farmersdto;
import lk.ijse.gdse74.mytest2.responsive.dto.Inventorydto;
import lk.ijse.gdse74.mytest2.responsive.model.CustomerModel;
import lk.ijse.gdse74.mytest2.responsive.model.FarmersModel;
import lk.ijse.gdse74.mytest2.responsive.model.InventoryModel;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.sql.Date;

import static java.util.Date.*;

public class InventoryController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Inventorydto,Integer> colCurrentStockBags;

    @FXML
    private TableColumn<Inventorydto,String> colInventory_id;

    @FXML
    private TableColumn<Inventorydto, Date> colLastupdated;

    @FXML
    private TableColumn<Inventorydto,String> colProduct_id;

    @FXML
    private TableView<Inventorydto> table;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtLatUpdated;

    @FXML
    private TextField txtProductId;

    @FXML
    private ComboBox<String> cmbProductId;

    @FXML
    private TextField txt_CurrentStock;

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtId.getText();
        try {
            InventoryModel inventoryModel = new InventoryModel();
            boolean isDelete = inventoryModel.deleteInventory(new Inventorydto(id));
            if (isDelete) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION,"Deleted successfully").show();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Delete failed");
            }

        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,"Delete failed");


        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws ParseException, SQLException, ClassNotFoundException {

        int CurrentStock = Integer.parseInt(txt_CurrentStock.getText());
        Date lastupdate = Date.valueOf(txtLatUpdated.getText());

        Inventorydto inventorydto = new Inventorydto(txtId.getText(),cmbProductId.getValue(),CurrentStock,lastupdate);
        try {
            InventoryModel inventoryModel = new InventoryModel();
            boolean isSave = inventoryModel.saveInventory(inventorydto);
            if (isSave) {
                clearFields();

                new Alert(Alert.AlertType.INFORMATION, "Inventory Saved").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Inventory Not Saved").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Inventory Not aSaved").show();
        }

    }

    private void clearFields() throws SQLException {

        txtId.setText("");

        txtLatUpdated.setText("");
        txt_CurrentStock.setText("");
        loadNextId();
        Platform.runLater(() -> {
            txtId.setText(txtId.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtId.getText());
        });

        // Refresh table
        loadTable();


    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int CurrentStock = Integer.parseInt(txt_CurrentStock.getText());
        Date lastupdate = Date.valueOf(txtLatUpdated.getText());

        Inventorydto inventorydto = new Inventorydto(txtId.getText(),cmbProductId.getValue(),CurrentStock,lastupdate);
        try {
            InventoryModel inventoryModel = new InventoryModel();
            boolean isUpdate = inventoryModel.updateInventory(inventorydto);
            if (isUpdate) {
                clearFields();

                new Alert(Alert.AlertType.INFORMATION, "Inventory updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Inventory Not updated").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Inventory Not updated").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        try {
            loadNextId();
            txtLatUpdated.setText(LocalDate.now().toString());
            txtLatUpdated.setEditable(false);
            loadProductIds();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadProductIds() throws SQLException {
        ArrayList<String> productIdList = InventoryModel.getAllproductIds();
        ObservableList<String> productIds = FXCollections.observableArrayList(productIdList);
        productIds.addAll(productIdList);
        cmbProductId.setItems(productIds);
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new InventoryModel().getNextId();
            txtId.setText(nextId);
            txtId.setEditable(false);
            System.out.println("DEBUG: Next ID retrieved: " + nextId); // Debug line
            if (nextId == null || nextId.isEmpty()) {
                System.out.println("DEBUG: Got empty or null ID"); // Debug line
            }
            txtId.setText(nextId);
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage()); // Debug line
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
        }
    }

    private void loadTable() {
        colInventory_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProduct_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colCurrentStockBags.setCellValueFactory(new PropertyValueFactory<>("currentStockBags"));
        colLastupdated.setCellValueFactory(new PropertyValueFactory<>("lastUpdated"));

        try {
            Inventorydto inventorymodel = new Inventorydto();
            ArrayList<Inventorydto> inventorydtos = InventoryModel.viewAllInventory();
            if(inventorydtos != null) {
                ObservableList<Inventorydto> inventory = FXCollections.observableArrayList(inventorydtos);
                table.setItems(inventory);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        // Get selected item from table
        Inventorydto selectedItem = table.getSelectionModel().getSelectedItem();

        // Only proceed if something is selected
        if (selectedItem == null) return;

        // Update form fields
        txtId.setText(selectedItem.getId());
        cmbProductId.setValue(selectedItem.getProductId()); // Changed to setValue for ComboBox
        txt_CurrentStock.setText(selectedItem.getCurrentStockBags() + "");
        txtLatUpdated.setText(selectedItem.getLastUpdated() + "");
    }

    public void cmbProductIdOnAction(ActionEvent actionEvent) {
        String selectedItem = (String) cmbProductId.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);

    }
}
