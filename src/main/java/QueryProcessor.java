import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {
    static QueryExecutor executor = new QueryExecutor();
    public static void processQuery(QueryType queryType, Pattern pattern, String originalQuery) throws IOException {
        if (queryType == QueryType.ERROR || pattern == null || originalQuery.isEmpty()) {
            return;
        }
        String DatabaseName = "Database1.db";

        switch (queryType) {
            case CREATE -> processCreateQuery(pattern, originalQuery, DatabaseName);
            case SELECT -> processSelectQuery(pattern, originalQuery);
            case INSERT -> processInsertQuery(pattern, originalQuery, DatabaseName);
            case UPDATE -> processUpdateQuery(pattern, originalQuery);
            case ALTER -> processAlterQuery(pattern, originalQuery);
            case DELETE -> processDeleteQuery(pattern, originalQuery);
            case DROP -> processDropQuery(pattern, originalQuery);
        }
    }

    public static void processCreateQuery(Pattern pattern, String originalQuery,String DatabaseName) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        String tableName = matcher.group(1);
        String[] columnString = matcher.group(2).split(",\s");
        List<String> columnList = Arrays.asList(columnString);
        ArrayList<String> columnArray = new ArrayList<>();
        ArrayList<String> dataTypeArray = new ArrayList<>();
        for(String s:columnList){
            String[] temp=s.split("\s");
            columnArray.add(temp[0]);
            dataTypeArray.add(temp[1]);
        }
        File db = new File("src/main/resources/Database/"+DatabaseName+".db");
        FileReader dbR = new FileReader(db);
        FileWriter dbW = new FileWriter(db,true);
        BufferedReader dbBR = new BufferedReader(dbR);
        String dbbr = dbBR.readLine();
        while (dbbr != null) {
            if (dbbr.equalsIgnoreCase(tableName)) {
                System.out.println("Table Already Exist!!!");
                return;
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
                    columnDetails = columnDetails + columnArray.get(i)+"\n";
            }
            dbW.append("\n\n");
            tableW.write(columnDetails);
            dbR.close();
            dbBR.close();
            dbW.close();
            tableW.close();
            return;
        }
    }

    private static void processSelectQuery(Pattern pattern, String originalQuery) {
    }

    public static void processInsertQuery(Pattern pattern, String originalQuery, String DatabaseName) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        String tableName = matcher.group(1);
        String[] columnArray = matcher.group(2).split(",\s");
        String[] dataArray = matcher.group(3).split(",\s");
        File db = new File("src/main/resources/Database/" + DatabaseName + ".db");
        FileReader dbR = new FileReader(db);
        BufferedReader dbBR = new BufferedReader(dbR);
        String dbbr = dbBR.readLine();
        while (dbbr != null) {
            if (dbbr.equalsIgnoreCase(tableName)) {
                File tb = new File("src/main/resources/Table/"+tableName+".tb");
                FileWriter tbW = new FileWriter(tb,true);
                String data = "";
                for (int i = 0; i < columnArray.length; i++) {
                    if (i != dataArray.length - 1)
                        data = data + dataArray[i] + ":";
                    else
                        data = data + dataArray[i];
                }
                tbW.append(data);
                tbW.close();
                dbBR.close();
                dbR.close();
                return;
            }
            dbbr = dbBR.readLine();
        }
        System.out.println("Table Not Found!!!");

    }


    private static void processUpdateQuery(Pattern pattern, String originalQuery) {
    }

    private static void processAlterQuery(Pattern pattern, String originalQuery) {
    }

    private static void processDeleteQuery(Pattern pattern, String originalQuery) {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2);
            if (condition != null && condition.indexOf('=') != -1) {
                String[] conditionArray = condition.split("=");
                String columnName = conditionArray[0].trim();
                String columnValue = conditionArray[1].trim();
                System.out.println("");
                executor.executeDeleteQuery(tableName, columnName, columnValue);
            }
            System.out.println("");
        }
    }

    private static void processDropQuery(Pattern pattern, String originalQuery) {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        String tableName = matcher.group(1);
        executor.executeDropQuery(tableName);
    }
}

