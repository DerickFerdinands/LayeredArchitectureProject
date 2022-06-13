package lk.ijse.pos.bo.custom.impl;

import lk.ijse.pos.DTO.ItemDTO;
import lk.ijse.pos.bo.custom.ItemBO;
import lk.ijse.pos.dao.DAOFactory;
import lk.ijse.pos.dao.custom.ItemDAO;
import lk.ijse.pos.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {
    private final ItemDAO iDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOTypes.ITEM);

    @Override
    public boolean saveItem(ItemDTO i) throws SQLException, ClassNotFoundException {
        return iDAO.save(new Item(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation()));
    }

    @Override
    public boolean updateItem(ItemDTO i) throws SQLException, ClassNotFoundException {
        return iDAO.update(new Item(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation()));
    }

    @Override
    public ArrayList<ItemDTO> getMatchingItems(String s) throws SQLException, ClassNotFoundException {
        ArrayList<Item> matchingResults = iDAO.getMatchingResults(s);
        ArrayList<ItemDTO> results = new ArrayList<>();
        for(Item i : matchingResults){
            results.add(new ItemDTO(i.getItemCode(),i.getDescription(),i.getPackSize(), i.getUnitPrice(),i.getQtyOnHand(), i.getImageLocation()));
        }
        return results;
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

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return iDAO.delete(id);
    }
}
