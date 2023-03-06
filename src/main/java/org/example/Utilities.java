package org.example;

import java.sql.Connection;
import java.sql.SQLException;

public class Utilities {
    public static int semesterDifference(String semester1, String semester2) {
        int year1 = Integer.parseInt(semester1.substring(0, 4));
        int year2 = Integer.parseInt(semester2.substring(0, 4));
        int semesterNum1 = semester1.charAt(4) == 'W' ? 0 : 1;
        int semesterNum2 = semester2.charAt(4) == 'W' ? 0 : 1;
        int numSemesters = (year2 - year1) * 2 + semesterNum2 - semesterNum1;

        return numSemesters;
    }

    public static String yearTermFinder(Connection connection) throws SQLException {
        return Period.get_period(connection).getString("year")+Period.get_period(connection).getString("term");
    }
}
