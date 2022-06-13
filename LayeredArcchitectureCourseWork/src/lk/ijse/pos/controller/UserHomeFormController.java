package lk.ijse.pos.controller;

import lk.ijse.pos.DTO.CustomerDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.DTO.OrderDetailDTO;
import animatefx.animation.*;
import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.UserHomeBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import lk.ijse.pos.util.NotificationUtil;
import lk.ijse.pos.view.tm.CartTM;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class UserHomeFormController {
    private final ObservableList<CartTM> obList = FXCollections.observableArrayList();
    public AnchorPane HomeScrollerPane;
    public AnchorPane addToCartPane;
    public ImageView ItemImageView;
    public Label lblItemCode;
    public Label lblDescription;
    public Label lblAvailableQty;
    public Label lblUnitPrice;
    public JFXTextField txtOrderQty;
    public JFXTextField txtDiscount;
    public JFXButton btnAddToCart;
    public AnchorPane placeOrderPane;
    public Label lblTotal;
    public JFXButton btnPlaceOrder;
    public JFXButton btnCancel;
    public JFXButton btnAddCustomer;
    public TableView<CartTM> tableOrderItems;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colCost;
    public TableColumn colDiscount;
    public TableColumn colOption;
    public JFXComboBox<String> cmbCustomerID;
    public JFXTextField txtCustName;
    public JFXTextField txtTitle;
    private final UserHomeBO uhBO = (UserHomeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.USER_HOME);
    public AnchorPane NavigationPane;
    private String OrderID;

    public void addToCartOnAction(ActionEvent actionEvent) {
        try {
            CartTM tm = (obList.stream().anyMatch(cartTM -> cartTM.getItemCode().equals(lblItemCode.getText()))) ? tableOrderItems.getItems().stream().filter(detail -> detail.getItemCode().equals(lblItemCode.getText())).findFirst().get() : null;
            if (Pattern.matches("^[0-9]*([.][0-9]{1,2})?$", txtDiscount.getText())) {


                if (tm == null) {
                    ItemDTO item = uhBO.getItem(lblItemCode.getText());
                    int qty = Integer.parseInt(txtOrderQty.getText());
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
                        NotificationUtil.playNotification(AnimationType.POPUP, "Item Added To Cart", NotificationType.SUCCESS, Duration.millis(3000));
                        tableOrderItems.refresh();
                        btnPlaceOrder.setDisable(false);
                    }
                } else {
                    int qty = Integer.parseInt(txtOrderQty.getText()) + tm.getQty();
                    double cost = (qty * tm.getUnitPrice());
                    double discount = txtDiscount.getText().isEmpty() ? tm.getDiscount() : Double.parseDouble(txtDiscount.getText());
                    if (btnAddToCart.getText().equalsIgnoreCase("Update Item")) {
                        qty = Integer.parseInt(txtOrderQty.getText());
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
                        NotificationUtil.playNotification(AnimationType.POPUP, "Item Updated In Cart", NotificationType.SUCCESS, Duration.millis(3000));
                        tableOrderItems.refresh();
                        btnPlaceOrder.setDisable(false);
                    }
                }

                calculateTotal();
            } else {
                new Alert(Alert.AlertType.WARNING, "Invalid Discount !..", ButtonType.OK).show();
                txtDiscount.clear();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new FadeOutRight(addToCartPane).play();
        addToCartPane.setVisible(false);
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


    public void hideAddToCartPaneOnAction(MouseEvent mouseEvent) {
        new FadeOutRight(addToCartPane).play();
        addToCartPane.setVisible(false);
    }

    public void initialize() {

        btnPlaceOrder.setDisable(true);
        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("Qty"));
        colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        colDiscount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        tableOrderItems.setItems(obList);
        try {
            OrderID = uhBO.generateNewOrderId();
            setItems(uhBO.getAllItems());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            cmbCustomerID.getItems().addAll(FXCollections.observableArrayList(uhBO.getAllCustomerIds()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbCustomerID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCustomerFields(newValue);
            }
        });

    }

    private void setCustomerFields(String newValue) {
        try {
            CustomerDTO customer = uhBO.getCustomer(newValue);
            txtTitle.setText(customer.getTitle());
            txtCustName.setText(customer.getName());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItems(ArrayList<ItemDTO> list) {
        double LayoutX = 30;
        double LayoutY = 16;
        for (ItemDTO dto : list) {
            AnchorPane itemView = getItemView(dto);
            if (LayoutX > 800) {
                LayoutX = 30;
                LayoutY += 230;
            }

            if (LayoutY > 450) {
                HomeScrollerPane.setPrefHeight(LayoutY + 250);
            }
            itemView.setLayoutY(LayoutY);
            itemView.setLayoutX(LayoutX);
            LayoutX += 190;
            HomeScrollerPane.getChildren().add(itemView);
        }
    }

    private AnchorPane getItemView(ItemDTO dto) {
        AnchorPane itemView = new AnchorPane();
        itemView.setPrefSize(174, 218);
        itemView.setStyle("-fx-border-color:  #EDEDF0;" + "-fx-border-width: 3;" + "-fx-background-color:  #FFFFFF;" + "-fx-background-radius: 10;" + "-fx-border-radius: 10");

        ImageView view = new ImageView("file:" + dto.getImageLocation());
        view.setFitHeight(120);
        view.setFitWidth(120);

        HBox hBox = getHBox(5, 4, 166, 17);
        hBox.getChildren().add((getLabel(dto.getItemCode(), new Font("Arial", 11))));

        HBox hBox1 = getHBox(26, 15, 123, 128);
        hBox1.getChildren().add(view);

        HBox hBox2 = getHBox(5, 134, 162, 30);
        hBox2.getChildren().add(getLabel(dto.getDescription(), new Font("Arial Narrow Bold", 20)));

        HBox hBox3 = getHBox(5, 164, 161, 17);
        hBox3.getChildren().add(getLabel("Available Qty: " + dto.getQtyOnHand(), new Font("Arial", 11)));

        HBox hBox4 = getHBox(5, 183, 164, 23);
        hBox4.getChildren().add(getLabel("Rs. " + dto.getUnitPrice() + "/=", new Font("Arial", 15)));

        itemView.setOnMouseClicked(event -> {
            setAddToCartFields(dto);
            addToCartPane.setVisible(true);
            new FadeInRight(addToCartPane).setSpeed(4).play();
        });

        itemView.getChildren().addAll(hBox, hBox1, hBox2, hBox3, hBox4);


        return itemView;
    }

    private HBox getHBox(double LayoutX, double LayoutY, double width, double height) {
        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setLayoutX(LayoutX);
        box.setLayoutY(LayoutY);
        box.setPrefSize(width, height);
        return box;
    }

    private Label getLabel(String text, Font font) {
        Label label = new Label(text);
        label.setFont(font);
        return label;
    }

    private void setAddToCartFields(ItemDTO dto) {
        lblItemCode.setText(dto.getItemCode());
        ItemImageView.setImage(new Image("file:" + dto.getImageLocation()));
        lblDescription.setText(dto.getDescription());
        lblAvailableQty.setText("Available Qty: " + dto.getQtyOnHand());
        lblUnitPrice.setText("Rs." + dto.getUnitPrice() + "/=");
    }

    public void placeOrderOnAction(ActionEvent actionEvent) {
        try {
            if (uhBO.placeOrder(new OrderDTO(OrderID, LocalDate.now(), cmbCustomerID.getValue(), calculateTotal(), obList.stream().map(cartTM ->
                    new OrderDetailDTO(OrderID, cartTM.getItemCode(), cartTM.getQty(), cartTM.getDiscount())).collect(Collectors.toList())))) {
                NotificationUtil.playNotification(AnimationType.POPUP, "Order Placed Succesfully!", NotificationType.SUCCESS, Duration.millis(2000));
                btnCancel.fire();
                OrderID = uhBO.generateNewOrderId();
                resetView();
            } else {
                NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong While Saving Order!", NotificationType.ERROR, Duration.millis(3000));
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

    }

    public void clearFieldsOnAction(ActionEvent actionEvent) {
        obList.clear();
        tableOrderItems.refresh();
        cmbCustomerID.getSelectionModel().clearSelection();
        btnPlaceOrder.setDisable(true);
        lblTotal.setText("0.00");
        txtCustName.clear();
        txtTitle.clear();
        txtOrderQty.setText("1");
        txtDiscount.setText("0.00");
    }

    public void addCustomerOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/lk/ijse/pos/view/CustomerManagementForm.fxml")))));
        stage.setResizable(false);
        btnPlaceOrder.setDisable(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        btnPlaceOrder.setDisable(false);
        try {
            cmbCustomerID.getItems().addAll(FXCollections.observableArrayList(uhBO.getAllCustomerIds()));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void hidePlaceOrderPane(MouseEvent mouseEvent) {
        new FadeInDown(placeOrderPane).play();
        placeOrderPane.setVisible(false);
    }

    public void showPlaceOrderPane(MouseEvent mouseEvent) {
        placeOrderPane.setVisible(true);
        new FadeInUp(placeOrderPane).setSpeed(4).play();
    }

    public void deductQty(ActionEvent actionEvent) {
        txtOrderQty.setText((Integer.parseInt(txtOrderQty.getText()) - 1) + "");
    }

    public void addQty(ActionEvent actionEvent) {
        txtOrderQty.setText((Integer.parseInt(txtOrderQty.getText()) + 1) + "");
    }

    public void resetView() throws IOException {
            NavigationPane.getChildren().clear();
        NavigationPane.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/UserHomeForm.fxml")));
            new FadeIn(NavigationPane).setSpeed(5).play();

    }
}
