package org.example;

import org.apache.commons.lang3.ArrayUtils;

import java.sql.Connection;
import java.sql.*;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Period {
    public static ResultSet get_period(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement();
        String query = "SELECT * FROM currentperiod";
        ResultSet rs = stmt.executeQuery(query);

        int yearInt=0;
        if (rs.next()) {
            return rs;

        } else {
            System.out.println("oh nooooooo");
            return rs;
        }
    }

    public static void set_period(Connection connection, ResultSet rs, Scanner scanner, String[] subPeriods) throws SQLException {

        System.out.println("1 for YES and forward event ||  OTHER for NO and go back to dashboard");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        int eventChoice = -1;
        String year= rs.getString("year"), term= rs.getString("term"), sub_period = rs.getString("sub_period");
        int yearInt = Integer.parseInt(year);
        eventChoice = scanner.nextInt();


        if(eventChoice==1){
            String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ? WHERE year = ? AND term = ? AND sub_period = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            if(sub_period.equals(subPeriods[5])){
                if(term.equals("S")){
                    pstmt.setInt(1, (yearInt+1));
                    pstmt.setString(2, "W");
                    pstmt.setString(3, subPeriods[0]);
                    pstmt.setInt(4, yearInt);
                    pstmt.setString(5, "S");
                    pstmt.setString(6, subPeriods[5]);
                }
                else{
                    pstmt.setInt(1, (yearInt));
                    pstmt.setString(2, "S");
                    pstmt.setString(3, subPeriods[0]);
                    pstmt.setInt(4, yearInt);
                    pstmt.setString(5, "W");
                    pstmt.setString(6, subPeriods[5]);
                }
            }
            else{
                int indexxx = ArrayUtils.indexOf(subPeriods,sub_period);
                pstmt.setInt(1, (yearInt));
                pstmt.setString(2, term);
                pstmt.setString(3, subPeriods[indexxx+1]);
                pstmt.setInt(4, yearInt);
                pstmt.setString(5, term);
                pstmt.setString(6, subPeriods[indexxx]);
            }
            pstmt.execute();
            pstmt.close();
        }
    }
}
