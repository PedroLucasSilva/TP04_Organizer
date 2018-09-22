package br.cefetmg.inf.organizer.model.dao;

import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;

public interface ITagDAO {

    boolean createTag(Tag tag) throws PersistenceException;   
    Tag readTag(Tag tag) throws PersistenceException;   
    boolean updateTag(Tag tag) throws PersistenceException;    
    boolean updateTagId(Tag tag, Long id) throws PersistenceException;
    boolean deleteTag(Tag tag) throws PersistenceException;
    ArrayList<Tag> listAlltag(User user) throws PersistenceException;
    Long searchTagByName(String nomeTag, User user) throws PersistenceException;
    Tag searchTagById(Long idTag) throws PersistenceException;
}
