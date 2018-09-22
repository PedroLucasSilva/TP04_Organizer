package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.io.File;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

@Named(value = "userMB")
@RequestScoped
public class UserMB {

    private String codEmail;
    private String userName;
    private String userPassword;
    private String confirmPassword;
    //private File userPhoto;
    //private int currentTheme;

    private IKeepUser keepUser;

    public UserMB() throws SocketException, UnknownHostException {
        this.codEmail = "";
        this.userName = "";
        this.userPassword = "";
        this.keepUser = new KeepUserProxy();
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    
    public String getCodEmail() {
        return codEmail;
    }

    public void setCodEmail(String codEmail) {
        this.codEmail = codEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean createUser() throws PersistenceException, BusinessException {
        
        if (userPassword.equals(confirmPassword)) {
            User user = new User();

            user.setCodEmail(codEmail);
            user.setUserName(userName);
            user.setUserPassword(userPassword);
            //user.setUserPhoto(userPhoto);
            //user.setCurrentTheme(currentTheme);

            return keepUser.registerUser(user);
        }
        
        return false;

    }

    public boolean updateUser() throws PersistenceException, BusinessException {
        User user = new User();

        user.setCodEmail(codEmail);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        //user.setUserPhoto(userPhoto);
        //user.setCurrentTheme(currentTheme);

        return keepUser.updateUser(user);
    }

    public boolean deleteUser() throws PersistenceException, BusinessException {
        User user = new User();

        user.setCodEmail(codEmail);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        //user.setUserPhoto(userPhoto);
        //user.setCurrentTheme(currentTheme);

        return keepUser.deleteAccount(user);
    }

}
