package br.cefetmg.inf.util.db;

import java.sql.Connection;
import java.sql.SQLException;


public interface ConnectionFactory {
    Connection getConnection() throws ClassNotFoundException, SQLException;
}
