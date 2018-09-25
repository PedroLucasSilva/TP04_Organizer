package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.ItemTag;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepItemTag;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepItemTagProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;

@Named(value = "itemMB")
@RequestScoped
public class ItemMB implements Serializable{
    
    private Item item;
    private ItemTag itemTag;
    @ManagedProperty(value="#loginMB")
    private LoginMB user;
    private IKeepItem keepItem;
    private IKeepTag keepTag;
    private IKeepItemTag keepItemTag;
    private String listTag;
    private String selectType;
    String idItemString;
    private ArrayList<Tag> tagItem;

    public ItemMB() throws SocketException, UnknownHostException {
        item = new Item();
        itemTag = new ItemTag();
        keepItem = new KeepItemProxy();
        keepTag = new KeepTagProxy();
        keepItemTag = new KeepItemTagProxy();
        listTag = "";
        selectType = "";
        idItemString = "";
        tagItem = new ArrayList();
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
    
    public void setSelectType(String selectType) {
        this.selectType = selectType;
    }

    public String getSelectType() {
        return selectType;
    }
    
    public void setIdItemString(String idItemString) {
        this.idItemString = idItemString;
    }
    
    public String getIdItemString() {
        return idItemString;
    }
    
    public boolean createItem() throws PersistenceException, BusinessException {
        
        boolean success;
        
        if (!listTag.isEmpty()) {
            insertTagInItem();
        }
        
        if (selectType.equals("TAR")) {
            item.setIdentifierStatus("A");
        } else {
            item.setIdentifierStatus(null);
        }
        
        success = keepItem.createItem(item);
        
        if (!listTag.isEmpty()) {
            Item itemWithId = keepItem.searchItemByName(item.getNameItem());
            
            itemTag.setItem(itemWithId);
            itemTag.setListTags(tagItem);
            success = keepItemTag.createTagInItem(itemTag);
        }
       
        return success;

    }
    
    public boolean updateItem() throws PersistenceException, BusinessException{
        
        boolean success = false;
        Long idItem = Long.parseLong(idItemString); 
        
        ArrayList<Tag> arrOldTags;
        
        if (!listTag.isEmpty()) {
            insertTagInItem();
        }
        
        arrOldTags = keepItemTag.listAllTagInItem(idItem);
        
        updateTagInItem(arrOldTags, idItem);
        
        success = keepItem.updateItem(item);
       
        return false;

    }
    
    public boolean deleteItem() throws PersistenceException{
        
        boolean success;        
        Long idItem = Long.parseLong(idItemString); 
       
        success = keepItem.deleteItem(idItem, user.getCurrentUser());
        
        if(success)
            success = keepItemTag.deleteTagByItemId(idItem);
       
        return success;

    }
    
    public void insertTagInItem() throws  PersistenceException, BusinessException{
        
        String[] vetTag = listTag.split(";");

        for (String vetTag1 : vetTag) {
            if(vetTag1.equals(" ")){
                    break;
            } else {
                if (keepTag.searchTagByName(vetTag1.trim(), user.getCurrentUser()) == null) {
                    System.out.println("Tag sem correspondencia");
                } else {
                    Tag tagOfUser = new Tag();

                    tagOfUser.setSeqTag(keepTag.searchTagByName(vetTag1.trim(), user.getCurrentUser()));
                    tagOfUser.setTagName(vetTag1.trim());
                    tagOfUser.setUser(user.getCurrentUser());

                    tagItem.add(tagOfUser);
                }
            }
        }
    }
    
    public void updateTagInItem(ArrayList<Tag> arrOldTags, Long idItem){
        
        boolean success;
        
        ArrayList<Tag> arrKeepTag = new ArrayList();
        ArrayList<Tag> arrDeleteTag = new ArrayList();
        ArrayList<Tag> arrNewTag = new ArrayList();
                
        if(arrOldTags == null && !tagItem.isEmpty()){            
            arrNewTag = tagItem;        
            
        } else if (arrOldTags != null && tagItem.isEmpty()){
            arrDeleteTag = arrOldTags;        
            
        } else if (arrOldTags != null && !tagItem.isEmpty()){      
            for(int i=0;i<tagItem.size();i++){
                for(int j=0;j<arrOldTags.size();j++){
                    if(tagItem.get(i).getTagName().contains(arrOldTags.get(j).getTagName())){
                        arrKeepTag.add(tagItem.get(i));
                        break;
                    }
                }
            }
            
            for(int i=0;i<tagItem.size();i++){
                for(int j=0;j<arrOldTags.size();j++){
                    if(!tagItem.get(i).getTagName().contains(arrOldTags.get(j).getTagName())){
                        arrNewTag.add(tagItem.get(i));
                        break;
                    }
                }
            }
            
            if(arrKeepTag.isEmpty()){
                arrDeleteTag = arrOldTags;            
            } else {
                for(int i=0;i<arrOldTags.size();i++){
                    for(int j=0;j<arrKeepTag.size();j++){
                        if(!(arrKeepTag.get(j).getTagName().contains(arrOldTags.get(i).getTagName()))){
                            arrDeleteTag.add(arrOldTags.get(i));
                            break;
                        }
                    }
                }
            }
        
        }
        
        if(!arrDeleteTag.isEmpty()){
            success = keepItemTag.deleteTagInItem(arrDeleteTag, idItem);
            
        } 
        
        if(!arrNewTag.isEmpty()){
            itemTag.setItem(item);
            itemTag.setListTags(arrNewTag);

            success = keepItemTag.createTagInItem(itemTag);
        }
    
    }   
}
    
