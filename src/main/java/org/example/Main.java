package org.example;
import com.opencsv.exceptions.CsvValidationException;
import org.example.dbconnect.dbFunctions;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        dbFunctions jodo = new dbFunctions();
        String[] subPeriods = {"stud_reg_catalogUpdate","currInfoUpload_floatCourses","autoEnroll_chooseElec","Ongoing Sem","gradeSubmission","semEnd"};
        Connection connection = null;
        try{
            connection = jodo.connect_to_db();
            int role=-1;
            boolean satisfied = false;
            Scanner scanner = new Scanner(System.in);
//            academicOffice.changeCatalog(connection);
//            academicOffice.fill_both(connection,scanner);

            do{
                System.out.println("Enter 1 to login as a student|| 2 to login as an instructor || 3 to login as Academic Office || 4 to see current even and move to the next event || 5 to say goodbye ");
                role = scanner.nextInt();
//====================================================================================================================================================================================================================================================
                if (role == 1) {
                    //student studentNice = new student();
                    String studentID = student.studentLogin(connection);                    //login method
                    int choiceStud=-1;
                    while(studentID != "q"){
                        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                        System.out.println("1. To see courses and grades. 2. Update personal info. 3. Enroll for a course 4. To go back to role select");
                        choiceStud = scanner.nextInt();
                        if (choiceStud == 1) {                                     //To see courses and grades

                        }
                        else if(choiceStud==2){                                    //Update personal info
                            student.changeInfo(connection, studentID, scanner);
                        }
                        else if(choiceStud==3){                                    //Enroll for a course

                        }
                        else if(choiceStud ==4){                                   //To go back to role select
                            studentID = "q";
                        }
                        else{
                            System.out.println("Invalid Input");
                        }
                    }
//====================================================================================================================================================================================================================================================
                } else if (role == 2) {
                    String instID = Instructor.instructorLogin(connection);
                    int choiceInst=-1;
                    while(instID != "q"){
                        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                        System.out.println("1. To see grades of a student. 2. Update personal info. 3. Offer a course 4. Upload grades for a course 5. To go back to role select");
                        choiceInst = scanner.nextInt();
                        if (choiceInst == 1) {                                     //To see grades of a student

                        }
                        else if(choiceInst==2){                                    //Update personal info
                            Instructor.changeinfo(connection, instID, scanner);
                        }
                        else if(choiceInst==3){                                    //Offer a course

                        }
                        else if(choiceInst ==4){                                   //Upload grades for a course
                            instID = "q";
                        }
                        else if(choiceInst ==5){                                   //To go back to role select
                            instID = "q";
                        }
                        else{
                            System.out.println("Invalid Input");
                        }
                    }
                }
//====================================================================================================================================================================================================================================================
                else if (role == 3) {

                    String acadlogtoken = academicOffice.acadLogin();               //login method
                    int choiceAacad = -1;
                    while(acadlogtoken.equals("nice")) {
                        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                        System.out.println("1. To register new students. 2. Register instructors 3 .Update course catalog. 4. View grades of a student 5. Generate Transcript 6. To go back to role select");
                        choiceAacad = scanner.nextInt();
                        if (choiceAacad == 1) {                                     //register new students

                            if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]))
                            {
                                academicOffice.addNewStudents(connection,scanner);
                            }
                            else{
                                System.out.println("Registration period is over, can not register students for year "+ Period.get_period(connection).getString("year")+" anymore");
                            }
                        }
                        else if (choiceAacad == 2) {
                            if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]))
                            {
                                academicOffice.addInstructors(connection);
                            }
                            else{
                                System.out.println("Registration period is over, can not register instructors for year "+ Period.get_period(connection).getString("year")+" anymore");
                            }//register instructors

                        }
                        else if(choiceAacad==3){
                            if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]))
                            {
                                academicOffice.changeCatalog(connection);
                            }
                            else{
                                System.out.println("Catalog update period is over, can not update course catalog for year "+ Period.get_period(connection).getString("year")+" anymore");
                            }//Update course catalog

                        }
                        else if(choiceAacad==4){                                    //View grades of a student

                        }
                        else if(choiceAacad==5){                                    //Generate Transcript

                        }
                        else if(choiceAacad ==6){                                   //To go back to role select
                            acadlogtoken = "q";
                        }
                        else{
                            System.out.println("Invalid Input");
                        }
                    }
                }
//====================================================================================================================================================================================================================================================
                else if (role == 4) {
                    ResultSet rs = Period.get_period(connection);
                    System.out.println("oooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo");
                    System.out.println("YEAR: "+ rs.getString("year")+" TERM "+ rs.getString("term")+ " EVENT: "+ rs.getString("sub_period"));
                    Period.set_period(connection, rs, scanner, subPeriods);
                }
//====================================================================================================================================================================================================================================================
                else if (role == 5) {
                    satisfied=true;
                }
//====================================================================================================================================================================================================================================================
                else {
                    System.out.println("Invalid input. ");
                }
            }while(!satisfied);

            System.out.println("\nGoodbye...");

            scanner.close();
            Statement stmt = connection.createStatement();
            stmt.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }


    }
}