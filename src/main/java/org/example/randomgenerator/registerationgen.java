package org.example.randomgenerator;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class registerationgen {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        registerationgen.actual(scanner);
    }

    public static void actual(Scanner scanner) throws IOException {
        System.out.print("Enter File name for output CSV file along with .csv: ");
        String outputFile = scanner.next();

        try (CSVWriter writer = new CSVWriter(new FileWriter("./src/main/java/org/example/randomgenerator/" + outputFile))) {

            writer.writeNext(new String[]{"studentid", "studentname", "contactinfo", "password"});
            System.out.print("Enter the batch of registering students: ");
            int year = scanner.nextInt();
            // Sample first and last names
            String[] firstNames = {"Abhay", "Abhijit", "Abhinav", "Abhishek", "Adarsh", "Adesh", "Adhar", "Adi", "Aditya", "Ajay", "Ajinkya", "Ajish", "Ajit", "Akasha", "Akhil", "Akshat", "Amarjit", "Amarsinh", "Ambika", "Amit", "Ammar", "Amrik", "Amrutlal", "Anand", "Ananya", "Angad", "Aniket", "Anil", "Anirban", "Aniruddha", "Ankit", "Anuj", "Anup", "Anupam", "Anurag", "Apoorva", "Arhaan", "Aritra", "Arjun", "Arnab", "Arun", "Arvind", "Arya", "Aseem", "Ashu", "Ashwin", "Bali", "Baltej", "Bellamkonda", "Bhagwant", "Bharat", "Bhavesh", "Bhupinder", "Bhushan", "Bibek", "Bibin", "Biju", "Binu", "Birendra", "Bishamber", "Byramjee", "Chakrapani", "Chamaraja", "Chandrajit", "Dayanidhi", "Debabrata", "Debaprasad", "Deepak", "Devang", "Devendra", "Devesh", "Dharanidhar", "Dheeraj", "Dhinakaran", "Dileesh", "Dinesh", "Dipankar", "Dnyandeo", "Elangovan", "Eswar", "Faizal", "Ganesh", "Ganga Prasad", "Ganpatrao", "Giridhar", "Girish", "Gnanasekaran", "Gopinathan", "Govindrao", "Gunda", "Gurcharan", "Gurunath", "Hansraj", "Hanuman", "Hara", "Harilal", "Harish", "Harjit", "Harmeet", "Harpreet", "Harsh", "Harshit", "Hemant", "Hilal", "Hiten", "Hitendra", "Hitesh", "Hriday", "Ira", "Irabot", "Ishana", "Jadunath", "Jagan", "Jahangir", "Jaipal", "Jalaj", "Jawahar", "Jayachandran", "Jayant", "Jitendra", "Jitu", "Joglekar", "K. Mallappa", "Kalanidhi", "Kalimuthu", "Kalpesh", "Kantilal", "Karan", "Karthik", "Karthikeyan", "Kaushal", "Kaustubh", "Kazi", "Keshav", "Khakhar", "Kirpa", "Kishor", "Kishore", "Kripa", "Kripesh", "Krishnadev", "Krishnappa", "Krishnayya", "Kulasekhara", "Kulbhushan", "Kumar", "Lakshan",
                    "Aarushi", "Aditi", "Aishwarya", "Akhila", "Ambika", "Ami", "Amita", "Amrita", "Ananya", "Anasuya", "Anjali", "Ankita", "Anoushka", "Anu", "Anupama", "Anuradha", "Anushree", "Aparna", "Apoorva", "Arpita", "Arti", "Arundhati", "Binita", "Bipasha", "Damayanti", "Deepa", "Deepali", "Deepika", "Deepti", "Devanshi", "Dhanishka", "Diya", "Eva", "Gayatri", "Gita", "Gitanjali", "Gul", "Haimanti", "Harpreet", "Harshita", "Hemalata", "Hemlata", "Himashree", "Hina", "Hira", "Indira", "Indrani", "Indumati", "Ishika", "Jesminder", "Joshna", "Juhi", "Kanchana", "Kanika", "Kareena", "Karthika", "Kavita", "Keerthi", "Kirtan", "Kshama", "Kumudini", "Lakshanya", "Lakshmi", "Lata", "Latha", "Latika", "Leela", "Lena", "Madhuri", "Mallika", "Malti", "Maneet", "Manjula", "Manorama", "Maya", "Meghana", "Meghna", "Mira", "Mitra", "Nadia", "Nahla", "Naila", "Nairanjana", "Namrata", "Nandita", "Neelam", "Neera", "Neetu", "Neha", "Nidhi", "Niharika", "Nikita", "Nimisha", "Nisha", "Nivetha", "Nupur", "Padmavati", "Pallavi", "Papiya", "Parvati", "Poonam", "Poulomi", "Pranati", "Pratibha", "Pratiksha", "Preeti", "Priya", "Priyanka", "Puja", "Rajathi", "Ramya", "Rani", "Rashmi", "Rati", "Rehana", "Rekha", "Ridhima", "Ritwika", "Roopa", "Rukmani", "Rukmini", "Saloni", "Samira", "Sanah", "Sandhya", "Sania", "Sanjana", "Saranya", "Sarita", "Satyana", "Savitri", "Seema", "Shahla", "Sharmila", "Sheela", "Sheetal", "Shilpa", "Shreya", "Shweta", "Simran", "Sneha", "Sonali", "Sreelekha", "Sudha", "Suhani", "Sujata", "Sulochana", "Sushma", "Susmita", "Swathi", "Tanisha", "Tanushree", "Tanvi", "Tejal", "Tejaswi", "Tulsi"};
            String[] lastNames = {"Khan", "Modi", "Fernandez", "Lehri", "Malhotra", "Singh", "Kumar", "Das", "Kaur", "Ram", "Yadav", "Kumari", "Lal", "Bai", "Khatun", "Mandal", "Ali", "Sharma", "Ray", "Mondal", "Khan", "Sah", "Patel", "Prasad", "Patil", "Ghosh", "Pal", "Sahu", "Gupta", "Shaikh", "Bibi", "Sekh", "Begam", "Biswas", "Sarkar", "Paramar", "Khatoon", "Mahto", "Ansari", "Nayak", "Ma", "Rathod", "Jadhav", "Mahato", "Rani", "Barman", "Behera", "Mishra", "Chand", "Roy", "Begum", "Saha", "Paswan", "Thakur", "Thakor", "Ahamad", "Chauhan", "Pawar", "Majhi", "Bano", "Naik", "Pradhan", "Alam", "Shinde", "Malik", "Sardar", "Nath", "Raut", "Bauri", "Shaik", "Chandra", "Patra", "Jha", "Murmu", "Solanki", "Cauhan", "Shah", "Prakash", "Sinh", "Pandey", "Patal", "Munda", "Dutta", "Chaudhari", "Raj", "Pandit"};



            String[] branches = {"csb", "mcb", "ee"};
            for (String branch : branches) {

                System.out.println("DOING FOR " + branch);
                System.out.print("Enter start of student ID range: ");
                int idStart = scanner.nextInt();
                System.out.print("Enter end of student ID range: ");
                int idEnd = scanner.nextInt();

                Random random = new Random();
                for (int j = idStart; j <= idEnd; j++) {
                    String studentID = year + branch + j;
                    String firstName = firstNames[random.nextInt(firstNames.length)];
                    String lastName = lastNames[random.nextInt(lastNames.length)];
                    String studentName = firstName + " " + lastName;
                    String contactInfo = String.format("%010d", (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L);
                    String password = "root";
                    writer.writeNext(new String[]{studentID, studentName, contactInfo, password});
                }
            }

            System.out.println("CSV file generated successfully");
        }
    }
}
