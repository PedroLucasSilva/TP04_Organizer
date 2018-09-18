package br.cefetmg.inf.organizer.model.domain;

import java.io.File;

/**
 *
 * @author aline
 */
public class User {
    
    private String codEmail;
    private String userName;
    private String userPassword;
    private File userPhoto;
    private int currentTheme;

    public int getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(int currentTheme) {
        this.currentTheme = currentTheme;
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

    public File getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(File userPhoto) {
        this.userPhoto = userPhoto;
    }
    
}
