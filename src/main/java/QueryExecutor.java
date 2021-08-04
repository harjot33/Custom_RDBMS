import java.io.*;
import java.util.ArrayList;

public class QueryExecutor {
    // all the query execution function written here
    public boolean executeCreateQuery(String DatabaseName,String tableName, ArrayList<String> columnArray,
                                      ArrayList<String> dataTypeArray) throws IOException {
        File db = new File("src/main/resources/Database/" + DatabaseName + ".db");
        FileReader dbR = new FileReader(db);
        FileWriter dbW = new FileWriter(db, true);
        BufferedReader dbBR = new BufferedReader(dbR);
        String dbbr = dbBR.readLine();
        while (dbbr != null) {
            if (dbbr.equalsIgnoreCase(tableName)) {
                System.out.println("Table Already Exist!!!");
                return false;
            }
            dbbr = dbBR.readLine();
        }
        File table = new File("src/main/resources/Table/" + tableName + ".tb");
        if (table.createNewFile()) {
            System.out.println(table + " table is created.");
            FileWriter tableW = new FileWriter(table);
            String columnDetails = "";
            dbW.append(tableName + "\n");
            for (int i = 0; i < columnArray.size(); i++) {
                dbW.append(columnArray.get(i) + " " + dataTypeArray.get(i) + "\n");
                if (i != columnArray.size() - 1)
                    columnDetails = columnDetails + columnArray.get(i) + ":";
                else
                    columnDetails = columnDetails + columnArray.get(i) + "\n";
            }
            dbW.append("\n\n");
            tableW.write(columnDetails);
            dbR.close();
            dbBR.close();
            dbW.close();
            tableW.close();
            return true;
        }
        else
            return false;
    }

    public boolean executeInsertQuery(String DatabaseName,String tableName,String[] dataArray) throws IOException {
        File db = new File("src/main/resources/Database/" + DatabaseName + ".db");
        FileReader dbR = new FileReader(db);
        BufferedReader dbBR = new BufferedReader(dbR);
        String dbbr = dbBR.readLine();
        while (dbbr != null) {
            if (dbbr.equalsIgnoreCase(tableName)) {
                File tb = new File("src/main/resources/Table/"+tableName+".tb");
                FileWriter tbW = new FileWriter(tb,true);
                String data = "";
                for (int i = 0; i < dataArray.length; i++) {
                    if (i != dataArray.length - 1)
                        data = data + dataArray[i] + ":";
                    else
                        data = data + dataArray[i];
                }
                tbW.append(data);
                tbW.close();
                dbBR.close();
                dbR.close();
                return true;
            }
            dbbr = dbBR.readLine();
        }
        System.out.println("Table Not Found!!!");
        return false;
    }

    // executing the delete query
    public boolean executeDeleteQuery(String tableName, String columnName, String conditionValue) {
        return false;
    }

    // executing the drop query
    public boolean executeDropQuery(String tableName) {
        return false;
    }
}
