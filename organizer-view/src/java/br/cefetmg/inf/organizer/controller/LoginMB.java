package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
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
    private String codEmail;
    private String userPassword;
    
    private IKeepUser keepUser;
    
    public LoginMB() throws SocketException, UnknownHostException {
        this.keepUser = new KeepUserProxy();
        currentUser = new User();
        codEmail = "";
        userPassword = "";
    }

    public String getCodEmail() {
        return codEmail;
    }

    public void setCodEmail(String codEmail) {
        this.codEmail = codEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void getUserLogin() throws PersistenceException, BusinessException{
        currentUser = keepUser.getUserLogin(codEmail, userPassword);
    }
}
