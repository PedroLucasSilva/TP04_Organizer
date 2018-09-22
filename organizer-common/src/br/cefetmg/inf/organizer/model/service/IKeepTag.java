package br.cefetmg.inf.organizer.model.service;

import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.exception.BusinessException;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.util.ArrayList;

public interface IKeepTag {

    boolean createTag(Tag tag) throws PersistenceException, BusinessException;
    Tag readTag(Tag tag) throws PersistenceException, BusinessException;
    boolean updateTag(Tag tag) throws PersistenceException, BusinessException;
    boolean updateTagId(Tag tag, Long id) throws PersistenceException, BusinessException;
    boolean deleteTag(Tag tag) throws PersistenceException, BusinessException;
    ArrayList<Tag> listAlltag(User user) throws PersistenceException, BusinessException;   
    Long searchTagByName(String nomeTag, User user) throws PersistenceException, BusinessException;    
    Tag searchTagById(Long idTag) throws PersistenceException, BusinessException;
}
