package org.example;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Scanner;

public class InstructorwithDBTest {
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
    }
    @AfterEach
    public void tearDown() throws SQLException {
        scanner.close();
        connection.close();
    }

    @Test
    public void offercoursetest() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String input = "2\nCS306\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);

        // assert that the course has been added
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM coursesoffered WHERE courseid = ?");
        pstmt.setString(1, "CS306");
        ResultSet rs = pstmt.executeQuery();
        Assertions.assertTrue(rs.next());

        // delete the added course
        PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM coursesoffered WHERE courseid = ?");
        deleteStmt.setString(1, "CS306");
        deleteStmt.executeUpdate();
        deleteStmt.close();
    }
}
