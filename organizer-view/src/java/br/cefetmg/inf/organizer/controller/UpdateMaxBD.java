package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.organizer.proxy.KeepMaxDataProxy;
import br.cefetmg.inf.util.exception.PersistenceException;
import com.google.gson.Gson;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class UpdateMaxBD{
    private final User user;
    private final Gson gson;

    private String[] itemsID;
    private String[] itemsType;
    private String[] itemsName;
    private String[] itemsDescription;
    private String[] itemsDate;
    private String[] itemsStatus;

    private String[] tagsID;
    private String[] tagsName;

    private String[] tagsItems;
    private String[] itemsTags;

    public UpdateMaxBD(){
        user = new User();
        user.setCodEmail("1");
        user.setUserPassword("1");
        gson = new Gson();
    }

    public void setItemsID(String itemsID){
        this.itemsID = gson.fromJson( itemsID, String[].class );
    }

    public void setItemsType(String itemsType){
        this.itemsType = gson.fromJson( itemsType, String[].class );
    }

    public void setItemsName(String itemsName){
        this.itemsName = gson.fromJson( itemsName, String[].class );
    }

    public void setItemsDescription(String itemsDescription){
        this.itemsDescription = gson.fromJson( itemsDescription, String[].class );
    }

    public void setItemsDate(String itemsDate){
        this.itemsDate = gson.fromJson( itemsDate, String[].class );
    }

    public void setItemsStatus(String itemsStatus){
        this.itemsStatus = gson.fromJson( itemsStatus, String[].class );
    }

    public void setTagsID(String tagsID){
        this.tagsID = gson.fromJson( tagsID, String[].class );
    }

    public void setTagsName(String tagsName){
        this.tagsName = gson.fromJson( tagsName, String[].class );
    }

    public void setTagsItems(String tagsItems){
        this.tagsItems = gson.fromJson( tagsItems, String[].class );
    }

    public void setItemsTags(String itemsTags){
        this.itemsTags = gson.fromJson( itemsTags, String[].class );
    }

    public String[] getItemsID(){
        return itemsID;
    }

    public String[] getItemsType(){
        return itemsType;
    }

    public String[] getItemsName(){
        return itemsName;
    }

    public String[] getItemsDescription(){
        return itemsDescription;
    }

    public String[] getItemsDate(){
        return itemsDate;
    }

    public String[] getItemsStatus(){
        return itemsStatus;
    }

    public String[] getTagsID(){
        return tagsID;
    }

    public String[] getTagsName(){
        return tagsName;
    }

    public String[] getTagsItems(){
        return tagsItems;
    }

    public String[] getItemsTags(){
        return itemsTags;
    }
    public void update() throws SocketException, UnknownHostException, PersistenceException{
        MaxDataObject maxDataObject = new MaxDataObject();

        maxDataObject.setUser(user);
        maxDataObject.setItemsID(itemsID);
        maxDataObject.setItemsType(itemsType);
        maxDataObject.setItemsName(itemsName);
        maxDataObject.setItemsDescription(itemsDescription);
        maxDataObject.setItemsDate(itemsDate);
        maxDataObject.getItemsStatus();

        maxDataObject.setTagsID(tagsID);
        maxDataObject.setTagsName(tagsName);

        maxDataObject.setTagsItems(tagsItems);
        maxDataObject.setItemsTags(itemsTags);

        IKeepMaxData keepMaxData = new KeepMaxDataProxy();

        keepMaxData.updateAllItemTag(maxDataObject);//PARAMETROS
        keepMaxData.updateAllItems(maxDataObject);//PARAMETROS
        keepMaxData.updateAllTags(maxDataObject);//PARAMETROS

    }

}
