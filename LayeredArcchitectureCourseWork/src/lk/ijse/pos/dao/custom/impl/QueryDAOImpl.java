package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.QueryDAO;
import lk.ijse.pos.entity.Custom;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public ArrayList<Custom> SearchOrderWithItemProperties(String OrderID) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT OrderDetail.OrderID, OrderDetail.ItemCode, Item.Description,Item.UnitPrice,OrderDetail.OrderQty,(Item.UnitPrice*OrderDetail.OrderQty),OrderDetail.Discount,Item.QtyOnHand  FROM OrderDetail JOIN Item  on OrderDetail.ItemCode = Item.ItemCode WHERE OrderDetail.OrderID = ?",OrderID);
        ArrayList<Custom> OrderDetailList = new ArrayList<>();
        while(result.next()){
            OrderDetailList.add(new Custom(result.getString(2),result.getString(3),result.getDouble(4),result.getInt("QtyOnHand"),result.getString(1),result.getInt(5),result.getDouble(6),result.getDouble(7)));
        }
        return OrderDetailList;
    }


}
