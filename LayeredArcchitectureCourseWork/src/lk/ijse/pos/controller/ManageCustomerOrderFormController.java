package lk.ijse.pos.controller;

import lk.ijse.pos.DTO.CustomDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.DTO.OrderDetailDTO;
import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ManageOrderBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import lk.ijse.pos.util.NotificationUtil;
import lk.ijse.pos.util.ValidationUtil;
import lk.ijse.pos.view.tm.CartTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ManageCustomerOrderFormController {
    private final ManageOrderBO manageOrderBO = (ManageOrderBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.MANAGE_ORDER);
    private final ObservableList<CartTM> obList = FXCollections.observableArrayList();
    private final ObservableList<CartTM> deletedList = FXCollections.observableArrayList();
    private final LinkedHashMap<JFXTextField, Pattern> RegexMap = new LinkedHashMap<>();
    public TableView<CartTM> tableOrderItems;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colCost;
    public TableColumn colDiscount;
    public TableColumn colOption;
    public JFXButton btnEditOrder;
    public JFXButton btnCancel;
    public JFXTextField txtQty;
    public JFXTextArea txtDescription;
    public ImageView imgItemImage;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPackSize;
    public JFXButton btnAddToCart;
    public JFXButton btnRemoveFromCart;
    public JFXTextField txtDiscount;
    public JFXComboBox<String> cmbCustomerID;
    public JFXTextField txtCustName;
    public JFXTextField txtSearch;
    public AnchorPane OrderListPane;
    public Label lblTotal;
    public JFXTextField txtItemCode;
    private String OrderID;
    private OrderDTO orderDto;

    public void editOrderOnAction(ActionEvent actionEvent) {
        try {
            if (manageOrderBO.editOrder(new OrderDTO(OrderID, orderDto.getOrderDate(), orderDto.getCustId(), calculateTotal(), obList.stream().map(tm -> new OrderDetailDTO(orderDto.getOrderId(), tm.getItemCode(), tm.getQty(), tm.getDiscount())).collect(Collectors.toList())), deletedList)) {
                NotificationUtil.playNotification(AnimationType.POPUP, "Order Succesfully Edited!", NotificationType.SUCCESS, Duration.millis(3000));
                clearAddTOCartFields();
                lblTotal.setText("00.0");
                obList.clear();
                tableOrderItems.refresh();
                String value = cmbCustomerID.getValue();
                cmbCustomerID.getSelectionModel().clearSelection();
                cmbCustomerID.getSelectionModel().select(value);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public void updateCartOnAction(ActionEvent actionEvent) {
        CartTM tm = obList.get(obList.indexOf(new CartTM(txtItemCode.getText())));
        System.out.println(tm);
        int tempQty = (tm.getQty() < Integer.valueOf(txtQty.getText())) ? (Integer.valueOf(txtQty.getText()) - tm.getQty()) : Integer.valueOf(txtQty.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double discount = txtDiscount.getText().isEmpty() ? 0 : Double.valueOf(txtDiscount.getText());
        double cost = (qty * tm.getUnitPrice());
        if (tempQty > tm.getQtyOnHand() && (tempQty > tm.getQty())) {
            System.out.println(tempQty + " " + tm.getQtyOnHand());
            new Alert(Alert.AlertType.WARNING, "Insufficient Qty !..", ButtonType.OK).show();
        } else if (discount > cost) {
            new Alert(Alert.AlertType.WARNING, "Discount Exceeds Cost !..", ButtonType.OK).show();
        } else {
            tm.setQty(qty);
            tm.setCost(cost);
            tm.setDiscount(discount);
            tableOrderItems.refresh();
            clearAddTOCartFields();
            btnAddToCart.setDisable(true);
            btnRemoveFromCart.setDisable(true);
            btnEditOrder.setDisable(false);
        }
        calculateTotal();
        btnEditOrder.setDisable(false);
    }

    private void clearAddTOCartFields() {
        for (JFXTextField jfxTextField : Arrays.asList(txtPackSize, txtUnitPrice, txtQtyOnHand, txtQty, txtDiscount, txtItemCode)) {
            jfxTextField.clear();
        }
        txtDescription.clear();
        setImageProperties(new Image("lk/ijse/pos/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
        resetFields(txtQty, txtDiscount);
        btnAddToCart.setText("Add To Cart");
    }

    public void resetFields(JFXTextField... fields) {
        for (JFXTextField field : fields) {
            field.getParent().setStyle("-fx-border-color :   #EDEDF0;" + "-fx-border-width:1.5;" + "-fx-border-radius:  5;" + "-fx-background-radius:  5;");
        }
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
        CartTM tm = obList.get(obList.indexOf(new CartTM(txtItemCode.getText())));
        deletedList.add(tm);
        obList.remove(tm);
    }

    public void searchMatchingItems(KeyEvent keyEvent) {
        try {
            setOrderList(manageOrderBO.getMatchingOrderResults("%" + txtSearch.getText() + "%"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        for (JFXButton jfxButton : Arrays.asList(btnAddToCart, btnRemoveFromCart, btnEditOrder, btnCancel)) {
            jfxButton.setDisable(true);
        }
        cmbCustomerID.getSelectionModel().select("All Orders");
        RegexMap.put(txtQty, Pattern.compile("^[0-9]+$"));
        RegexMap.put(txtDiscount, Pattern.compile("^[0-9]*(.[0-9]{1,2})?$"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        try {
            setOrderList(manageOrderBO.getAllOrders());
            cmbCustomerID.getItems().addAll(manageOrderBO.getAllCustomerIds());
            cmbCustomerID.getItems().add("All Orders");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    txtCustName.setText(manageOrderBO.getCustomerName(newValue));
                    if (newValue.equals("All Orders")) {
                        setOrderList(manageOrderBO.getAllOrders());
                    } else {
                        setOrderList(manageOrderBO.searchOrderByCustomer(newValue));
                    }
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        tableOrderItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    setItemFields(newValue.getItemCode());
                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                txtQty.setText(newValue.getQty() + "");
                txtDiscount.setText(newValue.getDiscount() + "");
                btnAddToCart.setText("Update Item");
            }
        });
    }

    private void setItemFields(String itemCode) throws SQLException, ClassNotFoundException {
        ItemDTO item = manageOrderBO.getItem(itemCode);
        txtItemCode.setText(item.getItemCode());
        txtPackSize.setText(item.getPackSize());
        txtUnitPrice.setText(item.getUnitPrice() + "");
        txtQtyOnHand.setText(item.getQtyOnHand() + "");
        txtDescription.setText(item.getDescription());
        btnAddToCart.setDisable(false);
        btnRemoveFromCart.setDisable(false);
        if (item.getImageLocation() != null) {
            setImageProperties(new Image("file:" + item.getImageLocation()), 130, 124);
        } else {
            setImageProperties(new Image("/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
        }

    }

    private void setOrderList(ArrayList<OrderDTO> list) {
        OrderListPane.getChildren().clear();
        double LayoutX = 13;
        for (OrderDTO dto : list) {
            JFXButton btn = getOrderButton(dto);
            btn.setLayoutY(9);
            if (LayoutX > OrderListPane.getPrefWidth()) {
                OrderListPane.setPrefWidth(LayoutX + 59);
            }
            btn.setLayoutX(LayoutX);
            OrderListPane.getChildren().add(btn);
            LayoutX += 65;
        }
    }

    private void setTableItems(ArrayList<CustomDTO> itemList) {
        deletedList.clear();
        obList.clear();
        for (CustomDTO dto : itemList) {
            obList.add(new CartTM(dto.getItemCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand(), dto.getOrderQty(), dto.getDiscount(), dto.getTotal(), getButton(dto.getItemCode())));
        }
        tableOrderItems.setItems(obList);
        tableOrderItems.refresh();
    }

    private JFXButton getButton(String itemCode) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-border-color: Black");
        btn.setOnAction(event -> {
            CartTM cartTM = obList.get(obList.indexOf(new CartTM(itemCode)));
            deletedList.add(cartTM);
            obList.remove(cartTM);
            if (obList.isEmpty()) {
                btnEditOrder.setDisable(true);
            }
        });
        tableOrderItems.refresh();
        return btn;
    }

    private JFXButton getOrderButton(OrderDTO Order) {
        JFXButton btn = new JFXButton(Order.getOrderId());
        btn.setStyle("-fx-background-color: #704232");
        btn.setTextFill(Paint.valueOf("#ffffff"));
        btn.setPrefSize(59, 52);
        btn.setOnAction(event -> {
            try {
                orderDto = Order;
                OrderID = Order.getOrderId();
                lblTotal.setText(Order.getTotal() + "");
                setTableItems(manageOrderBO.SearchOrderWithItemProperties(Order.getOrderId()));
                btnCancel.setDisable(false);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        return btn;
    }

    public void setImageProperties(Image img, double height, double width) {
        imgItemImage.setImage(img);
        imgItemImage.setFitHeight(height);
        imgItemImage.setFitWidth(width);
    }

    public void cancelOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ButtonType type = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure ?", ButtonType.YES, ButtonType.NO).showAndWait().get();
        if (type.equals(ButtonType.YES)) {
            if (manageOrderBO.cancelOrder(OrderID)) {
                NotificationUtil.playNotification(AnimationType.POPUP, "Order " + OrderID + " Cancelled Successfully!..", NotificationType.SUCCESS, Duration.millis(3000));
                clearAddTOCartFields();
                lblTotal.setText("00.0");
                obList.clear();
                tableOrderItems.refresh();
                String value = cmbCustomerID.getValue();
                cmbCustomerID.getSelectionModel().clearSelection();
                cmbCustomerID.getSelectionModel().select(value);
            }
        }
    }
}
