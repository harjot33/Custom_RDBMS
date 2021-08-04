import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class QueryExecutor {
    private static final String DatabaseURL = "src/main/resources/Database/";
    // all the query execution function written here
    public boolean executeCreateQuery(String DatabaseName,String tableName, ArrayList<String> columnArray,
                                      ArrayList<String> dataTypeArray) throws IOException {
        File db = new File( DatabaseURL+DatabaseName+".db");
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
                tbW.append(data+"\n");
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

    public boolean executeSelectAllQueryWithoutCondition(String tableName,String[] columns) throws IOException {
        for(String s:columns)
            System.out.print(s+"\t");
        System.out.println("\n");
        File tb = new File("src/main/resources/Table/"+tableName+".tb");
        FileReader tbR = new FileReader(tb);
        BufferedReader tbBr = new BufferedReader(tbR);
        String read = tbBr.readLine();
        String[] base = read.split(":");
        read = tbBr.readLine();
        while (read!=null){
            String[] data = read.split(":");
            for(int i=0;i<base.length;i++)
                for (int j=0;j<columns.length;j++){
                    if(base[i].equalsIgnoreCase(columns[j])){
                        System.out.print(data[i]+"\t");
                    }
                }
            System.out.println();
            read=tbBr.readLine();
        }
        return true;
    }

    public boolean executeSelectAllWithConditionQuery(String tableName, String condition) throws IOException {
        File tb = new File("src/main/resources/Table/"+tableName+".tb");
        FileReader tbR = new FileReader(tb);
        BufferedReader tbBr = new BufferedReader(tbR);
        String read = tbBr.readLine();
        String[] column=read.split(":");
        for(String s: column)
            System.out.print(s+" ");
        System.out.println();
        String[] cond = condition.split("=");
        int index=0;
        while (!column[index].equalsIgnoreCase(cond[0])){
            index++;
        }
        ArrayList<String[]> data = new ArrayList<>();
        read = tbBr.readLine();
        while(read!=null){
            String[] temp = read.split(":");
            if(temp[1].equalsIgnoreCase(cond[1])) {
                for (String s : temp)
                    System.out.print(s + " ");
                System.out.println();
            }
            read = tbBr.readLine();
        }
        return true;
    }

    public boolean executeSelectAllQuery(String tableName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("src/main/resources/Table/"+tableName+".tb"));
        StringBuffer sb = new StringBuffer();
        while(scanner.hasNextLine()){
            sb.append(scanner.nextLine()+System.lineSeparator());
        }
        String content = sb.toString();
        System.out.println(content);
        return true;
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
