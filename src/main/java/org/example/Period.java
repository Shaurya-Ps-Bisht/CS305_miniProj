package org.example;

import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
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
        if (!rs.next()) {
//            System.out.println("oh nooooooo");
        }
        return rs;
    }

    public static void set_period(Connection connection, ResultSet rs, Scanner scanner, String[] subPeriods) throws SQLException, CsvValidationException, IOException {

        System.out.println("1 for YES and forward event ||  OTHER for NO and go back to dashboard");
        System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
        String eventChoice = "";
        String year= rs.getString("year"), term= rs.getString("term"), sub_period = rs.getString("sub_period");
        int yearInt = Integer.parseInt(year);
        eventChoice = scanner.next();


        if(eventChoice.equals("1")){

            PreparedStatement pstmt= connection.prepareStatement("select * from curriculuminfo where year=?");
            pstmt.setInt(1,yearInt);
            rs = pstmt.executeQuery();

            while(term.equals("W") && sub_period.equals(subPeriods[1]) && !rs.next()) {
                System.out.println("Curriculum for the year has not been chosen, can not proceed to next event.");
                System.out.println("Choose curriculum? (Y/n)");
                String choice = scanner.next();

                if(choice.equals("y")||choice.equals("Y")){academicOffice.fill_both(connection,scanner);}
                else{
                    System.out.println("Event not updated");
                    return;
                }
            }


            String sql = "UPDATE currentPeriod SET year = ?, term = ?, sub_period = ? WHERE year = ? AND term = ? AND sub_period = ?";
            pstmt = connection.prepareStatement(sql);
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
                PreparedStatement pstmt2 = connection.prepareStatement("DELETE FROM coursesapproval");//Course approval pending requests were cleared for previous semester
                int rowsDeleted = pstmt2.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Course approval pending requests were cleared for previous semester");
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
        if(Period.get_period(connection).getString("sub_period").equals("autoEnroll_chooseElec")){
            Period.autoFillCourses(connection,year,term);
        }
    }

    public static void autoFillCourses(Connection connection, String year, String term) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("select * from autoenrollment where periodtoenroll=?");
        String pte =year+term;
        pstmt.setString(1,pte);

         ResultSet rs = pstmt.executeQuery();

         while(rs.next()){
             String course = rs.getString("courseid");
             String yearBranch = rs.getString("branchandyear");

             pstmt = connection.prepareStatement("SELECT studentid FROM students WHERE SUBSTRING(studentid, 1, 4) = ? AND SUBSTRING(studentid, 5, 3) = ?");
             pstmt.setString(1,yearBranch.substring(0,4));
             pstmt.setString(2,yearBranch.substring(4,7));

             ResultSet rsInner = pstmt.executeQuery();
             while(rsInner.next()){
                 String studid = rsInner.getString("studentid");
                 String courseType = "";

                 String sql = String.format("select %s from courseCatalog where TRIM(courseid) = TRIM(?)", yearBranch.substring(4,7));
                 PreparedStatement pstmt2 = connection.prepareStatement(sql);
                 pstmt2.setString(1,course);
                 ResultSet type = pstmt2.executeQuery();
                 if(type.next()){courseType = type.getString(yearBranch.substring(4,7));}

                 PreparedStatement pstmt3 = connection.prepareStatement("Insert into coursestaken (courseid , studentid , periodtaken , grade ,type) values(?,?,?,?,?) on conflict(courseid , studentid , periodtaken) do nothing ");
                 pstmt3.setString(1,course);
                 pstmt3.setString(2,studid);
                 pstmt3.setString(3,pte);
                 pstmt3.setNull(4,Types.VARCHAR);
                 pstmt3.setString(5,courseType);
                 pstmt3.execute();
                 pstmt3.close();
                 pstmt2.close();

             }
             pstmt.close();

         }
        System.out.println("Courses auto-filled");

    }
}
