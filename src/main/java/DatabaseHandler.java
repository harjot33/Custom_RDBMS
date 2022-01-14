import java.io.*;
import java.util.Scanner;
/**
 *@author: Arun G.
 */

public class DatabaseHandler {
    static Scanner scanner = new Scanner(System.in);
    public static String databaseName;
    public static String user;

    public static void DatabaseHandle(String user) throws IOException {
        DatabaseHandler.user=user;
        int ch=0;
        while (ch!=4){
            listDatabase(DatabaseHandler.user);
            System.out.println("\n1.Select Database\n2.Create Database\n3.Drop Database\n4.Back");
            System.out.println("Enter Choice:");
            ch=scanner.nextInt();
            switch (ch){
                case 1:
                    System.out.println("Your Databases are listed below: ");
                    if(listDatabase(DatabaseHandler.user)){
                        System.out.println("Enter the name of database to select: ");
                        databaseName=scanner.next();
                        System.out.println(databaseName+" is selected.");
                        QueryScreen obj = new QueryScreen();
                        String db = databaseName + ".db";
                        obj.queryScreenOptions(db);}
                    break;
                case 2:
                    System.out.println("Enter the name of database: ");
                    databaseName=scanner.next();
                    CreateDatabase(user,databaseName);
                    break;
                case 3:
                    if (listDatabase(DatabaseHandler.user)){
                        System.out.println("Enter the name of database to DROP!!!");
                        databaseName=scanner.next();
                        System.out.println(databaseName+" is ready to Drop...");
                        if(DatabaseHandler.dropDatabase(databaseName))
                            System.out.println(databaseName+"Drop Successful!!!");
                        else
                            System.out.println("Something went wrong, Drop UnSuccessful.");
                    }
//                default:
//                    System.out.println("Please enter a valid input");

            }
        }
    }

    public static boolean listDatabase(String user) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        FileReader grR = new FileReader(gr);
        BufferedReader grBR = new BufferedReader(grR);
        String grr = grBR.readLine();
        while (grr != null) {
            String[] data = grr.split(":");
            if(data[0].equalsIgnoreCase(user)){
                System.out.println("Your Databases are: ");
                for(int i=1;i<data.length;i++)
                    System.out.println(data[i]);
                return true;
            }
            grr = grBR.readLine();
        }
        System.out.println("You don't have any Database Please Create one.");
        return false;


    }
    public void CheckDatabase(String user, String databaseName) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        FileReader grR = new FileReader(gr);
        BufferedReader grBR = new BufferedReader(grR);
        String grr = grBR.readLine();
        while (grr != null) {
            String[] data = grr.split(":");
            if (data[0].equalsIgnoreCase(user))
                for (int i = 1; i < data.length; i++)
                    if (data[1].equalsIgnoreCase(databaseName)) {
                        System.out.println("Database Already exists!!!");
                        grR.close();
                        grBR.close();
                        return;
                    }
            grr = grBR.readLine();
        }
    }
    public static void CreateDatabase(String user, String databaseName) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        File database = new File("src/main/resources/Database/" + databaseName + ".db");
        if (database.createNewFile()) {
            FileReader grR1 = new FileReader(gr);
            StringBuffer grsb = new StringBuffer();
            Scanner sc = new Scanner(new File(String.valueOf(gr)));
            while(sc.hasNextLine())
                grsb.append(sc.nextLine()+System.lineSeparator());
            String content = grsb.toString();
            System.out.println(content);
            BufferedReader grBR1 = new BufferedReader(grR1);
            String read = grBR1.readLine();
            System.out.println(read);
            String old;
            String New;
            while(read!=null){
                String[] temp = read.split(":");
                System.out.println(4);
                if(temp[0].equalsIgnoreCase(user)){
                    old=read;
                    New=read+":"+databaseName;
                    content=content.replaceAll(old,New);
                    System.out.println(content);
                    FileWriter grW = new FileWriter(gr);
                    grW.write(content);
                    System.out.println(databaseName + " database is created.");
                    grW.close();
                    return;
                }
                read = grBR1.readLine();
            }
            FileWriter grW = new FileWriter(gr,true);
            String data = user+":"+databaseName;
            System.out.println(data);
            grW.append(data);
            grW.close();
            System.out.println("Database Created.");
        }
        else
            System.out.println(database + " Already Exists!");
    }
    public static boolean dropDatabase(String databaseName) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        Scanner scanner = new Scanner(new File("src/main/resources/GeneralRecord.gr"));
        StringBuffer sb = new StringBuffer();
        while(scanner.hasNextLine())
            sb.append(scanner.nextLine()+System.lineSeparator());
        String content = sb.toString();
        if(content.contains(":"+databaseName)) {
            content = content.replace(":" + databaseName, "");
            FileWriter grW = new FileWriter(gr);
            grW.write(content);
            grW.close();
            File db = new File("src/main/resources/Database/" + databaseName + ".db");
            db.delete();
            return true;
        }
        System.out.println("\nDatabase not Exits!!!\nPlease Provide a available database name...\n");
        return false;

    }
}

