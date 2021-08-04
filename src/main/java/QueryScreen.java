import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class QueryScreen {

  public void queryScreenOptions(String databasename) throws IOException {
    Scanner obj = new Scanner(System.in);
    while(true){
      System.out.println("To Generate the dump, Enter DUMP");
      System.out.println("OR");
      System.out.println("Enter your query below");
      String query = obj.next();
      if(query.equals("DUMP")){
        DataDump obj2 = new DataDump();
        obj2.generateDump(databasename);
      }
      FileWriter eventlog = new FileWriter("");
      ParsingUtil parsingUtil = new ParsingUtil();
      QueryType qs = parsingUtil.identifyQueryType(query);
      if(qs == QueryType.ERROR){
        Pattern pattern = parsingUtil.getPatternForQueryType(qs);
        QueryProcessor.processQuery(qs,pattern,query,databasename,eventlog);
      }
    }
  }
}
