package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.io.Serializable;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "userMB")
@RequestScoped
public class UserMB implements Serializable{
    
    private User user;
    private String confirmPassword;
    
    private IKeepUser keepUser;

    public UserMB() throws SocketException, UnknownHostException {
        this.user = new User();
        this.confirmPassword = "";
        this.keepUser = new KeepUserProxy();
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
    
    
    public boolean createUser() throws PersistenceException, BusinessException {
        
        if (user.getUserPassword().equals(confirmPassword)) {
           
           boolean confirm = keepUser.registerUser(user);
        }
        
        return false;

    }

    public boolean updateUser() throws PersistenceException, BusinessException {
       boolean confirm = keepUser.updateUser(user);
       return false;
    }

    public boolean deleteUser() throws PersistenceException, BusinessException {
        boolean confirm = keepUser.deleteAccount(user);
        return false;
    }

}
