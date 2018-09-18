package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.organizer.proxy.KeepMaxDataProxy;
import com.google.gson.Gson;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoadMax implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";
        
        // Iniciando sessão
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        // Carregando dados do modelo e transformando em strings json
        Gson gson = new Gson();       
        IKeepMaxData keepMaxData = new KeepMaxDataProxy();
        
        String jsonItem = gson.toJson(keepMaxData.loadItems(user)); 
        String jsonTag = gson.toJson(keepMaxData.loadTags(user));
        String jsonTagsItems = gson.toJson(keepMaxData.loadTagsItems(user));
        String jsonItemsTags = gson.toJson(keepMaxData.loadItemsTags(user));
                
        // Carregando dados na sessão
        session.setAttribute("jsonItem", jsonItem);
        session.setAttribute("jsonTag", jsonTag);
        session.setAttribute("jsonTagsItems", jsonTagsItems);
        session.setAttribute("jsonItemsTags", jsonItemsTags);
        
        pageJSP = "/max.jsp";
        
        return pageJSP;
    }
    
}