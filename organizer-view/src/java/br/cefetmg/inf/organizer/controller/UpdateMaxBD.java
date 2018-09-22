/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/
package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepMaxData;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepMaxDataProxy;
import br.cefetmg.inf.util.ErrorObject;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class UpdateMaxBD implements GenericProcess{

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";
        
         //Pegando usuário
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        
        //Obtendo dados
        Gson gson = new Gson();
        
        String[] itemsID = gson.fromJson( req.getParameter("itemsID"), String[].class );
        String[] itemsType = gson.fromJson( req.getParameter("itemsType"), String[].class );
        String[] itemsName = gson.fromJson( req.getParameter("itemsName"), String[].class );
        String[] itemsDescription = gson.fromJson( req.getParameter("itemsDescription"), String[].class );
        String[] itemsDate = gson.fromJson( req.getParameter("itemsDate"), String[].class );
        String[] itemsStatus = gson.fromJson( req.getParameter("itemsStatus"), String[].class );
        
        String[] tagsID = gson.fromJson( req.getParameter("tagsID"), String[].class );
        String[] tagsName = gson.fromJson( req.getParameter("tagsName"), String[].class );
        
        String[] tagsItems = gson.fromJson( req.getParameter("tagsItems"), String[].class );
        String[] itemsTags = gson.fromJson( req.getParameter("itemsTags"), String[].class );
        
        MaxDataObject maxDataObject = new MaxDataObject();
        
        maxDataObject.setUser(user);
        maxDataObject.setItemsID(itemsID);
        maxDataObject.setItemsType(itemsType);
        maxDataObject.setItemsName(itemsName);
        maxDataObject.setItemsDescription(itemsDescription);
        maxDataObject.setItemsDate(itemsDate);
        maxDataObject.getItemsStatus();
        
        maxDataObject.setTagsID(tagsID);
        maxDataObject.setTagsName(tagsName);
                
        maxDataObject.setTagsItems(tagsItems);
        maxDataObject.setItemsTags(itemsTags);
        
        IKeepMaxData keepMaxData = new KeepMaxDataProxy();
        
        boolean itemTagSuccess = keepMaxData.updateAllItemTag(maxDataObject);//PARAMETROS
        boolean itemSuccess = keepMaxData.updateAllItems(maxDataObject);//PARAMETROS
        boolean tagSuccess = keepMaxData.updateAllTags(maxDataObject);//PARAMETROS
        
        if(itemSuccess && tagSuccess && itemTagSuccess){          
            pageJSP = "/index.jsp";
        }else{
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro na execução da tarefa do assistente Max");
            error.setErrorSubtext("Verifique se houve um erro no pedido ao assistente Max");
            req.getSession().setAttribute("error", error);
            pageJSP = "/error.jsp";
        }
        
        return pageJSP;
    }
    
}
