package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepItem;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.proxy.KeepItemProxy;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.util.ErrorObject;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ItemFilter implements GenericProcess {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "/index.jsp";
        ArrayList<Tag> tagList = new ArrayList<>();
        ArrayList<String> typeList = new ArrayList<>();
        ArrayList<Item> itemList;
        String[] tags;
        String[] types;

        //booleans to check if the filter is being used
        boolean tagFiltering = false, typeFiltering = false;

        //getting values from the checkboxes
        tags = req.getParameterValues("tag");
        types = req.getParameterValues("tipo");

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "/login.jsp";
        }

        //checking if there is any tag to filter
        if (tags != null) {
            tagFiltering = true;
            for (String tagName : tags) {
                Tag tag = new Tag();
                tag.setTagName(tagName);
                tag.setUser(user);
                tagList.add(tag);
            }
        }

        //checking if there is any type to filter
        if (types != null) {
            typeFiltering = true;
            for (String type : types) {
                typeList.add(type);
            }
        }

        IKeepItem keepItem = new KeepItemProxy();

        try {
            if (tagFiltering && typeFiltering) {
                itemList = keepItem.searchItemByTagAndType(tagList, typeList, user);
            } else if (tagFiltering) {
                itemList = keepItem.searchItemByTag(tagList, user);
            } else if (typeFiltering) {
                itemList = keepItem.searchItemByType(typeList, user);
            } else {
                itemList = keepItem.listAllItem(user);
            }
        } catch (PersistenceException ex) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Filtragem indevida");
            error.setErrorDescription("Erro na filtragem de itens");
            error.setErrorSubtext("Tente novamente mais tarde ou entre em contato conosco");
            req.getSession().setAttribute("error", error);
            return "/error.jsp";
        }

        boolean concluidoExists = false;

        try {
            for (Tag tag : tagList) {
                if (tag.getTagName().equals("Concluidos") && (itemList != null)) {
                    concluidoExists = true;
                    for (Item item : new ArrayList<>(itemList)) {
                        if (item.getIdentifierItem().equals("TAR")
                                && item.getIdentifierStatus().equals("A")) {
                            itemList.remove(item);
                        }
                    }
                }
            }

            if (!concluidoExists && (itemList != null)) {
                for (Item item : new ArrayList<Item>(itemList)) {
                    if (item.getIdentifierItem().equals("TAR")
                            && item.getIdentifierStatus().equals("C")) {
                        itemList.remove(item);
                    }
                }
            }
        } catch (NullPointerException ex) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tarefa incorreta");
            error.setErrorDescription("Erro na filtragem de tarefas concluídas");
            error.setErrorSubtext("Tente novamente mais tarde ou entre em contato conosco");
            req.getSession().setAttribute("error", error);
            return "/error.jsp";
        }

        //maybe swap this to response if using ajax
        //or session (?)
        // Pedro - yes eu concordo, mas depois mudamos 
        if (itemList == null) {
            req.setAttribute("itemList", new ArrayList());
        } else {
            req.setAttribute("itemList", itemList);
        }

        IKeepTag keepTag = new KeepTagProxy();
        List<Tag> tagListAll = keepTag.listAlltag(user);
        if (tagListAll == null) {
            req.getSession().setAttribute("tagList", new ArrayList());
        } else {
            req.getSession().setAttribute("tagList", tagListAll);
        }

        return pageJSP;
    }

}
