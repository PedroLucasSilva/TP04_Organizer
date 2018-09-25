package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.PasswordCriptography;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import org.primefaces.PrimeFaces;

@Named(value = "userMB")
@RequestScoped
public class UserMB implements Serializable {

    @ManagedProperty(value = "#errorMB")
    private ErrorMB error;
    private User user;
    private String confirmPassword;

    private IKeepUser keepUser;

    public UserMB() throws SocketException, UnknownHostException {
        this.user = new User();
        this.confirmPassword = "";
        this.keepUser = new KeepUserProxy();
        error = new ErrorMB();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String createUser() throws PersistenceException, BusinessException {
        boolean confirm = false;
        
        if (user.getUserPassword().equals(confirmPassword)) {
            user.setUserPassword(PasswordCriptography.generateMd5(confirmPassword));
            user.setCurrentTheme(1);
            confirm = keepUser.registerUser(user);
        } else {
            error.setErrorCode("Error 406");
            error.setErrorText("Suas senhas não conferem!");
            error.setErrorSubText("Verifique as senhas anteriormente digitadas.");
            Map<String, Object> options = new HashMap<String, Object>();
            options.put("modal", true);
            options.put("width", 800);
            options.put("height", 600);
            options.put("contentWidth", "100%");
            options.put("contentHeight", "100%");
            options.put("closable", true);
            PrimeFaces.current().dialog().openDynamic("errorPage", options, null);
            user = new User();
            confirmPassword = "";
        }

        if (confirm) {
            return "true";
        } else {
            System.out.println("erro");
            user = new User();
            return "false";
        }
    }

    public boolean updateUser() throws PersistenceException, BusinessException {
        boolean confirm = keepUser.updateUser(user);

        if (confirm) {
            return true;
        } else {
            //Dialog erro
            System.out.println("erro");
            return false;
        }
    }

    public boolean deleteUser() throws PersistenceException, BusinessException {
        boolean confirm = keepUser.deleteAccount(user);
        if (confirm) {
            return true;
        } else {
            //Dialog erro
            System.out.println("erro");
            return false;
        }
    }

}
