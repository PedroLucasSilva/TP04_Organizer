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
import javax.el.ELContext;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import org.primefaces.PrimeFaces;

@Named(value = "userMB")
@RequestScoped
public class UserMB implements Serializable {

    private User user;
    private String confirmPassword;

    private String newUpdatedPassword;
    private String confirmUpdatedPassword;
    private IKeepUser keepUser;

    private LoginMB currentUser;

    public UserMB() throws SocketException, UnknownHostException {
        this.user = new User();
        this.confirmPassword = "";
        this.newUpdatedPassword = "";
        this.confirmUpdatedPassword = "";
        this.keepUser = new KeepUserProxy();
        FacesContext context = FacesContext.getCurrentInstance();
        ELContext elContext = context.getELContext();
        currentUser = (LoginMB) elContext.getELResolver().getValue(elContext, null, "loginMB");
    }

    public void setCurrentUser(LoginMB currentUser) {
        this.currentUser = currentUser;
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

    public String getNewUpdatedPassword() {
        return newUpdatedPassword;
    }

    public void setNewUpdatedPassword(String newUpdatedPassword) {
        this.newUpdatedPassword = newUpdatedPassword;
    }

    public String getConfirmUpdatedPassword() {
        return confirmUpdatedPassword;
    }

    public void setConfirmUpdatedPassword(String confirmUpdatedPassword) {
        this.confirmUpdatedPassword = confirmUpdatedPassword;
    }

    public String createUser() throws PersistenceException, BusinessException {
        boolean confirm = false;

        if (user.getUserPassword().equals(confirmPassword)) {
            user.setUserPassword(PasswordCriptography.generateMd5(confirmPassword));
            user.setCurrentTheme(1);
            confirm = keepUser.registerUser(user);
        } else {
            FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("Suas senhas não conferem!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique as senhas anteriormente digitadas."));
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('errorDialog').show();");
            user = new User();
            confirmPassword = "";
        }

        if (confirm) {
            return "create";
        } else {
            FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("Erro na criação de usuário!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique se esse email já não está cadastrado."));
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('errorDialog').show();");
            confirmPassword = "";
            user = new User();
            return "false";
        }
    }

    public String updateUser() throws PersistenceException, BusinessException {
        User auxUser = currentUser.getCurrentUser();

        if (user.getUserName().equals("")) {
            user.setUserName(auxUser.getUserName());
        }
        if (user.getUserPassword().equals("")) {
            user.setUserPassword(auxUser.getUserPassword());
        } else {
            user.setUserPassword(PasswordCriptography.generateMd5(user.getUserPassword()));
        }

        user.setCodEmail(auxUser.getCodEmail());
        user.setUserPhoto(auxUser.getUserPhoto());
        user.setCurrentTheme(auxUser.getCurrentTheme());

        boolean confirm = keepUser.updateUser(user);

        if (confirm) {
            user.setUserPassword("");
            return "update";
        } else {
            FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("Erro na alteração de usuário!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique se os campos estão preenchidos corretamente "
                    + "ou se você está logado."));
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('errorDialog').show();");
            return "false";
        }
    }

    public String deleteUser() throws PersistenceException, BusinessException {
        String aux = PasswordCriptography.generateMd5(confirmPassword);
        User auxUser = currentUser.getCurrentUser();

        if (auxUser.getUserPassword().equals(aux)) {
            user = auxUser;
            boolean confirm = keepUser.deleteAccount(user);
            if (confirm) {
                currentUser.setCurrentUser(null);
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                return "delete";
            } else {
                FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
                FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("Erro na exclusão de usuário!"));
                FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique se os campos estão preenchidos corretamente "
                        + "ou se você está logado."));
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('errorDialog').show();");
                confirmPassword="";
                System.out.println("erro");
                return "false";
            }
        } else {
            FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("A senha digitada não confere com a antiga!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique a senha anteriormente digitada."));
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('errorDialog').show();");
            confirmPassword="";
            System.out.println("erro");
            return "false";
        }
    }

    public void updatePasswordField() {
        String aux = PasswordCriptography.generateMd5(confirmPassword);
        User auxUser = currentUser.getCurrentUser();

        if (auxUser.getUserPassword().equals(aux)) {
            if (newUpdatedPassword.equals(confirmUpdatedPassword)) {
                user.setUserPassword(newUpdatedPassword);
            } else {
                confirmPassword = "";
                newUpdatedPassword = "";
                confirmUpdatedPassword = "";
                user.setUserPassword("");
                FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
                FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("Suas novas senhas não conferem!"));
                FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique as senhas anteriormente digitadas."));
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('errorDialog').show();");
            }
        } else {
            confirmPassword = "";
            newUpdatedPassword = "";
            confirmUpdatedPassword = "";
            user.setUserPassword("");
            FacesContext.getCurrentInstance().addMessage("errorForm:errorCodeInput", new FacesMessage("Error 406!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorTextInput", new FacesMessage("A senha digitada não confere com a antiga!"));
            FacesContext.getCurrentInstance().addMessage("errorForm:errorSubTextInput", new FacesMessage("Verifique a senha anteriormente digitada."));
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('errorDialog').show();");
        }
    }
}
