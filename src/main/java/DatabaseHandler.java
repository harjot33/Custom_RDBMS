import java.io.*;
import java.util.Scanner;

public class DatabaseHandler {
    public static String databaseName="data7";
    private static String user = "rahul1";

    public void listDatabase(String user) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        FileReader grR = new FileReader(gr);
        BufferedReader grBR = new BufferedReader(grR);
        String grr = grBR.readLine();
        while (grr != null) {
//            System.out.println(grr);
            String[] data = grr.split(":");
            if(data[0].equalsIgnoreCase(user)){
                System.out.println("Your Databases are: ");
                for(int i=1;i<data.length;i++)
                    System.out.println(data[i]);
                return;
            }
            grr = grBR.readLine();
        }
        System.out.println("You don't have any Database Please Create one.");


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
    public void CreateDatabase(String user,String databaseName) throws IOException {
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
    public void dropDatabase(String user, String databaseName){

    }

    public static void main(String[] args) throws IOException {
        DatabaseHandler al = new DatabaseHandler();
        al.CreateDatabase(user,databaseName);
    }
}

