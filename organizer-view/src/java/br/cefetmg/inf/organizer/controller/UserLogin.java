
package br.cefetmg.inf.organizer.controller;


import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.ErrorObject;
import br.cefetmg.inf.util.PasswordCriptography;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class UserLogin implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";
        List<Item> itemList;
        
        String email = req.getParameter("email");
        String password = PasswordCriptography.generateMd5(req.getParameter("password"));
        
        IKeepUser keepUser = new KeepUserProxy();
        User user = keepUser.getUserLogin(email, password);
        
        if(user == null){
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro no login");
            error.setErrorSubtext("Verifique se você já está cadastrado, senão crie uma conta");
            req.getSession().setAttribute("error", error);
            pageJSP = "/errorLogin.jsp";
        }else{
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            
            IKeepItem keepItem = new KeepItemProxy();
            itemList = keepItem.listAllItem(user);
            if(itemList == null){
                req.setAttribute("itemList", new ArrayList());
            }else{
                req.setAttribute("itemList", itemList);
            }
            IKeepTag keepTag = new KeepTagProxy();
            List<Tag> tagList = keepTag.listAlltag(user);
            if(tagList == null){
                req.getSession().setAttribute("tagList", new ArrayList());
            }else{
               req.getSession().setAttribute("tagList", tagList);
            }
                
            pageJSP = "/index.jsp";
        }
        
        return pageJSP;
    }
    
}
