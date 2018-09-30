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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Named;

@Named(value="tagDialog")
@SessionScoped
public class TagDialog implements Serializable{  
    private List<Tag> tag;
    private IKeepTag keepTag;
    
    @ManagedProperty(value="#loginMB")
    private LoginMB user;
    
    public TagDialog() {      
        tag = new ArrayList<>();
        try {
            keepTag = new KeepTagProxy();
            tag = keepTag.listAlltag(user.getCurrentUser());
           
        } catch (SocketException | UnknownHostException | PersistenceException | BusinessException ex) {
            Logger.getLogger(TagDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Tag> getTag() {
        return tag;
    }
    
}
