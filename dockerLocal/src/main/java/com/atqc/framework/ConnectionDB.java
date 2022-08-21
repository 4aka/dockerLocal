package com.atqc.framework;

import java.sql.*;

import static com.atqc.framework.Config.*;

public class ConnectionDB {

    private final String url = "jdbc:postgresql://" + dbHost + "/" + dbName;
    Connection connection;

    private Connection createConnection() {
        try {
            connection = DriverManager
                    .getConnection(url, dbUser, dbPass);
        } catch (SQLException e) {
            System.err.println("Connection Failed");
            e.printStackTrace();
        }
        return connection;
    }

    private Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            connection = createConnection();
        }
        return connection;
    }

    public String getOAIId(String email) {
        connection = getConnection();
        Statement statement;
        String result = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            String sql = "SELECT identifier FROM PUBLIC.options_user WHERE email = '" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) result = rs.getString("identifier");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public String getValueOfDBField(String filedName, String email) {
        connection = getConnection();
        Statement statement;
        String result = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            String sql = "SELECT " + filedName + " FROM PUBLIC.options_user WHERE email = '" + email + "'";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) result = rs.getString(filedName);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
