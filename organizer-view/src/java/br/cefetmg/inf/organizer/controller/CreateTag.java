
package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.ErrorObject;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CreateTag implements GenericProcess {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";
        List<Item> itemList;

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        String nameTag = req.getParameter("name");

        Tag tag = new Tag();
        tag.setTagName(nameTag);
        tag.setUser(user);
        tag.setSeqTag(null);

        IKeepTag keepTag = new KeepTagProxy();
        boolean success = keepTag.createTag(tag);

        if (!success) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro na criação da tag");
            error.setErrorSubtext("Verifique se a tag já existe ou se ocorreu um erro no preenchimento do campo");
            req.getSession().setAttribute("error", error);
            pageJSP = "/error.jsp";
        } else {
            IKeepItem keepItem = new KeepItemProxy();
            itemList = keepItem.listAllItem(user);
            if(itemList == null){
                req.setAttribute("itemList", new ArrayList());
            }else{
                req.setAttribute("itemList", itemList);
            }
            
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