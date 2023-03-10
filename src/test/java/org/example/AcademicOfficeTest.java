package org.example;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class AcademicOfficeTest {
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
    public void login(){
        String input = "root\n";
        scanner = new Scanner(input);
        String sol = academicOffice.acadLogin(scanner);
        Assertions.assertEquals("nice",sol);

        input = "asdasdd\nq\n";
        scanner = new Scanner(input);
        sol = academicOffice.acadLogin(scanner);
        Assertions.assertEquals("q",sol);


        scanner.close();
    }
    @Test
    public void addStudents(){
        String input = "2020batch.csv\n";
        scanner = new Scanner(input);
        academicOffice.addNewStudents(connection,scanner);


        scanner.close();
    }

    @Test
    public void addInstructors(){
        String input = "instList.csv\n";
        scanner = new Scanner(input);
        academicOffice.addInstructors(connection,scanner);

        scanner.close();
    }

    @Test
    public void catalogChangeTest(){
        String input = "courseCat.csv\n";
        scanner = new Scanner(input);
        academicOffice.changeCatalog(connection,scanner);

        scanner.close();
    }

    @Test
    public void curriculumAddTest() throws CsvValidationException, SQLException, IOException {
        String input = "2020currinfo.csv\n";
        scanner = new Scanner(input);
        academicOffice.fill_both(connection,scanner);

        scanner.close();
    }
    @Test
    public void transcript() throws CsvValidationException, SQLException, IOException {
        String input = "2020csb1000";
        scanner = new Scanner(input);
        academicOffice.generateTranscript(connection,scanner,input);

        scanner.close();
    }
    @Test
    void viewGrades() throws SQLException {
        String input = "2020csb1000\n";
        scanner = new Scanner(input);
        academicOffice.viewGrades(connection, scanner);
        scanner.close();
    }



}
