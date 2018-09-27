
package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named(value = "itemFilterMB")
@ManagedBean
@RequestScoped
public class ItemFilterMB {
    ArrayList<Item> itemList;
    ArrayList<Tag> tagList;
    ArrayList<String> typeList;
    IKeepItem keepItem;
    User user;
    
    public ItemFilterMB() throws SocketException, UnknownHostException{
        itemList = new ArrayList<>();
        tagList = new ArrayList<>();
        typeList = new ArrayList<>();
        keepItem = new KeepItemProxy();
        user = new User();
        //remover futuramente
        user.setCodEmail("1");
        user.setUserName("1");
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public ArrayList<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(ArrayList<Tag> tagList) {
        this.tagList = tagList;
    }

    public ArrayList<String> getTypeList() {
        return typeList;
    }

    public void setTypeList(ArrayList<String> typeList) {
        this.typeList = typeList;
    }

    public IKeepItem getKeepItem() {
        return keepItem;
    }

    public void setKeepItem(IKeepItem keepItem) {
        this.keepItem = keepItem;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void itemListFilter(){
        
        try {
            if(!tagList.isEmpty() && !typeList.isEmpty()){
                itemList = keepItem.searchItemByTagAndType(tagList, typeList, user);
            }else if(!tagList.isEmpty()){
                itemList = keepItem.searchItemByTag(tagList, user);
            }else if(!typeList.isEmpty()){
                itemList = keepItem.searchItemByType(typeList, user);
            }else {
                itemList = keepItem.listAllItem(user);
            }
        } catch (PersistenceException ex) {
            //Erro de persistencia
            /*
            ErrorObject error = new ErrorObject();
            error.setErrorName("Filtragem indevida");
            error.setErrorDescription("Erro na filtragem de itens");
            error.setErrorSubtext("Tente novamente mais tarde ou entre em contato conosco");
            req.getSession().setAttribute("error", error);
            return "/error.jsp";
             */
        }
        
        //filtragem das tarefas caso a tag "Concluidos" seja selecionada ou nao
        boolean concluidoExists = false;

        try {
            for (Tag tag : tagList) {
                if (tag.getTagName().equals("Concluidos") && (itemList != null)) {
                    concluidoExists = true;
                    for (Item item : new ArrayList<>(itemList)) {
                        if (item.getIdentifierItem().equals("TAR")
                                && item.getIdentifierStatus().equals("A")) {
                            //remove da lista as tarefas nao concluidas
                            itemList.remove(item);
                        }
                    }
                }
            }

            if (!concluidoExists && (itemList != null)) {
                for (Item item : new ArrayList<Item>(itemList)) {
                    if (item.getIdentifierItem().equals("TAR")
                            && item.getIdentifierStatus().equals("C")) {
                        //remove da lista as tarefas concluidas
                        itemList.remove(item);
                    }
                }
            }
        } catch (NullPointerException ex) {
            //erro
            /*
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tarefa incorreta");
            error.setErrorDescription("Erro na filtragem de tarefas concluídas");
            error.setErrorSubtext("Tente novamente mais tarde ou entre em contato conosco");
            req.getSession().setAttribute("error", error);
            return "/error.jsp";
            */
        }
    }
}
