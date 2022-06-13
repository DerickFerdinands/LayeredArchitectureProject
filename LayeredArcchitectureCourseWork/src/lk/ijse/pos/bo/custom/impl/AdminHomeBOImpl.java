package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.AdminHomeBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.dao.custom.OrderDAO;

import java.sql.SQLException;

public class AdminHomeBOImpl implements AdminHomeBO {
    private OrderDAO oDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);
    private CustomerDAO cDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);
    private ItemDAO iDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);
    @Override
    public double getTotalOrderRevenue() throws SQLException, ClassNotFoundException {
        return oDAO.getRevenueUpToDate();
    }

    @Override
    public int getCustomerCount() throws SQLException, ClassNotFoundException {
        return cDAO.getCustomerCount();
    }

    @Override
    public int getItemCount() throws SQLException, ClassNotFoundException {
        return iDAO.getItemCount();
    }
}
