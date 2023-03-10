package org.example;
import com.opencsv.exceptions.CsvValidationException;
import org.example.dbconnect.dbFunctions;

import java.io.IOException;
import java.sql.*;
import java.util.*;
//todo
// Course and department compatibility while floating
//  check for core course not being actually floated by any instructor
//  min cgpa implement
//  TIME EVENT RESTRICTION FOR STUDENT FUCNTIONS
//   NNNNNNNNNNNNNeed check for cgpa even if in current sem but past grade submission

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        dbFunctions jodo = new dbFunctions();
        Connection connection = null;
        try{
            connection = jodo.connect_to_db();
            Main.actual(scanner,connection);


            System.out.println("\nGoodbye...");

            scanner.close();
            Statement stmt = connection.createStatement();
            stmt.close();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (CsvValidationException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void actual(Scanner scanner, Connection connection) throws SQLException, CsvValidationException, IOException {
        String[] subPeriods = {"stud_reg_catalogUpdate","currInfoUpload_floatCourses","autoEnroll_chooseElec","Ongoing Sem","gradeSubmission","semEnd"};
        int role=-1;
        boolean satisfied = false;

//            academicOffice.changeCatalog(connection);
//            academicOffice.fill_both(connection,scanner);
//            Instructor.offerCourse(connection,"69",scanner);

        do{
            System.out.println("=================================================================================================================================================");
            System.out.println("Enter 1. Student Login|| 2. Instructor Login || 3. Academic Office Login || 4. See current event and move to the next event || 5. to say goodbye ");
            role = scanner.nextInt();
//====================================================================================================================================================================================================================================================
            if (role == 1) {
                String studentID;
                String password;
                boolean loginSucc = false;
                student s = null;

                System.out.println("Welcome Student! Enter your student ID or press q to go back to select role screen: ");
                studentID = scanner.next();
                while(!loginSucc) {
                    if (studentID.equals("q")) {
                        break;
                    }
                    System.out.print("Your Password: ");
                    password = scanner.next();
                    s = new student(studentID,password);
                    studentID = s.login(connection);
                    if(studentID!="q"){
                        loginSucc = true;
                    }
                    else{
                        System.out.println("Invalid email or password! Enter your id again or enter q to go back to role screen");
                        studentID = scanner.next();
                        if (studentID.equals("q")) {
                            break;
                        }
                        else{
                            continue;
                        }
                    }
                }
                if(!loginSucc){continue;}
                String choice = "";
                while(!choice.equals("5")){
                    System.out.println("=======================================================================================");
                    System.out.println("1. To see courses and grades. 2. Update personal info. 3. Enroll for a course 4. Graduation Check 5. Logout");
                    choice = scanner.next();
                    switch (choice) {
                        case "1" ->                                      //To see courses and grades

                                student.viewGrades(connection, studentID,false);
                        case "2" ->                                     //Update personal info

                                s.changeInfo(connection, scanner);
                        case "3" -> {                                     //Enroll for a course
                            if (Period.get_period(connection).getString("sub_period").equals(subPeriods[2])) {
                                s.enrollHaha(connection, scanner);
                            } else {
                                System.out.println("Can not enroll for a course, period to enroll over.");
                            }
                        }

                        case "4" -> {                                     //Enroll for a course
                            s.graduationCheck(connection,scanner);
                        }
                        case "5" ->                                    //To go back to role select
                                choice = "4";
                        default -> System.out.println("Invalid Input");
                    }
                }
//====================================================================================================================================================================================================================================================
            } else if (role == 2) {
                String instID;
                String password;
                boolean loginSucc = false;
                Instructor instructor = null;

                System.out.println("Welcome Instructor! Enter your Instructor ID or press q to go back to select role screen: ");
                instID = scanner.next();
                while(!loginSucc) {
                    if (instID.equals("q")) {
                        break;
                    }
                    System.out.print("Your Password: ");
                    password = scanner.next();
                    instructor= new Instructor(instID,password);
                    instID = instructor.login(connection);
                    if(instID!="q"){
                        loginSucc = true;
                    }
                    else{
                        System.out.println("Invalid email or password! Enter your id again or enter q to go back to role screen");
                        instID = scanner.next();
                        if (instID.equals("q")) {
                            break;
                        }
                        else{
                            continue;
                        }
                    }
                }
                if(!loginSucc){continue;}
                String choice = "";

                while(!choice.equals("6")){
                    System.out.println("============================================================================================================================================");
                    System.out.println("1. To see grades of a student. 2. Update personal info. 3. Offer a course 4. Upload grades for a course 5. Approve credit requests 6. Logout");
                    choice = scanner.next();
                    switch (choice) {
                        case "1" ->                                                     //To see grades of a student
                                instructor.viewGrades(connection, scanner);
                        case "2" ->                                                     //Update personal info
                                instructor.changeinfo(connection, scanner);
                        case "3" -> {                                                   //Offer a course

                            if (Period.get_period(connection).getString("sub_period").equals(subPeriods[1])) {
                                instructor.offerCourse(connection, scanner);
                            } else {
                                System.out.println("Course floating period is over, can not float courses until next semester :O");
                            }
                        }
                        case "4" -> {                                                     //Upload grades for a course
                            if (Period.get_period(connection).getString("sub_period").equals(subPeriods[4])) {
                                instructor.uploadGrades(scanner, connection);
                            } else {
                                System.out.println("Course floating period is over, can not float courses until next semester :O");
                            }
                            instructor.uploadGrades(scanner, connection);
                        }
                        case "5" ->                                                     //Approve req
                                instructor.approveRequests(connection, scanner);
                        case "6" ->                                                     //To go back to role select
                                choice = "6";
                        default -> System.out.println("Invalid Input");

                    }
                }
            }
//====================================================================================================================================================================================================================================================
            else if (role == 3) {

                String acadlogtoken = academicOffice.acadLogin(scanner);               //login method
                int choiceAacad = -1;
                while(acadlogtoken.equals("nice")) {
                    System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                    System.out.println("1. To register new students. 2. Register instructors 3. Update course catalog 4. View grades of a student 5. Generate Transcript 6. To go back to role select");
                    choiceAacad = scanner.nextInt();
                    if (choiceAacad == 1) {                                     //register new students

                        if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]) && Period.get_period(connection).getString("term").equals("W"))
                        {
                            academicOffice.addNewStudents(connection,scanner);
                        }
                        else{
                            System.out.println("Registration period is over, can not register students for year "+ Period.get_period(connection).getString("year")+" anymore");
                        }
                    }
                    else if (choiceAacad == 2) {
                        if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]) && Period.get_period(connection).getString("term").equals("W"))
                        {
                            academicOffice.addInstructors(connection, scanner);
                        }
                        else{
                            System.out.println("Registration period is over, can not register instructors for year "+ Period.get_period(connection).getString("year")+" anymore");
                        }//register instructors

                    }
                    else if(choiceAacad==3){
                        if(Period.get_period(connection).getString("sub_period").equals(subPeriods[0]))
                        {
                            academicOffice.changeCatalog(connection, scanner);
                        }
                        else{
                            System.out.println("Catalog update period is over, can not update course catalog at the moment.");
                        }//Update course catalog

                    }
                    else if(choiceAacad==4){                                    //View grades of a student
                            academicOffice.viewGrades(connection,scanner);
                    }
                    else if(choiceAacad==5){                                    //Generate Transcript
                        System.out.println("Enter the student ID: ");
                        String studentid = scanner.next();
                        academicOffice.generateTranscript(connection,scanner,studentid);

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
                System.out.println("YEAR:"+ rs.getString("year")+" TERM:"+ rs.getString("term")+ " EVENT:"+ rs.getString("sub_period"));
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
    }
}