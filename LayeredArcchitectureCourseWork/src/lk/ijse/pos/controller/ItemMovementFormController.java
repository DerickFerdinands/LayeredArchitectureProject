package lk.ijse.pos.controller;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.ItemMovementBO;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ItemMovementFormController {
    public BarChart barCHartItems;
    public Label lblMost;
    public Label lblLeast;
    private ItemMovementBO imBO = (ItemMovementBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.ITEM_MOVEMENT);

    public void initialize() {
        try {
            setChartValues();
            LinkedHashMap<String, Integer> mostAndLeastMovedItem = imBO.getMostAndLeastMovedItem();

            mostAndLeastMovedItem.forEach((s, integer) -> {
                if (lblMost.getText().isEmpty()) {
                    lblMost.setText(s + " - " + integer);
                } else {
                    lblLeast.setText(s + " - " + integer);
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setChartValues() throws SQLException, ClassNotFoundException {
        LinkedHashMap<String, Integer> totalItemOrderQty = imBO.getTotalItemOrderQty();

        XYChart.Series series = new XYChart.Series();

        for (String s : totalItemOrderQty.keySet()) {
            series.getData().add(new XYChart.Data<>(s, totalItemOrderQty.get(s)));
        }

        barCHartItems.getData().add(series);
    }
}
