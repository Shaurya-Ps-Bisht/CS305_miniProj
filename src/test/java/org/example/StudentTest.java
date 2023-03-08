package org.example;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class StudentTest {
    @Test
    public void testLogin() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("studentname")).thenReturn("Pankaj kumar");
        when(mockResultSet.getString("contactno")).thenReturn("9999999999");
        when(mockResultSet.getString("studentID")).thenReturn("2020csb1000");

        student s = new student("2020csb1000", "root");
        String result = s.login(mockConnection);

        verify(mockPreparedStatement).setString(1, "2020csb1000");
        verify(mockPreparedStatement).setString(2, "root");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();

        assertEquals("2020csb1000", result);
    }
    @Test
    public void testLoginFail() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        PreparedStatement mockPreparedStatement = mock(PreparedStatement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);
        when(mockResultSet.getString("studentname")).thenReturn("Pankaj kumar");
        when(mockResultSet.getString("contactno")).thenReturn("9999999999");
        when(mockResultSet.getString("studentID")).thenReturn("2020csb1000");

        student s = new student("2020csb1000", "root");
        String result = s.login(mockConnection);

        verify(mockPreparedStatement).setString(1, "2020csb1000");
        verify(mockPreparedStatement).setString(2, "root");
        verify(mockResultSet).close();
        verify(mockPreparedStatement).close();

        assertEquals("q", result);
    }
}
