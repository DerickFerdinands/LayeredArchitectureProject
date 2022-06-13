package lk.ijse.pos.bo.custom;

import lk.ijse.pos.bo.SuperBO;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public interface IncomeReportsBO extends SuperBO {
    LinkedHashMap<String, Double> getDailyIncome() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Double> getMonthlyIncome() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Double> getAnnualIncome() throws SQLException, ClassNotFoundException;
}
