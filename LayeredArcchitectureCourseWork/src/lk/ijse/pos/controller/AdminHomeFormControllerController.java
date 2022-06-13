package lk.ijse.pos.controller;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.AdminHomeBO;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class AdminHomeFormControllerController {
    public Label lblDayName;
    public Label lblDate;
    public Label lblTime;
    public Label lblTotalNoItems;
    public Label lblTotalNoCustomers;
    public Label lblRevenue;

    private final AdminHomeBO ahBO = (AdminHomeBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ADMIN_HOME);

    public void initialize(){
        setTimeAndDate();
        try {
            lblRevenue.setText(ahBO.getTotalOrderRevenue()+"");
            lblTotalNoCustomers.setText(ahBO.getCustomerCount()+"");
            lblTotalNoItems.setText(ahBO.getItemCount()+"");
        } catch (SQLException | ClassNotFoundException e) {
           e.printStackTrace();
        }
    }

    private void setTimeAndDate() {
        lblDate.setText(LocalDate.now()+"");
        lblDayName.setText(LocalDate.now().getDayOfWeek().name());
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(String.format("%02d",currentTime.getHour()) + ":" + String.format("%02d",currentTime.getMinute()) + ":" + String.format("%02d",currentTime.getSecond()));
        }),
                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
}
