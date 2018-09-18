package br.cefetmg.inf.organizer.model.dao;

import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;

/**
 *
 * @author aline
 */
public interface IUserDAO {
    
    boolean createUser(User user) throws PersistenceException;
    User readUser(User user) throws PersistenceException; //temp, não sei se vou usar
    boolean updateUser(User user) throws PersistenceException;
    boolean deleteUser(User user) throws PersistenceException;
    User getUserLogin(String email, String password) throws PersistenceException;

}
