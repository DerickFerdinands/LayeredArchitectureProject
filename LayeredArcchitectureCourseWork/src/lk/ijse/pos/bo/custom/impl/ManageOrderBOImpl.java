package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.DTO.CustomDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.DTO.OrderDetailDTO;
import lk.ijse.pos.bo.custom.ManageOrderBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.*;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.Custom;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.OrderDetail;
import lk.ijse.pos.entity.Orders;
import javafx.collections.ObservableList;
import lk.ijse.pos.view.tm.CartTM;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManageOrderBOImpl implements ManageOrderBO {
    private final OrderDAO oDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO odDAO = (OrderDetailDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);
    private final QueryDAO qDAO = (QueryDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.QUERY);
    private final CustomerDAO cDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO iDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public ArrayList<OrderDTO> getMatchingOrderResults(String s) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> matchingResults = oDAO.getMatchingResults(s);
        ArrayList<OrderDTO> results = new ArrayList<>();
        for(Orders o: matchingResults){
            results.add(new OrderDTO(o.getOrderId(),o.getOrderDate(),o.getCustId(),o.getTotal()));
        }
        return results;
    }

    @Override
    public ArrayList<OrderDTO> getAllOrders() throws SQLException, ClassNotFoundException {
        ArrayList<Orders> all = oDAO.getAll();
        ArrayList<OrderDTO> results = new ArrayList<>();
        for(Orders o: all){
            results.add(new OrderDTO(o.getOrderId(),o.getOrderDate(),o.getCustId(),o.getTotal()));
        }
        return results;
    }

    @Override
    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        return cDAO.getAllCustomerIds();
    }

    @Override
    public String getCustomerName(String id) throws SQLException, ClassNotFoundException {
        return cDAO.getCustomerName(id);
    }

    @Override
    public ArrayList<OrderDTO> searchOrderByCustomer(String id) throws SQLException, ClassNotFoundException {
        ArrayList<Orders> orders = oDAO.searchOrderByCustomer(id);
        ArrayList<OrderDTO> results = new ArrayList<>();
        for(Orders o: orders){
            results.add(new OrderDTO(o.getOrderId(),o.getOrderDate(),o.getCustId(),o.getTotal()));
        }
        return results;
    }

    @Override
    public ItemDTO getItem(String itemCode) throws SQLException, ClassNotFoundException {
        Item i = iDAO.get(itemCode);
        return new ItemDTO(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation());
    }

    @Override
    public ArrayList<CustomDTO> SearchOrderWithItemProperties(String orderId) throws SQLException, ClassNotFoundException {
        ArrayList<Custom> customs = qDAO.SearchOrderWithItemProperties(orderId);
        ArrayList<CustomDTO> customsDTO = new ArrayList<>();
        for(Custom c : customs){
            customsDTO.add(new CustomDTO(c.getItemCode(),c.getDescription(),c.getUnitPrice(),c.getQtyOnHand(), c.getOrderId(),c.getOrderQty(),c.getDiscount(),c.getTotal()));
        }
        return customsDTO;
    }

    @Override
    public boolean cancelOrder(String orderID) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        for (OrderDetail detail : odDAO.getMatchingResults(orderID)) {
            Item dto = iDAO.get(detail.getItemCode());
            dto.setQtyOnHand(dto.getQtyOnHand() + detail.getOrderQty());
            if (!iDAO.update(dto)) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }

        if (!odDAO.delete(orderID)) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        if (!oDAO.delete(orderID)) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        return true;
    }

    @Override
    public boolean editOrder(OrderDTO orderDTO, ObservableList<CartTM> deletedList) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        for (CartTM tm : deletedList) {
            Item dto = iDAO.get(tm.getItemCode());
            dto.setQtyOnHand(dto.getQtyOnHand() + tm.getQty());
            if ((!iDAO.update(dto)) || (!odDAO.deleteSpecific(orderDTO.getOrderId(), dto.getItemCode()))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }

        for (OrderDetailDTO dt : orderDTO.getList()) {
            OrderDetail odDTO = odDAO.getSpecific(dt.getOrderId(), dt.getItemCode());
            Item dto = iDAO.get(dt.getItemCode());

            if (odDTO.getOrderQty() < dt.getOrderQty()) {

                int qtyTobeAdded = dt.getOrderQty() - odDTO.getOrderQty();
                odDTO.setOrderQty(odDTO.getOrderQty() + qtyTobeAdded);
                dto.setQtyOnHand(dto.getQtyOnHand() - qtyTobeAdded);

            } else if (odDTO.getOrderQty() > dt.getOrderQty()) {

                int qtyTobeDeducted = odDTO.getOrderQty() - dt.getOrderQty();
                odDTO.setOrderQty(odDTO.getOrderQty() - qtyTobeDeducted);
                dto.setQtyOnHand(dto.getQtyOnHand() + qtyTobeDeducted);

            }

            if ((!iDAO.update(dto)) || (!odDAO.update(odDTO))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        if (!oDAO.update(new Orders(orderDTO.getOrderId(),orderDTO.getOrderDate(),orderDTO.getCustId(),orderDTO.getTotal()))) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        return true;
    }

}
