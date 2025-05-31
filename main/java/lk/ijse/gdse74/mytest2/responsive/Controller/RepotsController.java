package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.Reportsdto;
import lk.ijse.gdse74.mytest2.responsive.model.ReportsModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class RepotsController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Reportsdto, String> colOrderdate;

    @FXML
    private TableColumn<Reportsdto,String> colReport_id;

    @FXML
    private TableColumn<Reportsdto,String> colReport_type;

    @FXML
    private TableView<Reportsdto> table;

    @FXML
    private TextField txtGenerated_date;

    @FXML
    private TextField txtReport_Type;

    @FXML
    private TextField txtReport_id;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtReport_id.getText();
        try {
            boolean isDelete = new ReportsModel().deleteReport(new Reportsdto(id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION,"Deleted successfully").show();
            } else {
                new Alert(Alert.AlertType.ERROR,"Failed to delete report").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Failed to delete report").show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {

        Reportsdto reportsdto = new Reportsdto(txtReport_id.getText(),txtReport_Type.getText(),txtGenerated_date.getText());
        try {
            ReportsModel repModel = new ReportsModel();
            boolean isSave = repModel.saveReport(reportsdto);
            if (isSave) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Report saved successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Report save failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Report save failed").show();
        }

    }

    private void clearFields() {
        loadTable();
        txtReport_id.setText("");
        txtReport_Type.setText("");
        txtGenerated_date.setText("");

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Reportsdto reportsdto = new Reportsdto(txtReport_id.getText(),txtReport_Type.getText(),txtGenerated_date.getText());
        try {
            ReportsModel repModel = new ReportsModel();
            boolean isUpdate = repModel.updateReport(reportsdto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Report update successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Report update failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Report update failed").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable ();
    }

    private void loadTable() {
        colReport_id.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        colReport_type.setCellValueFactory(new PropertyValueFactory<>("reportType"));
        colOrderdate.setCellValueFactory(new PropertyValueFactory<>("reportDate"));
         try {
             ReportsModel reportsModel = new ReportsModel();
             ArrayList<Reportsdto> reportsdtos = ReportsModel.viewAllReports();
             if (reportsdtos != null) {
                 ObservableList<Reportsdto> reportsdtoObservableList = FXCollections.observableArrayList(reportsdtos);
                 table.setItems(reportsdtoObservableList);

             }else {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
             }


         }catch (Exception e){
             e.printStackTrace();
         }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Reportsdto reportsdto = (Reportsdto) table.getSelectionModel().getSelectedItem();
        if(reportsdto != null) {
            txtReport_id.setText(reportsdto.getReportId());
            txtReport_Type.setText(reportsdto.getReportType());
            txtGenerated_date.setText(reportsdto.getReportDate());
        }
    }
}
