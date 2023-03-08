package org.example;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class InstructorTest {
    @Test
    public void testLogin() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("instructorname")).thenReturn("Instructor1");
        when(mockResultSet.getString("contactno")).thenReturn("9999999999");
        when(mockResultSet.getString("instructorid")).thenReturn("CSE0001");

        Instructor instructor = new Instructor("CSE0001", "root");
        String result = instructor.login(mockConnection);

        verify(mockPreparedStatement).setString(1, "CSE0001");
        verify(mockPreparedStatement).setString(2, "root");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();

        assertEquals("CSE0001", result);
    }
    @Test
    public void testLoginfail() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getString("instructorname")).thenReturn("John Doe");
        when(mockResultSet.getString("contactno")).thenReturn("555-5555");
        when(mockResultSet.getString("instructorid")).thenReturn("1234");

        Instructor instructor = new Instructor("1234", "password");
        String result = instructor.login(mockConnection);

        verify(mockPreparedStatement).setString(1, "1234");
        verify(mockPreparedStatement).setString(2, "password");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();

        assertEquals("q", result);
    }

    @Test
    public void testOfferCourse() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Scanner mockScanner = mock(Scanner.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        Statement mockStatement = mock(Statement.class);

        when(mockScanner.nextInt()).thenReturn(2);
        when(mockScanner.next()).thenReturn("trash");
        String input = mockScanner.next();
        when(mockScanner.next()).thenReturn("q");


        Instructor instructor = new Instructor("1234", "password");
        instructor.offerCourse(mockConnection, mockScanner);
//
        verify(mockScanner, times(1)).nextInt();
        verify(mockScanner, times(2)).next();
//
    }

    @Test
    public void testOfferCoursePass() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Scanner mockScanner = mock(Scanner.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        Statement mockStatement = mock(Statement.class);


        when(mockScanner.nextInt()).thenReturn(2).thenReturn(4).thenReturn(4).thenReturn(5);
        when(mockScanner.next()).thenReturn("CS304");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("periodoffered")).thenReturn("2020S");
        when(Utilities.yearTermFinder(mockConnection)).thenReturn("2020S");
        when(mockPreparedStatement.executeUpdate()).thenReturn(0).thenReturn(1);

//        when(mockConnection.prepareStatement("INSERT INTO coursesoffered (courseid, instructorId, mincgpa, periodoffered) VALUES (?, ?, ?, ?)")).thenReturn(mockPreparedStatement);

        Instructor instructor = new Instructor("1234", "password");
        instructor.offerCourse(mockConnection, mockScanner);


        verify(mockScanner, times(4)).nextInt();
        verify(mockScanner, times(3)).next();
    }

    @Test
    public void testOfferCourseCatalog() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        Scanner mockScanner = mock(Scanner.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);
        Statement mockStatement = mock(Statement.class);

        when(mockScanner.nextInt()).thenReturn(1).thenReturn(3).thenReturn(5);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getString("periodoffered")).thenReturn("2020S");
        when(Utilities.yearTermFinder(mockConnection)).thenReturn("2020S");

//        when(mockConnection.prepareStatement("INSERT INTO coursesoffered (courseid, instructorId, mincgpa, periodoffered) VALUES (?, ?, ?, ?)")).thenReturn(mockPreparedStatement);

        Instructor instructor = new Instructor("1234", "password");
        instructor.offerCourse(mockConnection, mockScanner);


        verify(mockScanner, times(3)).nextInt();
    }





}
