package br.cefetmg.inf.organizer.model.service.impl;

import br.cefetmg.inf.organizer.model.dao.IMaxDAO;
import br.cefetmg.inf.organizer.model.dao.impl.MaxDAO;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;



public class KeepMaxData implements IKeepMaxData {

    private final IMaxDAO maxDAO;
    
    public KeepMaxData() {
        maxDAO = new MaxDAO();
    }
    
    //Não existem regras de negócio pois essas funções servem para alterar o BD real baseado no BD simulado do assistente MAX. Em caso de dúvidas contate os membros do grupo;

    @Override
    public ArrayList<Item> loadItems(User user) throws PersistenceException {
        return maxDAO.loadItems(user);
    }

    @Override
    public ArrayList<Tag> loadTags(User user) throws PersistenceException {
        return maxDAO.loadTags(user);
    }

    @Override
    public ArrayList<String> loadTagsItems(User user) throws PersistenceException {
        return maxDAO.loadTagsItems(user);
    }

    @Override
    public ArrayList<String> loadItemsTags(User user) throws PersistenceException {
        return maxDAO.loadItemsTags(user);
    }
    
        @Override
    public boolean updateAllItems(MaxDataObject maxDataObject) throws PersistenceException {    
        return maxDAO.updateAllItems(maxDataObject); 
    }

    @Override
    public boolean updateAllTags(MaxDataObject maxDataObject) throws PersistenceException {        
        return maxDAO.updateAllTags(maxDataObject);
    }

    @Override
    public boolean updateAllItemTag(MaxDataObject maxDataObject) throws PersistenceException {
        return maxDAO.updateAllItemTag(maxDataObject);
    }
   
}
