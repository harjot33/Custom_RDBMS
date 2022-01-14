import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {
    static QueryExecutor executor = new QueryExecutor();
    public static void processQuery(QueryType queryType, Pattern pattern, String originalQuery, String DatabaseName, FileWriter eventLogWriter ) throws IOException {
        if (queryType == QueryType.ERROR || pattern == null || originalQuery.isEmpty()) {
            return;
        }

        switch (queryType) {
            case CREATE -> processCreateQuery(pattern, originalQuery, DatabaseName, eventLogWriter);
            case SELECT -> processSelectQuery(pattern, originalQuery, DatabaseName, eventLogWriter);
            case INSERT -> processInsertQuery(pattern, originalQuery, DatabaseName, eventLogWriter);
            case UPDATE_TABLE -> processUpdateQuery(pattern, originalQuery);
            case ALTER_TABLE_DROP -> processAlterDropColumnQuery(pattern,
                originalQuery, DatabaseName);
            case ALTER_TABLE_ADD ->  processAlterAddColumn(pattern,originalQuery,DatabaseName);
            case DELETE -> processDeleteQuery(pattern, originalQuery, DatabaseName, eventLogWriter);
            case DROP -> processDropQuery(pattern, originalQuery, DatabaseName, eventLogWriter);
        }
    }

    public static void processCreateQuery(Pattern pattern, String originalQuery, String DatabaseName, FileWriter eventLogWriter)
            throws IOException {
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
        //getting start time when query executes
        long dropQueryStart = System.nanoTime();
        if(executor.executeCreateQuery(DatabaseName,tableName,columnArray,dataTypeArray)){
            System.out.println("Query Execution Successful");
            // getting end time when query ends
            long dropQueryEnd = System.nanoTime();  // Get the end Time
            long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart;  // Calculate the execution Time
            System.out.println("\n");
            eventLogWriter.append("Create Query Execution Time --> " + totalDropQueryExecutionTime );
        }
        else
            System.out.println(("Query failed!!!"));
    }

    private static void processAlterAddColumn(Pattern pattern,
                                              String originalQuery,
                                              String database) throws IOException {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            String tableName = matcher.group(1);
            String columnName = matcher.group(2);
            String datatype = matcher.group(3);
            String datatypeval = matcher.group(4);
            List<String> ok = new ArrayList<>();
            ok.add(columnName);
            String creo = datatype+"("+datatypeval+")";
            ok.add(creo);

            executor.executeAlterAddColumn(tableName, columnName, database,ok);
            System.out.println("");
        }
    }


    private static void processAlterDropColumnQuery(Pattern pattern,
                                                    String originalQuery,
                                                    String database) throws IOException {
        String upperCaseQuery = originalQuery;
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            System.out.println(matcher.group());
            String tableName = matcher.group(1);
            String columnName = matcher.group(2);

            executor.executeAlterColumnDrop(tableName, columnName, database);
            System.out.println("");
        }
    }

    private static void processUpdateQuery(Pattern pattern,
                                           String originalQuery) throws IOException {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            String tableName = matcher.group(1);
            String columnvalues = matcher.group(2);
            String condition = matcher.group(3);

            executor.executeUpdateQuery(tableName, columnvalues,
                condition);
            System.out.println("");
        }
    }


    public static void processSelectQuery(Pattern pattern, String originalQuery, String DatabaseName, FileWriter eventLogWriter ) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        String tableName = matcher.group(2);
        String[] columns;
        columns=matcher.group(1).split(",");
