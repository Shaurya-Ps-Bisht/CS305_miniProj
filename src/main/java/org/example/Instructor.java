package org.example;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a7.*;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Instructor extends Person{
    private String instID;
    private String password;

    private void setInstID(String instID){
        this.instID = instID;
    }
    private void setPassword(String password){
        this.password=password;
    }
    public Instructor(String instID,String password) {
        setInstID(instID);
        setPassword(password);
    }
    @Override
    public String login(Connection connection) throws SQLException {
        String toRettt= "";
        String sql = "SELECT * FROM instructors WHERE instructorid = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, this.instID);
        pstmt.setString(2, this.password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("instructorname");
            String contactNumber = rs.getString("contactno");
            System.out.println("Login successful! Welcome " + name + "!");
            toRettt= rs.getString("instructorid");
        } else {
            toRettt= "q";
        }
        rs.close();
        pstmt.close();
        return toRettt;
    }

    public void offerCourse(Connection connection, Scanner scanner) throws SQLException {
        int choice = -1;

        do{
            System.out.println("Select 1. To view the course catalog 2. To offer a course 3. View your offered courses for this semester 4. Deregister offered course 5. To go back dashboard");
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
                    if(rs.next() && rs.getString("periodoffered").equals(Utilities.yearTermFinder(connection))){
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
                pstmt.setString(2,this.instID);
                pstmt.setNull(3, Types.INTEGER );
                pstmt.setString(4, Utilities.yearTermFinder(connection));
                pstmt.execute();
                pstmt.close();
                System.out.println("Offered successfully");
            }
            else if(choice==3){
                String query = "SELECT * from coursesoffered where periodoffered = ? and instructorid=?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1,Utilities.yearTermFinder(connection));
                pstmt.setString(2,this.instID);
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
                pstmt.close();
            }

            else if(choice==4){
                System.out.println("Enter the course code: ");
                String removalCourse = scanner.next();
                PreparedStatement pstmt = connection.prepareStatement("Delete from coursesoffered where courseid=? and instructorid=? and periodoffered=?");
                pstmt.setString(1,removalCourse);
                pstmt.setString(2,this.instID);
                pstmt.setString(3,Utilities.yearTermFinder(connection));

                int rowDel = pstmt.executeUpdate();
                if(rowDel>0){System.out.println("Deregistered successfully");}
                else{System.out.println("Could not deregister this course. Check for typo or existence in this semester.");}
                pstmt.close();
            }

            else if(choice == 5){
                break;
            }

            else {System.out.println("Invalid Input: ");}
        }while(choice !=5);
    }
    public void changeinfo(Connection connection,Scanner scanner) throws SQLException {
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
                pstmt.setString(2, this.instID);
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
                pstmt.setString(2, this.instID);
                pstmt.executeUpdate();
                pstmt.close();
            }
            else if(choice == 3){
                break;
            }
            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);

    }

    public void approveRequests(Connection connection, Scanner scanner) throws SQLException {
        String choice = "";
        while(!choice.equals("4")){
            System.out.println("Select 1. To view requests 2. To approve all pending 3. Approve invidiually 4. Back to Dashboard");
            choice = scanner.next();
            PreparedStatement pstmt;

            switch (choice) {
                case "1":
                    AsciiTable at = new AsciiTable();
                    at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

                    at.addRule();
                    at.addRow("Course ID", "Student ID");
                    at.addRule();
                    String query = "SELECT * FROM coursesapproval WHERE instructorid = ?";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, this.instID);
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
                    pstmt.setString(1, this.instID);
                    rs = pstmt.executeQuery();

                    while(rs.next()){
                        PreparedStatement addToCourseTaken = connection.prepareStatement("Insert into coursestaken(courseid , studentid  ,periodtaken , grade ,type) values(?,?,?,?,?)");
                        addToCourseTaken.setString(1,rs.getString("courseid"));
                        addToCourseTaken.setString(2,rs.getString("studentid"));
                        addToCourseTaken.setString(3, Utilities.yearTermFinder(connection));
                        addToCourseTaken.setNull(4, Types.VARCHAR);

                        String courseType = "";
                        String sql = String.format("select %s from courseCatalog where TRIM(courseid) = TRIM(?)", rs.getString("studentid").substring(4,7));
                        PreparedStatement findType = connection.prepareStatement(sql);
                        findType.setString(1,rs.getString("courseid"));
                        ResultSet type = findType.executeQuery();
                        if(type.next()){courseType = type.getString(rs.getString("studentid").substring(4,7));}

                        addToCourseTaken.setString(5,courseType);
                        addToCourseTaken.execute();

                        addToCourseTaken = connection.prepareStatement("Delete from coursesapproval where courseid=? and studentid=?");
                        addToCourseTaken.setString(1,rs.getString("courseid"));
                        addToCourseTaken.setString(2,rs.getString("studentid"));
                        addToCourseTaken.execute();

                        addToCourseTaken.close();

                    }
                    pstmt.close();
                    break;

                case "3":
                    break;
            }
        }
    }

    public void viewGrades(Connection connection,Scanner scanner) throws SQLException {
        System.out.println("Enter the ID of the student whose grades you want to view:");
        String studentid = scanner.next();
        student.viewGrades(connection,scanner,studentid);
    }

    public void uploadGrades(Scanner scanner, Connection connection) throws IOException, CsvValidationException, SQLException {
        System.out.println("Enter the file containing grades for this semester or q to leave: ");
        String fileName = scanner.next();
        if (fileName.equals("q")) {
            return;
        }
        String[] nextLine;
        CSVReader reader = new CSVReader(new FileReader("./src/main/java/org/example/dataFiles/gradeInfo/"+ fileName));
        while ((nextLine = reader.readNext()) != null) {

            String courseid = nextLine[0];
            String studentid = nextLine[1];
            String grade = nextLine[2];

            PreparedStatement isOffered = connection.prepareStatement("select * from coursesoffered where courseid=? and instructorid=? and periodoffered=?");
            isOffered.setString(1, courseid.replaceAll("\\s", ""));
            isOffered.setString(2, this.instID);
            isOffered.setString(3,Utilities.yearTermFinder(connection));
            ResultSet rs = isOffered.executeQuery();
            if(rs.next()){
                PreparedStatement updateGrade = connection.prepareStatement("UPDATE coursestaken SET grade = ? WHERE courseid = ? AND  periodtaken=? and studentid=?");
                updateGrade.setString(1,grade);
                updateGrade.setString(2,courseid);
                updateGrade.setString(3,Utilities.yearTermFinder(connection));
                updateGrade.setString(4,studentid);
                updateGrade.execute();
                updateGrade.close();
            }
            else{
                System.out.println("Course was not offered by you this semester, typo?");
                return;
            }
            isOffered.close();
        }
    }
}
