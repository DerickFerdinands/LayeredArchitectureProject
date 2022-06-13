package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.DTO.CustomerDTO;
import lk.ijse.pos.bo.custom.CustomerBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.CustomerDAO;
import lk.ijse.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {
    private final CustomerDAO cDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDTO c) throws SQLException, ClassNotFoundException {
        return cDAO.save(new Customer(c.getId(), c.getTitle(), c.getName(), c.getAddress(), c.getCity(), c.getProvince(), c.getPostalCode()));
    }

    @Override
    public boolean updateCustomer(CustomerDTO c) throws SQLException, ClassNotFoundException {
        return cDAO.update(new Customer(c.getId(), c.getTitle(), c.getName(), c.getAddress(), c.getCity(), c.getProvince(), c.getPostalCode()));
    }

    @Override
    public ArrayList<CustomerDTO> getMatchingCustomers(String search) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> matchingResults = cDAO.getMatchingResults("%" + search + "%");
        ArrayList<CustomerDTO> results = new ArrayList<>();
        for (Customer c : matchingResults) {
            results.add(new CustomerDTO(c.getCustID(), c.getCustTitle(), c.getCustName(), c.getCustAddress(), c.getCity(), c.getProvince(), c.getPostalCode()));
        }
        return results;
    }

    @Override
    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException, ClassNotFoundException {
        ArrayList<Customer> all = cDAO.getAll();
        ArrayList<CustomerDTO> results = new ArrayList<>();
        for (Customer c : all) {
            results.add(new CustomerDTO(c.getCustID(), c.getCustTitle(), c.getCustName(), c.getCustAddress(), c.getCity(), c.getProvince(), c.getPostalCode()));
        }
        return results;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return cDAO.delete(id);
    }

    @Override
    public String generateCustomerId() throws SQLException, ClassNotFoundException {
        return cDAO.generateNextId();
    }
}
