package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.ItemMovementBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.OrderDAO;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class ItemMovementBOImpl implements ItemMovementBO {
    private final OrderDAO oDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public LinkedHashMap<String, Integer> getTotalItemOrderQty() throws SQLException, ClassNotFoundException {
        return oDAO.getTotalItemOrderQty();
    }

    @Override
    public LinkedHashMap<String, Integer> getMostAndLeastMovedItem() throws SQLException, ClassNotFoundException {
        return oDAO.getMostAndLeastMovedItem();
    }
}
