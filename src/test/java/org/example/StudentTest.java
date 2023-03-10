package org.example;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class StudentTest {
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
    void Login() throws SQLException {
        student s = new student("2020csb1000", "root");
        String sol = s.login(connection);
        Assertions.assertEquals("2020csb1000",sol);

        Instructor instructorfail = new Instructor("CSE00012", "root");
        sol = instructorfail.login(connection);
        Assertions.assertEquals("q",sol);
    }

    @Test
    void viewGrades() throws SQLException {

        String input = "2020csb1000";
        student.viewGrades(connection, input, false);
    }

    @Test
    void changeInfo() throws SQLException {
        student s = new student("2020csb1000", "root");
        String input = "1\n2031341024\n2\nnotroot\n3\n";
        scanner = new Scanner(input);
        s.changeInfo(connection, scanner);
    }

    @Test
    void enrollCheck() throws SQLException {
        student s = new student("2020csb1000", "root");
        String input = "1\n2\nBM101\nGE102\nCS301\nq\n";
        scanner = new Scanner(input);
        s.enrollHaha(connection, scanner);
    }

    @Test
    void graduationTest() throws SQLException {
        student s = new student("2020csb1000", "root");
        String input = "";
        scanner = new Scanner(input);
        s.graduationCheck(connection, scanner);
    }

}
