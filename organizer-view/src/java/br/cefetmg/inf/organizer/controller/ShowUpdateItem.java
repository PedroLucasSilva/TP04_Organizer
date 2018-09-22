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
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class ShowUpdateItem implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        
        String pageJSP = "";
        List<Tag> tagList;
        Item item;
        
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        String id = req.getParameter("takeIdU");
        String typeItem = req.getParameter("takeTypeU");
        
        long seq_item;
        seq_item = Long.parseLong(id);
        
        IKeepItemTag keepItemTag = new KeepItemTagProxy();
        ArrayList<Tag> oldTags = keepItemTag.listAllTagInItem(seq_item);
        String listTags = "";
        
        if(oldTags != null){
            for(Tag tag : oldTags){
                listTags += tag.getTagName() + "; ";
            }        
        }
      
        // Session
        req.getSession().setAttribute("idItem", seq_item);
        req.getSession().setAttribute("itemTag", listTags);
        
        IKeepItem keepItem = new KeepItemProxy();
        item = keepItem.searchItemById(seq_item);
        if(item == null){
            req.setAttribute("itemList", null);
        }else{
            req.setAttribute("itemList", item);
        }
        
        IKeepTag keepTag = new KeepTagProxy();
        tagList = keepTag.listAlltag(user);
        if (tagList == null) {
            req.setAttribute("tagList", new ArrayList());
        } else {
            req.setAttribute("tagList", tagList);
        }
        
        switch (typeItem) {
            case "SIM":
                pageJSP = "/updateSimples.jsp";
                break;
            case "TAR":
                pageJSP = "/updateTarefa.jsp";   
                break;
            case "LEM":
                pageJSP = "/updateLembrete.jsp";
                break;
            default:
                break;
        }
        
        return pageJSP;    
    }    
}
