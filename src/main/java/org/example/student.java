package org.example;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import de.vandermeer.asciithemes.a7.A7_Grids;

import java.sql.*;
import java.io.*;
import java.util.*;
import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a7.*;
public class student {

    public static String studentLogin(Connection connection) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int studlog=-1;
        System.out.println("Welcome Student! Enter your student ID or press q to go back to select role screen: ");
        do {
            String mail = scanner.next();
            if(mail.equals("q")){
                break;
            }
            System.out.print("Your Password: ");
            String password = scanner.next();

            String sql = "SELECT * FROM students WHERE studentid = ? AND password = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, mail);
            pstmt.setString(2, password);


            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("studentname");
                String contactNumber = rs.getString("contactno");
                System.out.println("Login successful! Welcome " + name + "!");
                studlog=0;
                return rs.getString("studentid");

            } else {

                System.out.println("Invalid email or password! Enter your id again or enter q to go back to role screen");
            }
            rs.close();
            pstmt.close();
        }while(studlog!=0);
        return "q";

    }

    public static void viewGrades(Connection connection, Scanner scanner, String studentid) throws SQLException {
        Map<String, Double> gradeToPoint = new HashMap<>();
        gradeToPoint.put("A", 10.0);
        gradeToPoint.put("A-", 9.0);
        gradeToPoint.put("B", 8.0);
        gradeToPoint.put("B-", 7.0);
        gradeToPoint.put("C", 6.0);
        gradeToPoint.put("C-", 5.0);
        gradeToPoint.put("D", 4.0);
        gradeToPoint.put("E", 2.0);
        gradeToPoint.put("F", 0.0);

        AsciiTable at = new AsciiTable();
        at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

        at.addRule();
        at.addRow("Course ID", "Academic Session", "Grade", "Type");
        at.addRule();
        String query = "SELECT * FROM coursestaken WHERE studentid = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1,studentid);
        ResultSet rs = pstmt.executeQuery();


        while (rs.next()) {
            String grade = rs.getString("grade");
            if (grade == null) {
                grade = "";  // replace null with an empty string
            }

            at.addRow(rs.getString("courseid"),
                    rs.getString("periodtaken"),
                    grade,
                    rs.getString("type"));
            at.addRule();
        }

        at.setPaddingLeftRight(1);
        at.getRenderer().setCWC(new CWC_LongestWord());
        System.out.println(at.render());
        pstmt.close();


        int semsPassed =Utilities.semesterDifference(studentid.substring(0,4)+"W" ,Utilities.yearTermFinder(connection));
        double credsEarned=0;
        double cumilativePoints=0;

        if(semsPassed>0){
            for(int i =0; i<semsPassed; i++){

                double credsReg=0;
                double pointsSecured=0;

                String baseYear  = studentid.substring(0,4);
                int intBaseYear = Integer.parseInt(baseYear);
                int intCurrentYear = intBaseYear+ (semsPassed/2);
                String currentYearTerm ="";

                if(i%2==1){
                    currentYearTerm = intCurrentYear+ "S";
                }
                else{
                    currentYearTerm = intCurrentYear+ "W";
                }

                PreparedStatement pstmt3 = connection.prepareStatement("select * from coursestaken where studentid=? and periodtaken=?");
                pstmt3.setString(1,studentid);
                pstmt3.setString(2,currentYearTerm);
                ResultSet rs3 = pstmt3.executeQuery();


                while(rs3.next()){
                    String courseID = rs3.getString("courseid");
                    String grade = rs3.getString("grade");
                    double creds;
                    PreparedStatement getCred = connection.prepareStatement("select * from coursecatalog where courseid=?");
                    getCred.setString(1,courseID);
                    ResultSet credRes = getCred.executeQuery();
                    if(credRes.next()){
                        creds = credRes.getDouble("creds");
                        credsReg = credsReg + creds;
                        pointsSecured = pointsSecured + (creds*gradeToPoint.getOrDefault(grade, 0.0));
                        if(gradeToPoint.getOrDefault(grade, 0.0)>2.0){
                            credsEarned = credsEarned+ creds;
                            cumilativePoints = cumilativePoints + (creds*gradeToPoint.getOrDefault(grade, 0.0));
                        }
                    }
                }
                System.out.println("Semester "+(i+1)+ ": "+ pointsSecured/credsReg);
            }
            System.out.println("CGPA: "+cumilativePoints/credsEarned);
        }
    }
    public static void sgpaCalc(String studentid, int sempassed, Connection connection) throws SQLException {


    }

    public static void changeInfo(Connection connection, String studentid, Scanner scanner) throws SQLException {
        int choice = -1;

        do{
            System.out.println("Select 1. To change contact number 2. To change password. 3. To go back dashboard");
            choice = scanner.nextInt();
            if(choice ==1) {
                String newPassword;
                System.out.println("Enter the new contact number: ");
                newPassword = scanner.next();
                System.out.println("ok");
                String query = "UPDATE students SET contactno = ? WHERE studentid = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, studentid);
                pstmt.executeUpdate();
                pstmt.close();

            }
            else if(choice ==2) {
                String newPassword;
                System.out.println("Choose your new password: ");
                newPassword = scanner.next();
                String query = "UPDATE students SET password = ? WHERE studentid = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, newPassword);
                pstmt.setString(2, studentid);
                pstmt.executeUpdate();
                pstmt.close();
            }
            else if(choice == 3){
                break;
            }
            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);



    }

    public static void enrollHaha(Connection connection, Scanner scanner, String studentid) throws SQLException {
        int choice = -1;

        do{
            System.out.println("Select 1. To view the Offered Courses for this semester 2. To credit a course 3. To go back dashboard");
            choice = scanner.nextInt();
            if(choice ==1) {
                String query = "SELECT * from coursesoffered where periodoffered = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1,Utilities.yearTermFinder(connection));
                ResultSet rs = pstmt.executeQuery();

                AsciiTable at = new AsciiTable();
                at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

                at.addRule();
                at.addRow("Course ID", "Instructor ID", "CGPA cutoff");
                at.addRule();

                while (rs.next()) {
                    at.addRow(rs.getString("courseid"),
                            rs.getString("instructorid"),
                            rs.getInt("minCGPA"));
                    at.addRule();
                }

                at.setPaddingLeftRight(1);
                at.getRenderer().setCWC(new CWC_LongestWord());
                System.out.println(at.render());


            }
            else if(choice ==2) {

                String cCode;
                while(1==1){
                    System.out.println("Enter the course code or enter q to return to dashboard: ");
                    cCode = scanner.next();
                    if(cCode.equals("q")){break;}

                    PreparedStatement pstmt = connection.prepareStatement("Select * from coursesoffered where courseid =? and periodOffered=?");
                    pstmt.setString(1,cCode);
                    pstmt.setString(2,Utilities.yearTermFinder(connection));
                    ResultSet rs = pstmt.executeQuery();
                    if(!rs.next()){
                        System.out.println("Not valid for this semester! Try again.");
                        continue;
                    }
                    else{
                        pstmt = connection.prepareStatement("Insert into coursesapproval (courseid,instructorid,studentid) values (?,?,?)");
                        pstmt.setString(1,cCode);
                        pstmt.setString(2,rs.getString("instructorid"));
                        pstmt.setString(3,studentid);
                        pstmt.execute();
                        System.out.println("Request for credit has been sent.");
                    }

                    pstmt.close();
                    break;

                }
                if(cCode.equals("q")){break;}

            }
            else if(choice == 3){
                break;
            }

            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);
    }
}
