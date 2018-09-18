package br.cefetmg.inf.organizer.proxy;

import br.cefetmg.inf.organizer.dist.ClientDistribution;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.util.PseudoPackage;
import br.cefetmg.inf.util.RequestType;
import br.cefetmg.inf.util.exception.PersistenceException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class KeepItemProxy implements IKeepItem {
    
    private ClientDistribution client;

    public KeepItemProxy() throws SocketException, UnknownHostException {
        client = ClientDistribution.getInstance();
    }

    @Override
    public boolean createItem(Item item) throws PersistenceException {
    
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(item));
        
        RequestType requestType = RequestType.CREATEITEM;
        contentPackage = new PseudoPackage(requestType, jsonContent); 
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            return Boolean.valueOf(receivedPackage.getContent().get(0));
           
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public boolean updateItem(Item item) throws PersistenceException {
        
        PseudoPackage contentPackage;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(item));
        
        RequestType requestType = RequestType.UPDATEITEM;
        contentPackage = new PseudoPackage(requestType, jsonContent); 
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            return Boolean.valueOf(receivedPackage.getContent().get(0));
           
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }

    @Override
    public boolean deleteItem(Long idItem, User user) throws PersistenceException {
        
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(idItem));
        jsonContent.add(json.toJson(user));
        
        RequestType requestType = RequestType.DELETEITEM;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            return Boolean.valueOf(receivedPackage.getContent().get(0));
           
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return false;
    }

    @Override
    public ArrayList<Item> listAllItem(User user) throws PersistenceException {
        
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));
       
        RequestType requestType = RequestType.LISTALLITEM;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                Type type = new TypeToken<ArrayList<Item>>() {}.getType();
                return json.fromJson(reader, type);
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public Item searchItemById(Long idItem) throws PersistenceException {
        
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(idItem));
        
        RequestType requestType = RequestType.SEARCHITEMBYID;
        
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                return json.fromJson(reader, Item.class);
            }

        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return null;
    }

    @Override
    public Item searchItemByName(String nomeItem) throws PersistenceException {
        
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(nomeItem);
        
        RequestType requestType = RequestType.SEARCHITEMBYNAME;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                return json.fromJson(reader, Item.class);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public ArrayList<Item> searchItemByTag(List<Tag> tagList, User user) throws PersistenceException {
        PseudoPackage contentPackage;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        ArrayList<String> jsonContent;
        JsonReader reader;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tagList));
        jsonContent.add(json.toJson(user));
        
        
        RequestType requestType = RequestType.SEARCHITEMBYTAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                Type type = new TypeToken<ArrayList<Item>>() {}.getType();
                return json.fromJson(reader, type);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public ArrayList<Item> searchItemByType(List<String> typeList, User user) throws PersistenceException {
        
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(typeList));
        jsonContent.add(json.toJson(user));
        
        
        RequestType requestType = RequestType.SEARCHITEMBYTYPE;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                Type type = new TypeToken<ArrayList<Item>>() {}.getType();
                return json.fromJson(reader, type);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return null;
    }

    @Override
    public ArrayList<Item> searchItemByTagAndType(List<Tag> tagList, List<String> typeList, User user) throws PersistenceException {
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson  json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        ArrayList<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tagList));
        jsonContent.add(json.toJson(typeList));
        jsonContent.add(json.toJson(user));
        
        
        RequestType requestType = RequestType.SEARCHITEMBYTAGANDTYPE;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                Type type = new TypeToken<ArrayList<Item>>() {}.getType();
                return json.fromJson(reader, type);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
}
