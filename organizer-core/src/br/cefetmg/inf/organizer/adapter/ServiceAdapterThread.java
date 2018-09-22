package br.cefetmg.inf.organizer.adapter;

import br.cefetmg.inf.organizer.dist.ServerDistribution;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.ItemTag;
import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepItemTag;
import br.cefetmg.inf.organizer.model.service.impl.KeepItemTag;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.model.service.impl.KeepItem;
import br.cefetmg.inf.organizer.model.service.impl.KeepMaxData;
import br.cefetmg.inf.organizer.model.service.impl.KeepTag;
import br.cefetmg.inf.organizer.model.service.impl.KeepUser;
import br.cefetmg.inf.util.PackageShredder;
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
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceAdapterThread implements Runnable {

    private InetAddress IPAddress;
    private int clientPort;
    private int serverPort;
    private PseudoPackage contentPackage;
    private Gson gson;

    public ServiceAdapterThread(InetAddress IPAddress, int clientPort, int serverPort, PseudoPackage contentPackage) {
        this.IPAddress = IPAddress;
        this.clientPort = clientPort;
        this.contentPackage = contentPackage;
        this.serverPort = serverPort;
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    }

    @Override
    public void run() {
        try {
            evaluateRequest();
            return;
        } catch (PersistenceException ex) {
            Logger.getLogger(ServiceAdapterThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BusinessException ex) {
            Logger.getLogger(ServiceAdapterThread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceAdapterThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void prepareToSend(PseudoPackage responsePackage) throws IOException {

        byte[][] sendData;

        String responseString = gson.toJson(responsePackage);
        PackageShredder packageShredder = new PackageShredder();
        sendData = packageShredder.fragment(responseString);

        PseudoPackage numPackage;
        List<String> jsonContentAux;
        jsonContentAux = new ArrayList();
        jsonContentAux.add(String.valueOf(sendData.length));

        RequestType requestTypeAux = RequestType.NUMPACKAGE;
        numPackage = new PseudoPackage(requestTypeAux, jsonContentAux);

        System.out.println("Enviando número de pacotes de saída");
        //primeiramente envia o numero de pacotes de resposta ao cliente
        ServerDistribution.sendData(IPAddress, clientPort, numPackage);
        //depois, envia os dados em si
        ServerDistribution.sendData(IPAddress, clientPort, responsePackage);

        System.out.println("Recebeu  pacotes de saída");

    }

    public void evaluateRequest() throws PersistenceException, BusinessException, IOException {
        RequestType requestType = contentPackage.getRequestType();
        boolean confirm;
        PseudoPackage responsePackage;
        List<String> jsonContent;
        User user;
        MaxDataObject maxDataObject;
        IKeepUser keepUser;
        Item item;
        IKeepItem keepItem;
        ArrayList<Item> itemList;
        List<Tag> tagList;
        List<String> typeList;
        IKeepItemTag keepItemTag;
        IKeepMaxData keepMaxData;
        IKeepTag keepTag;
        Tag tag;

        Type type;

        switch (requestType) {
            // Cases about User
            case REGISTERUSER:
                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepUser = new KeepUser();
                confirm = keepUser.registerUser(user);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;
            case SEARCHUSER:
                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepUser = new KeepUser();

                User foundUser = keepUser.searchUser(user);
                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(foundUser));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;
            case UPDATEUSER:
                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepUser = new KeepUser();
                confirm = keepUser.updateUser(user);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;
            case DELETEACCOUNT:
                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepUser = new KeepUser();
                confirm = keepUser.deleteAccount(user);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;
            case GETUSERLOGIN:
                String emailUser = contentPackage.getContent().get(0);
                String userPassword = contentPackage.getContent().get(1);

                keepUser = new KeepUser();
                user = keepUser.getUserLogin(emailUser, userPassword);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(user));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            // Cases about Item
            case CREATEITEM:

                item = gson.fromJson(contentPackage.getContent().get(0), Item.class);
                keepItem = new KeepItem();
                confirm = keepItem.createItem(item);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATEITEM:

                item = gson.fromJson(contentPackage.getContent().get(0), Item.class);
                keepItem = new KeepItem();
                confirm = keepItem.updateItem(item);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case DELETEITEM:

                Long idItem = gson.fromJson(contentPackage.getContent().get(0), Long.class);
                user = gson.fromJson(contentPackage.getContent().get(1), User.class);
                keepItem = new KeepItem();
                confirm = keepItem.deleteItem(idItem, user);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LISTALLITEM:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepItem = new KeepItem();
                itemList = keepItem.listAllItem(user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemList));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHITEMBYID:

                idItem = gson.fromJson(contentPackage.getContent().get(0), Long.class);
                keepItem = new KeepItem();
                Item itemById = keepItem.searchItemById(idItem);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemById, Item.class));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHITEMBYNAME:

                String nameItem = contentPackage.getContent().get(0);
                keepItem = new KeepItem();
                Item itemByName = keepItem.searchItemByName(nameItem);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemByName));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHITEMBYTAG:

                type = new TypeToken<ArrayList<Tag>>() {
                }.getType();

                tagList = gson.fromJson(contentPackage.getContent().get(0), type);
                user = gson.fromJson(contentPackage.getContent().get(1), User.class);

                keepItem = new KeepItem();

                itemList = keepItem.searchItemByTag(tagList, user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemList));
                responsePackage = new PseudoPackage(RequestType.SEARCHITEMBYTAG, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHITEMBYTYPE:

                typeList = gson.fromJson(contentPackage.getContent().get(0), List.class);
                user = gson.fromJson(contentPackage.getContent().get(1), User.class);

                keepItem = new KeepItem();

                itemList = keepItem.searchItemByType(typeList, user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemList));
                responsePackage = new PseudoPackage(RequestType.SEARCHITEMBYTYPE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHITEMBYTAGANDTYPE:

                type = new TypeToken<ArrayList<Tag>>() {
                }.getType();

                tagList = gson.fromJson(contentPackage.getContent().get(0), type);
                typeList = gson.fromJson(contentPackage.getContent().get(1), List.class);
                user = gson.fromJson(contentPackage.getContent().get(2), User.class);

                keepItem = new KeepItem();

                itemList = keepItem.searchItemByTagAndType(tagList, typeList, user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(itemList));
                responsePackage = new PseudoPackage(RequestType.SEARCHITEMBYTAGANDTYPE, jsonContent);

                prepareToSend(responsePackage);
                break;

            // Cases about ItemTag
            case CREATETAGINITEM:

                ItemTag itemTag = gson.fromJson(contentPackage.getContent().get(0), ItemTag.class);
                keepItemTag = new KeepItemTag();
                confirm = keepItemTag.createTagInItem(itemTag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case DELETETAGINITEM:

                type = new TypeToken<ArrayList<Tag>>() {
                }.getType();
                ArrayList<Tag> arrTagOfItem = gson.fromJson(contentPackage.getContent().get(0), type);
                Long id = gson.fromJson(contentPackage.getContent().get(1), Long.class);
                keepItemTag = new KeepItemTag();
                confirm = keepItemTag.deleteTagInItem(arrTagOfItem, id);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LISTALLTAGINITEM:

                Long seqItem = gson.fromJson(contentPackage.getContent().get(0), Long.class);
                keepItemTag = new KeepItemTag();
                ArrayList<Tag> listAllTagsOfItem = keepItemTag.listAllTagInItem(seqItem);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(listAllTagsOfItem));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case DELETETAGBYITEMID:

                idItem = gson.fromJson(contentPackage.getContent().get(0), Long.class);
                keepItemTag = new KeepItemTag();
                confirm = keepItemTag.deleteTagByItemId(idItem);

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            // Cases about Max
            case LOADITEMS:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(keepMaxData.loadItems(user)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LOADTAGS:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(keepMaxData.loadTags(user)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LOADTAGSITEMS:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(keepMaxData.loadTagsItems(user)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LOADITEMSTAGS:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(keepMaxData.loadItemsTags(user)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATEALLITEMS:

                maxDataObject = gson.fromJson(contentPackage.getContent().get(0), MaxDataObject.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(keepMaxData.updateAllItems(maxDataObject)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATEALLTAGS:

                maxDataObject = gson.fromJson(contentPackage.getContent().get(0), MaxDataObject.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(keepMaxData.updateAllTags(maxDataObject)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATEALLITEMTAG:

                maxDataObject = gson.fromJson(contentPackage.getContent().get(0), MaxDataObject.class);
                keepMaxData = new KeepMaxData();

                jsonContent = new ArrayList();
                jsonContent.add(String.valueOf(keepMaxData.updateAllItemTag(maxDataObject)));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            // Cases about Tag
            case CREATETAG:

                tag = gson.fromJson(contentPackage.getContent().get(0), Tag.class);
                keepTag = new KeepTag();
                confirm = keepTag.createTag(tag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case READTAG:

                tag = gson.fromJson(contentPackage.getContent().get(0), Tag.class);
                keepTag = new KeepTag();
                Tag readTag = keepTag.readTag(tag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(readTag));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATETAG:

                tag = gson.fromJson(contentPackage.getContent().get(0), Tag.class);
                keepTag = new KeepTag();
                confirm = keepTag.updateTag(tag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case UPDATETAGID:

                tag = gson.fromJson(contentPackage.getContent().get(0), Tag.class);
                Long idTag = gson.fromJson(contentPackage.getContent().get(1), Long.class);
                keepTag = new KeepTag();
                confirm = keepTag.updateTagId(tag, idTag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case DELETETAG:

                tag = gson.fromJson(contentPackage.getContent().get(0), Tag.class);
                keepTag = new KeepTag();
                confirm = keepTag.deleteTag(tag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(confirm));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case LISTALLTAG:

                user = gson.fromJson(contentPackage.getContent().get(0), User.class);
                keepTag = new KeepTag();
                List<Tag> listTag = keepTag.listAlltag(user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(listTag));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHTAGBYNAME:

                String tagName = gson.fromJson(contentPackage.getContent().get(0), String.class);
                user = gson.fromJson(contentPackage.getContent().get(1), User.class);
                keepTag = new KeepTag();
                idTag = keepTag.searchTagByName(tagName, user);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(idTag));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            case SEARCHTAGBYID:

                idTag = gson.fromJson(contentPackage.getContent().get(0), Long.class);
                keepTag = new KeepTag();
                tag = keepTag.searchTagById(idTag);

                jsonContent = new ArrayList();
                jsonContent.add(gson.toJson(tag));
                responsePackage = new PseudoPackage(RequestType.RESPONSEPACKAGE, jsonContent);

                prepareToSend(responsePackage);
                break;

            default:
            //exception
        }
    }

}
