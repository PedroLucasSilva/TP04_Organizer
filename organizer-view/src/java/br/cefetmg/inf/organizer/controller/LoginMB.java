package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.PasswordCriptography;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.faces.context.FacesContext;


@Named(value = "loginMB")
@SessionScoped
public class LoginMB implements Serializable {
    
    private User currentUser;
    
    private IKeepUser keepUser;
    
    public LoginMB() throws SocketException, UnknownHostException {
        this.keepUser = new KeepUserProxy();
        currentUser = new User();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

   
    public String getUserLogin() throws PersistenceException, BusinessException{
        currentUser = keepUser.getUserLogin(currentUser.getCodEmail(), 
                PasswordCriptography.generateMd5(currentUser.getUserPassword()));
        
        if(currentUser == null){
            //Dialog de erro
            System.out.println("Erro no login");
            return "false";
        }else{
            //Redireciona p/ index
            return "true";
        }
    }
    
    public void userLogout(){
        currentUser = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
    
    
}
