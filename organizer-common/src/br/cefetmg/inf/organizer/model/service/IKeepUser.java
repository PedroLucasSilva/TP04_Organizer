
package br.cefetmg.inf.organizer.model.service;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;

public interface IKeepUser {
    
    boolean registerUser(User user) throws PersistenceException, BusinessException;
    User searchUser(User user) throws PersistenceException, BusinessException;
    boolean updateUser(User user) throws PersistenceException, BusinessException;
    boolean deleteAccount(User user) throws PersistenceException, BusinessException;
    User getUserLogin(String email, String password) throws PersistenceException, BusinessException;
}
