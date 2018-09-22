package br.cefetmg.inf.organizer.proxy;

import br.cefetmg.inf.organizer.dist.ClientDistribution;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.util.PseudoPackage;
import br.cefetmg.inf.util.RequestType;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeepUserProxy implements IKeepUser {

    private ClientDistribution client;

    public KeepUserProxy() throws SocketException, UnknownHostException {
        client = ClientDistribution.getInstance();
    }
    
    @Override
    public boolean registerUser(User user) throws PersistenceException, BusinessException {

        PseudoPackage contentPackage;
        Gson json = new Gson();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));
        
        RequestType requestType = RequestType.REGISTERUSER;
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
    public User searchUser(User user) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();
        User returnUser = null;
                
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));
        
        RequestType requestType = RequestType.SEARCHUSER;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            returnUser = json.fromJson(reader, User.class);
            return returnUser;
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnUser;
    }

    @Override
    public boolean updateUser(User user) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));
        
        RequestType requestType = RequestType.UPDATEUSER;
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
    public boolean deleteAccount(User user) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();
        
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));
        
        RequestType requestType = RequestType.DELETEACCOUNT;
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
    public User getUserLogin(String email, String password) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();
        User returnUser = null;
                
        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(email);
        jsonContent.add(password);
        
        RequestType requestType = RequestType.GETUSERLOGIN;
        contentPackage = new PseudoPackage(requestType, jsonContent);
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);
            
            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);
            
            if (receivedPackage.getContent().get(0).equals("false")) {
                return null;
            } else {
                returnUser = json.fromJson(reader, User.class);
                return returnUser;
            }
            
            
        } catch (IOException ex) {
            Logger.getLogger(KeepUserProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnUser;
    }

}
