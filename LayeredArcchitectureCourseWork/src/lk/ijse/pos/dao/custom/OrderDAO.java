package lk.ijse.pos.dao.custom;

import lk.ijse.pos.dao.CrudDAO;
import lk.ijse.pos.entity.Orders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface OrderDAO extends CrudDAO<Orders, String> {
    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    ArrayList<Orders> searchOrderByCustomer(String OrderID) throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Double> getDailyIncome() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Double> getMonthlyIncome() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Double> getAnnualIncome() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Integer> getTotalItemOrderQty() throws SQLException, ClassNotFoundException;

    LinkedHashMap<String, Integer> getMostAndLeastMovedItem() throws SQLException, ClassNotFoundException;

    String getLastOrderID() throws SQLException, ClassNotFoundException;

    double getRevenueUpToDate() throws SQLException, ClassNotFoundException;
}
