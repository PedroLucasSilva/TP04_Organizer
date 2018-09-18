/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.organizer.model.service.impl;

import br.cefetmg.inf.organizer.model.dao.IItemTagDAO;
import br.cefetmg.inf.organizer.model.dao.impl.ItemTagDAO;
import br.cefetmg.inf.organizer.model.domain.ItemTag;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.service.IKeepItemTag;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aline
 */
public class KeepItemTag implements IKeepItemTag {
    
    private final IItemTagDAO itemTagDAO = new ItemTagDAO();

    @Override
    public boolean createTagInItem(ItemTag itemTag) {
        
        if((itemTag.getItem().getSeqItem() == null)){
            //exceção
        }
        
        if((itemTag.getListTags() == null) || (itemTag.getListTags().isEmpty())){
            //exceção
        }
        
        boolean result=false;
        try {
            result = itemTagDAO.createTagInItem(itemTag);
        } catch (PersistenceException ex) {
            Logger.getLogger(KeepItemTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }

    @Override
    public boolean deleteTagInItem(ArrayList<Tag> itemTag, Long id) {
        
        boolean result=false;
        try {
            result = itemTagDAO.deleteTagInItem(itemTag, id);
        } catch (PersistenceException ex) {
            Logger.getLogger(KeepItemTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }

    @Override
    public ArrayList<Tag> listAllTagInItem(Long seqItem){
        
        ArrayList<Tag> result=null;
        try {
            result = itemTagDAO.listAllTagInItem(seqItem);
        } catch (PersistenceException ex) {
            Logger.getLogger(KeepItemTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }

    @Override
    public boolean deleteTagByItemId(Long idItem) {
        
        boolean result=false;
        try {
            result = itemTagDAO.deleteTagByItemId(idItem);
        } catch (PersistenceException ex) {
            Logger.getLogger(KeepItemTag.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
        
    }
    
}
