/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.organizer.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aline
 */
public class ServletController extends HttpServlet {

   @Override
   protected void service(HttpServletRequest request,HttpServletResponse response) throws ServletException, UnsupportedEncodingException{
       request.setCharacterEncoding("UTF-8");

       String parameter = request.getParameter("process");//nome do campo que deve-se passar no form, seja por js ou html
       String className = "br.cefetmg.inf.organizer.controller." + parameter;
       
       try{
           Class classReference = Class.forName(className);
           GenericProcess genericProcess = (GenericProcess) classReference.newInstance();
           
           String pageJSP = genericProcess.execute(request, response);
           request.getRequestDispatcher(pageJSP).forward(request, response);
           
       }catch(Exception ex){
           ex.printStackTrace();
           throw new ServletException("Ocorreu um erro na execução "+ex.getMessage()); 
       }
   }

}
