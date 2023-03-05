package org.example;

import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a7.*;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Instructor {

    public static String instructorLogin(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int studlog=-1;
        System.out.println("Welcome Instructor! Enter your Instructor ID or press q to go back to select role screen: ");
        do {
            String mail = scanner.next();
            if(mail.equals("q")){
                break;
            }
            System.out.print("Your Password: ");
            String password = scanner.next();

            String sql = "SELECT * FROM instructors WHERE instructorid = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, mail);
            pstmt.setString(2, password);


            ResultSet rs = pstmt.executeQuery();

            // Checking if the result set has any rows
            if (rs.next()) {
                String name = rs.getString("instructorname");
                String contactNumber = rs.getString("contactno");
                System.out.println("Login successful! Welcome " + name + "!");
                studlog=0;
                return rs.getString("instructorid");

            } else {

                System.out.println("Invalid email or password! Enter your id again or enter q to go back to role screen");
            }
            rs.close();
            pstmt.close();
        }while(studlog!=0);
        return "q";

    }

    public static void offerCourse(Connection connection, String instID, Scanner scanner) throws SQLException {
        int choice = -1;

        do{
            System.out.println("Select 1. To view the course catalog 2. To offer a course 3. To go back dashboard");
            choice = scanner.nextInt();
            if(choice ==1) {
                String query = "SELECT courseid, coursename, ltps, creds, csb, mcb, eeb, chb, mmb, ceb, meb FROM coursecatalog";
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(query);



                AsciiTable at = new AsciiTable();
                at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

                at.addRule();
                at.addRow("Course ID", "Course Name", "LTPs", "Credits", "CSB", "MCB", "EEB", "CHB", "MMB", "CEB", "MEB");
                at.addRule();



                while (rs.next()) {
                    at.addRow(rs.getString("courseid"),
                            rs.getString("coursename"),
                            rs.getString("ltps"),
                            rs.getInt("creds"),
                            rs.getString("csb"),
                            rs.getString("mcb"),
                            rs.getString("eeb"),
                            rs.getString("chb"),
                            rs.getString("mmb"),
                            rs.getString("ceb"),
                            rs.getString("meb"));
                    at.addRule();
                }

                at.setPaddingLeftRight(1);
                at.getRenderer().setCWC(new CWC_LongestWord());
                System.out.println(at.render());

            }
            else if(choice ==2) {

                String cCode;
                while(1==1){
                    System.out.println("Enter the course code or enter q to return to dashboard: ");        //to add course code and department check !!!!
                    cCode = scanner.next();
                    if(cCode.equals("q")){break;}

                    PreparedStatement pstmt = connection.prepareStatement("Select courseid, periodoffered from coursesoffered where courseid =?");
                    pstmt.setString(1,cCode);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next() && rs.getString("periodoffered").equals(Period.get_period(connection).getString("year")+Period.get_period(connection).getString("term"))){
                        System.out.println("Already offered course! Try again.");
                        continue;
                    }

                    pstmt = connection.prepareStatement("Select courseid from coursecatalog where courseid =?");
                    pstmt.setString(1,cCode);
                    rs = pstmt.executeQuery();
                    if(!rs.next()){
                        System.out.println("Course does not exist in the catalog! Try again.");
                        continue;
                    }
                    pstmt.close();
                    break;

                }
                if(cCode.equals("q")){break;}

//                System.out.println("Set a minimum CGPA requirement: ");
//                int minCGPA = scanner.nextInt();

                PreparedStatement pstmt = connection.prepareStatement("INSERT INTO coursesoffered (courseid, instructorId, mincgpa, periodoffered) VALUES (?, ?, ?, ?)");
                pstmt.setString(1,cCode);
                pstmt.setString(2,instID);
                pstmt.setNull(3, Types.INTEGER );
                pstmt.setString(4, Period.get_period(connection).getString("year")+Period.get_period(connection).getString("term"));

                pstmt.execute();
                pstmt.close();
                System.out.println("Offered successfully");


            }
            else if(choice == 3){
                break;
            }

            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);
    }
    public static void changeinfo(Connection connection, String instID, Scanner scanner) throws SQLException {
        int choice = -1;

        do{
            System.out.println("Select 1. To change contact number 2. To change password. 3. To go back dashboard");
            choice = scanner.nextInt();
            if(choice ==1) {
                String newPassword;
                System.out.println("Enter the new contact number: ");
                newPassword = scanner.next();
                System.out.println("ok");
                String query = "UPDATE instructors SET contactno = ? WHERE instructorid = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, instID);
                pstmt.executeUpdate();
                pstmt.close();

            }
            else if(choice ==2) {
                String newPassword;
                System.out.println("Choose your new password: ");
                newPassword = scanner.next();
                String query = "UPDATE instructors SET password = ? WHERE instructorid = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, instID);
                pstmt.executeUpdate();
                pstmt.close();
            }
            else if(choice == 3){
                break;
            }
            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);

    }

    public static void approveRequests(Connection connection, Scanner scanner, String instructorid) throws SQLException {
        String choice = "";
        while(!choice.equals("4")){
            System.out.println("Select 1. To view requests 2. To approve all pending 3. Approve invidiually 4. Back to Dashboard");
            choice = scanner.next();

            switch (choice) {
                case "1":
                    AsciiTable at = new AsciiTable();
                    at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

                    at.addRule();
                    at.addRow("Course ID", "Student ID");
                    at.addRule();
                    String query = "SELECT * FROM coursesapproval WHERE instructorid = ?";
                    PreparedStatement pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, instructorid);
                    ResultSet rs = pstmt.executeQuery();


                    while (rs.next()) {
                        at.addRow(rs.getString("courseid"),
                                rs.getString("studentid"));
                        at.addRule();
                    }

                    at.setPaddingLeftRight(1);
                    at.getRenderer().setCWC(new CWC_LongestWord());
                    System.out.println(at.render());
                    pstmt.close();

                    break;
                case "2":
                    query = "SELECT * FROM coursesapproval WHERE instructorid = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, instructorid);
                    rs = pstmt.executeQuery();

                    while(rs.next()){

                    }

                    break;
                case "3":

                    break;
            }
        }
    }

    public static void viewGrades(){

    }

    public static void uploadGrades(){

    }
}
