package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.DTO.CustomerDTO;
import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.DTO.OrderDTO;
import lk.ijse.pos.DTO.OrderDetailDTO;
import lk.ijse.pos.bo.custom.UserHomeBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.OrderDAO;
import lk.ijse.pos.dao.custom.OrderDetailDAO;
import lk.ijse.pos.db.DBConnection;
import lk.ijse.pos.entity.Customer;
import lk.ijse.pos.entity.Item;
import lk.ijse.pos.entity.OrderDetail;
import lk.ijse.pos.entity.Orders;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserHomeBOImpl implements UserHomeBO {
    private final CustomerDAO cDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private final ItemDAO iDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);
    private final OrderDAO oDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    private final OrderDetailDAO odDAO = (OrderDetailDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER_DETAIL);

    @Override
    public ItemDTO getItem(String code) throws SQLException, ClassNotFoundException {
        Item i = iDAO.get(code);
        return new ItemDTO(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation());
    }

    @Override
    public CustomerDTO getCustomer(String id) throws SQLException, ClassNotFoundException {
        Customer c = cDAO.get(id);
        return new CustomerDTO(c.getCustID(),c.getCustTitle(),c.getCustName(),c.getCustAddress(),c.getCity(),c.getProvince(),c.getPostalCode());
    }

    @Override
    public ArrayList<String> getAllCustomerIds() throws SQLException, ClassNotFoundException {
        return cDAO.getAllCustomerIds();
    }

    @Override
    public ArrayList<String> getAllItemIds() throws SQLException, ClassNotFoundException {
        return iDAO.getAllItemIds();
    }

    @Override
    public String generateNewOrderId() throws SQLException, ClassNotFoundException {
        return oDAO.generateNewOrderId();
    }

    @Override
    public boolean placeOrder(OrderDTO orderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBConnection.getInstance().getConnection();
        connection.setAutoCommit(false);

        if (!oDAO.save(new Orders(orderDTO.getOrderId(),orderDTO.getOrderDate(),orderDTO.getCustId(),orderDTO.getTotal()))) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }

        for (OrderDetailDTO dto : orderDTO.getList()) {
            if (!odDAO.save(new OrderDetail(dto.getOrderId(),dto.getItemCode(),dto.getOrderQty(),dto.getDiscunt()))) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
            Item itemDTO = iDAO.get(dto.getItemCode());
            itemDTO.setQtyOnHand(itemDTO.getQtyOnHand() - dto.getOrderQty());
            if (!iDAO.update(itemDTO)) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        return true;

    }

    @Override
    public ArrayList<ItemDTO> getAllItems() throws SQLException, ClassNotFoundException {
        ArrayList<Item> all = iDAO.getAll();
        ArrayList<ItemDTO> results = new ArrayList<>();
        for(Item i : all){
            results.add(new ItemDTO(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation()));
        }
        return results;
    }
}
