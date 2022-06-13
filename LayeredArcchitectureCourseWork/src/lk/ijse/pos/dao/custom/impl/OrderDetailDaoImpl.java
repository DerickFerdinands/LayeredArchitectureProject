package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.OrderDetailDAO;
import lk.ijse.pos.entity.OrderDetail;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDetailDaoImpl implements OrderDetailDAO {
    @Override
    public boolean save(OrderDetail model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO OrderDetail VALUES (?,?,?,?)", model.getOrderId(), model.getItemCode(), model.getOrderQty(), model.getDiscount());
    }

    @Override
    public boolean update(OrderDetail model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE OrderDetail SET OrderQty=?,Discount=? WHERE OrderId=?", model.getOrderQty(), model.getDiscount(), model.getOrderId());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Delete FROM OrderDetail WHERE OrderId =?", s);
    }

    @Override
    public ArrayList<OrderDetail> getAll() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM OrderDetail");
        ArrayList<OrderDetail> OrderDetailList = new ArrayList<>();
        while (result.next()) {
            OrderDetailList.add(new OrderDetail(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)));
        }
        return OrderDetailList;
    }

    @Override
    public ArrayList<OrderDetail> getMatchingResults(String search) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM OrderDetail WHERE OrderId LIKE ? OR ItemCode LIKE? OR OrderQty LIKE ? OR Discount LIKE ?",search,search,search,search);
        ArrayList<OrderDetail> OrderDetailList = new ArrayList<>();
        while (result.next()) {
            OrderDetailList.add(new OrderDetail(result.getString(1), result.getString(2) , result.getInt(3), result.getDouble(4)));
        }
        return OrderDetailList;
    }

    @Override
    public OrderDetail get(String s) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM OrderDetail WHERE OrderId=?",s);
        return result.next()?new OrderDetail(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)):null;
    }

    @Override
    public OrderDetail getSpecific(String OrderID, String ItemCode) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM OrderDetail WHERE OrderId=? AND ItemCode=?",OrderID,ItemCode);
        return result.next()?new OrderDetail(result.getString(1), result.getString(2), result.getInt(3), result.getDouble(4)):null;
    }

    @Override
    public boolean deleteSpecific(String OrderID, String ItemCode) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("Delete FROM OrderDetail WHERE OrderId =? AND ItemCode=?", OrderID,ItemCode);
    }
}
