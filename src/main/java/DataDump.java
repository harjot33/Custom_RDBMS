import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataDump {

  public static void generateDump(String username,
                                  String dburl) throws IOException {
    String filepath1 = "src/main/resources/Dump/dump.txt";
    String filepath = "src/main/resources/Database/" + dburl + ".db";
    File file = new File(filepath);
    File file2 = new File(filepath1);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    int count = 0;
    int ind = 0;
    String line = "";
    List<String> columnentites = new ArrayList<>();
    List<List<String>> columndata = new ArrayList<>();
    while ((line = bufferedReader.readLine()) != null) {
      if (count == 0) {
        List<String> table = new ArrayList<>();
        table.add(line);
        columndata.add(table);
        count++;
      } else {
        if (line.isBlank()) {
          count = 0;
          String blankline = "\n";
          List<String> blank = new ArrayList<>();
          blank.add(blankline);
        } else {
          List<String> singlecolumndata = new ArrayList<>(Arrays.asList(line.split(":")));
          columndata.add(singlecolumndata);
        }
      }
    }
    QueryExecutor obj = new QueryExecutor();
  }

  public boolean dataDumpWriter(File file, List<List<String>> columndata) throws IOException{
    BufferedWriter bufferedWriter =
        new BufferedWriter(new FileWriter(file));
    for(int i =0; i<columndata.size();i++){
      String row = "";
      for(int j = 0; j<columndata.get(i).size();j++){
        if( i == 0){

        }
      }
      bufferedWriter.write(row);
      bufferedWriter.write("\n");
    }
    bufferedWriter.close();
    return true;
  }

  public boolean dumpWriter(File file, List<List<String>> columndata) throws IOException{
    BufferedWriter bufferedWriter =
        new BufferedWriter(new FileWriter(file));
    for(int i =0; i<columndata.size();i++){
      String row = "";
      for(int j = 0; j<columndata.get(i).size();j++){
        String val = columndata.get(i).get(j);
        row = row + ":" + val;
      }
      bufferedWriter.write(row);
      bufferedWriter.write("\n");
    }
    bufferedWriter.close();
    return true;
  }


}
