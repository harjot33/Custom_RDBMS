import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public Boolean executeDeleteQuery(String tableName,
                                      String columnName,
                                      String conditionValue,
                                      String DatabaseName) throws IOException {
        File dbFile = new File("src/main/resources/Database/"+DatabaseName+".db");
        BufferedReader dbBR = new BufferedReader(new FileReader(dbFile));
        String readTableName;
        Boolean didDeleteAny = false;
        while ((readTableName = dbBR.readLine()) != null) {
            ArrayList<String> finalFileLines = new ArrayList<String>();
            if (readTableName.equalsIgnoreCase(tableName)) {
                // now read contents for table
                String tableFilePath = "src/main/resources/Tables/"+readTableName +".tb";
                BufferedReader tableBR = new BufferedReader(new FileReader(tableFilePath));
                String firstRowDBName = tableBR.readLine();
                finalFileLines.add(firstRowDBName);
                finalFileLines.add("\n");
                String secondRowRowOfColumnNames = tableBR.readLine();
                int columnIndex = 0;
                if (secondRowRowOfColumnNames != null) {
                    // First row is column names
                    String[] columns = secondRowRowOfColumnNames.split(":");
                    for (String column: columns) {
                        if (column.equals(columnName)) {
                            break;
                        }
                        columnIndex = columnIndex + 1;
                    }
                    finalFileLines.add(secondRowRowOfColumnNames);
                    finalFileLines.add("\n");
                }
                String tableDataRows;
                while ((tableDataRows = tableBR.readLine()) != null && !tableDataRows.isEmpty()) {
                    String[] columns = tableDataRows.split(":");
                    if (!columns[columnIndex].equals(conditionValue)) {
                        finalFileLines.add(tableDataRows);
                        finalFileLines.add("\n");
                    } else {
                        didDeleteAny = true;
                    }
                }
                FileWriter writer = new FileWriter(tableFilePath, false);
                for (String str: finalFileLines) {
                    writer.append(str);
                }
                writer.flush();
                writer.close();
            }
        }
        return didDeleteAny;
    }


    // executing the drop query
    public Boolean executeDropQuery(String tableName,
                                    String DatabaseName) throws IOException {
        File dbFile = new File("src/main/resources/Database/"+DatabaseName+".db");
        BufferedReader dbBR = new BufferedReader(new FileReader(dbFile));
        String readTableName;
        Boolean didDeleteAny = false;
        ArrayList<String> finalFileLines = new ArrayList<String>();
        while ((readTableName = dbBR.readLine()) != null) {
            if (!readTableName.equalsIgnoreCase(tableName)) {
                finalFileLines.add(readTableName);
            } else {
                didDeleteAny = true;
            }
        }
        FileWriter writer = new FileWriter(dbFile, false);
        for (String str: finalFileLines) {
            writer.append(str);
        }
        writer.flush();
        writer.close();
        if (didDeleteAny) {
            String tableFilePath = "src/main/resources/Tables/"+tableName +".tb";
            File fileToDelete = new File(tableFilePath);
            if (fileToDelete.delete()) {
                System.out.println("Deleted the file: " + fileToDelete.getName());
            } else {
                System.out.println("Failed to delete the file." + fileToDelete.getName());
            }
        }
        return didDeleteAny;
    }
}
