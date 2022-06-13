package lk.ijse.pos.controller;

import lk.ijse.pos.bo.BOFactory;
import lk.ijse.pos.bo.custom.IncomeReportsBO;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import lk.ijse.pos.db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import lk.ijse.pos.view.tm.AnnualIncomeTM;
import lk.ijse.pos.view.tm.DailyIncomeTM;
import lk.ijse.pos.view.tm.MonthlyIncomeTM;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;

public class IncomeReportsFormController {
    public LineChart IncomeChart;
    public JFXComboBox<String> cmbRecordType;
    public JFXButton btnViewReport;
    public TableView<DailyIncomeTM> tblDailyIncome;
    public TableColumn colDate;
    public TableColumn colDIncome;
    public TableView<MonthlyIncomeTM> tblMonthlyIncome;
    public TableColumn colMYear;
    public TableColumn colMmonth;
    public TableColumn colMIncome;
    public TableView<AnnualIncomeTM> tblYearlyIncome;
    public TableColumn ColYyear;
    public TableColumn colYIncome;
    public JFXButton btnViewReport1;
    public JFXButton btnViewReport11;
    private IncomeReportsBO irBO = (IncomeReportsBO) BOFactory.getInstance().getBO(BOFactory.BOTypes.INCOME_REPORTS);
    public void initialize() {

        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        colMYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colMmonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colMIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        ColYyear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colYIncome.setCellValueFactory(new PropertyValueFactory<>("income"));

        cmbRecordType.getItems().addAll("Daily", "Monthly", "Annual");
        cmbRecordType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setChartList(newValue);
            }
        });
        try {
            setTableItems();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbRecordType.getSelectionModel().select("Daily");
    }

    private void setTableItems() throws SQLException, ClassNotFoundException {
        ObservableList<DailyIncomeTM> dailyIncome = FXCollections.observableArrayList();
        LinkedHashMap<String, Double> map1 = irBO.getDailyIncome();
        for (String date :map1.keySet()) {
            dailyIncome.add(new DailyIncomeTM(LocalDate.parse(date),map1.get(date)));
        }
        tblDailyIncome.setItems(dailyIncome);

        ObservableList<MonthlyIncomeTM> monthlyIncome = FXCollections.observableArrayList();
        LinkedHashMap<String, Double> map2 = irBO.getMonthlyIncome();
        for (String date :map2.keySet()) {
            String[] split = date.split("-");
            monthlyIncome.add(new MonthlyIncomeTM(split[0],split[1],map2.get(date)));
        }
        tblMonthlyIncome.setItems(monthlyIncome);

        ObservableList<AnnualIncomeTM> AnnualIncome = FXCollections.observableArrayList();
        LinkedHashMap<String, Double> map3 = irBO.getAnnualIncome();
        for (String date :map3.keySet()) {
            AnnualIncome.add(new AnnualIncomeTM(date,map3.get(date)));
        }
        tblYearlyIncome.setItems(AnnualIncome);

        }

    private void setChartList(String recordType) {
        try {
            switch (recordType) {
                case "Daily": {
                    IncomeChart.setTitle("Daily Income Report");
                    IncomeChart.getXAxis().setLabel("Day");
                    setLineChartValues(irBO.getDailyIncome());
                    break;
                }
                case "Monthly": {
                    IncomeChart.setTitle("Monthly Income Report");
                    IncomeChart.getXAxis().setLabel("Month");
                    setLineChartValues(irBO.getMonthlyIncome());
                    break;
                }
                case "Annual":{
                    IncomeChart.setTitle("Annual Income Report");
                    IncomeChart.getXAxis().setLabel("Year");
                    setLineChartValues(irBO.getAnnualIncome());
                    break;
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setLineChartValues(LinkedHashMap<String, Double> dailyIncome) {
        IncomeChart.getData().clear();
        XYChart.Series series = new XYChart.Series();
        series.setName("Income");
        for (String date : dailyIncome.keySet()) {
            series.getData().add(new XYChart.Data(date + "", dailyIncome.get(date)));
        }
        IncomeChart.getData().add(series);
        IncomeChart.setAxisSortingPolicy(LineChart.SortingPolicy.X_AXIS);
    }


    public void ViewAnnualReportOnAction(ActionEvent actionEvent) {
        JasperReport compileReport = null;
        try {
            compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/lk/ijse/pos/reports/AnnualIncome.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void ViewMonthlyReportOnAction(ActionEvent actionEvent) {
        JasperReport compileReport = null;
        try {
            compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/lk/ijse/pos/reports/MonthlyIncome.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void ViewDailyReportOnAction(ActionEvent actionEvent) {
        JasperReport compileReport = null;
        try {
            compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/lk/ijse/pos/reports/DailyIncome.jasper"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, null, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
