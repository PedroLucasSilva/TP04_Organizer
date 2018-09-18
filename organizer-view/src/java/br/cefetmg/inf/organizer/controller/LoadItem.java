package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class LoadItem implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        String pageJSP = "";
        List<Item> itemList;
        
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        IKeepItem keepItem = new KeepItemProxy();
        itemList = keepItem.listAllItem(user);
        
        if(itemList == null){
            req.setAttribute("itemList", null);
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
        
        return pageJSP;
    
    }
    
}
