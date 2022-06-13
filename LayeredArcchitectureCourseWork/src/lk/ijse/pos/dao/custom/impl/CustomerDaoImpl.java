package lk.ijse.pos.dao.custom.impl;

import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDaoImpl implements CustomerDAO {

    @Override
    public boolean save(Customer model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?,?,?,?)",
                model.getCustID(), model.getCustTitle(), model.getCustName(), model.getCustAddress(), model.getCity(), model.getProvince(), model.getPostalCode());
    }

    @Override
    public boolean update(Customer model) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Customer SET CustTitle=?,CustName=?,CustAddress=?,City=?,Province=?,PostalCode=? WHERE CustID=?",
                model.getCustTitle(), model.getCustName(), model.getCustAddress(), model.getCity(), model.getProvince(), model.getPostalCode(), model.getCustID());
    }

    @Override
    public boolean delete(String s) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("DELETE FROM Customer WHERE CustID=?", s);
    }

    @Override
    public ArrayList<Customer> getAll() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");
        ArrayList<Customer> cusList = new ArrayList();
        while (result.next()) {
            cusList.add(new Customer(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7)));
        }
        return cusList;
    }

    @Override
    public ArrayList<Customer> getMatchingResults(String search) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE CustId LIKE ? OR CustTitle LIKE? OR CustName LIKE ? OR CustAddress LIKE ? OR City LIKE ? OR Province LIKE ? OR PostalCode LIKE ?", search, search,search,search,search,search,search);
        ArrayList<Customer> cusList = new ArrayList();
        while (result.next()) {
            cusList.add(new Customer(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5), result.getString(6), result.getString(7)));
        }
        return cusList;
    }

    @Override
    public Customer get(String s) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE CustID =?",s);
        return result.next()?new Customer(result.getString(1),result.getString(2),result.getString(3),result.getString(4),result.getString(5),result.getString(6),result.getString(7)):null;
    }

    @Override
    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT CustID FROM Customer");
        ArrayList<String> customerIDS = new ArrayList<>();
        while(result.next()){
            customerIDS.add(result.getString(1));
        }
        return customerIDS;
    }

    @Override
    public String getCustomerName(String CustId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT CustName FROM Customer WHERE CustId =?",CustId);
        return result.next()? result.getString(1):null;
    }

    @Override
    public String generateNextId() throws SQLException, ClassNotFoundException {
        ResultSet result =CrudUtil.execute("SELECT CustId FROM Customer Order BY CustID DESC LIMIT 1");
        return result.next()? String.format("C%03d",Integer.valueOf(result.getString(1).replace("C",""))+1):"C001";
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT COUNT(CustId) FROM Customer");
        return result.next()? result.getInt(1):0;
    }
}
