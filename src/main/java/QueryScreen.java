import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class QueryScreen {
  static List<String> transactionqueries = new ArrayList<>();

  public static void main(String[] args) throws IOException {
    QueryScreen obj = new QueryScreen();
    obj.queryScreenOptions("Database1");
  }

  public static void queryScreenOptions(String databasename) throws IOException {

    System.out.println("Welcome to 5408_RDbMS_Custom(RDb)");
    int userinput = 0;
    boolean correctinput = false;
    int tries = 0;
    Scanner scanner = new Scanner(System.in);
    File file = new File("src/main/resources/logs.lg");
    FileWriter eventlog = new FileWriter(file);
    System.out.println("Choose the following options from the options. \n" +
        "1. Perform SQL Query. \n" +
        "2. Perform Transaction. \n" +
        "3. Generate ERD. \n" +
        "4. Generate Data Dump. \n" +
        "5. Change your currently selected database. \n");

    try {
      userinput = scanner.nextInt();
      if (userinput > 0 && userinput <= 5) {
        correctinput = true;
      } else {
        System.out.println("Wrong Input Choice.");
        queryScreenOptions("");
      }
    } catch (InputMismatchException inputMismatchException) {
      System.out.println("You can only enter an integer value");
    }

    if (correctinput) {
      if (userinput == 1) {
        queryPerformer(databasename);
        queryScreenOptions(databasename);
      } else if (userinput == 2) {
        performTransaction(databasename,eventlog);
      } else if (userinput == 3) {
      }else if(userinput == 4){
        DataDump dataDump = new DataDump();
        dataDump.generateDump(databasename);
        queryScreenOptions(databasename);
      }else if(userinput == 5){

      }else {
        System.out.println("Wrong input");
        System.out.println("Redirecting to the menu.");
        queryScreenOptions(databasename);
      }
    }
  }
  public static void transactionPerformer(List<String> transactions,
                                   String databasename, FileWriter eventlog) throws IOException{
    ParsingUtil parsingUtil = new ParsingUtil();
    for(int i =0; i<transactions.size(); i++){
      String query = transactions.get(i);
      QueryType qs = parsingUtil.identifyQueryType(query);
      if(qs != QueryType.ERROR){
        Pattern pattern = parsingUtil.getPatternForQueryType(qs);
        QueryProcessor.processQuery(qs,pattern,query,databasename,eventlog);
      }else{
        System.out.println("The transaction couldn't be completed due to " +
            "there being anamolies in the syntax of the transaction queries.");
      }
    }
  }

  public static void queryPerformer(String databasename) throws  IOException{
    Scanner sc = new Scanner(System.in);
    System.out.println("Please enter your query, Make sure to follow the " +
        "proper SQL Syntax.");
    String query = sc.nextLine();
    if(query == null || query.isEmpty()){
      System.out.println("Wrong query syntax entered, enter again.");
      queryPerformer(databasename);
    }else{
      FileWriter eventlog = new FileWriter("src/main/resources/logs.lg");
      ParsingUtil parsingUtil = new ParsingUtil();
      QueryType qs = parsingUtil.identifyQueryType(query);
      if(qs != QueryType.ERROR){
        Pattern pattern = parsingUtil.getPatternForQueryType(qs);
        QueryProcessor.processQuery(qs,pattern,query,databasename,eventlog);
      }else{
        System.out.println("You have entered wrong SQL Query Syntax.");
        System.out.println("Enter 'BACK' to return to the main screen");
        String input = sc.next();
        if(input.equals("BACK")){
          queryScreenOptions(databasename);
        }
        System.out.println("Enter your query again.");
        queryPerformer(databasename);
      }
    }
  }

  public static void performTransaction(String databasename,
                                     FileWriter eventlog) throws IOException{
    boolean done = false;
    while (!done){
      System.out.println("Enter the transaction queries, when you have " +
          "finished entering the queries, enter 'DONE");
      Scanner sc=  new Scanner(System.in);
      String query = sc.nextLine();
      if(query.equals("DONE")){
        System.out.println("Finished the transaction, enter 'COMMIT' to " +
            "commit the transaction, or 'ROLLBACK' to rollback the transaction.");
        String uinput = sc.next();
        if(uinput.equals("COMMIT")){
          transactionPerformer(transactionqueries,databasename,eventlog);
          System.out.println("Successfully Performed All the Transactions");
          queryScreenOptions(databasename);

        }else if(uinput.equals("ROLLBACK")){
          System.out.println("You have successfully rolled back the " +
              "transaction");
          transactionqueries.clear();
        }else{
          System.out.println("Wrong Input Entered, Returning to the " +
              "transaction queries screen");
          performTransaction(databasename,eventlog);
        }
      }
      ParsingUtil parsingUtil = new ParsingUtil();
      QueryType qs = parsingUtil.identifyQueryType(query);
      if(qs != QueryType.ERROR){
        transactionqueries.add(query);
      }else{
        System.out.println("You have entered a wrong transaction query " +
            "format, enter again.");
        performTransaction(databasename,eventlog);
      }
    }
  }

}
