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
import lk.ijse.gdse74.mytest2.responsive.dto.Paymentsdto;
import lk.ijse.gdse74.mytest2.responsive.model.CustomerModel;
import lk.ijse.gdse74.mytest2.responsive.model.PaymentsModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Paymentsdto,Integer> colAmount_paid;

    @FXML
    private TableColumn<Paymentsdto,String> colOrder_id;

    @FXML
    private TableColumn<Paymentsdto, Date> colPaymentDate;

    @FXML
    private TableColumn<Paymentsdto,String> colPayment_Method;

    @FXML
    private TableColumn<Paymentsdto, String> colPayment_id;

    @FXML
    private TableView<Paymentsdto> table;

    @FXML
    private TextField txtAmount_paid;

    @FXML
    private TextField txtOrder_id;

    @FXML
    private TextField txtPayement_Id;

    @FXML
    private TextField txtPayemnt_date;

    @FXML
    private TextField txtPaymentMethod;

    @FXML
    private ComboBox<String> cmbPaymentId;

    @FXML
    void btnClearOnAction(ActionEvent event) throws SQLException {
        clearFields();

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtPayement_Id.getText();
        try {
            boolean isDelete = new PaymentsModel().deletePayment(new Paymentsdto(id));
            if (isDelete) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION, "Payment deleted successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Payment could not be deleted").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Payment could not be deleted").show();
        }

    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        Date paymentDate = Date.valueOf(txtPayemnt_date.getText());
        int amount = Integer.parseInt(txtAmount_paid.getText());

        Paymentsdto paymentsdto = new Paymentsdto(txtPayement_Id.getText(),txtOrder_id.getText(),paymentDate,txtPaymentMethod.getText(),amount);
        try {
            PaymentsModel paymentsModel = new PaymentsModel();
            boolean isSave = paymentsModel.savePayment(paymentsdto);
            if (isSave) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION,"Payment saved Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Payment save Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Payment save Failed").show();
        }

    }

    private void clearFields() throws SQLException {

        txtAmount_paid.setText("");
        txtOrder_id.setText("");
        txtPayement_Id.setText("");
        txtPayemnt_date.setText("");
        txtPaymentMethod.setText("");
        txtAmount_paid.setText("");
        loadNextId();
        Platform.runLater(() -> {
            txtPayement_Id.setText(txtPayement_Id.getText()); // Forces refresh
            System.out.println("UI refreshed with ID: " + txtPayement_Id.getText());
        });


        loadTable();

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Date paymentDate = Date.valueOf(txtPayemnt_date.getText());
        int amount = Integer.parseInt(txtAmount_paid.getText());

        Paymentsdto paymentsdto = new Paymentsdto(txtPayement_Id.getText(),txtOrder_id.getText(),paymentDate,txtPaymentMethod.getText(),amount);
        try {
            PaymentsModel paymentsModel = new PaymentsModel();
            boolean isUpdated = paymentsModel.updatePayment(paymentsdto);
            if (isUpdated) {
                clearFields();
                loadTable();
                new Alert(Alert.AlertType.INFORMATION,"Payment updated Successfully").show();
            }else {
                new Alert(Alert.AlertType.ERROR,"Payment update Failed").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Payment update Failed").show();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadNextId();
            loadPaymentIds();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        loadTable();
    }

    private void loadPaymentIds() throws SQLException {
        ArrayList<String> paymentIdList = PaymentsModel.getAlCustomerIds();
        ObservableList<String> paymentId = FXCollections.observableArrayList(paymentIdList);
        paymentId.addAll(paymentIdList);
        cmbPaymentId.setItems(paymentId);
    }

    private void loadNextId() throws SQLException {
        try {
            String nextId = new PaymentsModel().getNextId();
            txtPayement_Id.setText(nextId);
            txtPayement_Id.setEditable(false);
            System.out.println("DEBUG: Next ID retrieved: " + nextId); // Debug line
            if (nextId == null || nextId.isEmpty()) {
                System.out.println("DEBUG: Got empty or null ID"); // Debug line
            }
            txtPayement_Id.setText(nextId);
        } catch (SQLException e) {
            System.err.println("ERROR in loadNextId: " + e.getMessage()); // Debug line
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading next ID").show();
        }
    }

    private void loadTable() {
        colPayment_id.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        colOrder_id.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colPaymentDate.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        colPayment_Method.setCellValueFactory(new PropertyValueFactory<>("paymentMethods"));
        colAmount_paid.setCellValueFactory(new PropertyValueFactory<>("amountPaid"));

        try {
            PaymentsModel paymentsModel = new PaymentsModel();
            ArrayList<Paymentsdto> paymentsdtos = PaymentsModel.viewPayments();

            if(paymentsdtos!= null){
                ObservableList<Paymentsdto> ObservableList = FXCollections.observableArrayList(paymentsdtos);
                table.setItems(ObservableList);


            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
            }

    }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {
        Paymentsdto paymentsdto = (Paymentsdto) table.getSelectionModel().getSelectedItem();
        if(paymentsdto!=null){
            txtPayement_Id.setText(paymentsdto.getPaymentId());
            txtOrder_id.setText(paymentsdto.getOrderId());
            txtPayemnt_date.setText(String.valueOf(paymentsdto.getPaymentDate()));
            txtPaymentMethod.setText(paymentsdto.getPaymentMethods());
            txtAmount_paid.setText(String.valueOf(paymentsdto.getAmountPaid()));

        }
    }

    public void cmbPaymentIdOnAction(ActionEvent actionEvent) {
        String selectedPaymentId = (String) cmbPaymentId.getSelectionModel().getSelectedItem();
        System.out.println(selectedPaymentId);

    }
}
