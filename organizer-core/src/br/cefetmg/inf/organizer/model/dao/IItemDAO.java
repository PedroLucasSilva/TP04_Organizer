/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.organizer.model.dao;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.util.exception.PersistenceException;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aline
 */
public interface IItemDAO {
    
    boolean createItem (Item item) throws PersistenceException;
    boolean updateItem(Item item)throws PersistenceException;
    boolean deleteItem(Long idItem, User user) throws PersistenceException;
    ArrayList<Item> listAllItem(User user) throws PersistenceException;
    boolean checkIfItemAlreadyExistsToCreate(Item item) throws PersistenceException;
    boolean checkIfItemAlreadyExistsToUpdate(Item item) throws PersistenceException;
    Item searchItemById(Long idItem) throws PersistenceException;
    Item searchItemByName(String nomeItem) throws PersistenceException;
    ArrayList<Item> searchItemByTag(List<Tag> tagList, User user) throws PersistenceException;
    ArrayList<Item> searchItemByType(List<String> typeList, User user) throws PersistenceException;
    ArrayList<Item> searchItemByTagAndType(List<Tag> tagList, List<String> typeList, User user) 
            throws PersistenceException;
    
}
