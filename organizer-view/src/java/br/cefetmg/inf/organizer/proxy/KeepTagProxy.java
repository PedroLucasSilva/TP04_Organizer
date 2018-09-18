package br.cefetmg.inf.organizer.proxy;

import br.cefetmg.inf.organizer.dist.ClientDistribution;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.util.PseudoPackage;
import br.cefetmg.inf.util.RequestType;
import br.cefetmg.inf.util.exception.BusinessException;
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

public class KeepTagProxy implements IKeepTag {

    private final ClientDistribution client;

    public KeepTagProxy() throws SocketException, UnknownHostException {
        client = ClientDistribution.getInstance();
    }

    @Override
    public boolean createTag(Tag tag) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tag));

        RequestType requestType = RequestType.CREATETAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            return Boolean.valueOf(receivedPackage.getContent().get(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Tag readTag(Tag tag) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();
        Tag returnTag = null;

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tag));

        RequestType requestType = RequestType.READTAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);

            returnTag = json.fromJson(reader, Tag.class);
            return returnTag;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return returnTag;
    }

    @Override
    public boolean updateTag(Tag tag) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tag));

        RequestType requestType = RequestType.UPDATETAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            return Boolean.valueOf(receivedPackage.getContent().get(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTagId(Tag tag, Long id) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tag));
        jsonContent.add(json.toJson(id));

        RequestType requestType = RequestType.UPDATETAGID;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            return Boolean.valueOf(receivedPackage.getContent().get(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTag(Tag tag) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new Gson();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(tag));

        RequestType requestType = RequestType.DELETETAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            return Boolean.valueOf(receivedPackage.getContent().get(0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public ArrayList<Tag> listAlltag(User user) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(user));

        RequestType requestType = RequestType.LISTALLTAG;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        ArrayList<Tag> listAllTag;

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);

            if (receivedPackage.getContent().get(0).equals("false")) {
                return null;
            } else {
                Type type = new TypeToken<ArrayList<Tag>>() {
                }.getType();
                return json.fromJson(reader, type);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Long searchTagByName(String nomeTag, User user) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(nomeTag));
        jsonContent.add(json.toJson(user));

        RequestType requestType = RequestType.SEARCHTAGBYNAME;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        Long returnId;

        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);

            if (receivedPackage.getContent().get(0).equals("false")) {
                return null;
            } else {
                returnId = json.fromJson(reader, Long.class);
                return returnId;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Tag searchTagById(Long idTag) throws PersistenceException, BusinessException {
        PseudoPackage contentPackage;
        Gson json = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        List<String> jsonContent;
        jsonContent = new ArrayList();
        jsonContent.add(json.toJson(idTag));

        RequestType requestType = RequestType.SEARCHTAGBYID;
        contentPackage = new PseudoPackage(requestType, jsonContent);

        Tag returnTag;
        
        try {
            PseudoPackage receivedPackage = client.request(contentPackage);

            JsonReader reader = new JsonReader(new StringReader(receivedPackage.getContent().get(0)));
            reader.setLenient(true);

            if (receivedPackage.getContent().get(0).equals("false")) {
                return null;
            } else {
                returnTag = json.fromJson(reader, Tag.class);
                return returnTag;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
