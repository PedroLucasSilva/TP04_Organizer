package br.cefetmg.inf.organizer.model.dao.impl;

import br.cefetmg.inf.organizer.model.dao.IMaxDAO;
import br.cefetmg.inf.organizer.model.domain.Item;
import br.cefetmg.inf.organizer.model.domain.MaxDataObject;
import br.cefetmg.inf.organizer.model.domain.Tag;
import br.cefetmg.inf.organizer.model.domain.User;
import br.cefetmg.inf.util.db.ConnectionManager;
import br.cefetmg.inf.util.exception.PersistenceException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MaxDAO implements IMaxDAO {

    @Override
    public boolean updateAllItems(MaxDataObject maxDataObject) throws PersistenceException {

        Date date;
        java.sql.Date itemDate;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            String sql;

            sql = "DELETE FROM item WHERE cod_email =?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, maxDataObject.getUser().getCodEmail());
                preparedStatement.execute();
            }

            sql = "INSERT INTO item ( nom_item, des_item, dat_item, idt_item, idt_estado, cod_email)"
                    + " VALUES ( ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < maxDataObject.getItemsID().length; i++) {
                    if (maxDataObject.getItemsDate()[i] == null || maxDataObject.getItemsDate()[i].equals("") || maxDataObject.getItemsDate()[i].isEmpty()) {
                        itemDate = null;
                    } else {
                        date = formatter.parse(maxDataObject.getItemsDate()[i]);
                        itemDate = new java.sql.Date(date.getTime());
                    }

                    preparedStatement.setString(1, maxDataObject.getItemsName()[i]);
                    preparedStatement.setString(2, maxDataObject.getItemsDescription()[i]);
                    preparedStatement.setDate(3, itemDate);
                    preparedStatement.setString(4, maxDataObject.getItemsType()[i]);
                    if(maxDataObject.getItemsType()[i].equals("TAR")){
                        preparedStatement.setString(5, "A");
                    } else {
                        preparedStatement.setString(5, null);
                    }
                    preparedStatement.setString(6, maxDataObject.getUser().getCodEmail());

                    preparedStatement.execute();

                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public boolean updateAllTags(MaxDataObject maxDataObject) throws PersistenceException {

        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            String sql;

            sql = "DELETE FROM tag WHERE cod_email =?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, maxDataObject.getUser().getCodEmail());
                preparedStatement.execute();
            }

            sql = "INSERT INTO tag (nom_tag, cod_email)"
                    + " VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < maxDataObject.getTagsID().length; i++) {
                    //preparedStatement.setString(1, tagsID[i]);
                    preparedStatement.setString(1, maxDataObject.getTagsName()[i]);
                    preparedStatement.setString(2, maxDataObject.getUser().getCodEmail());

                    preparedStatement.execute();
                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PersistenceException(ex.getMessage(), ex);
        }

    }

    @Override
    public boolean updateAllItemTag(MaxDataObject maxDataObject) throws PersistenceException {

        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            String sql;

            sql = "DELETE FROM item_tag WHERE seq_item IN (SELECT seq_item FROM item WHERE cod_email = ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, maxDataObject.getUser().getCodEmail());
                preparedStatement.execute();
            }

            sql = "INSERT INTO item_tag (seq_item, seq_tag)"
                    + " VALUES (?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                for (int i = 0; i < maxDataObject.getTagsItems().length; i++) {
                    preparedStatement.setInt(1, Integer.valueOf(maxDataObject.getTagsItems()[i]));
                    preparedStatement.setInt(2, Integer.valueOf(maxDataObject.getItemsTags()[i]));

                    preparedStatement.execute();
                }

                return true;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new PersistenceException(ex.getMessage(), ex);
        }
    }

    @Override
    public ArrayList<Item> loadItems(User user) throws PersistenceException {
        ArrayList<Item> listAllItem = null;
        ItemDAO itemControl = new ItemDAO();
        if(itemControl.listAllItem( user ) != null)
            listAllItem = itemControl.listAllItem( user );
        return listAllItem; 
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Tag> loadTags(User user) throws PersistenceException {
        ArrayList<Tag> listAlltag = null;
        TagDAO tagControl = new TagDAO();
        listAlltag = tagControl.listAlltag( user );
        return listAlltag;
    }

    @Override
    public ArrayList<String> loadTagsItems(User user) throws PersistenceException {
        ArrayList<String> listAllTagsItems = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            
            String sql = 
            "SELECT A.nom_item, C.nom_tag FROM item A JOIN item_tag B ON (A.seq_item = B.seq_item) "
                    + "JOIN tag C ON (B.seq_tag = C.seq_tag) WHERE A.cod_email = ? AND C.cod_email = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                
                preparedStatement.setString(1, user.getCodEmail());
                preparedStatement.setString(2, user.getCodEmail());
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        do {
                            listAllTagsItems.add( result.getString("nom_item") );
                        } while (result.next());
                    }
                }
            }           
        } catch (Exception ex) {
            //exception
        }
        return listAllTagsItems;
    }

    @Override
    public ArrayList<String> loadItemsTags(User user) throws PersistenceException {
        ArrayList<String> listAllItemsTags = new ArrayList<>();
        try (Connection connection = ConnectionManager.getInstance().getConnection()) {
            
            String sql = 
            "SELECT A.nom_item, C.nom_tag FROM item A JOIN item_tag B ON (A.seq_item = B.seq_item) "
                    + "JOIN tag C ON (B.seq_tag = C.seq_tag) WHERE A.cod_email = ? AND C.cod_email = ?";
            
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                
                preparedStatement.setString(1, user.getCodEmail());
                preparedStatement.setString(2, user.getCodEmail());
                
                try (ResultSet result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        do {
                            listAllItemsTags.add( result.getString("nom_tag") );
                        } while (result.next());
                    }
                }
            }           
        } catch (Exception ex) {
            //exception
        }
        return listAllItemsTags;
    }

}
