package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.MachineMaintenancedto;
import lk.ijse.gdse74.mytest2.responsive.model.MachineMaintenanceModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MachineMaintenanceController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<MachineMaintenancedto,String> colMain_id;

    @FXML
    private TableColumn<MachineMaintenancedto,Integer> col_cost;

    @FXML
    private TableColumn<MachineMaintenancedto,String> coldescription;

    @FXML
    private TableColumn<MachineMaintenancedto,String> colmachine_name;

    @FXML
    private TableColumn<MachineMaintenancedto,String> colmain_date;

    @FXML
    private TableView<MachineMaintenancedto> table;

    @FXML
    private TextField txtcost;

    @FXML
    private TextField txtdescription;

    @FXML
    private TextField txtmachine_name;

    @FXML
    private TextField txtmain_date;

    @FXML
    private TextField txtmain_id;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException ,ClassNotFoundException {
        String id = txtmain_id.getText();
        try {
            boolean isDelete = new MachineMaintenanceModel().deleteMachineMaintenance(new MachineMaintenancedto(id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Delete Succesfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed").show();
            }
        } catch (SQLException e) {
        e.printStackTrace();
        new Alert(Alert.AlertType.ERROR,"Delete Failed").show();

        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        int cost = Integer.parseInt(txtcost.getText());
        MachineMaintenancedto machineMaintenancedto = new MachineMaintenancedto(txtmain_id.getText(),txtmachine_name.getText(),txtmain_date.getText(),txtdescription.getText(),cost);
     try {
         boolean isSave = new MachineMaintenanceModel().saveMachineMaintenance(machineMaintenancedto);
         if (isSave) {
             clearFields();
             new Alert(Alert.AlertType.INFORMATION,"Machine Maintenance Saved").show();
         }else {
             Alert alert = new Alert(Alert.AlertType.ERROR,"Machine Maintenance Save Failed");
         }
     }catch (Exception e){
         e.printStackTrace();
         Alert alert = new Alert(Alert.AlertType.ERROR,"Machine Maintenance Save Failed");
     }

    }

    private void clearFields() {
        loadTable();
        txtcost.setText("");
        txtdescription.setText("");
        txtmachine_name.setText("");
        txtmain_date.setText("");
        txtmain_id.setText("");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        int cost = Integer.parseInt(txtcost.getText());
        MachineMaintenancedto machineMaintenancedto = new MachineMaintenancedto(txtmain_id.getText(),txtmachine_name.getText(),txtmain_date.getText(),txtdescription.getText(),cost);
        try {
            boolean isUpdate = new MachineMaintenanceModel().updateMachineMaintenance(machineMaintenancedto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Machine Maintenance updated").show();
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR,"Machine Maintenance update Failed");
            }
        }catch (Exception e){
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR,"Machine Maintenance update Failed");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colMain_id.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colmachine_name.setCellValueFactory(new PropertyValueFactory<>("machineName"));
        colmain_date.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        coldescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        col_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        try {
            MachineMaintenanceModel machineMaintenanceModel = new MachineMaintenanceModel();
            ArrayList<MachineMaintenancedto> machineMaintenancedtos = machineMaintenanceModel.viewAllMachineMaintenance();
            if(machineMaintenancedtos!= null){
                ObservableList<MachineMaintenancedto> observableList = FXCollections.observableList(machineMaintenancedtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        MachineMaintenancedto machineMaintenancedto = table.getSelectionModel().getSelectedItem();
        if (machineMaintenancedto != null) {
            txtmain_id.setText(machineMaintenancedto.getMaintenanceId());
            txtmachine_name.setText(machineMaintenancedto.getMachineName());
            txtmain_date.setText(machineMaintenancedto.getMaintenanceDate());
            txtdescription.setText(machineMaintenancedto.getDescription());
            txtcost.setText(String.valueOf(machineMaintenancedto.getCost()));
        }
    }
}
