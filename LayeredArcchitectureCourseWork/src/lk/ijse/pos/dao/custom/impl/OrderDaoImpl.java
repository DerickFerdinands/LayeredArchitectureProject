package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.entity.Orders;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class OrderDaoImpl implements OrderDAO {
    @Override
    public boolean save(Orders model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Orders VALUES (?,?,?,?)", model.getOrderId(), model.getOrderDate(), model.getCustId(), model.getTotal());
    }

    @Override
    public boolean update(Orders model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Orders SET OrderDate=?,CustId=?,Total=? WHERE OrderId=?", model.getOrderDate(), model.getCustId(), model.getTotal(), model.getOrderId());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Orders WHERE OrderId =?", s);
    }

    @Override
    public ArrayList<Orders> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Orders> orderList = new ArrayList<>();
        ResultSet result = CrudUtil.execute("SELECT * FROM Orders");
        while (result.next()) {
            orderList.add(new Orders(result.getString(1), LocalDate.parse(result.getDate(2) + ""), result.getString(3), result.getDouble(4)));
        }
        return orderList;
    }

    @Override
    public ArrayList<Orders> getMatchingResults(String search) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Orders WHERE OrderId LIKE ? OR OrderDate LIKE? OR CustId LIKE ? OR Total LIKE ?", search, search, search, search);
        ArrayList<Orders> orderList = new ArrayList<>();
        while (result.next()) {
            orderList.add(new Orders(result.getString(1), LocalDate.parse(result.getDate(2) + ""), result.getString(3), result.getDouble(4)));
        }
        return orderList;
    }

    @Override
    public Orders get(String s) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Orders WHERE OrderId =?", s);
        if (result.next())
            return new Orders(result.getString(1), LocalDate.parse(result.getDate(2) + ""), result.getString(3), result.getDouble(4));
        return null;
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT OrderID FROM Orders ORDER BY OrderID DESC LIMIT 1");
        return result.next() ? String.format("O-%02d", (Integer.parseInt(result.getString(1).replace("O-", "")) + 1)) : "O-01";
    }

    @Override
    public ArrayList<Orders> searchOrderByCustomer(String OrderID) throws SQLException, ClassNotFoundException {

            ResultSet result = CrudUtil.execute("SELECT * FROM Orders WHERE CustId =?", OrderID);
            ArrayList<Orders> orderList = new ArrayList<>();
            while (result.next()) {
                orderList.add(new Orders(result.getString(1), LocalDate.parse(result.getDate(2) + ""), result.getString(3), result.getDouble(4)));
            }
            return orderList;

    }


    @Override
    public LinkedHashMap<String, Double> getDailyIncome() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT OrderDate, SUM(Total) AS 'Sales' FROM Orders GROUP BY OrderDate");
        LinkedHashMap<String, Double> DailyIncomeResults = new LinkedHashMap<>();
        while (result.next()) {
            DailyIncomeResults.put(result.getDate(1) + "", result.getDouble(2));
        }
        return DailyIncomeResults;
    }

    @Override
    public LinkedHashMap<String, Double> getMonthlyIncome() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT YEAR(OrderDate), MONTH(OrderDate), SUM(Total) AS Income\n" +
                "FROM Orders\n" +
                "GROUP BY YEAR(OrderDate), MONTH(OrderDate)\n" +
                "ORDER BY YEAR(OrderDate), MONTH(OrderDate)");
        LinkedHashMap<String, Double> DailyIncomeResults = new LinkedHashMap<>();
        while (result.next()) {
            DailyIncomeResults.put(result.getString(1) + "-" + result.getString(2), result.getDouble(3));
        }
        return DailyIncomeResults;
    }

    @Override
    public LinkedHashMap<String, Double> getAnnualIncome() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT YEAR(OrderDate), SUM(Total) AS 'Sales' FROM Orders GROUP BY YEAR(OrderDate)");
        LinkedHashMap<String, Double> DailyIncomeResults = new LinkedHashMap<>();
        while (result.next()) {
            DailyIncomeResults.put(result.getLong(1) + "", result.getDouble(2));
        }
        return DailyIncomeResults;
    }

    @Override
    public LinkedHashMap<String, Integer> getTotalItemOrderQty() throws SQLException, ClassNotFoundException {
        LinkedHashMap<String, Integer> ItemQtySold = new LinkedHashMap<>();
        ResultSet result = CrudUtil.execute("SELECT ItemCode, SUM(OrderQty) AS TotalQuantity FROM OrderDetail GROUP BY ItemCode");
        while (result.next()) {
            ItemQtySold.put(result.getString(1), result.getInt(2));
        }
        return ItemQtySold;
    }

    @Override
    public LinkedHashMap<String, Integer> getMostAndLeastMovedItem() throws SQLException, ClassNotFoundException {
        LinkedHashMap<String, Integer> MostAndLeastSold = new LinkedHashMap<>();
        ResultSet result = CrudUtil.execute("SELECT ItemCode, SUM(OrderQty) AS TotalQuantity FROM OrderDetail GROUP BY ItemCode ORDER BY SUM(OrderQty) DESC LIMIT 1");
        if (result.next()) MostAndLeastSold.put(result.getString(1), result.getInt(2));
        result = CrudUtil.execute("SELECT ItemCode, SUM(OrderQty) AS TotalQuantity FROM OrderDetail GROUP BY ItemCode ORDER BY SUM(OrderQty) ASC LIMIT 1");
        if (result.next()) MostAndLeastSold.put(result.getString(1), result.getInt(2));
        return MostAndLeastSold;
    }

    @Override
    public String getLastOrderID() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT OrderID FROM Orders ORDER BY OrderID DESC LIMIT 1");
        return result.next() ? result.getString(1) : null;
    }

    @Override
    public double getRevenueUpToDate() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT SUM(Total) FROM Orders");
        return result.next()?result.getDouble(1):0;
    }
}
