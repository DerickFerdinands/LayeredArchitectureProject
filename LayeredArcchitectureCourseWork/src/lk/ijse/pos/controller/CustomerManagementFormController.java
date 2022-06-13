package lk.ijse.pos.controller;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.CustomerBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lk.ijse.pos.DTO.CustomerDTO;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import lk.ijse.pos.util.NotificationUtil;
import lk.ijse.pos.util.ValidationUtil;
import lk.ijse.pos.view.tm.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerManagementFormController {
    private final CustomerBO cBO = (CustomerBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.CUSTOMER);
    private final ObservableList<CustomerTM> obList = FXCollections.observableArrayList();
    private final LinkedHashMap<JFXTextField, Pattern> RegexMap = new LinkedHashMap<>();
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXButton btnAddCustomer;
    public JFXTextField txtAddress;
    public JFXTextField txtCity;
    public JFXTextField txtPostalCode;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colTitle;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colCity;
    public TableColumn colProvince;
    public TableColumn colPostalCode;
    public TableColumn colOption;
    public JFXTextField txtSearch;
    public JFXButton btnCancel;
    public JFXComboBox<String> cmbTitle;
    public JFXComboBox<String> cmbProvince;

    public void validateFields(KeyEvent keyEvent) {
        Object validate = ValidationUtil.Validate(RegexMap, btnAddCustomer);

        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            if (validate instanceof JFXTextField) {
                ((JFXTextField) validate).requestFocus();
            } else {
                btnAddCustomer.fire();
            }
        }
    }

    private void addError(Node node){
        node.getParent().setStyle("-fx-border-color: red;"+"-fx-border-width:1;"+"-fx-border-radius:  5;"+"-fx-background-radius:  5;");
    }

    private void removeError(Node node){
        node.getParent().setStyle("-fx-border-color: green;"+"-fx-border-width:1;"+"-fx-border-radius:  5;"+"-fx-background-radius:  5;");
    }

    public void addCustomerOnAction(ActionEvent actionEvent) {
        if (cmbTitle.getSelectionModel().getSelectedItem()==null) {
            new Alert(Alert.AlertType.WARNING, "Customer Title Not Selected", ButtonType.OK).showAndWait();
            addError(cmbTitle);
        } else if (cmbProvince.getSelectionModel().getSelectedItem()==null) {
            new Alert(Alert.AlertType.WARNING, "Customer Province Not Selected", ButtonType.OK).showAndWait();
            removeError(cmbTitle);
            addError(cmbProvince);
        } else {
            removeError(cmbTitle);
            removeError(cmbProvince);
            removeError(txtId);
            try {
                if (btnAddCustomer.getText().equals("Add Client")) {
                     if(obList.stream().anyMatch(customerTM -> {return customerTM.getId().equals(txtId.getText());})){
                        new Alert(Alert.AlertType.WARNING, "Customer Id Already Exist", ButtonType.OK).showAndWait();
                        removeError(cmbProvince);
                        addError(txtId);
                    }else {
                         if (cBO.saveCustomer(new CustomerDTO(txtId.getText(), cmbTitle.getValue(), txtName.getText(), txtAddress.getText(), txtCity.getText(), cmbProvince.getValue(), txtPostalCode.getText()))) {
                             NotificationUtil.playNotification(AnimationType.POPUP, "Client Added Successfuly!", NotificationType.SUCCESS, Duration.millis(2000));
                         } else {
                             NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                         }
                     }
                } else {
                    if (cBO.updateCustomer(new CustomerDTO(txtId.getText(), cmbTitle.getValue(), txtName.getText(), txtAddress.getText(), txtCity.getText(), cmbProvince.getValue(), txtPostalCode.getText()))) {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Client Updated Successfuly!", NotificationType.SUCCESS, Duration.millis(2000));
                    } else {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                    }
                }
                loadALlCustomers();
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
        txtId.clear();
        cmbTitle.getSelectionModel().clearSelection();
        txtName.clear();
        txtAddress.clear();
        txtCity.clear();
        cmbProvince.getSelectionModel().clearSelection();
        txtPostalCode.clear();
        btnAddCustomer.setDisable(true);
        btnAddCustomer.setText("Add Client");
        tblCustomer.getSelectionModel().clearSelection();
        resetFields(txtId,txtName,txtAddress,txtCity,txtPostalCode,cmbTitle,cmbProvince);
    }

    public void searchMatchingItems(KeyEvent keyEvent) {
        try {
            ArrayList<CustomerDTO> cusList = cBO.getMatchingCustomers(txtSearch.getText());
            obList.clear();
            for (CustomerDTO c : cusList) {
                obList.add(new CustomerTM(c.getId(), c.getTitle(), c.getName(), c.getAddress(), c.getCity(), c.getProvince(), c.getPostalCode(), getButton(c.getId())));
            }
            tblCustomer.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colProvince.setCellValueFactory(new PropertyValueFactory<>("Province"));
        colPostalCode.setCellValueFactory(new PropertyValueFactory<>("PostalCode"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadALlCustomers();

        RegexMap.put(txtId, Pattern.compile("^(C)[0-9]{3}$"));
        RegexMap.put(txtName, Pattern.compile("^[A-z ]+$"));
        RegexMap.put(txtAddress, Pattern.compile("^[A-z0-9/,. ]+$"));
        RegexMap.put(txtCity, Pattern.compile("^[A-z0-9 ]+$"));
        RegexMap.put(txtPostalCode, Pattern.compile("^[0-9]{5}$"));

        cmbTitle.getItems().addAll("Mr.", "Mrs.", "Ms.", "Miss", "Custom", "Rather Not Say");
        cmbProvince.getItems().addAll("Western Province", "Central Province","Southern Province","Uva Province","Sabaragamuwa Province","North Western Province","North Central Province","Northern Province","Eastern Province");
        try {
            txtId.setText(cBO.generateCustomerId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtName.requestFocus();
    }

    private void loadALlCustomers() {
        try {
            ArrayList<CustomerDTO> cusList = cBO.getAllCustomers();
            obList.clear();
            for (CustomerDTO c : cusList) {
                obList.add(new CustomerTM(c.getId(), c.getTitle(), c.getName(), c.getAddress(), c.getCity(), c.getProvince(), c.getPostalCode(), getButton(c.getId())));
            }
            tblCustomer.setItems(obList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setUpdateFields(newValue);
            }
        });
    }

    private void setUpdateFields(CustomerTM tm) {
        txtId.setText(tm.getId());
        txtId.setEditable(false);
        cmbTitle.getSelectionModel().select(tm.getTitle());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtCity.setText(tm.getCity());
        cmbProvince.getSelectionModel().select(tm.getProvince());
        txtPostalCode.setText(tm.getPostalCode());
        btnAddCustomer.setDisable(false);
        btnAddCustomer.setText("Update Client");
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
                    if (cBO.deleteCustomer(id)) {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Client Deleted Successfuly!", NotificationType.SUCCESS, Duration.millis(2000));
                    } else {
                        NotificationUtil.playNotification(AnimationType.POPUP, "Something Went Wrong!", NotificationType.ERROR, Duration.millis(2000));
                    }
                    loadALlCustomers();
                    btnCancel.fire();
                } catch (SQLException | ClassNotFoundException e) {
                    NotificationUtil.playNotification(AnimationType.POPUP, e.getMessage(), NotificationType.ERROR, Duration.millis(2000));
                }
            }
        });
        return btn;
    }
}
