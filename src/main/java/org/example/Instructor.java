package org.example;

import java.sql.Connection;
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

            String sql = "SELECT * FROM students WHERE instructorid = ? AND password = ?";
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

    public static void offerCourse(){

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
            else {System.out.println("Invalid Input: ");}
        }while(choice !=3);

    }

    public static void viewGrades(){

    }

    public static void uploadGrades(){

    }
}
