package br.cefetmg.inf.organizer.controller;


import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.organizer.model.service.IKeepUser;
import br.cefetmg.inf.organizer.proxy.KeepUserProxy;
import br.cefetmg.inf.util.ErrorObject;
import br.cefetmg.inf.util.PasswordCriptography;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CreateUser implements GenericProcess {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";
        String email = req.getParameter("email");
        String name = req.getParameter("name");
        String password = PasswordCriptography.generateMd5(req.getParameter("password"));

        User user = new User();

        user.setCodEmail(email);
        user.setUserName(name);
        user.setUserPassword(password);
        user.setUserPhoto(null);
        user.setCurrentTheme(1);

        IKeepUser keepUser = new KeepUserProxy();
        boolean success = keepUser.registerUser(user);

        if (!success) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro na criação do usuário");
            error.setErrorSubtext("Verifique se o usuário já existe ou se ocorreu um erro no preenchimento dos campos");
            req.getSession().setAttribute("error", error);
            pageJSP = "/errorLogin.jsp";
        } else {
            pageJSP = "/login.jsp";
        }

        return pageJSP;
    }

}