//        System.out.println(matcher.groupCount());

        if(columns[0].equalsIgnoreCase("*") && !originalQuery.contains("WHERE")) {
            System.out.println(1);
            //getting start time when query executes
            long dropQueryStart = System.nanoTime();

            executor.executeSelectAllQuery(tableName);

            // getting end time when query ends
            long dropQueryEnd = System.nanoTime();  // Get the end Time
            long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart;  // Calculate the execution Time
            System.out.println("\n");
            eventLogWriter.append("Select * Query Execution Time --> " + totalDropQueryExecutionTime );
            return;
        }

        if(!originalQuery.contains("WHERE") && !columns[0].equalsIgnoreCase("*")){
            //getting start time when query executes
            long dropQueryStart2 = System.nanoTime();

            executor.executeSelectAllQueryWithoutCondition(tableName,columns);

            // getting end time when query ends
            long dropQueryEnd = System.nanoTime();  // Get the end Time
            long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart2;  // Calculate the execution Time
            System.out.println("\n");
            eventLogWriter.append("Select 2 Query Execution Time --> " + totalDropQueryExecutionTime );
        }

        if(originalQuery.contains("WHERE") && columns[0].equalsIgnoreCase("*")){
            //getting start time when query executes
            long dropQueryStart3 = System.nanoTime();

            executor.executeSelectAllWithConditionQuery(tableName,matcher.group(4));

            // getting end time when query ends
            long dropQueryEnd = System.nanoTime();  // Get the end Time
            long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart3;  // Calculate the execution Time
            System.out.println("\n");
            eventLogWriter.append("Select 3 Query Execution Time --> " + totalDropQueryExecutionTime );
        }

//        System.out.println(tableName);
    }

    public static void processInsertQuery(Pattern pattern, String originalQuery, String DatabaseName, FileWriter eventLogWriter ) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        String tableName = matcher.group(1);
        String[] dataArray = matcher.group(3).split(",\s");

        //getting start time when query executes
        long dropQueryStart = System.nanoTime();
        if(executor.executeInsertQuery(DatabaseName, tableName, dataArray)) {
            System.out.println("Insertion done.");
            // getting end time when query ends
            long dropQueryEnd = System.nanoTime();  // Get the end Time
            long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart;  // Calculate the execution Time
            System.out.println("\n");
            eventLogWriter.append("Insert Query  Time --> " + totalDropQueryExecutionTime );
        }
        else
            System.out.println("Table not found!!!");

    }

    private static void processDeleteQuery(Pattern pattern, String originalQuery, String DatabaseName, FileWriter eventLogWriter) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2);
            if (condition != null && condition.indexOf('=') != -1) {
                String[] conditionArray = condition.split("=");
                String columnName = conditionArray[0].trim();
                String columnValue = conditionArray[1].trim();
                System.out.println("");

                //getting start time when query executes
                long dropQueryStart = System.nanoTime();

                // Linking user to the Query Execution
                executor.executeDeleteQuery(tableName, columnName, columnValue, DatabaseName);

                // getting end time when query ends
                long dropQueryEnd = System.nanoTime();  // Get the end Time
                long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart;  // Calculate the execution Time
                System.out.println("\n");
                eventLogWriter.append("Delete Query Execution Time --> " + totalDropQueryExecutionTime + "--> All the values deleted from column --> " + columnName);
                System.out.println("\n");
            }
            System.out.println("");
        }
    }

    private static void processDropQuery(Pattern pattern, String originalQuery, String DatabaseName,FileWriter eventLogWriter) throws IOException {
        Matcher matcher = pattern.matcher(originalQuery);
        matcher.matches();
        String tableName = matcher.group(1);

        //getting start time when query executes
        long dropQueryStart = System.nanoTime();  // Get the start Time
        // Linking user to the Query Execution
        Boolean didPass = executor.executeDropQuery(tableName, DatabaseName);

        // getting end time when query ends
        long dropQueryEnd = System.nanoTime();  // Get the end Time
        long totalDropQueryExecutionTime = dropQueryEnd - dropQueryStart;  // Calculate the execution Time
        eventLogWriter.append("Drop Query Execution Time --> " + totalDropQueryExecutionTime + "--> Table that was dropped --> " + tableName);
        System.out.println("Drop did Pass/fail " + didPass);
    }

}

