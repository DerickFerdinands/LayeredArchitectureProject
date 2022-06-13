package lk.ijse.pos.bo.custom;

import lk.ijse.pos.DTO.CustomDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.bo.SuperBO;
import javafx.collections.ObservableList;
import lk.ijse.pos.view.tm.CartTM;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ManageOrderBO extends SuperBO {

    ArrayList<OrderDTO> getMatchingOrderResults(String s) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException;

    ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException;

    String getCustomerName(String newValue) throws SQLException, ClassNotFoundException;

    ArrayList<OrderDTO> searchOrderByCustomer(String newValue) throws SQLException, ClassNotFoundException;

    ItemDTO getItem(String itemCode) throws SQLException, ClassNotFoundException;

    ArrayList<CustomDTO> SearchOrderWithItemProperties(String orderId) throws SQLException, ClassNotFoundException;

    boolean cancelOrder(String orderID) throws SQLException, ClassNotFoundException;

    boolean editOrder(OrderDTO orderDTO, ObservableList<CartTM> deletedList)throws SQLException, ClassNotFoundException;
}
