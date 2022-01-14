import java.awt.desktop.SystemSleepEvent;
import java.io.*;
import java.util.*;
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
                String tableFilePath = "src/main/resources/Table/"+readTableName +".tb";
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
            String tableFilePath = "src/main/resources/Table/"+tableName +".tb";
            File fileToDelete = new File(tableFilePath);
            if (fileToDelete.delete()) {
                System.out.println("Deleted the file: " + fileToDelete.getName());
            } else {
                System.out.println("Failed to delete the file." + fileToDelete.getName());
            }
        }
        return didDeleteAny;
    }

    public Boolean executeUpdateQuery(String tableName,
                                      String columnvalues, String condition) throws IOException {
        String filepath = "src/main/resources/Table/"+tableName +".tb";
        HashMap<String, String> valuesmap = new HashMap<>();
        File file = new File(filepath);
        String[] values = columnvalues.split(",");
        for(int i = 0; i< values.length;i++){
            String incoming = values[i];
            String[] vals = incoming.split("=");
            String columnname = vals[0];
            String value = vals[1];
            columnname = columnname.trim();
            value = value.trim();
            valuesmap.put(columnname,value);
        }
        String[] condval = condition.split("=");
        String condval1 = condval[0].trim();
        String condval2 = condval[1].trim();
        HashMap<String, String> conditionVal = new HashMap<>();
        conditionVal.put(condval1,condval2);

        List<List<String>> columndata = updateChanges(filepath,valuesmap,
            conditionVal);
        boolean status = alterChangesWrite(file,columndata);

        if(!status){
            status = false;
        }

        return status;
    }

    public Boolean executeAlterColumnDrop(String tableName,
                                          String columnName,
                                          String databaseURL) throws IOException {
        String filepath = "src/main/resources/Table/"+tableName +".tb";
        String datapath = "src/main/resources/Database/"+databaseURL+".db";
        File file = new File(filepath);
        File file2 = new File(datapath);
        List<List<String>> columndata = alterChanges(filepath,columnName);
        boolean status = alterChangesWrite(file,columndata);
        List<List<String>> columndata2 = DBalterChangesDrop(datapath,columnName);
        boolean status2 = alterChangesWrite(file2,columndata2);
        if(!status || !status2){
            status = false;
        }

        return status;
    }

    public Boolean executeAlterAddColumn(String tableName,
                                         String columnName,
                                         String databaseURL,
                                         List<String> cred) throws IOException {
        String filepath = "src/main/resources/Table/"+tableName +".tb";
        String datapath = "src/main/resources/Database/"+databaseURL+".db";
        File file = new File(filepath);
        File file2 = new File(datapath);
        List<List<String>> columndata = alterAddChanges(filepath,columnName);
        boolean status = alterChangesWrite(file,columndata);
        List<List<String>> columndata2 = DBalterChangesAdd(datapath,
            columnName,cred);
        boolean status2 = alterChangesWrite(file2,columndata2);
        if(!status || !status2){
            status = false;
        }

        return status;
    }

    public List<List<String>> alterAddChanges(String filepath,
                                              String columnName) throws IOException{
        File file = new File(filepath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line = "";
        int count = 0;
        int ind = 0;
        List<String> columnentites = new ArrayList<>();
        List<List<String>> columndata = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            if (count == 0) {
                String[] colums = line.split(":");
                columnentites = new ArrayList<>(Arrays.asList(colums));
                columnentites.add(columnName);
                columndata.add(columnentites);
                count++;
            }else{
                List<String> singlecolumndata = new ArrayList<>(Arrays.asList(line.split(":")));
                singlecolumndata.add("null");
                columndata.add(singlecolumndata);
            }

        }

        return columndata;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<List<String>> alterChanges(String filepath, String columnName) throws IOException{
        File file = new File(filepath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        String line = "";
        int count = 0;
        int ind = 0;
        List<String> columnentites = new ArrayList<>();
        List<List<String>> columndata = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            if (count == 0) {
                String[] colums = line.split(":");
                columnentites = new ArrayList<>(Arrays.asList(colums));
                if (columnentites.contains(columnName)) {
                    ind = columnentites.indexOf(columnName);
                    columnentites.remove(ind);
                    columndata.add(columnentites);
                    count++;
                }else{
                    return null;
                }
            }else{
                List<String> singlecolumndata = new ArrayList<>(Arrays.asList(line.split(":")));
                singlecolumndata.remove(ind);
                columndata.add(singlecolumndata);
            }

        }

            return columndata;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }



    public List<List<String>> DBalterChangesDrop(String filepath,
                                            String columnName) throws IOException{
        File file = new File(filepath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            int count = 0;
            int ind = 0;
            List<String> columnentites = new ArrayList<>();
            List<List<String>> columndata = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
               String[] incoming = line.split(":");
               if(count==0){
                   columnentites.add(incoming[0]);
                   columndata.add(columnentites);
                   count++;
               }else{
                   columnentites = new ArrayList<>(Arrays.asList(incoming));
                   if(!columnentites.contains(columnName)){
                       columndata.add(columnentites);
                   }
               }
            }

            return columndata;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    public List<List<String>> DBalterChangesAdd(String filepath,
                                                 String columnName,
                                                List<String> cred) throws IOException{
        File file = new File(filepath);
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = "";
            int count = 0;
            int ind = 0;
            List<String> columnentites = new ArrayList<>();
            List<List<String>> columndata = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] incoming = line.split(":");
                if(count==0){
                    columnentites.add(incoming[0]);
                    columndata.add(columnentites);
                    count++;
                }else{
                    columnentites = new ArrayList<>(Arrays.asList(incoming));
                    if(!columnentites.contains(columnName)){
                        columndata.add(columnentites);
                    }
                }
            }
            String add = cred.get(0) + ":" + cred.get(1);
            List<String> adders = new ArrayList<>();
            adders.add(add);
            columndata.add(adders);
            return columndata;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }


    public List<List<String>> updateChanges(String filepath,
                                            HashMap<String,String> columnValues
        , HashMap<String,String> conditionalValues
    ) throws IOException{
        File file = new File(filepath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Set<String> keys = conditionalValues.keySet();
        String conditionalColumn = "";
        String conditionalvalue = "";
        for(String S : keys){
            conditionalColumn = S;
            conditionalvalue = conditionalValues.get(S);

        }
        String line = "";
        int count = 0;
        int ind = 0;
        List<String> columnentites = new ArrayList<>();
        List<List<String>> columndata = new ArrayList<>();
        while ((line = bufferedReader.readLine()) != null) {
            if (count == 0) {
                String[] colums = line.split(":");
                columnentites = new ArrayList<>(Arrays.asList(colums));
                if (columnentites.contains(conditionalColumn)) {
                    ind = columnentites.indexOf(conditionalColumn);
                    columndata.add(columnentites);
                    count++;
                }else{
                    return null;
                }
            }else{
                List<String> singlecolumndata = new ArrayList<>(Arrays.asList(line.split(":")));
                if(singlecolumndata.get(ind).equals(conditionalvalue)){
                    Set<String> columnvalkeys = columnValues.keySet();
                    for(String S : columnvalkeys){
                        int id = columnentites.indexOf(S);
                        String columnvals = columnValues.get(S);
                        singlecolumndata.set(id, columnvals.substring(1,
                            columnvals.length()-1));
                    }
                    columndata.add(singlecolumndata);
                }else{
                    columndata.add(singlecolumndata);
                }
            }

        }
        return columndata;
    }

    public boolean alterChangesWrite(File file, List<List<String>> columndata) throws IOException{
        BufferedWriter bufferedWriter =
            new BufferedWriter(new FileWriter(file));
        for(int i =0; i<columndata.size();i++){
            String row = "";
            for(int j = 0; j<columndata.get(i).size();j++){
                String val = columndata.get(i).get(j);
                if(j == columndata.get(i).size()-1){
                    row = row + val;
                }else{
                    row = row + val + ":";
                }
            }
            bufferedWriter.write(row);
            bufferedWriter.write("\n");
        }
        bufferedWriter.close();
        return true;
    }


}
