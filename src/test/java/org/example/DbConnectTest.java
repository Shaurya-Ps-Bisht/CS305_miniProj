package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import org.example.dbconnect.dbFunctions;


public class DbConnectTest {
    private static Connection connection;
    private static Scanner scanner;

    @BeforeEach
    public void setUp() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/aisehi";
        String user = "postgres";
        String password = "root";
        connection = DriverManager.getConnection(url, user, password);
        if (connection != null) {
            System.out.println("Connected to the PostgreSQL server successfully.");
        } else {
            System.out.println("Failed to make connection!");
        };
        connection.setAutoCommit(false);
    }
    @AfterEach
    public void tearDown() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
        connection.close();

    }

    @Test
    public void tableCreateTest() throws SQLException {
        dbFunctions.createTables(connection);
    }

    @Test
    public void getConn() throws SQLException {
        dbFunctions test = new dbFunctions();
        Connection cnct = test.connect_to_db();
    }
}
