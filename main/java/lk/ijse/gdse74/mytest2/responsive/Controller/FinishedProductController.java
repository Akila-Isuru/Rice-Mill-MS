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
import lk.ijse.gdse74.mytest2.responsive.model.FinishedProductModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class FinishedProductController implements Initializable {

    @FXML private Button btnClear;
    @FXML private Button btnDelete;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private TableColumn<FinishedProductdto,String> colMilling_id;
    @FXML private TableColumn<FinishedProductdto, Double> colPackaging_size;
    @FXML private TableColumn<FinishedProductdto, Integer> colPricePer_bag;
    @FXML private TableColumn<FinishedProductdto,String> colProduct_id;
    @FXML private TableColumn<FinishedProductdto,String> colProduct_type;
    @FXML private TableColumn<FinishedProductdto,Integer> colTotal_quantity;
    @FXML private TableView<FinishedProductdto> table;
    @FXML private TextField txtMilling_id;
    @FXML private TextField txtPackaging_size;
    @FXML private TextField txtPricePer_bag;
    @FXML private TextField txtProduct_id;
    @FXML private TextField txtProduct_type;
    @FXML private TextField txtQuantity_bags;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactories();
        loadTable();
        loadNextId();
        setupFieldListeners();
    }

    private void setCellValueFactories() {
        colProduct_id.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colMilling_id.setCellValueFactory(new PropertyValueFactory<>("millingId"));
        colProduct_type.setCellValueFactory(new PropertyValueFactory<>("productType"));
        colPackaging_size.setCellValueFactory(new PropertyValueFactory<>("packageSize"));
        colTotal_quantity.setCellValueFactory(new PropertyValueFactory<>("quantityBags"));
        colPricePer_bag.setCellValueFactory(new PropertyValueFactory<>("pricePerBag"));
    }

    private void loadTable() {
        try {
            ArrayList<FinishedProductdto> allProducts = FinishedProductModel.viewAllFinishedProduct();
            table.setItems(FXCollections.observableArrayList(allProducts));
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to load data").show();
        }
    }

    private void loadNextId() {
        try {
            String nextId = new FinishedProductModel().getNextId();
            txtProduct_id.setText(nextId);
            txtProduct_id.setDisable(true);
            btnSave.setDisable(false);
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to generate next ID").show();
        }
    }

    private void setupFieldListeners() {
        txtMilling_id.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPackaging_size.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtPricePer_bag.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtProduct_type.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
        txtQuantity_bags.textProperty().addListener((observable, oldValue, newValue) -> updateSaveButtonState());
    }

    private void updateSaveButtonState() {
        boolean allFieldsFilled = !txtMilling_id.getText().isEmpty() &&
                !txtPackaging_size.getText().isEmpty() &&
                !txtPricePer_bag.getText().isEmpty() &&
                !txtProduct_type.getText().isEmpty() &&
                !txtQuantity_bags.getText().isEmpty();

        btnSave.setDisable(!allFieldsFilled);
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Delete Product Record");
        alert.setContentText("Are you sure you want to delete this product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                boolean isDeleted = new FinishedProductModel().deleteFinishedProduct(
                        new FinishedProductdto(txtProduct_id.getText())
                );

                if (isDeleted) {
                    new Alert(Alert.AlertType.INFORMATION, "Deleted Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Delete Failed!").show();
                }
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        try {
            if (!validateFields()) {
                return;
            }

            double packagingSize = Double.parseDouble(txtPackaging_size.getText());
            int quantityBags = Integer.parseInt(txtQuantity_bags.getText());
            int pricePerBag = Integer.parseInt(txtPricePer_bag.getText());

            FinishedProductdto dto = new FinishedProductdto(
                    txtProduct_id.getText(),
                    txtMilling_id.getText(),
                    txtProduct_type.getText(),
                    packagingSize,
                    quantityBags,
                    pricePerBag
            );

            boolean isSaved = new FinishedProductModel().saveFinishedProduct(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "Saved Successfully!").show();
                clearFields();
                loadTable();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!").show();
            }
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid numeric value").show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Update Product Record");
        alert.setContentText("Are you sure you want to update this product?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                if (!validateFields()) {
                    return;
                }

                double packagingSize = Double.parseDouble(txtPackaging_size.getText());
                int quantityBags = Integer.parseInt(txtQuantity_bags.getText());
                int pricePerBag = Integer.parseInt(txtPricePer_bag.getText());

                FinishedProductdto dto = new FinishedProductdto(
                        txtProduct_id.getText(),
                        txtMilling_id.getText(),
                        txtProduct_type.getText(),
                        packagingSize,
                        quantityBags,
                        pricePerBag
                );

                boolean isUpdated = new FinishedProductModel().updateFinishedProduct(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Updated Successfully!").show();
                    clearFields();
                    loadTable();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!").show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR, "Invalid numeric value").show();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong!").show();
                e.printStackTrace();
            }
        }
    }

    @FXML
    void tableColumnOnClicked(MouseEvent mouseEvent) {
        FinishedProductdto selectedItem = table.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            txtProduct_id.setText(selectedItem.getProductId());
            txtMilling_id.setText(selectedItem.getMillingId());
            txtProduct_type.setText(selectedItem.getProductType());
            txtPackaging_size.setText(String.valueOf(selectedItem.getPackageSize()));
            txtQuantity_bags.setText(String.valueOf(selectedItem.getQuantityBags()));
            txtPricePer_bag.setText(String.valueOf(selectedItem.getPricePerBag()));

            btnSave.setDisable(true);
        }
    }

    private boolean validateFields() {
        if (txtMilling_id.getText().isEmpty() ||
                txtPackaging_size.getText().isEmpty() ||
                txtPricePer_bag.getText().isEmpty() ||
                txtProduct_type.getText().isEmpty() ||
                txtQuantity_bags.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all fields").show();
            return false;
        }

        try {
            Double.parseDouble(txtPackaging_size.getText());
            Integer.parseInt(txtQuantity_bags.getText());
            Integer.parseInt(txtPricePer_bag.getText());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, "Numeric fields must contain valid numbers").show();
            return false;
        }

        return true;
    }

    private void clearFields() {
        txtMilling_id.clear();
        txtPackaging_size.clear();
        txtPricePer_bag.clear();
        txtProduct_type.clear();
        txtQuantity_bags.clear();

        loadNextId();
        loadTable();
    }
}