package br.cefetmg.inf.organizer.proxy;

import br.cefetmg.inf.organizer.dist.ClientDistribution;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.ItemTag;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.service.IKeepItemTag;
import br.cefetmg.inf.util.PseudoPackage;
import br.cefetmg.inf.util.RequestType;
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

public class KeepItemTagProxy implements IKeepItemTag{
    
    private ClientDistribution client;

    public KeepItemTagProxy() throws SocketException, UnknownHostException {
        client = ClientDistribution.getInstance();
    }

    @Override
    public boolean createTagInItem(ItemTag itemTag) {
        
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(itemTag));
        
        RequestType requestType = RequestType.CREATETAGINITEM;        
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
    public boolean deleteTagInItem(ArrayList<Tag> itemTag, Long id) {
        
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(itemTag));
        jsonContent.add(json.toJson(id));
        
        RequestType requestType = RequestType.DELETETAGINITEM;
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
    public ArrayList<Tag> listAllTagInItem(Long seqItem) {
        
        PseudoPackage contentPackage;
        JsonReader reader;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(seqItem));
        
        RequestType requestType = RequestType.LISTALLTAGINITEM;
        
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
           
            if(receivedPackage.getContent().get(0).equals("false")){
                return null;
            }else{
                Type type = new TypeToken<ArrayList<Tag>>() {}.getType();
                return json.fromJson(reader, type);
            }
            
             
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return new ArrayList();
    }

    @Override
    public boolean deleteTagByItemId(Long idItem) {
       
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(idItem));
        
        RequestType requestType = RequestType.DELETETAGBYITEMID;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            return Boolean.valueOf(receivedPackage.getContent().get(0));
           
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        return false;
    }    
}
