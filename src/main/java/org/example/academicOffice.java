package org.example;
import java.sql.*;
import java.io.*;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class academicOffice {


    public static String acadLogin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome Academic Office! Enter the password to login or press q to go back to select role screen: ");

        do {
            String password = scanner.next();
            if(password.equals("q")){
                break;
            }
            else if(password.equals("root")){
                return "nice";
            }
            else{
                System.out.println("Invalid password. Enter the password to login or press q to go back to select role screen: ");
            }
        }while(1==1);
        return "q";
    }
    public static void addNewStudents(Connection connection,Scanner scanner){
        int success = 0;
        System.out.println("Enter the name of the input file to register students: ");
        String fileName = scanner.next();

        try(BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/org/example/dataFiles/Student_reg_data/" + fileName))){
            String line;
            int i =0;
            while((line = reader.readLine()) != null){
                if(i==0){i++;continue;}
                String[] values = line.split(",");
                String studentId = values[0].replaceAll("\"","");
                String studentName = values[1].replaceAll("\"","");
                String contactInfo = values[2].replaceAll("\"","");
                String password = values[3].replaceAll("\"","");
                String sql = "INSERT INTO students (studentid, studentname, contactno, password) VALUES (?, ?, ?, ?) on conflict (studentid) do nothing;";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, studentId);
                    statement.setString(2, studentName);
                    statement.setString(3, contactInfo);
                    statement.setString(4, password);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            success=1;
        }
        if(success==0){System.out.println("Succesfully added entries from "+ fileName+"\n");}
    }
    public static void addInstructors(Connection connection){
        Scanner scanner = new Scanner(System.in);
        int success = 0;
        System.out.println("Enter the name of the input file to register instructors: ");
        String fileName = scanner.next();

        try(BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/org/example/dataFiles/Inst_reg_data/" + fileName))){
            String line;
            int i =0;
            while((line = reader.readLine()) != null){
                if(i==0){i++;continue;}
                String[] values = line.split(",");
                String instructorId = values[0].replaceAll("\"","");
                String instructorName = values[1].replaceAll("\"","");
                String contactInfo = values[2].replaceAll("\"","");
                String password = values[3].replaceAll("\"","");
                String sql = "INSERT INTO instructors (instructorId, instructorName, contactno, password) VALUES (?, ?, ?, ?) on conflict (instructorId) do nothing;";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, instructorId);
                    statement.setString(2, instructorName);
                    statement.setString(3, contactInfo);
                    statement.setString(4, password);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            success=1;
        }
        if(success==0){System.out.println("Succesfully added entries from "+ fileName);}
    }
    public static void changeCatalog(Connection connection){
        Scanner scanner = new Scanner(System.in);
        int success=0;
        System.out.println("Enter the name of the input file for course catalog: ");
        String fileName = scanner.next();

        try(BufferedReader reader = new BufferedReader(new FileReader("./src/main/java/org/example/dataFiles/courseCatalog/" + fileName))){
            String line;
            int i =0;
            while((line = reader.readLine()) != null){
                if(i==0){i++;continue;}
                String[] values = line.split(",");
                String courseid = values[0].replaceAll("\\s","");
                String coursename = values[1];
                String ltpsc = (values.length > 2 && !values[2].isEmpty()) ? values[2] : "";
                ltpsc = ltpsc.replaceAll("\\s", "");
                String csb = (values.length > 4 && !values[4].isEmpty()) ? values[4] : "";
                String mcb = (values.length > 5 && !values[5].isEmpty()) ? values[5] : "";
                String eeb = (values.length > 6 && !values[6].isEmpty()) ? values[6] : "" ;
                String chb = (values.length > 7 && !values[7].isEmpty()) ? values[7] : "";
                String mmb = (values.length > 8 && !values[8].isEmpty()) ? values[8] : "";
                String ceb = (values.length > 9 && !values[9].isEmpty()) ? values[9] : "";
                String meb = (values.length > 10 && !values[10].isEmpty()) ? values[10] : "";
                String[] ltpsc_split = ltpsc.split("(?<=.)(?=-[^-]*$)");
                String ltps = ltpsc_split[0];
                String creds = ltpsc_split[1].replaceAll("-","");
                double credint = Double.parseDouble(creds);

                String sql = "INSERT INTO coursecatalog (courseid ,coursename , ltps , creds ,csb,mcb, eeb,chb,mmb, ceb,meb) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " + "ON CONFLICT (courseid) DO UPDATE SET coursename = EXCLUDED.coursename, ltps = EXCLUDED.ltps, creds = EXCLUDED.creds, csb = EXCLUDED.csb, eeb = EXCLUDED.eeb, chb = EXCLUDED.chb, mmb = EXCLUDED.mmb, ceb = EXCLUDED.ceb, meb = EXCLUDED.meb, mcb = EXCLUDED.mcb; " ;

                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, courseid);
                    statement.setString(2, coursename);
                    statement.setString(3, ltps);
                    statement.setDouble(4, credint);
                    statement.setString(5, csb);
                    statement.setString(6, mcb);
                    statement.setString(7, eeb);
                    statement.setString(8, chb);
                    statement.setString(9, mmb);
                    statement.setString(10, ceb);
                    statement.setString(11, meb);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
            success=1;
        }

        if(success==0){System.out.println("Succesfully Updated catalog with "+ fileName+"\n");}

    }

    public static void fill_both(Connection connection, Scanner scanner) throws CsvValidationException, IOException, SQLException {
        System.out.println("Enter the name of the input file for curriculum info or q to leave: ");
        String fileName = scanner.next();
        if(fileName.equals("q")){return;}
        double PC;

        CSVReader reader = new CSVReader(new FileReader("./src/main/java/org/example/dataFiles/currInfo/"+ fileName));
        String[] nextLine;
        int i=0;
        while ((nextLine = reader.readNext()) != null) {
            // Get data from CSV row
            if(i==0){i++; continue;}
            String branch = nextLine[0];
            String sem1 = nextLine[1];
            String sem2 = nextLine[2];
            String sem3 = nextLine[3];
            String sem4 = nextLine[4];
            String sem5 = nextLine[5];
            String sem6 = nextLine[6];
            String sem7 = nextLine[7];
            String sem8 = nextLine[8];
            String creditStructure = nextLine[9];

            String[] creditStructureList  = creditStructure.split("-");
            double total = Double.parseDouble(creditStructureList[0]);
            double pcpe = Double.parseDouble(creditStructureList[1]);
            double cp = Double.parseDouble(creditStructureList[2]);
            int year = Period.get_period(connection).getInt("year");

            System.out.println(total+" "+pcpe+" "+cp);

            String coursesAllSem = (sem1+","+sem2+","+sem3+","+sem4+","+sem5+","+sem6+","+sem7+","+sem8).replaceAll("\\s","");
            String[] allCourses = coursesAllSem.split(",");
            PC= find_PC_Total(connection,allCourses,nextLine[0]);
            System.out.println(PC);

            fill_curriculuminfo(connection, year, total-pcpe, PC, pcpe-PC, cp, nextLine);
            fill_autoEnroll(connection,nextLine,year);


        }
        reader.close();


    }

    public static void fill_curriculuminfo(Connection connection, int year, double gr, double pc, double pe, double cp, String[] values) throws SQLException {
        String sql = "insert into curriculuminfo(year,branch,gr,pc,pe,cp) values(?,?,?,?,?,?) ON CONFLICT (year,branch) DO UPDATE SET gr = EXCLUDED.gr,pc = EXCLUDED.pc,pe= EXCLUDED.pe,cp = EXCLUDED.cp ";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, (year));
        pstmt.setString(2, (values[0]));
        pstmt.setDouble(3, (gr));
        pstmt.setDouble(4, (pc));
        pstmt.setDouble(5, (pe));
        pstmt.setDouble(6, (cp));
        pstmt.execute();
        pstmt.close();
    }
    public static void fill_autoEnroll(Connection connection, String[] values, int year) throws SQLException {
        for(int i=1; i<=8; i++){
            String[] courses = values[i].replaceAll("\\s","").split(",");
            for(String course: courses){
                String sql = "INSERT INTO autoenrollment (courseid ,branchandyear , periodtoenroll) values (?,?,?) ON CONFLICT (courseid ,branchandyear , periodtoenroll) DO UPDATE SET courseid = EXCLUDED.courseid,branchandyear = EXCLUDED.branchandyear,periodtoenroll = EXCLUDED.periodtoenroll ";
                PreparedStatement pstmt = connection.prepareStatement(sql);
                String periodToEnroll = "";
                String yearBranch = year+values[0];
                if(i%2==0){
                    periodToEnroll = (year + (int)Math.ceil(i / 2.0)-1)+"S";
                }
                else{
                    periodToEnroll = (year + (int)Math.ceil(i / 2.0)-1)+"W";
                }
                pstmt.setString(1,(course));
                pstmt.setString(2,(yearBranch));
                pstmt.setString(3,(periodToEnroll));
                pstmt.execute();
                pstmt.close();
            }
        }
    }

    public static double find_PC_Total(Connection connection, String [] allCourses,String branch) throws SQLException {
        double total_creds=0;

        for(String course:allCourses){
            String sql = String.format("select creds from coursecatalog where TRIM(courseid) = TRIM(?) and %s='pc'", branch);
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, (course));
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()){
                total_creds= total_creds + rs.getDouble("creds");
            }
        }
        return total_creds;
    }

//    SELECT * FROM students
//    WHERE SUBSTRING(student_id, 1, 4) = '2020' AND
//    SUBSTRING(student_id, 5, 3) IN ('csb', 'ee');

    public static void generateTranscript(){

    }
}
