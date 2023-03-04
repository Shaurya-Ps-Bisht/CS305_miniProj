package org.example.dbconnect;
import java.sql.*;
import java.io.*;
import java.util.*;
public class dbFunctions {
    public Connection connect_to_db(){
        Connection cnct = null;
        try{
            String password = "root";
            String user = "postgres";
            String url = "jdbc:postgresql://localhost:5432/aisehi";
            cnct = DriverManager.getConnection(url, user, password);
            if (cnct != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }

        }catch(Exception e){
            System.out.println(e);

        }
        return cnct;
    }

    public static void main(String[] args) {
        dbFunctions db= new dbFunctions();
        Connection connection = null;

        try {
            connection = db.connect_to_db();
            String query = "";

            try {
                File file = new File("./src/main/java/org/example/dbconnect/random.sql");
                Scanner scan = new Scanner(file);
                while (scan.hasNextLine()) {
                    query = query.concat(scan.nextLine() + " ");
                }
                scan.close();
            } catch (IOException e) {
                System.out.print(e.getLocalizedMessage());
            }
            Statement stmt = connection.createStatement();
            stmt.execute(query);
            stmt.close();


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                assert connection != null;
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
