package lk.ijse.gdse74.mytest2.responsive.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.gdse74.mytest2.responsive.dto.*;
import lk.ijse.gdse74.mytest2.responsive.dto.tm.cartTM;
import lk.ijse.gdse74.mytest2.responsive.model.*;
import lk.ijse.gdse74.mytest2.responsive.utill.CrudUtill;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.sql.Date;
import java.util.ResourceBundle;

public class SalesOrderController implements Initializable {

    int lastQty = -1;
    ObservableList<SalesOrderDetailsdto> observableList = FXCollections.observableArrayList();

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnAddToCart;
    @FXML private Label lblCustomerName;
    @FXML private Label lblItemName;
    @FXML private Label lblItemQty;
    @FXML private Label lblOrder_Date;
    @FXML private Label lblUnitPrice;
    @FXML private TextField txtAddCartQuantity;
    @FXML private TextField txtOrdera;
    @FXML private TableColumn<?, ?> colAction;
    @FXML private TableColumn<cartTM, String> colProductID;
    @FXML private TableColumn<cartTM, String> colProductName;
    @FXML private TableColumn<cartTM, Integer> colQty;
    @FXML private TableColumn<cartTM, Integer> colTotalAmount;
    @FXML private TableColumn<cartTM, Integer> colUnitPrice;
    @FXML private TableView<cartTM> table;
    @FXML private ComboBox<String> cmbCustomerId;
    @FXML private ComboBox<String> cmbItemId;

