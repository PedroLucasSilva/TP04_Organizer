package br.cefetmg.inf.organizer.controller;

import br.cefetmg.inf.util.ErrorObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLogout implements GenericProcess {

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String pageJSP = "";

        HttpSession session = req.getSession(false);

        if (session == null) {
            ErrorObject error = new ErrorObject();
            error.setErrorName("Tente novamente");
            error.setErrorDescription("Erro no logout");
            error.setErrorSubtext("Verifique se você está logado");
            req.getSession().setAttribute("error", error);
            pageJSP = "/error.jsp";
        } else {
            session.invalidate();
            pageJSP = "/login.jsp";
        }

        return pageJSP;
    }

}
