
package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.inject.Named;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Iterator;

@Named(value = "itemFilterMB")
@ManagedBean
@ViewScoped
public class ItemFilterMB implements Serializable {
    ArrayList<Item> itemList;
    ArrayList<Tag> tagList;
    ArrayList<String> typeList;
    IKeepItem keepItem;
    IKeepTag keepTag;
    @ManagedProperty(value="#{loginMB}")
    LoginMB login;
    String expression;
    
    public ItemFilterMB() throws SocketException, UnknownHostException{
        itemList = new ArrayList<>();
        tagList = new ArrayList<>();
        typeList = new ArrayList<>();
        keepItem = new KeepItemProxy();
        expression = new String();
        keepTag = new KeepTagProxy();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
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

    public LoginMB getLogin() {
        return login;
    }

    public void setLogin(LoginMB login) {
        this.login = login;
    }
    
    public IKeepTag getKeepTag() {
        return keepTag;
    }

    public void setKeepTag(IKeepTag keepTag) {
        this.keepTag = keepTag;
    }
    
    public void itemListFilter(){
        try {
            if(!tagList.isEmpty() && !typeList.isEmpty()){
                itemList = keepItem.searchItemByTagAndType(tagList, typeList, login.getCurrentUser());
            }else if(!tagList.isEmpty()){
                itemList = keepItem.searchItemByTag(tagList, login.getCurrentUser());
            }else if(!typeList.isEmpty()){
                itemList = keepItem.searchItemByType(typeList, login.getCurrentUser());
            }else {
                itemList = keepItem.listAllItem(login.getCurrentUser());
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
    
    public String nameFilter(Item item){
        //System.out.println("entrou");
        if(expression.isEmpty()){
            //System.out.println("empty");
            return "true";
        }else{
            //System.out.println(item.getNameItem());
            //System.out.println(expression);
            //System.out.println(item.getNameItem().toLowerCase().indexOf(expression));
            if(item.getNameItem().toLowerCase().indexOf(expression.toLowerCase()) == -1){
                //System.out.println("false");
                return "false";
            }else{
                //System.out.println("true");
                return "true";
            }
        }
    }
    
    public void changeTypeList(String type){
        //se o tipo não estiver na lista, adiciona
        if(!typeList.contains(type)){
            typeList.add(type);
        }else{
            //se o tipo ja estiver na lista, remove
            typeList.remove(type);
        }
    }
    
    public void changeTagList(Long seqTag) throws PersistenceException, BusinessException, SocketException, UnknownHostException{
        Tag tag = keepTag.searchTagById(seqTag);
        tag.setUser(login.getCurrentUser());
        
        ArrayList<Long> seqTagList = new ArrayList<>();
        
        for(Tag obj : tagList){
            seqTagList.add(obj.getSeqTag());
        }
        
        //se a tag não estiver na lista, adiciona
        if(!seqTagList.contains(tag.getSeqTag())){
            tagList.add(tag);
        }else{
            //se a tag ja estiver na lista, remove
            Iterator<Tag> iter = tagList.iterator();

            while (iter.hasNext()) {
                Tag obj = iter.next();

                if (obj.getSeqTag().equals(tag.getSeqTag())){
                    iter.remove();
                }
            }
        }
    }
    
    public ArrayList<Tag> listAllTags() throws PersistenceException, BusinessException{
         return keepTag.listAlltag(login.getCurrentUser());
    }
}
