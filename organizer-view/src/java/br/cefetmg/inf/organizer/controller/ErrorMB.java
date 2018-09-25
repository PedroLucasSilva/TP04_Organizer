package br.cefetmg.inf.organizer.controller;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@Named(value = "errorMB")
@ViewScoped
public class ErrorMB implements Serializable {

    private String errorCode;
    private String errorText;
    private String errorSubText;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getErrorSubText() {
        return errorSubText;
    }

    public void setErrorSubText(String errorSubText) {
        this.errorSubText = errorSubText;
    }

    public ErrorMB(String errorCode, String errorText, String errorSubText) {
        this.errorCode = errorCode;
        this.errorText = errorText;
        this.errorSubText = errorSubText;
    }
    
    public ErrorMB(){
        this.errorCode = "";
        this.errorText = "";
        this.errorSubText = "";
    }

}
