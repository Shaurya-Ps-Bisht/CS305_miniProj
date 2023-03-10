package org.example;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class InstructorTest {
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
    public void offercoursetest() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String input = "2\ncsdsasdasdasdsa\nCS306\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);

        input = "2\nCS306\nq\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);

        input = "1\nsad\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);

        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM coursesoffered WHERE courseid = ? AND periodoffered = ? and instructorid=?");
        pstmt.setString(1, "CS306");
        pstmt.setString(3, "CSE0001");
        pstmt.setString(2, Utilities.yearTermFinder(connection));
        ResultSet rs = pstmt.executeQuery();
        Assertions.assertTrue(rs.next());

        input = "3\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);


        input = "4\nCS306\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);
        input = "4\nCS3sssssss06\n5\n";
        scanner = new Scanner(input);
        instructor.offerCourse(connection, scanner);

        pstmt = connection.prepareStatement("SELECT * FROM coursesoffered WHERE courseid = ? AND periodoffered = ? and instructorid=?");
        pstmt.setString(1, "CS306");
        pstmt.setString(3, "CSE0001");
        pstmt.setString(2, Utilities.yearTermFinder(connection));
        rs = pstmt.executeQuery();
        Assertions.assertFalse(rs.next());

        scanner.close();
    }

    @Test
    void Login() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String sol = instructor.login(connection);
        Assertions.assertEquals("CSE0001",sol);

        Instructor instructorfail = new Instructor("CSE00012", "root");
        sol = instructorfail.login(connection);
        Assertions.assertEquals("q",sol);
    }

    @Test
    void viewGrades() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String input = "2020csb1000\n";
        scanner = new Scanner(input);
        instructor.viewGrades(connection, scanner);
        scanner.close();
    }

    @Test
    void changeInfo() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String input = "1\n2031341024\n2\nnotroot\n3\n";
        scanner = new Scanner(input);
        instructor.changeinfo(connection, scanner);
        scanner.close();
    }

    @Test
    void approveReq() throws SQLException {
        Instructor instructor = new Instructor("CSE0001", "root");
        String input = "1\n2\n4\n";
        scanner = new Scanner(input);
        instructor.approveRequests(connection, scanner);
        scanner.close();
    }

    @Test
    void uploadGrade() throws SQLException, CsvValidationException, IOException {
        String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (2020));
        pstmt.setString(2, "S");
        pstmt.setString(3, "autoEnroll_chooseElec");
        pstmt.execute();
        String input = "gradeInfo.csv\n";
        Instructor instructor = new Instructor("CSE0001", "root");
        scanner = new Scanner(input);
        instructor.uploadGrades(scanner,connection);
        scanner.close();
    }





}
