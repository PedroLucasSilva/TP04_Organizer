package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.organizer.proxy.KeepMaxDataProxy;
import br.cefetmg.inf.util.exception.PersistenceException;
import com.google.gson.Gson;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class LoadMax{
    private String items;
    private String tags;
    private String tagsItems;
    private String itemsTags;

    public LoadMax() throws SocketException, UnknownHostException, PersistenceException{
        User user = new User();
        user.setCodEmail("1");
        user.setUserName("a");
        user.setUserPassword("1");
        Gson gson = new Gson();
        IKeepMaxData keepMaxData = new KeepMaxDataProxy();      
        
        items = gson.toJson(keepMaxData.loadItems(user));
        tags = gson.toJson(keepMaxData.loadTags(user));
        tagsItems = gson.toJson(keepMaxData.loadTagsItems(user));
        itemsTags = gson.toJson(keepMaxData.loadItemsTags(user));
    }

    public String getItems(){
        return items;
    }

    public String getTags(){
        return tags;
    }

    public String getTagsItems(){
        return tagsItems;
    }

    public String getItemsTags(){
        return itemsTags;
    }
    
    public void setItems(String items){
        this.items = items;
    }

    public void setTags(String tags){
        this.tags = tags;
    }

    public void setTagsItems(String tagsItems){
       this.tagsItems = tagsItems;
    }

    public void setItemsTags(String itemTags){
        this.itemsTags = itemTags;
    }

}
