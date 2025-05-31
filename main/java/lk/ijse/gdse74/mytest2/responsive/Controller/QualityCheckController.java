package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.QualityCheckdto;
import lk.ijse.gdse74.mytest2.responsive.model.QualityCheckModel;

import java.net.URL;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

public class QualityCheckController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<QualityCheckdto,String> colBrokent_precentage;

    @FXML
    private TableColumn<QualityCheckdto,String> colCheck_Id;

    @FXML
    private TableColumn<QualityCheckdto,String> colForeignMaterial;

    @FXML
    private TableColumn<QualityCheckdto,String> colMoisture_level;

    @FXML
    private TableColumn<QualityCheckdto,String> colPaddy_id;

    @FXML
    private TableColumn<QualityCheckdto, Date> col_inception_date;

    @FXML
    private TableView<QualityCheckdto> table;

    @FXML
    private TextField txtBroken_precentage;

    @FXML
    private TextField txtCheck_id;

    @FXML
    private TextField txtForeign_Material;

    @FXML
    private TextField txtInception_date;

    @FXML
    private TextField txtMoisture_level;

    @FXML
    private TextField txtPaddy_id;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String check_id = txtCheck_id.getText();
        try {
            boolean isDelete = new QualityCheckModel().deleteQualityCheck(new QualityCheckdto(check_id));
            if (isDelete) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Quality Check Deleted").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Quality Check not Deleted").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Quality Check not Deleted").show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Date inceptionDate = java.sql.Date.valueOf(txtInception_date.getText());

        QualityCheckdto qualityCheckdto = new QualityCheckdto(txtCheck_id.getText(),txtPaddy_id.getText(),txtMoisture_level.getText(),txtForeign_Material.getText(),txtBroken_precentage.getText(),inceptionDate);
        try {
            boolean isSave = new QualityCheckModel().saveQualityCheck(qualityCheckdto);
            if (isSave) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Quality Check Saved").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Quality Check not Saved").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Quality Check not Saved").show();
        }

    }

    private void clearFields() {
        loadTable();
        txtCheck_id.setText("");
        txtPaddy_id.setText("");
        txtMoisture_level.setText("");
        txtForeign_Material.setText("");
        txtBroken_precentage.setText("");
        txtCheck_id.clear();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Date inceptionDate = java.sql.Date.valueOf(txtInception_date.getText());

        QualityCheckdto qualityCheckdto = new QualityCheckdto(txtCheck_id.getText(),txtPaddy_id.getText(),txtMoisture_level.getText(),txtForeign_Material.getText(),txtBroken_precentage.getText(),inceptionDate);
        try {
            boolean isUpdate = new QualityCheckModel().updateQualityCheck(qualityCheckdto);
            if (isUpdate) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Quality Check update").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Quality Check not updated").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Quality Check not updated").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    private void loadTable() {
        colCheck_Id.setCellValueFactory(new PropertyValueFactory<>("checkId"));
        colPaddy_id.setCellValueFactory(new PropertyValueFactory<>("paddyId"));
        colMoisture_level.setCellValueFactory(new PropertyValueFactory<>("moistureLevel"));
        colForeignMaterial.setCellValueFactory(new PropertyValueFactory<>("foreignMaterial"));
        colBrokent_precentage.setCellValueFactory(new PropertyValueFactory<>("brokenPrecentage"));
        col_inception_date.setCellValueFactory(new PropertyValueFactory<>("inceptionDate"));

        try {
            QualityCheckModel qualityCheckModel = new QualityCheckModel();
            ArrayList<QualityCheckdto> qualityCheckdtos = QualityCheckModel.viewAllQualityCheck();
            if(qualityCheckdtos!=null){
                ObservableList<QualityCheckdto> observableList = FXCollections.observableArrayList(qualityCheckdtos);
                table.setItems(observableList);
            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        QualityCheckdto qualityCheckdto = (QualityCheckdto) table.getSelectionModel().getSelectedItem();
        if(qualityCheckdto!=null){
            txtCheck_id.setText(qualityCheckdto.getCheckId());
            txtPaddy_id.setText(qualityCheckdto.getPaddyId());
            txtMoisture_level.setText(qualityCheckdto.getMoistureLevel());
            txtForeign_Material.setText(qualityCheckdto.getForeignMaterial());
            txtBroken_precentage.setText(qualityCheckdto.getBrokenPrecentage());
            txtInception_date.setText(String.valueOf(qualityCheckdto.getInceptionDate()));
        }
    }
}
