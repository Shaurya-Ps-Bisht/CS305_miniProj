package org.example;
import java.sql.*;
import java.io.*;
import java.util.*;
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

    public static void viewGrades(Connection connection, String studentid){

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

    public static void enrollHaha(Connection connection){

    }
}
