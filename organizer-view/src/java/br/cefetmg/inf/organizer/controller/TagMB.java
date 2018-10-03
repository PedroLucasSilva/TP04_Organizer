package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import javax.el.ELContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named(value = "tagMB")
@RequestScoped
public class TagMB implements Serializable {
    
    private Tag tag;
    private final LoginMB currentUser;
    private final IKeepTag keepTag;
    private List<Tag> tagList;

    public TagMB() throws SocketException, UnknownHostException {
        tag = new Tag();
        keepTag = new KeepTagProxy();
        tagList = new ArrayList<>();
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        currentUser = (LoginMB) elContext.getELResolver().getValue(elContext, null, "loginMB");
        
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public List<Tag> getTagList() {
        return tagList;
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
    }
    
    public String createTag() throws PersistenceException, BusinessException {
        
        tag.setUser(currentUser.getCurrentUser());
        boolean success = keepTag.createTag(tag);
        
        if(success){
            tag.setSeqTag(keepTag.searchTagByName(tag.getTagName(), tag.getUser()));
        }
        
        if (success) {
            return "true";
        } else {
            System.out.println("erro");
            return "false";
        }
    }
    
    public String updateTag() throws PersistenceException, BusinessException {
        
        boolean success = keepTag.updateTag(tag);
        
        if (success) {
            return "true";
        } else {
            System.out.println("erro");
            return "false";
        }
    }
    
    public String deleteTag() throws PersistenceException, BusinessException {
        
        String value = FacesContext.getCurrentInstance().
		getExternalContext().getRequestParameterMap().get("idOldTag");
        tag.setUser(currentUser.getCurrentUser());
        tag.setSeqTag(Long.parseLong(value));
        
        boolean success = keepTag.deleteTag(tag);
        
        if (success) {
            return "true";
        } else {
            System.out.println("erro");
            return "false";
        }
    }
    
    public String listAllTag() throws PersistenceException, BusinessException {
        
         boolean success;
         tagList = keepTag.listAlltag(currentUser.getCurrentUser());
         
        success = !tagList.isEmpty();
                  
        if (success) {
            return "true";
        } else {
            System.out.println("erro");
            return "false";
        }
    }
    
}
