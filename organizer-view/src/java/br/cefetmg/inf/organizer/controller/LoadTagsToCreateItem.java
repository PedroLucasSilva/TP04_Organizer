package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoadTagsToCreateItem implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
    
        String pageJSP = "";
        List<Tag> tagList;
        
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        IKeepTag keepTag = new KeepTagProxy();
        tagList = keepTag.listAlltag(user);
        if (tagList == null) {
            req.setAttribute("tagList", new ArrayList());
        } else {
            req.setAttribute("tagList", tagList);
        }
        
        pageJSP = "/createItem.jsp";
        
        return pageJSP;
    }
    
}
