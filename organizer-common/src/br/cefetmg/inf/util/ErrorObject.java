
package br.cefetmg.inf.util;


public class ErrorObject {
    
    private String errorName;
    private String errorDescription;
    private String errorSubtext;

    public String getErrorSubtext() {
        return errorSubtext;
    }

    public void setErrorSubtext(String errorSubtext) {
        this.errorSubtext = errorSubtext;
    }


    public String getErrorName() {
        return errorName;
    }

    public void setErrorName(String errorName) {
        this.errorName = errorName;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

}
