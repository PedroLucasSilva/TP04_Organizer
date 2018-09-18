/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.cefetmg.inf.organizer.model.dao.impl;

import br.cefetmg.inf.organizer.model.dao.IItemDAO;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.db.ConnectionManager;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aline
 */
public class ItemDAO implements IItemDAO {

    @Override
    public boolean createItem(Item item) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "INSERT INTO item (nom_item, des_item, dat_item, idt_item, idt_estado, cod_email)"
                    + "VALUES(?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, item.getNameItem());
            preparedStatement.setString(2, item.getDescriptionItem());
            if (item.getDateItem() == null) {
                preparedStatement.setDate(3, null);
            } else {
                preparedStatement.setDate(3, new java.sql.Date(item.getDateItem().getTime()));
            }
            preparedStatement.setString(4, item.getIdentifierItem());
            preparedStatement.setString(5, item.getIdentifierStatus());
            preparedStatement.setString(6, item.getUser().getCodEmail());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

            return true;

        } catch (Exception e) {
            throw new PersistenceException(e.getMessage());
        }
    }

    @Override
    public boolean updateItem(Item item) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "UPDATE item SET nom_item=?, des_item=?, dat_item=?, idt_estado=?"
                    + " WHERE cod_email=? and seq_item=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, item.getNameItem());
            preparedStatement.setString(2, item.getDescriptionItem());
            if (item.getDateItem() == null) {
                preparedStatement.setDate(3, null);
            } else {
                preparedStatement.setDate(3, new java.sql.Date(item.getDateItem().getTime()));
            }
            if (item.getIdentifierStatus() == null) {
                preparedStatement.setString(4, null);
            } else {
                preparedStatement.setString(4, item.getIdentifierStatus());
            }

            preparedStatement.setString(5, item.getUser().getCodEmail());
            preparedStatement.setLong(6, item.getSeqItem());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            return true;
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteItem(Long idItem, User user) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "DELETE FROM item WHERE cod_email=? and seq_item=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getCodEmail());
            preparedStatement.setLong(2, idItem);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();

            return true;

        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Item> listAllItem(User user) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
 
            String sql = "SELECT * FROM item WHERE cod_email=? AND (idt_estado <> 'C' OR idt_estado IS NULL) ORDER BY dat_item";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getCodEmail());

            ResultSet result = preparedStatement.executeQuery();

            ArrayList<Item> listAllItem = null;

            if (result.next()) {
                listAllItem = new ArrayList<>();
                do {
                    Item item = new Item();
                    item.setSeqItem(result.getLong("seq_item"));
                    item.setNameItem(result.getString("nom_item"));
                    item.setDescriptionItem(result.getString("des_item"));
                    item.setIdentifierItem(result.getString("idt_item"));
                    item.setDateItem(result.getDate("dat_item"));
                    item.setIdentifierStatus(result.getString("idt_estado"));

                    listAllItem.add(item);
                } while (result.next());
            }

            result.close();
            preparedStatement.close();
            connection.close();

            return listAllItem;
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }

    }

    @Override
    public Item searchItemByName(String nomeItem) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT * FROM item WHERE nom_item=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, nomeItem);

            ResultSet result = preparedStatement.executeQuery();

            Item item = new Item();

            if (result.next()) {

                item.setSeqItem(result.getLong("seq_item"));
                item.setNameItem(result.getString("nom_item"));
                item.setDescriptionItem(result.getString("des_item"));
                item.setIdentifierItem(result.getString("idt_item"));
                item.setDateItem(result.getDate("dat_item"));
                item.setIdentifierStatus(result.getString("idt_estado"));

            }

            result.close();
            preparedStatement.close();
            connection.close();

            return item;

        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public Item searchItemById(Long idItem) throws PersistenceException {

        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT * FROM item WHERE seq_item=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, idItem);

            ResultSet result = preparedStatement.executeQuery();

            Item item = new Item();

            if (result.next()) {

                item.setSeqItem(result.getLong("seq_item"));
                item.setNameItem(result.getString("nom_item"));
                item.setDescriptionItem(result.getString("des_item"));
                item.setIdentifierItem(result.getString("idt_item"));
                item.setDateItem(result.getDate("dat_item"));
                item.setIdentifierStatus(result.getString("idt_estado"));

            }

            result.close();
            preparedStatement.close();
            connection.close();

            return item;

        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Item> searchItemByTag(List<Tag> tagList, User user) throws PersistenceException {

        try {
            //conditions of the sql's WHERE clause
            String sqlConditions = "";

            //number of sql's conditions (also number of tags in the ArrayList)
            int countConditions = tagList.size();

            //user's email
            String userEmail = user.getCodEmail();

            for (Tag tag : tagList) {
                //conditions in the format "seq_tag = ? OR seq_tag = ? OR ..."
                //*REMEMBER: change nom to seq after (or not)
                sqlConditions += "nom_tag = ? OR ";
            }

            //removing the last " OR " from the String
            sqlConditions = sqlConditions.substring(0, sqlConditions.lastIndexOf(" OR "));

            try (Connection connection = ConnectionManager.getInstance().getConnection()) {

                String sql = "SELECT A.* FROM item A JOIN item_tag B "
                        + "ON (A.seq_item = B.seq_item) JOIN tag C "
                        + "ON (B.seq_tag = C.seq_tag AND A.cod_email = C.cod_email) "
                        + "WHERE (" + sqlConditions + " AND A.cod_email = ?) "
                        + "GROUP BY 1 HAVING COUNT(*) = ? ORDER BY dat_item";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int i;
                    for (i = 1; i <= countConditions; i++) {
                        //setting the first ?'s as tag names
                        preparedStatement.setString(i, tagList.get(i - 1).getTagName());
                    }
                    preparedStatement.setString(i, userEmail);
                    preparedStatement.setInt(i + 1, countConditions);

                    try (ResultSet result = preparedStatement.executeQuery()) {
                        ArrayList<Item> itemList = null;
                        if (result.next()) {
                            itemList = new ArrayList<>();
                            do {
                                Item item = new Item();
                                item.setSeqItem(result.getLong("seq_item"));
                                item.setNameItem(result.getString("nom_item"));
                                item.setDescriptionItem(result.getString("des_item"));
                                item.setIdentifierItem(result.getString("idt_item"));
                                item.setDateItem(result.getDate("dat_item"));
                                item.setIdentifierStatus(result.getString("idt_estado"));

                                itemList.add(item);
                            } while (result.next());
                        }

                        return itemList;
                    }
                }
            }
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Item> searchItemByType(List<String> typeList, User user) throws PersistenceException {
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {

            //conditions of the sql's WHERE clause
            String sqlConditions = "";

            //number of conditions (also number of types in the ArrayList)
            int countConditions = typeList.size();

            //user's email
            String userEmail = user.getCodEmail();

            for (String type : typeList) {
                //filling the sqlConditions with a String in the format
                //"idt_item = ? OR idt_item = ? OR ..."
                sqlConditions += "idt_item = ? OR ";
            }
            //removing the last " OR " from the string
            sqlConditions = sqlConditions.substring(0, sqlConditions.lastIndexOf(" OR "));

            String sql = "SELECT * FROM item WHERE " + sqlConditions
                    + " AND cod_email = ? ORDER BY dat_item";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                int i;
                for (i = 1; i <= countConditions; i++) {
                    preparedStatement.setString(i, typeList.get(i - 1));
                }
                preparedStatement.setString(i, userEmail);

                try (ResultSet result = preparedStatement.executeQuery()) {

                    ArrayList<Item> itemList = null;
                    if (result.next()) {
                        itemList = new ArrayList<>();
                        do {
                            Item item = new Item();
                            item.setSeqItem(result.getLong("seq_item"));
                            item.setNameItem(result.getString("nom_item"));
                            item.setDescriptionItem(result.getString("des_item"));
                            item.setIdentifierItem(result.getString("idt_item"));
                            item.setDateItem(result.getDate("dat_item"));
                            item.setIdentifierStatus(result.getString("idt_estado"));

                            itemList.add(item);
                        } while (result.next());
                    }

                    return itemList;
                }
            }
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public ArrayList<Item> searchItemByTagAndType(List<Tag> tagList, List<String> typeList, User user) throws PersistenceException {

        try {
            //conditions of the sql's WHERE clause
            String sqlTagConditions = "";
            String sqlTypeConditions = "";

            //number of tag and type sql's conditions
            int countTagConditions = tagList.size();
            int countTypeConditions = typeList.size();

            //user's email
            String userEmail = user.getCodEmail();

            for (Tag tag : tagList) {
                //conditions in the format "seq_tag = ? OR seq_tag = ? OR ..."
                //*REMEMBER: change nom to seq after (or not)
                sqlTagConditions += "nom_tag = ? OR ";
            }

            for (String type : typeList) {
                //conditions in the format "idt_item = ? OR idt_item = ? OR ..."
                sqlTypeConditions += "idt_item = ? OR ";
            }

            //removing the last " OR " from the Strings
            sqlTagConditions = sqlTagConditions.substring(0, sqlTagConditions.lastIndexOf(" OR "));
            sqlTypeConditions = sqlTypeConditions.substring(0, sqlTypeConditions.lastIndexOf(" OR "));

            try (Connection connection = ConnectionManager.getInstance().getConnection()) {

                String sql = "SELECT A.* FROM item A JOIN item_tag B "
                        + "ON (A.seq_item = B.seq_item) JOIN tag C "
                        + "ON (B.seq_tag = C.seq_tag AND A.cod_email = C.cod_email) "
                        + "WHERE (" + sqlTagConditions + " AND (" + sqlTypeConditions
                        + ") AND A.cod_email = ?) GROUP BY 1 HAVING COUNT(*) = ? "
                        + "ORDER BY dat_item";

                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int i;
                    for (i = 1; i <= countTagConditions + countTypeConditions; i++) {
                        if (i <= countTagConditions) {
                            preparedStatement.setString(i, tagList.get(i - 1).getTagName());
                        } else {
                            preparedStatement.setString(i, typeList.get(i - countTagConditions - 1));
                        }
                    }
                    preparedStatement.setString(i, userEmail);
                    preparedStatement.setInt(i + 1, countTagConditions);

                    try (ResultSet result = preparedStatement.executeQuery()) {
                        ArrayList<Item> itemList = null;
                        if (result.next()) {
                            itemList = new ArrayList<>();
                            do {
                                Item item = new Item();
                                item.setSeqItem(result.getLong("seq_item"));
                                item.setNameItem(result.getString("nom_item"));
                                item.setDescriptionItem(result.getString("des_item"));
                                item.setIdentifierItem(result.getString("idt_item"));
                                item.setDateItem(result.getDate("dat_item"));
                                item.setIdentifierStatus(result.getString("idt_estado"));

                                itemList.add(item);
                            } while (result.next());
                        }

                        return itemList;
                    }
                }
            }
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }

    @Override
    public boolean checkIfItemAlreadyExistsToCreate(Item item) throws PersistenceException {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT nom_item FROM item WHERE nom_item=? and idt_item=? and cod_email=?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, item.getNameItem());
            preparedStatement.setString(2, item.getIdentifierItem());
            preparedStatement.setString(3, item.getUser().getCodEmail());
            
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return true;
            }

            result.close();
            preparedStatement.close();
            connection.close();

            return false;
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }
    
    @Override
    public boolean checkIfItemAlreadyExistsToUpdate(Item item) throws PersistenceException {
        try {
            Connection connection = ConnectionManager.getInstance().getConnection();
            String sql = "SELECT nom_item FROM item WHERE nom_item=? and idt_item=? and cod_email=? and seq_item <> ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, item.getNameItem());
            preparedStatement.setString(2, item.getIdentifierItem());
            preparedStatement.setString(3, item.getUser().getCodEmail());
            preparedStatement.setLong(4, item.getSeqItem());
            
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                return true;
            }

            result.close();
            preparedStatement.close();
            connection.close();

            return false;
        } catch (Exception ex) {
            throw new PersistenceException(ex.getMessage());
        }
    }
}

