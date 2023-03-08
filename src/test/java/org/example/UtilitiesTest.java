package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import java.sql.*;
;

public class UtilitiesTest {
    @Test
    void semDiff() {
        int difference = Utilities.semesterDifference("2021W", "2022S");
        Assertions.assertEquals(3, difference);

        difference = Utilities.semesterDifference("2020S", "2023S");
        Assertions.assertEquals(6, difference);

        difference = Utilities.semesterDifference("2021W", "2021S");
        Assertions.assertEquals(1, difference);

        difference = Utilities.semesterDifference("2021W", "2024W");
        Assertions.assertEquals(6, difference);
    }

//    @Test
//    void yearTermFinderTests() throws SQLException {
//        Connection connection = Mockito.mock(Connection.class);
//        ResultSet resultSet = Mockito.mock(ResultSet.class);
//
//        // Mock the behavior of the result set to return some test values
//        Mockito.when(resultSet.getString("year")).thenReturn("2020");
//        Mockito.when(resultSet.getString("term")).thenReturn("W");
//
//        // Mock the behavior of the Period.get_period method to return the mock result set
//        Mockito.when(Period.get_period(connection)).thenReturn(resultSet);
//
//        // Call the method under test
//        String result = Utilities.yearTermFinder(connection);
//
//        // Verify that the correct SQL query was executed and the correct value was returned
//        Mockito.verify(connection).prepareStatement("SELECT * FROM currentPeriod");
//        Assertions.assertEquals("2020W", result);
//
//    }
}
