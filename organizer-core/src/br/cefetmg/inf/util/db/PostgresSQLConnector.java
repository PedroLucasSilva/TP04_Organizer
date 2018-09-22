package br.cefetmg.inf.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class PostgresSQLConnector implements ConnectionFactory{
    public final String databaseDriver = "org.postgresql.Driver";
    private final static String databaseURL = "jdbc:postgresql://localhost:5432/organizer";
    private final static String user = "postgres";
    private final static String password = "123456";
    
    @Override
    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName(databaseDriver);
        return DriverManager.getConnection(databaseURL, user, password);
    }
    
}
