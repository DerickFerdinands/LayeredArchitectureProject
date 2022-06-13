package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.bo.custom.IncomeReportsBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.OrderDAO;

import java.sql.SQLException;
import java.util.LinkedHashMap;

public class IncomeReportsBOImpl implements IncomeReportsBO {
    private final OrderDAO oDAO = (OrderDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ORDER);

    @Override
    public LinkedHashMap<String, Double> getDailyIncome() throws SQLException, ClassNotFoundException {
        return oDAO.getDailyIncome();
    }

    @Override
    public LinkedHashMap<String, Double> getMonthlyIncome() throws SQLException, ClassNotFoundException {
        return oDAO.getMonthlyIncome();
    }

    @Override
    public LinkedHashMap<String, Double> getAnnualIncome() throws SQLException, ClassNotFoundException {
        return oDAO.getAnnualIncome();
    }
}
