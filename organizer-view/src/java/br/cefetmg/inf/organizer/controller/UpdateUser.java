
package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepTag;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepTagProxy;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.ErrorObject;
import br.cefetmg.inf.util.PasswordCriptography;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateUser implements GenericProcess {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";

        String name = req.getParameter("name");
        String password = req.getParameter("password");

        User user = (User) req.getSession().getAttribute("user");
        User tempUser = new User();
        
        if (name.isEmpty() || name == null) {
            name = user.getUserName();
        }
        
        if (password.isEmpty() || password == null) {
            password = user.getUserPassword();
        } else {
            password = PasswordCriptography.generateMd5(password);
        }

        tempUser.setCodEmail(user.getCodEmail());
        tempUser.setUserName(name);
        tempUser.setUserPassword(password);
        tempUser.setCurrentTheme(user.getCurrentTheme());

        IKeepUser keepUser = new KeepUserProxy();
        boolean success = keepUser.updateUser(tempUser);
        if (!success) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro na alteração do usuário");
            error.setErrorSubtext("Verifique se os campos estão preenchidos corretamente ou se você está logado");
            req.getSession().setAttribute("error", error);
            pageJSP = "/error.jsp";
        } else {
            req.getSession().setAttribute("user",tempUser);
            
            IKeepTag keepTag = new KeepTagProxy();
            List<Tag> tagList = keepTag.listAlltag(user);
            if (tagList == null) {
                req.getSession().setAttribute("tagList", new ArrayList());
            } else {
                req.getSession().setAttribute("tagList", tagList);
            }
            
            pageJSP = "/configuracoes.jsp";
        }
        
        return pageJSP;
    }

}
