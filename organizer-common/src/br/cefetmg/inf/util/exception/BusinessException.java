/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.util.exception;

/**
 *
 * @author Aluno
 */
public class BusinessException extends Exception {

    public  BusinessException() {
    }


    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Exception exception) {
        super(msg, exception);
    }
}
