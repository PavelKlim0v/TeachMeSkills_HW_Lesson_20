package com.teachmeskills.lesson_20.task_1.connector;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class MySQLConnector {

    private static final String MYSQL_DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    private static final String DATA_BASE_CONNECTIVITY_PROPERTY_FILE_PATH = "src/resources/database.properties";
    private Properties properties;
    private Connection connection;

    public MySQLConnector() {
        init();
    }

    public void init() {
        try {
            properties = loadProperties();
            Class.forName(MYSQL_DRIVER_CLASS_NAME);

        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load MySQL Driver!");
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection(int url) throws IOException, SQLException {
        if (connection != null && !connection.isClosed()) {
            return connection;
        }
        try {
            if (url == 1) {
                return connection = DriverManager.getConnection(
                        properties.getProperty("url1"),
                        properties.getProperty("user"),
                        properties.getProperty("password")
                );
            } else if (url == 2) {
                return connection = DriverManager.getConnection(
                        properties.getProperty("url2"),
                        properties.getProperty("user"),
                        properties.getProperty("password")
                );
            }
        } catch (SQLException e) {
            System.out.println("Unable to ger connection to MySQL schema!");
            e.printStackTrace();
        }

        return null;
    }

    private Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Paths.get(DATA_BASE_CONNECTIVITY_PROPERTY_FILE_PATH))) {
            properties.load(input);
        }

        return properties;
    }

}
