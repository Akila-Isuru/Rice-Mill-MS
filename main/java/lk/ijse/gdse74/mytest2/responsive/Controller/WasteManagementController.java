package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.WasteManagementdto;
import lk.ijse.gdse74.mytest2.responsive.model.WasteManagementModel;

import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

public class WasteManagementController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<WasteManagementdto,String> colDisposalMethod;

    @FXML
    private TableColumn<WasteManagementdto,String> colMilling_id;

    @FXML
    private TableColumn<WasteManagementdto,Integer> colQuantity;

    @FXML
    private TableColumn<WasteManagementdto, Date> colRecordedDate;

    @FXML
    private TableColumn<WasteManagementdto,String> colWaste_id;

    @FXML
    private TableColumn<WasteManagementdto,String> colWaste_type;

    @FXML
    private TableView<WasteManagementdto> table;

    @FXML
    private TextField txtDisposal_method;

    @FXML
    private TextField txtMilling_id;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtWaste_id;

    @FXML
    private TextField txtWaste_type;

    @FXML
    private TextField txtrecorded_date;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String waste_id = txtWaste_id.getText();
        try {
            boolean isDelete = new WasteManagementModel().deleteWasteManagement(new WasteManagementdto(waste_id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Waste Deleted Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Waste Deletion Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        int quantity = Integer.parseInt(txtQuantity.getText());
        Date recordDate = Date.valueOf(txtrecorded_date.getText());

        WasteManagementdto wasteManagementdto = new WasteManagementdto(txtWaste_id.getText(),txtMilling_id.getText(),txtWaste_type.getText(),quantity,txtDisposal_method.getText(),recordDate);
        try {
            boolean isSave = new WasteManagementModel().saveWasteManagement(wasteManagementdto);
            if (isSave) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Waste Management has been saved successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Waste Management could not be saved").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Waste Management could not be saved").show();
        }

    }

    private void clearFields() {
        loadTable();
        txtWaste_id.setText("");
        txtMilling_id.setText("");
        txtWaste_type.setText("");
        txtDisposal_method.setText("");
        txtQuantity.setText("");
        txtrecorded_date.setText("");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int quantity = Integer.parseInt(txtQuantity.getText());
        Date recordDate = Date.valueOf(txtrecorded_date.getText());

        WasteManagementdto wasteManagementdto = new WasteManagementdto(txtWaste_id.getText(),txtMilling_id.getText(),txtWaste_type.getText(),quantity,txtDisposal_method.getText(),recordDate);
        try {
            boolean isUpdate = new WasteManagementModel().updateWasteManagement(wasteManagementdto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Waste Management has been updated successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Waste Management could not be updated").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Waste Management could not be updated").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colWaste_id.setCellValueFactory(new PropertyValueFactory<>("wasteId"));
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colWaste_type.setCellValueFactory(new PropertyValueFactory<>("wasteType"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDisposalMethod.setCellValueFactory(new PropertyValueFactory<>("disposalMethod"));
        colRecordedDate.setCellValueFactory(new PropertyValueFactory<>("recordDate"));

        try {
            WasteManagementModel model = new WasteManagementModel();
            ArrayList<WasteManagementdto> wasteManagementdtos = WasteManagementModel.viewAllWasteManagement();
            if(wasteManagementdtos!= null){
                ObservableList<WasteManagementdto> observableList = FXCollections.observableArrayList(wasteManagementdtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        WasteManagementdto wasteManagementdto = table.getSelectionModel().getSelectedItem();
        if(wasteManagementdto!=null){
            txtWaste_id.setText(wasteManagementdto.getWasteId());
            txtMilling_id.setText(wasteManagementdto.getMillingId());
            txtWaste_type.setText(wasteManagementdto.getWasteType());
            txtQuantity.setText(String.valueOf(wasteManagementdto.getQuantity()));
            txtDisposal_method.setText(wasteManagementdto.getDisposalMethod());
            txtrecorded_date.setText(String.valueOf(wasteManagementdto.getRecordDate()));
        }
    }
}
