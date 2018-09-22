package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepItemTag;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepItemTagProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.ErrorObject;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class DeleteItem implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        String pageJSP = "";
        List<Item> itemList;
        
        // Pegando usuário
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        String idItemString = req.getParameter("takeId");
        Long idItem = Long.parseLong(idItemString); 
       
        IKeepItem keepItem = new KeepItemProxy();
        boolean result = keepItem.deleteItem(idItem, user);
       
        if(!result){
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro ao deletar Item");
            error.setErrorSubtext("Não foi possível deletar o item.");
            req.getSession().setAttribute("error", error);
            pageJSP = "/error.jsp";
        } else {
            
            IKeepItemTag keepItemTag = new KeepItemTagProxy();
            result = keepItemTag.deleteTagByItemId(idItem);
            
            if(!result){
                ErrorObject error = new ErrorObject();
                error.setErrorName("Tente novamente");
                error.setErrorDescription("Erro ao deletar Tag");
                error.setErrorSubtext("Erro ao deslinkar tags referentes ao item.");
                req.getSession().setAttribute("error", error);
                pageJSP = "/error.jsp";
            } else {
                
                itemList = keepItem.listAllItem(user);
                if(itemList == null){
                    req.setAttribute("itemList", new ArrayList());
                }else{
                    req.setAttribute("itemList", itemList);
                }
                
                IKeepTag keepTag = new KeepTagProxy();
                List<Tag> tagList = keepTag.listAlltag(user);
                if (tagList == null) {
                    req.getSession().setAttribute("tagList", new ArrayList());
                } else {
                   req.getSession().setAttribute("tagList", tagList);
                }
                
                pageJSP = "/index.jsp";
            
            }
        }
        
        return pageJSP;
        
    }    
}
