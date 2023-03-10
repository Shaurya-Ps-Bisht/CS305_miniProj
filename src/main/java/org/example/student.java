package org.example;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWord;
import de.vandermeer.asciithemes.a7.A7_Grids;

import java.sql.*;
import java.io.*;
import java.util.*;
import de.vandermeer.asciitable.*;
import de.vandermeer.asciithemes.a7.*;
class student extends Person{

    private String studentID;
    private String password;

    private void setInstID(String instID){
        this.studentID = instID;
    }
    private void setPassword(String password){
        this.password=password;
    }
    public student(String studentID,String password) {
        setInstID(studentID);
        setPassword(password);
    }

    @Override
    public String login(Connection connection) throws SQLException {
        String toRettt= "";
        String sql = "SELECT * FROM students WHERE studentid = ? AND password = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, this.studentID);
        pstmt.setString(2, this.password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString("studentname");
            String contactNumber = rs.getString("contactno");
            System.out.println("Login successful! Welcome " + name + "!");
            toRettt= rs.getString("studentID");
        } else {
            toRettt= "q";
        }
        rs.close();
        pstmt.close();
        return toRettt;
    }

    public static double viewGrades(Connection connection, String studentID, boolean onlycg) throws SQLException {
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

        if(!onlycg) {
            AsciiTable at = new AsciiTable();
            at.getContext().setGrid(A7_Grids.minusBarPlusEquals());

            at.addRule();
            at.addRow("Course ID", "Academic Session", "Grade", "Type");
            at.addRule();
            String query = "SELECT * FROM coursestaken WHERE studentid = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, studentID);
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
        }


        int semsPassed =Utilities.semesterDifference(studentID.substring(0,4)+"W" ,Utilities.yearTermFinder(connection));
        double credsEarned=0;
        double cumilativePoints=0;

        if(semsPassed>0){
            for(int i =0; i<semsPassed; i++){

                double credsReg=0;
                double pointsSecured=0;

                String baseYear  = studentID.substring(0,4);
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
                pstmt3.setString(1,studentID);
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
            return cumilativePoints/credsEarned;
        }
        return 0;
    }

    public void changeInfo(Connection connection, Scanner scanner) throws SQLException {
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
                pstmt.setString(2, studentID);
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
                pstmt.setString(2, studentID);
                pstmt.executeUpdate();
                pstmt.close();
            }
            else if(choice == 3){
                break;
            }
            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);

    }

    public void enrollHaha(Connection connection, Scanner scanner) throws SQLException {
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

                    PreparedStatement pstmt = connection.prepareStatement("Select * from coursestaken where courseid =? and studentid=?");
                    pstmt.setString(1,cCode);
                    pstmt.setString(2,this.studentID);
                    ResultSet rs = pstmt.executeQuery();
                    if(rs.next()){
                        String grade= rs.getString("grade");
                        String period = rs.getString("periodtaken");
                        if(period.equals(Utilities.yearTermFinder(connection))){
                            System.out.println("Already enrolled for this course in this semester!");
                            continue;
                        }
                        else if(!(grade.equals("F")||grade.equals("E"))){
                            System.out.println("Already enrolled for this course in previous semesters!");
                            continue;
                        }
                    }

                    pstmt = connection.prepareStatement("Select * from coursesoffered where courseid =? and periodOffered=?");
                    pstmt.setString(1,cCode);
                    pstmt.setString(2,Utilities.yearTermFinder(connection));
                    rs = pstmt.executeQuery();
                    if(!rs.next()){
                        System.out.println("Not valid for this semester! Try again.");
                        continue;
                    }
                    else{
                        if(rs.getObject("mincgpa") != null && student.viewGrades(connection,this.studentID,true)< rs.getDouble("mincgpa")){System.out.println("CGPA is lacking, credit not possible.");continue;}
                        pstmt = connection.prepareStatement("Insert into coursesapproval (courseid,instructorid,studentid) values (?,?,?) on conflict (courseid,instructorid,studentid) do nothing");
                        pstmt.setString(1,cCode);
                        pstmt.setString(2,rs.getString("instructorid"));
                        pstmt.setString(3,studentID);
                        int doesItExist = pstmt.executeUpdate();
                        if(doesItExist>0){System.out.println("Request for credit has been sent.");}
                        else{System.out.println("Request exists already.");continue;}

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
    public void graduationCheck(Connection connection, Scanner scanner) throws SQLException {
        double neededPC= 0.00, neededGR= 0.00, neededPE= 0.00, neededCP= 0.00;
        double actualPC= 0.00, actualGR= 0.00, actualCP = 0.00,actualPE = 0.00;
        PreparedStatement pstmt = connection.prepareStatement("select * from curriculuminfo where year=? and branch=?");
        pstmt.setInt(1,Integer.parseInt(this.studentID.substring(0,4)));
        pstmt.setString(2, this.studentID.substring(4,7));
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            neededGR = rs.getDouble("gr");
            neededPC = rs.getDouble("pc");
            neededCP = rs.getDouble("cp");
            neededPE = rs.getDouble("pe");
        }

        pstmt = connection.prepareStatement("select * from coursestaken where type=? and studentid=?");
        pstmt.setString(1,"pc");
        pstmt.setString(2, this.studentID);
        rs = pstmt.executeQuery();
        while(rs.next()){
            if(rs.getObject("grade")!=null && !(rs.getString("grade").equals("F")||rs.getString("grade").equals("E"))){
                PreparedStatement innner = connection.prepareStatement("select * from coursecatalog where courseid=?");
                innner.setString(1, rs.getString("courseid"));
                ResultSet innnnerRes = innner.executeQuery();
                if(innnnerRes.next()){
                    actualPC = actualPC + innnnerRes.getDouble("creds");
                }
            }
        }
        pstmt = connection.prepareStatement("select * from coursestaken where type=? and studentid=?");
        pstmt.setString(1,"cp");
        pstmt.setString(2, this.studentID);
        rs = pstmt.executeQuery();
        while(rs.next()){
            if(rs.getObject("grade")!=null && !(rs.getString("grade").equals("F")||rs.getString("grade").equals("E"))){
                PreparedStatement innner = connection.prepareStatement("select * from coursecatalog where courseid=?");
                innner.setString(1, rs.getString("courseid"));
                ResultSet innnnerRes = innner.executeQuery();
                if(innnnerRes.next()){
                    actualCP = actualCP + innnnerRes.getDouble("creds");
                }
            }
        }
        pstmt = connection.prepareStatement("select * from coursestaken where type=? and studentid=?");
        pstmt.setString(1,"pe");
        pstmt.setString(2, this.studentID);
        rs = pstmt.executeQuery();
        while(rs.next()){
            if(rs.getObject("grade")!=null && !(rs.getString("grade").equals("F")||rs.getString("grade").equals("E"))){
                PreparedStatement innner = connection.prepareStatement("select * from coursecatalog where courseid=?");
                innner.setString(1, rs.getString("courseid"));
                ResultSet innnnerRes = innner.executeQuery();
                if(innnnerRes.next()){
                    actualPE = actualPE + innnnerRes.getDouble("creds");
                }
            }
        }
        pstmt = connection.prepareStatement("select * from coursestaken where type NOT IN (?, ?, ?) and studentid=?");
        pstmt.setString(1,"pe");
        pstmt.setString(2,"pc");
        pstmt.setString(3,"cp");
        pstmt.setString(4, this.studentID);
        rs = pstmt.executeQuery();
        while(rs.next()){
            if(rs.getObject("grade")!=null && !(rs.getString("grade").equals("F")||rs.getString("grade").equals("E"))){
                PreparedStatement innner = connection.prepareStatement("select * from coursecatalog where courseid=?");
                innner.setString(1, rs.getString("courseid"));
                ResultSet innnnerRes = innner.executeQuery();
                if(innnnerRes.next()){
                    actualGR = actualGR + innnnerRes.getDouble("creds");
                }
            }
        }

        System.out.println("Needed PC: "+neededPC);
        System.out.println("Needed PE: "+neededPE);
        System.out.println("Needed CP: "+neededCP);
        System.out.println("Needed GR: "+neededGR);

        System.out.println("\nYour PC: "+actualPC);
        System.out.println("Your PE: "+actualPE);
        System.out.println("Your CP: "+actualCP);
        System.out.println("Your GR: "+actualGR);

        if((actualPC<neededPC)||(actualGR<neededGR)||(actualPE<neededPE)||(actualCP<neededCP)){
            System.out.println("Not eligible for graduation");
        }
        else{
            System.out.println("Eligible for graduation");
        }




    }

}
