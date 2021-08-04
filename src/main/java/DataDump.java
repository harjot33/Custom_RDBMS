import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataDump {
  public static void main(String[] args) {
    DataDump obj = new DataDump();
    try {
      obj.generateDump("Database1");
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  public void generateDump(String dbname) throws IOException {
    String filepath1 = "src/main/resources/Dump/dump.txt";
    String filepath = "src/main/resources/Database/" + dbname + ".db";
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
          columndata.add(blank);
        } else {
          List<String> singlecolumndata = new ArrayList<>(Arrays.asList(line.split(":")));
          columndata.add(singlecolumndata);
        }
      }
    }
    dumpWriter(file2,columndata);
  }

  public boolean dumpWriter(File file, List<List<String>> columndata) throws IOException {
    int count = 0;
    String tablename = "";
    BufferedWriter bufferedWriter =
        new BufferedWriter(new FileWriter(file));
    for (int i = 0; i < columndata.size(); i++) {
      String row = "";
      for (int j = 0; j < columndata.get(i).size(); j++) {
        if (count == 0) {
          String ok = "CREATE TABLE IF NOT EXISTS ";
          String val = columndata.get(i).get(j);
          tablename = val;
          if(columndata.get(i).get(j).isBlank()){
            bufferedWriter.write("\n");
          }else {
            String com = ok + val + " (";
            bufferedWriter.write(com);
            count++;
          }
        }else{
          if(columndata.get(i).get(j).isBlank()){
            bufferedWriter.write(")");
            bufferedWriter.write("\n");
            String filepath = "src/main/resources/Table/" + tablename +
                ".tb";
            BufferedReader bufferedReader =
                new BufferedReader(new FileReader(filepath));
            BufferedReader bufferedReader2 =
                new BufferedReader(new FileReader(filepath));
            String line = "";
            int counter = 0;
            String line2="";
            while((line=bufferedReader.readLine())!=null){
              if(counter==0){
                String[] vals = line.split(":");
                String initialrow = "";
                for(int o = 0 ; o< vals.length;o++){
                  if(o== vals.length-1){
                    initialrow = initialrow + vals[o];
                  }else{
                  initialrow = initialrow + vals[o] + ", ";
                }
                  }
                String initial = "INSERT INTO "+tablename+" ("+initialrow+")";
                bufferedWriter.write(initial);
                bufferedWriter.write("\n");
                bufferedWriter.write("VALUES");
                bufferedWriter.write("\n");
                counter++;
              }else {
                String[] vals = line.split(":");
                String initialrow = "";
                for (int o = 0; o < vals.length; o++) {
                  if (o == vals.length - 1) {
                    initialrow = initialrow + vals[o];
                  } else {
                    initialrow = initialrow + vals[o] + ", ";
                  }
                }

                if (bufferedReader2.readLine() == null) {
                  String initial = "(" + initialrow + ");\n";
                  bufferedWriter.write(initial);
                  counter++;
                }else{
                  String initial = "(" + initialrow + "),\n";
                  bufferedWriter.write(initial);
                  counter++;
                }
              }
            }
            bufferedWriter.write(");");
            count=0;
          }else{
            String ok = columndata.get(i).get(j);
            row = row + " " + ok;
          }
        }
      }
      if(!row.isBlank()) {
        bufferedWriter.write(row + ",");
      }
      bufferedWriter.write("\n");
    }
    bufferedWriter.close();
    return true;
  }


}
