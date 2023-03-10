package org.example;

import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.example.randomgenerator.registerationgen;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class PeriodTest {
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
    public void getPeriod() throws SQLException {
        Period.get_period(connection);
    }

    @Test
    public void setPeriod() throws SQLException, CsvValidationException, IOException {
        String input = "4\n1\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }
    @Test
    public void setPeriodChangeSem() throws SQLException, CsvValidationException, IOException {
        String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (2020));
        pstmt.setString(2, "S");
        pstmt.setString(3, "semEnd");
        pstmt.execute();


        String input = "4\n1\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }

    @Test
    public void setPeriodAutoEnroll() throws SQLException, CsvValidationException, IOException {
        String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (2020));
        pstmt.setString(2, "S");
        pstmt.setString(3, "currInfoUpload_floatCourses");
        pstmt.execute();


        String input = "4\n1\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }
    @Test
    public void setPeriodW() throws SQLException, CsvValidationException, IOException {
        String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (2020));
        pstmt.setString(2, "W");
        pstmt.setString(3, "semEnd");
        pstmt.execute();


        String input = "4\n1\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }
    @Test
    public void setPeriodS() throws SQLException, CsvValidationException, IOException {
        String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (2020));
        pstmt.setString(2, "S");
        pstmt.setString(3, "semEnd");
        pstmt.execute();


        String input = "4\n1\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }
    @Test
    public void studentDash() throws SQLException, CsvValidationException, IOException {
        String input = "1\n2020csb100222220\nroot\n2020csb1000\nroot\n1\n2\n3\n3\n3\n4\n5\n";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }

    @Test
    public void instDash() throws SQLException, CsvValidationException, IOException {
        String input = "2\n2020csb1000\nroot\nCSE0001\nroot\n1\n2020csb1000\n2\n3\n3\n4\nq\n5\n4\n6\n5";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }

    @Test
    public void acadDash() throws SQLException, CsvValidationException, IOException {
        String input = "3\nroot\n1\n2\n3\n4\n2020csb1000\n5\n2020csb1000\n6\n5";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }

    @Test
    public void breakage() throws SQLException, CsvValidationException, IOException {
        String input = "1\nq\n1\nasdsd\nroot\nq\n5";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);

        input = "2\nq\n2\nasdsd\nroot\nq\n5";
        scanner = new Scanner(input);
        Main.actual(scanner, connection);
    }

    @Test
    public void random() throws SQLException, CsvValidationException, IOException {
        String input = "researchPurposes.csv\n2077\n1000\n1001\n1002\n1003\n1004\n1005\n1006\n";
        scanner = new Scanner(input);
        registerationgen.actual(scanner);
    }



}
