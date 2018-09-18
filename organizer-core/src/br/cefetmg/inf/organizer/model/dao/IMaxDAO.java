package br.cefetmg.inf.organizer.model.dao;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;


public interface IMaxDAO {
    boolean updateAllItems(MaxDataObject maxDataObject) throws PersistenceException;
    boolean updateAllTags(MaxDataObject maxDataObject) throws PersistenceException;
    boolean updateAllItemTag(MaxDataObject maxDataObject) throws PersistenceException;
    ArrayList<Item> loadItems(User user) throws PersistenceException;
    ArrayList<Tag> loadTags(User user) throws PersistenceException;
    ArrayList<String> loadTagsItems(User user) throws PersistenceException;
    ArrayList<String> loadItemsTags(User user) throws PersistenceException;
}
