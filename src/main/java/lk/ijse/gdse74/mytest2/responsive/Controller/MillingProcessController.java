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
import lk.ijse.gdse74.mytest2.responsive.dto.MillingProcessdto;
import lk.ijse.gdse74.mytest2.responsive.model.MillingProcessModel;

import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MillingProcessController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<MillingProcessdto,Double> colBran_rice;

    @FXML
    private TableColumn<MillingProcessdto,Double> colBroken_rice;

    @FXML
    private TableColumn<MillingProcessdto, Time> colEnd_time;

    @FXML
    private TableColumn<MillingProcessdto,Double> colHusk;

    @FXML
    private TableColumn<MillingProcessdto,String> colMilling_id;

    @FXML
    private TableColumn<MillingProcessdto,String> colPaddy_id;

    @FXML
    private TableColumn<MillingProcessdto,Time> colStart_time;

    @FXML
    private TableColumn<MillingProcessdto,Double> colmilled_Quantity;

    @FXML
    private TableView<MillingProcessdto> table;

    @FXML
    private TextField txtBran;

    @FXML
    private TextField txtBrokenRice;

    @FXML
    private TextField txtEnd_time;

    @FXML
    private TextField txtHusk;

    @FXML
    private TextField txtMilledQuantity;

    @FXML
    private TextField txtMilling_id;

    @FXML
    private TextField txtPaddyid;

    @FXML
    private TextField txtStart_time;

    private final String timePattern = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$";

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtMilling_id.getText();
        try {
            boolean isDelete = MillingProcessModel.deleteMillingProcess(new MillingProcessdto(id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Deleted").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Not Deleted").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR);
        }

    }

    @FXML
   public  void btnSaveOnAction(ActionEvent event) {

        String millingId = txtMilling_id.getText();
        String paddy = txtPaddyid.getText();
        Time starTime = Time.valueOf(txtStart_time.getText());
        Time endTime = Time.valueOf(txtEnd_time.getText());
        double milledQuantity = Double.parseDouble(txtMilledQuantity.getText());
        double broken = Double.parseDouble(txtBrokenRice.getText());
        double husk = Double.parseDouble(txtHusk.getText());
        double bran = Double.parseDouble(txtBran.getText());

        MillingProcessdto millingProcessdto = new MillingProcessdto(millingId,paddy,starTime,endTime,milledQuantity,broken,husk,bran);
        try {
            MillingProcessModel millingProcessModel = new MillingProcessModel();
            boolean isSave = MillingProcessModel.saveMillingProcess(millingProcessdto);
            if(isSave){
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Milling Process Saved").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Milling Process Not Saved").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void clearFields() throws SQLException {

        txtMilling_id.clear();
        txtPaddyid.clear();
        txtStart_time.clear();
        txtEnd_time.clear();
        txtMilledQuantity.clear();
        txtBrokenRice.clear();
        txtHusk.clear();
        txtBran.clear();
        loadNextId();
        Platform.runLater(() -> {
            txtMilling_id.setText(txtMilling_id.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtMilling_id.getText());
        });
    }

    @FXML
    public void btnUpdateOnAction(ActionEvent event) {
        String millingId = txtMilling_id.getText();
        String paddy = txtPaddyid.getText();
        Time starTime = Time.valueOf(txtStart_time.getText());
        Time endTime = Time.valueOf(txtEnd_time.getText());
        double milledQuantity = Double.parseDouble(txtMilledQuantity.getText());
        double broken = Double.parseDouble(txtBrokenRice.getText());
        double husk = Double.parseDouble(txtHusk.getText());
        double bran = Double.parseDouble(txtBran.getText());

        MillingProcessdto millingProcessdto = new MillingProcessdto(millingId,paddy,starTime,endTime,milledQuantity,broken,husk,bran);
        try {
            MillingProcessModel millingProcessModel = new MillingProcessModel();
            boolean isisUpdate = MillingProcessModel.updateMillingProcess(millingProcessdto);
            if(isisUpdate){
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Milling Process updated").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Milling Process Not updated").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
    }

    private void loadNextId() throws SQLException {
        String id = new MillingProcessModel().getNextId();
        txtMilling_id.setText(id);
    }

    private void loadTable() {
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colStart_time.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colEnd_time.setCellValueFactory(new PropertyValueFactory<>("endTime"));
        colmilled_Quantity.setCellValueFactory(new PropertyValueFactory<>("milledQuantity"));
        colBroken_rice.setCellValueFactory(new PropertyValueFactory<>("brokenRice"));
        colHusk.setCellValueFactory(new PropertyValueFactory<>("husk"));
        colBran_rice.setCellValueFactory(new PropertyValueFactory<>("bran"));

        try {
            MillingProcessModel millingProcessModel = new MillingProcessModel();
            ArrayList<MillingProcessdto> millingProcessdtos = MillingProcessModel.viewAllMillingProcess();
            if(millingProcessdtos != null) {
                ObservableList<MillingProcessdto> observableList = FXCollections.observableList(millingProcessdtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        MillingProcessdto millingProcessdto = (MillingProcessdto) table.getSelectionModel().getSelectedItem();
        if (millingProcessdto != null) {
            txtMilling_id.setText(millingProcessdto.getMillingId());
            txtPaddyid.setText(millingProcessdto.getPaddyId());
            txtStart_time.setText(String.valueOf(millingProcessdto.getStartTime()));
            txtEnd_time.setText(String.valueOf(millingProcessdto.getEndTime()));
            txtMilledQuantity.setText(String.valueOf(millingProcessdto.getMilledQuantity()));
            txtBrokenRice.setText(String.valueOf(millingProcessdto.getBrokenRice()));
            txtHusk.setText(String.valueOf(millingProcessdto.getHusk()));
            txtBran.setText(String.valueOf(millingProcessdto.getBran()));

        }
    }
}
