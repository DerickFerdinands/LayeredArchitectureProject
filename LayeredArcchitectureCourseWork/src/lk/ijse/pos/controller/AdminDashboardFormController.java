package lk.ijse.pos.controller;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AdminDashboardFormController {
    public JFXButton btnHome;
    public JFXButton btnItems;
    public JFXButton btnIncome;
    public JFXButton btnMovement;
    public JFXButton btnLogout;
    public AnchorPane SwitchPrivilegePane;
    public AnchorPane MainContext;
    public AnchorPane navigationPane;
    private JFXButton lastClicked;

    public void initialize() {
        lastClicked = btnHome;
        btnHome.fire();
    }
    public void setUI(String URI) throws IOException {
        navigationPane.getChildren().clear();
        navigationPane.getChildren().add(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/" + URI + ".fxml")));
        new FadeIn(navigationPane).setSpeed(5).play();
    }

    public void goToHomeOnAction(ActionEvent actionEvent) throws IOException {
        setUI("AdminHomeForm");
    }

    public void MaximizeButtons(MouseEvent mouseEvent) {
        Object o = mouseEvent.getSource();
        if (o instanceof JFXButton) {
            JFXButton btn = (JFXButton) o;
            if (btn != lastClicked) {
                ScaleTransition scaleT = new ScaleTransition(Duration.millis(50), btn);
                scaleT.setToX(1.1);
                scaleT.setToY(1.1);
                scaleT.play();
                btn.setStyle("-fx-background-color: #E7E7E7");
            }
        }

    }

    public void ResetButtons(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof JFXButton && mouseEvent.getSource() != lastClicked) {
            JFXButton btn = (JFXButton) mouseEvent.getSource();
            resetButton(btn);
        }
    }

    public void resetButton(JFXButton btn) {
        btn.setTextFill(Paint.valueOf("#5b5353"));
        btn.setStyle("-fx-background-radius: 15");
        ColorAdjust ca = new ColorAdjust();
        ca.setBrightness(0.55);
        btn.getGraphic().setEffect(ca);
        ScaleTransition scaleT = new ScaleTransition(Duration.millis(100), btn);
        scaleT.setToX(1);
        scaleT.setToY(1);
        scaleT.play();
    }

    public void changeButtonUI(MouseEvent mouseEvent) {
        if (mouseEvent.getSource() instanceof JFXButton) {
            if (lastClicked != null) resetButton(lastClicked);
            lastClicked = (JFXButton) mouseEvent.getSource();
            resetButton(lastClicked);
            lastClicked.setTextFill(Paint.valueOf("#fffefe"));
            lastClicked.setStyle("-fx-background-color:  #704232;");
            ColorAdjust ca = new ColorAdjust();
            ca.setBrightness(1);
            lastClicked.getGraphic().setEffect(ca);
        }
    }

    public void goToItemManagementOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ItemManagementForm");
    }

    public void goToIncomeFormOnAction(ActionEvent actionEvent) throws IOException {
        setUI("IncomeReportsForm");
    }

    public void goToItemMovementInfoOnAction(ActionEvent actionEvent) throws IOException {
        setUI("ItemMovementForm");
    }

    public void logOutOnAction(ActionEvent actionEvent) {
        ButtonType type = new Alert(Alert.AlertType.CONFIRMATION,"Are You Sure ?",ButtonType.YES,ButtonType.NO).showAndWait().get();
        if(type.equals(ButtonType.YES)){
            System.exit(0);
        }
    }

    public void ShowSwitchPrivilegePane(MouseEvent mouseEvent) {
        SwitchPrivilegePane.setVisible(true);
        FadeIn slideInDown = new FadeIn(SwitchPrivilegePane);
        slideInDown.setCycleCount(1).play();
    }

    public void HidePrivilegePane(MouseEvent mouseEvent) {
        new FadeOut(SwitchPrivilegePane).play();
        SwitchPrivilegePane.setVisible(false);

    }
    public void switchOptionsOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        ButtonType type = alert.getResult();
        if (type.equals(ButtonType.YES)) {
            Stage stage = (Stage) MainContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/lk/ijse/pos/view/DashboardForm.fxml"))));
        }
    }
}
