package lk.ijse.pos.controller;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ItemBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import lk.ijse.pos.DTO.ItemDTO;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import lk.ijse.pos.util.NotificationUtil;
import lk.ijse.pos.util.ValidationUtil;
import lk.ijse.pos.view.tm.ItemTM;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class ItemManagementFormController {
    private final LinkedHashMap<JFXTextField, Pattern> RegexMap = new LinkedHashMap<>();
    public JFXTextField txtCode;
    public JFXTextArea txtDescription;
    public ImageView imgItemImage;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPackSize;
    public JFXButton btnAddItem;
    public JFXButton btnCancel;
    public TableView<ItemTM> tblItems;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colPackSize;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOption;
    public JFXTextField txtSearch;
    public AnchorPane NavigationContext;
    private final ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ITEM);
    private File file = null;

    public void validateFields(KeyEvent keyEvent) {
        Object validate = ValidationUtil.Validate(RegexMap, btnAddItem);

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (validate instanceof JFXTextField) {
                ((JFXTextField) validate).requestFocus();
            } else {
                btnAddItem.fire();
            }
        }
    }

    public void addItemOnAction(ActionEvent actionEvent) {
        if (file == null) {
            new Alert(Alert.AlertType.WARNING, "Item Image Not Selected!", ButtonType.OK).show();
        } else {
            try {
                if (btnAddItem.getText().equals("Add Item")) {
                    if (itemBO.saveItem(new ItemDTO(txtCode.getText(), txtDescription.getText(), txtPackSize.getText(), Double.valueOf(txtUnitPrice.getText()), Integer.valueOf(txtQtyOnHand.getText()), file.getPath()))) {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Item Added Successfully!", NotificationType.SUCCESS, Duration.millis(2000));
                    } else {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                    }
                } else {
                    if (itemBO.updateItem(new ItemDTO(txtCode.getText(), txtDescription.getText(), txtPackSize.getText(), Double.valueOf(txtUnitPrice.getText()), Integer.valueOf(txtQtyOnHand.getText()), file.getPath()))) {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Item Updated Successfuly!", NotificationType.SUCCESS, Duration.millis(2000));
                    } else {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                    }
                }
                loadAllItems(null);
                btnCancel.fire();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                NotificationUtil.playNotification(AnimationType.POPUP, e.getMessage(), NotificationType.ERROR, Duration.millis(2000));
            }
        }
    }

    public void resetFields(Node... fields) {
        for (Node field : fields) {
            field.getParent().setStyle("-fx-border-color :   #EDEDF0;" + "-fx-border-width:1.5;" + "-fx-border-radius:  5;" + "-fx-background-radius:  5;");
        }
    }

    public void clearFieldsOnAction(ActionEvent actionEvent) {
        txtCode.setEditable(true);
        btnAddItem.setText("Add Item");
        for (JFXTextField jfxTextField : Arrays.asList(txtCode, txtPackSize, txtUnitPrice, txtQtyOnHand)) {
            jfxTextField.clear();
        }
        txtDescription.clear();
        setImageProperties(new Image("lk/ijse/pos/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
        file = null;
        tblItems.getSelectionModel().clearSelection();
        resetFields(txtCode, txtPackSize, txtUnitPrice, txtQtyOnHand);
    }

    public void searchMatchingItems(KeyEvent keyEvent) {
        try {
            loadAllItems(itemBO.getMatchingItems("%" + txtSearch.getText() + "%"));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("ItemCode"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colPackSize.setCellValueFactory(new PropertyValueFactory<>("PackSize"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("QtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        try {
            loadAllItems(itemBO.getAllItems());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            NotificationUtil.playNotification(AnimationType.POPUP, e.getMessage(), NotificationType.ERROR, Duration.millis(3000));
        }

        tblItems.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) setUpdateFields(newValue);
        });

        RegexMap.put(txtCode, Pattern.compile("^[A-z0-9_]+$"));
        RegexMap.put(txtPackSize, Pattern.compile("^(.)+$"));
        RegexMap.put(txtUnitPrice, Pattern.compile("^[0-9]+([.][0-9]{1,2})?$"));
        RegexMap.put(txtQtyOnHand, Pattern.compile("^[0-9]+$"));
    }

    private void setUpdateFields(ItemTM tm) {
        txtCode.setEditable(false);
        btnAddItem.setText("Update Item");
        txtCode.setText(tm.getItemCode());
        txtPackSize.setText(tm.getPackSize());
        txtUnitPrice.setText(tm.getUnitPrice() + "");
        txtQtyOnHand.setText(tm.getQtyOnHand() + "");
        txtDescription.setText(tm.getDescription());
        file = new File(tm.getImageLocation());
        setImageProperties(new Image("file:" + tm.getImageLocation()), 130, 124);
    }


    private void loadAllItems(ArrayList<ItemDTO> itemList) throws SQLException, ClassNotFoundException {
        ObservableList<ItemTM> oblIst = FXCollections.observableArrayList();
        if (itemList == null) {
            itemList = itemBO.getAllItems();
        }
        for (ItemDTO dto : itemList) {
            oblIst.add(new ItemTM(dto.getItemCode(), dto.getDescription(), dto.getPackSize(), dto.getUnitPrice(), dto.getQtyOnHand(), dto.getImageLocation(), getButton(dto.getItemCode())));

        }
        tblItems.setItems(oblIst);
    }

    private JFXButton getButton(String id) {
        JFXButton btn = new JFXButton("Delete");
        btn.setStyle("-fx-border-color: Black");
        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            ButtonType type = alert.getResult();
            if (type.equals(ButtonType.YES)) {
                try {
                    if (itemBO.deleteItem(id)) {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Item Deleted Successfuly!", NotificationType.SUCCESS, Duration.millis(2000));
                    } else {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                    }
                    loadAllItems(null);
                    btnCancel.fire();
                } catch (SQLException | ClassNotFoundException e) {
                    NotificationUtil.playNotification(AnimationType.POPUP, e.getMessage(), NotificationType.ERROR, Duration.millis(2000));
                }
            }
        });
        return btn;
    }

    public void setImageOnAction(MouseEvent keyEvent) {
        FileChooser selectFile = new FileChooser();
        selectFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpeg", "*.jpg"));
        file = selectFile.showOpenDialog(NavigationContext.getScene().getWindow());
        if (file != null) {
            setImageProperties(new Image("file:" + file.getPath()), 130, 124);
        } else {
            setImageProperties(new Image("/assets/add_photo_alternate_FILL0_wght400_GRAD0_opsz48.png"), 48, 48);
        }
    }

    public void setImageProperties(Image img, double height, double width) {
        imgItemImage.setImage(img);
        imgItemImage.setFitHeight(height);
        imgItemImage.setFitWidth(width);

    }
}