    private final ObservableList<cartTM> cartData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotalAmount.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));
        table.setItems(cartData);

        try {
            txtOrdera.setText(new SalesOrderModel().getNextId());
            lblOrder_Date.setText(LocalDate.now().toString());
            txtOrdera.setEditable(false);
            loadItemIds();
            loadCustomerIds();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Initialization error: " + e.getMessage()).show();
        }
    }

    @FXML
    void btnAddToCartOnAction(ActionEvent actionEvent) {
        try {
            String selectedItemId = cmbItemId.getValue();
            String cartQtyString = txtAddCartQuantity.getText();

            if (selectedItemId == null) {
                new Alert(Alert.AlertType.WARNING, "Please select an item").show();
                return;
            }

            if (!cartQtyString.matches("^[0-9]+$") || cartQtyString.isEmpty()) {
                new Alert(Alert.AlertType.WARNING, "Please enter valid quantity").show();
                return;
            }

            int cartQty = Integer.parseInt(cartQtyString);
            int currentAvailableQty = Integer.parseInt(lblItemQty.getText());
            String itemName = lblItemName.getText();
            int unitPrice = Integer.parseInt(lblUnitPrice.getText());
            int total = unitPrice * cartQty;

            if (cartQty <= 0) {
                new Alert(Alert.AlertType.WARNING, "Quantity must be positive").show();
                return;
            }


            for (cartTM existingItem : cartData) {
                if (existingItem.getProductId().equals(selectedItemId)) {
                    int newQty = existingItem.getQty() + cartQty;
                    int originalStock = currentAvailableQty + existingItem.getQty();

                    if (newQty > originalStock) {
                        new Alert(Alert.AlertType.WARNING,
                                "Cannot add more than available stock. Available: " +
                                        (originalStock - existingItem.getQty())).show();
                        return;
                    }

                    existingItem.setQty(newQty);
                    existingItem.setTotal(newQty * unitPrice);
                    lblItemQty.setText(String.valueOf(originalStock - newQty));
                    txtAddCartQuantity.clear();
                    table.refresh();
                    return;
                }
            }

            // For new items
            if (cartQty > currentAvailableQty) {
                new Alert(Alert.AlertType.WARNING,
                        "Not enough quantity! Available: " + currentAvailableQty).show();
                return;
            }

            Button removeBtn = new Button("Remove");
            cartTM newItem = new cartTM(
                    selectedItemId,
                    itemName,
                    unitPrice,
                    cartQty,
                    total,
                    removeBtn
            );

            removeBtn.setOnAction(event -> {
                int currentStock = Integer.parseInt(lblItemQty.getText());
                lblItemQty.setText(String.valueOf(currentStock + newItem.getQty()));
                cartData.remove(newItem);
                table.refresh();
            });

            lblItemQty.setText(String.valueOf(currentAvailableQty - cartQty));
            cartData.add(newItem);
            txtAddCartQuantity.clear();
            table.refresh();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    // Rest of your existing methods remain exactly the same
    @FXML void btnClearOnAction(ActionEvent event) { clearFields(); }

    @FXML void btnDeleteOnAction(ActionEvent event) {
        String id = txtOrdera.getText();
        try {
            boolean isDelete = new SalesOrderModel().DeleteSalesOrder(new SalesOrderdto(id));
            new Alert(Alert.AlertType.INFORMATION,
                    isDelete ? "Deleted successfully" : "Failed to delete").show();
            if (isDelete) clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to delete sales order").show();
        }
    }

    @FXML void btnSaveOnAction(ActionEvent event) {
        // Your existing save implementation
    }

    private void clearFields() {
        cmbItemId.getSelectionModel().clearSelection();
        lblUnitPrice.setText("");
        lblItemName.setText("");
        txtAddCartQuantity.setText("");
    }

   /* @FXML void btnUpdateOnAction(ActionEvent event) {
        Date orderDate = Date.valueOf(LocalDate.now());
        int totalAmount = Integer.parseInt(lblUnitPrice.getText()) * Integer.parseInt(lblItemQty.getText());
        try {
            boolean isUpdate = new SalesOrderModel().UpdateSalesOrder(
                    new SalesOrderdto(txtOrdera.getText(), cmbCustomerId.getValue(),
                            orderDate, totalAmount, "Paid"));
            new Alert(Alert.AlertType.INFORMATION,
                    isUpdate ? "Order updated" : "Order not updated").show();
            if (isUpdate) clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Order not updated").show();
        }
    }*/

    private void loadItemIds() throws SQLException {
        cmbItemId.setItems(FXCollections.observableArrayList(FinishedProductModel.getAllItemIds()));
    }

    private void loadCustomerIds() throws ClassNotFoundException, SQLException {
        cmbCustomerId.setItems(FXCollections.observableArrayList(CustomerModel.getAllCustomerIds()));
    }

    public void cmbCustomerOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String selectedCustomerId = cmbCustomerId.getValue();
        if (selectedCustomerId != null) {
            lblCustomerName.setText(CustomerModel.findNameById(selectedCustomerId));
        }
    }

    public void cmbItemIdOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String selectedItemId = cmbItemId.getValue();
        if (selectedItemId != null) {
            FinishedProductdto product = FinishedProductModel.findById(selectedItemId);
            if (product != null) {
                lblItemName.setText(product.getProductType());
                lblItemQty.setText(String.valueOf(product.getQuantityBags()));
                lblUnitPrice.setText(String.valueOf(product.getPricePerBag()));
            }
        }
    }

    public void tableColumnOnClicked(MouseEvent mouseEvent) {}

    
    public void btnOnActionPlaceOrder(ActionEvent actionEvent) {
        if (table.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please add items to cart").show();
            return;
        }
        if (cmbCustomerId.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "Please select customer").show();
            return;
        }
        String selectedCustomerId = cmbCustomerId.getValue();
        String orderId = txtOrdera.getText();
        Date date  = Date.valueOf(lblOrder_Date.getText());
        String productId = cmbItemId.getValue();

        ArrayList<SalesOrderDetailsdto> cartList = new ArrayList<>();
         int orderTotal = 0;

        for(cartTM cartTM : cartData) {

            orderTotal += cartTM.getTotal();
            SalesOrderDetailsdto salesOrderDetailsdto = new SalesOrderDetailsdto(
                    orderId,
                    productId,
                    cartTM.getQty(),
                    cartTM.getUnitPrice(),
                    cartTM.getTotal()
            );
            cartList.add(salesOrderDetailsdto);
        }
        SalesOrderdto salesOrderdto = new SalesOrderdto(
                orderId,
                selectedCustomerId,
                date,
               orderTotal,
                cartList

        );
        try {
            boolean isPlaced = SalesOrderModel.placeOrder(salesOrderdto);
            if (isPlaced) {
                clearFields();
                new Alert(Alert.AlertType.INFORMATION, "Order placed successfully!").show();
                table.getItems().clear();
            }else {
                new Alert(Alert.AlertType.ERROR, "Sorry, could not place order").show();
            }

        }catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Sorry, could not place the order").show();
        }

    }
}

