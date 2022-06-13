package lk.ijse.pos.controller;

import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.DTO.OrderDetailDTO;
import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.PlaceOrderBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import lk.ijse.pos.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lk.ijse.pos.DTO.CustomerDTO;
import lk.ijse.pos.DTO.ItemDTO;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import lk.ijse.pos.util.NotificationUtil;
import lk.ijse.pos.util.ValidationUtil;
import lk.ijse.pos.view.tm.CartTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PlaceOrderFormController {
    private final PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.PLACE_ORDER);
    private final ObservableList<CartTM> obList = FXCollections.observableArrayList();
    private final LinkedHashMap<JFXTextField, Pattern> RegexMap = new LinkedHashMap<>();
    public JFXComboBox<String> cmbCustomerID;
    public JFXTextField txtAddress;
    public JFXTextField txtTitle;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextArea txtDescription;
    public ImageView imgItemImage;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPackSize;
    public JFXButton btnAddToCart;
    public JFXButton btnRemoveFromCart;
    public TableView<CartTM> tableOrderItems;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colCost;
    public TableColumn colDiscount;
    public TableColumn colOption;
    public JFXTextField txtDiscount;
    public JFXButton btnPlaceOrder;
    public JFXButton btnCancel;
    public Label lblTotal;
    public JFXButton btnAddCustomer;
    public AnchorPane PlacedOrderPane;
    public JFXTextField txtCustName;
    public JFXTextField txtQty;
    public JFXButton btnViewReceipt;
    public JFXButton btnCLose;
    private String OrderID;

    public void validateFields(KeyEvent keyEvent) {
        Object validate = ValidationUtil.Validate(RegexMap, btnAddToCart);
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (validate instanceof JFXTextField) {
                ((JFXTextField) validate).requestFocus();
            } else {
                btnAddToCart.fire();
            }
        }
    }

    public CartTM itemExistsInCart(String code) {
        if (!obList.isEmpty()) {
            for (CartTM tm : obList) {
                if (tm.getItemCode().equals(code)) return tm;
            }
        }
        return null;
    }

    private void clearAddTOCartFields() {
        cmbItemCode.getSelectionModel().clearSelection();
        txtPackSize.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtDescription.clear();
        txtQty.clear();
        txtDiscount.clear();
        setImageProperties(new Image("lk/ijse/pos/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
        resetFields(txtQty, txtDiscount);
        btnAddToCart.setText("Add To Cart");
    }

    public void resetFields(JFXTextField... fields) {
        for (JFXTextField field : fields) {
            field.getParent().setStyle("-fx-border-color :   #EDEDF0;" + "-fx-border-width:1.5;" + "-fx-border-radius:  5;" + "-fx-background-radius:  5;");
        }
    }

    public void addToCartOnAction(ActionEvent actionEvent) {
        try {
            CartTM tm = itemExistsInCart(cmbItemCode.getValue());
            if (tm == null) {
                ItemDTO item = placeOrderBO.getItem(cmbItemCode.getValue());
                int qty = Integer.parseInt(txtQty.getText());
                double cost = (qty * item.getUnitPrice());
                double discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());
                if (qty > item.getQtyOnHand()) {
                    new Alert(Alert.AlertType.WARNING, "Insufficient Qty !..", ButtonType.OK).show();
                } else if (discount > cost) {
                    new Alert(Alert.AlertType.WARNING, "Discount Exceeds Cost !..", ButtonType.OK).show();
                } else {
                    CartTM cartTM = new CartTM(item.getItemCode(), item.getDescription(), item.getUnitPrice(), item.getQtyOnHand(), qty, cost, discount, null);
                    cartTM.setBtn(getButton(cartTM));
                    obList.add(cartTM);
                    clearAddTOCartFields();
                    btnAddToCart.setDisable(true);
                    btnRemoveFromCart.setDisable(true);
                    btnPlaceOrder.setDisable(false);
                    tableOrderItems.setItems(obList);
                }
            } else {
                int qty = Integer.parseInt(txtQty.getText()) + tm.getQty();
                double cost = (qty * tm.getUnitPrice());
                double discount = txtDiscount.getText().isEmpty() ? tm.getDiscount() : Double.parseDouble(txtDiscount.getText());
                System.out.println(qty);
                if (btnAddToCart.getText().equalsIgnoreCase("Update Item")) {
                    qty = Integer.parseInt(txtQty.getText());
                    discount = txtDiscount.getText().isEmpty() ? 0 : Double.parseDouble(txtDiscount.getText());
                }

                if (qty > tm.getQtyOnHand()) {
                    new Alert(Alert.AlertType.WARNING, "Insufficient Qty !..", ButtonType.OK).show();
                } else if (discount > cost) {
                    new Alert(Alert.AlertType.WARNING, "Discount Exceeds Cost !..", ButtonType.OK).show();
                } else {
                    tm.setQty(qty);
                    tm.setCost(cost);
                    tm.setDiscount(discount);
                    tableOrderItems.refresh();
                    clearAddTOCartFields();
                    System.out.println(tm);
                    btnAddToCart.setDisable(true);
                    btnRemoveFromCart.setDisable(true);
                    btnPlaceOrder.setDisable(false);
                }
            }

            calculateTotal();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private JFXButton getButton(CartTM tm) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-border-color: Black");
        btn.setOnAction(event -> obList.remove(tm));
        tableOrderItems.refresh();
        return btn;
    }

    private double calculateTotal() {
        double grandTotal = 0;
        for (CartTM tm : obList) {
            grandTotal += (tm.getCost() - tm.getDiscount());
        }
        lblTotal.setText(grandTotal + "");
        return grandTotal;
    }

    public void removeFromCartOnAction(ActionEvent actionEvent) {
        CartTM cartTM = itemExistsInCart(cmbItemCode.getValue());
        if (cartTM == null) {
            new Alert(Alert.AlertType.WARNING, "Item Does Not Exist In Cart", ButtonType.OK).show();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Are You Sure", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            ButtonType type = alert.getResult();
            if (type.equals(ButtonType.YES)) {
                obList.remove(cartTM);
                tableOrderItems.refresh();
                clearAddTOCartFields();
            }
        }
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        try {
            if (placeOrderBO.placeOrder(new OrderDTO(OrderID, LocalDate.now(), cmbCustomerID.getValue(), calculateTotal(), obList.stream().map(cartTM ->
                    new OrderDetailDTO(OrderID, cartTM.getItemCode(), cartTM.getQty(), cartTM.getDiscount())).collect(Collectors.toList())))) {
                PlacedOrderPane.setVisible(true);
                new FadeIn(PlacedOrderPane).play();
                btnCancel.fire();
            } else {
                NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong While Saving Order!", NotificationType.ERROR, Duration.millis(3000));
            }
            btnCancel.fire();
            OrderID = placeOrderBO.generateNewOrderId();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void clearFieldsOnAction(ActionEvent actionEvent) {
        clearAddTOCartFields();
        cmbCustomerID.getSelectionModel().clearSelection();
        txtCustName.clear();
        txtTitle.clear();
        txtAddress.clear();
        obList.clear();
        tableOrderItems.refresh();
        btnAddToCart.setDisable(true);
        btnRemoveFromCart.setDisable(true);
        btnPlaceOrder.setDisable(true);
        lblTotal.setText("00.0");
    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/lk/ijse/pos/view/CustomerManagementForm.fxml")))));
        stage.setResizable(false);
        btnPlaceOrder.setDisable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        btnPlaceOrder.setDisable(false);
        loadAllCustomersAndItems();
    }

    private void loadAllCustomersAndItems() {
        try {
            ObservableList<String> CustomerNames = FXCollections.observableArrayList(placeOrderBO.getAllCustomerIds());
            cmbCustomerID.setItems(CustomerNames);
            ObservableList<String> ItemNames = FXCollections.observableArrayList(placeOrderBO.getAllItemIds());
            cmbItemCode.setItems(ItemNames);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        PlacedOrderPane.setVisible(false);
        btnAddToCart.setDisable(true);
        btnRemoveFromCart.setDisable(true);
        btnPlaceOrder.setDisable(true);
        loadAllCustomersAndItems();

        try {
            OrderID = placeOrderBO.generateNewOrderId();
        } catch (SQLException | ClassNotFoundException e) {

            e.printStackTrace();
        }

        RegexMap.put(txtQty, Pattern.compile("^[0-9]+$"));
        RegexMap.put(txtDiscount, Pattern.compile("^[0-9]*(.[0-9]{1,2})?$"));
        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setItemFields(newValue);
                txtQty.requestFocus();
                btnAddToCart.setDisable(false);
                btnRemoveFromCart.setDisable(false);
            }
        });

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCustomerFields(newValue);
            }
        });

        tableOrderItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                cmbItemCode.getSelectionModel().select(newValue.getItemCode());
                txtQty.setText(newValue.getQty() + "");
                txtDiscount.setText(newValue.getDiscount() + "");
                btnAddToCart.setText("Update Item");
            }
        });
    }

    private void setCustomerFields(String newValue) {
        try {
            CustomerDTO customer = placeOrderBO.getCustomer(newValue);
            txtTitle.setText(customer.getTitle());
            txtCustName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemFields(String newValue) {
        try {
            ItemDTO item = placeOrderBO.getItem(newValue);
            if (item.getImageLocation() != null) {
                setImageProperties(new Image("file:" + item.getImageLocation()), 130, 124);
            } else {
                setImageProperties(new Image("/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
            }
            txtPackSize.setText(item.getPackSize());
            txtUnitPrice.setText(item.getUnitPrice() + "");
            txtQtyOnHand.setText(item.getQtyOnHand() + "");
            txtDescription.setText(item.getDescription());
            txtQty.setText("1");
            txtDiscount.setText("0");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setImageProperties(Image img, double height, double width) {
        imgItemImage.setImage(img);
        imgItemImage.setFitHeight(height);
        imgItemImage.setFitWidth(width);

    }

    public void viewRecieptOnAction(ActionEvent actionEvent) {
        try {
            HashMap paramMap = new HashMap();
            OrderDTO order = placeOrderBO.getOrder(placeOrderBO.getLastOrderID());
            CustomerDTO customer = placeOrderBO.getCustomer(order.getCustId());
            paramMap.put("Order ID", order.getOrderId());// id = report param name // customerID = UI typed value
            paramMap.put("Customer ID", order.getCustId());
            paramMap.put("Customer Name", customer.getName());
            paramMap.put("Customer Address", customer.getAddress());
            paramMap.put("Total", order.getTotal());
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/lk/ijse/pos/reports/Order_reciept.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void closePadPaneOnAction(MouseEvent mouseEvent) {
        new FadeOut(PlacedOrderPane).play();
        PlacedOrderPane.setVisible(false);
    }
}


