package lk.ijse.pos.bo.custom;

import lk.ijse.pos.DTO.CustomerDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.bo.SuperBO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserHomeBO extends SuperBO {
    ItemDTO getItem(String newValue) throws SQLException, ClassNotFoundException;

    CustomerDTO getCustomer(String newValue) throws SQLException, ClassNotFoundException;

    ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException;

    String generateNewOrderId() throws SQLException, ClassNotFoundException;

    boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException;

    ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException;

}
