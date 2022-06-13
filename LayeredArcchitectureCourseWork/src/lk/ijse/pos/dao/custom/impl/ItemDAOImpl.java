package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.entity.Item;
import lk.ijse.pos.util.CrudUtil;
import lk.ijse.pos.dao.custom.ItemDAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public boolean save(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?,?,?)", item.getItemCode(), item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getImageLocation());
    }


    @Override
    public boolean update(Item item) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET Description=?,PackSize=?,UnitPrice=?,QtyOnHand=?,ImageLocation=? WHERE ItemCode=?", item.getDescription(), item.getPackSize(), item.getUnitPrice(), item.getQtyOnHand(), item.getImageLocation(), item.getItemCode());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Item WHERE ItemCode=?", s);
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<Item> ItemList = new ArrayList<>();
        while (result.next()) {
            ItemList.add(new Item(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), result.getInt(5), result.getString(6)));
        }
        return ItemList;
    }

    @Override
    public ArrayList<Item> getMatchingResults(String search) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE ItemCode LIKE ? OR Description LIKE? OR PackSize LIKE ? OR UnitPrice LIKE ? OR QtyOnHand LIKE ? OR ImageLocation LIKE ? ", search, search, search, search, search, search);
        ArrayList<Item> ItemList = new ArrayList<Item>();
        while (result.next()) {
            ItemList.add(new Item(result.getString(1), result.getString(2), result.getString(3), result.getDouble(4), result.getInt(5), result.getString(6)));
        }
        return ItemList;
    }

    @Override
    public Item get(String s) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE ItemCode =?",s);
        return result.next() ? new Item(result.getString(1),result.getString(2),result.getString(3),result.getDouble(4),result.getInt(5),result.getString(6)):null;
    }

    @Override
    public ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT ItemCode FROM Item");
        ArrayList<String> ItemCodeList = new ArrayList<>();
        while(result.next()) {
            ItemCodeList.add(result.getString(1));
        }
        return ItemCodeList;
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(ItemCode) FROM Item");
        return result.next() ? result.getInt(1) :0;
    }
}
