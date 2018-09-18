package br.cefetmg.inf.util;

import br.cefetmg.inf.util.RequestType;
import java.util.List;

public class PseudoPackage {

    private RequestType requestType;
    private List<String> content;

    public PseudoPackage(RequestType requestType, List<String> content) {
        this.requestType = requestType;
        this.content = content;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public List<String> getContent() {
        return content;
    }
   
}
