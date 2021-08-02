import java.io.*;
import java.util.ArrayList;

public class Action {
    private static String DatabaseURL = "src/main/resources/Database/";

    public void CreateDatabase(String databaseName) throws IOException {
        File gr = new File("src/main/resources/GeneralRecord.gr");
        FileReader grReader = new FileReader(gr);
        FileWriter grWriter = new FileWriter(gr);
        BufferedReader grBufferReader = new BufferedReader(grReader);
        String grRead = grBufferReader.readLine();
        while (grRead != null) {
            if (grRead.equalsIgnoreCase(databaseName)) {
                System.out.println("Database Exist!!!");
                return;
            }
            grRead=grBufferReader.readLine();
        }
        File database = new File("src/main/resources/Database/" + databaseName + ".db");
        if (database.createNewFile()) {
            System.out.println(databaseName + " database is created.");
            grWriter.append(databaseName);
            grWriter.close();
        }
        else
            System.out.println(database + " already Exists!");
    }

    public void CreateTable(String databaseURL, String tableName, ArrayList<String> columnName, ArrayList<String> dataType) throws IOException {
        FileReader databaseReader = new FileReader(databaseURL);
        FileWriter databaseWriter = new FileWriter(databaseURL);
        BufferedReader databaseBufferReader = new BufferedReader(databaseReader);
        String dbbr = databaseBufferReader.readLine();
        while (dbbr != null) {
            if (dbbr.equalsIgnoreCase(tableName)) {
                System.out.println("Table Exist!");
                return;
            }
            File table = new File("src/main/resources/Table/" + tableName + ".tb");
            if (table.createNewFile()) {
                System.out.println(table + " table is created.");
                databaseWriter.append(tableName+"\n");
                FileWriter tableWriter = new FileWriter(table);
                for(int i=0;i<columnName.size();i++){
                    tableWriter.append(columnName.get(i)+" "+dataType.get(i)+"\n");
                }
            }
        }
    }
}


