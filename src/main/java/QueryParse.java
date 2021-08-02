import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParse {
    private static final Pattern CREATE_TABLE_PATTERN = Pattern.compile("(?i)(CREATE\\sTABLE\\s(\\w+)\\s?\\(((?:\\s?\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\)\\s?;)");
    private static final Pattern DROP_TABLE_PATTERN = Pattern.compile("(?i)(DROP\\sTABLE\\s(\\w+);)");
    private static final Pattern SELECT_PATTERN = Pattern.compile("(?i)(SELECT\\s([\\s\\S]+)\\sFROM\\s(\\w+)+\\s?(WHERE\\s([\\s\\S]+))?;)");
    private static final Pattern INSERT_PATTERN = Pattern.compile("(?i)(INSERT\\sINTO\\s+(\\w+)\\s+\\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);)");
    private static final Pattern DELETE_PATTERN = Pattern.compile("(?i)(DELETE\\sFROM\\s+(\\w+)\\s+WHERE\\s+([\\s\\S]+);)");
    private static final Pattern UPDATE_PATTERN = Pattern.compile("(?i)(UPDATE\\s(\\w+)\\sSET\\s([\\s\\S]+)\\sWHERE\\s([\\s\\S]+);)");
    private static final Pattern ALTER_PK_PATTERN = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sPRIMARY\\sKEY\\s\\(([\\s\\S]+)\\);)");
    private static final Pattern ALTER_FK_PATTERN = Pattern.compile("(?i)(ALTER\\sTABLE\\s(\\w+)\\sADD\\sFOREIGN\\sKEY\\s\\(([\\s\\S]+)\\)\\sREFERENCES\\s(\\w+)\\s\\(([\\s\\S]+)\\);)");

    public static void parse(String username) throws IOException {
        QueryProcess qp = new QueryProcess();
        System.out.println("Please enter the SQL queries, type 'dump' to get your dump file, or type 'exit' to quit");
        Scanner sc = new Scanner(System.in);
        String input= "CREATE TABLE Persons (PersonID int, LastName varchar(255), FirstName varchar(255), Address varchar(255), City varchar(255));";
        File file = new File("src/main/resources/Event_Logs.txt"); //default event log text file
        File file1 = new File("src/main/resources/General_Logs.txt");  //default general log text file
        if (file.createNewFile())   //if no file exists, we create one
        {
            System.out.println("New Event Logs created!");
        }
        if (file1.createNewFile())   //if no file exists, we create one
        {
            System.out.println("New General Logs created!");
        }
        FileWriter el = new FileWriter(file, true);   //true means while is appended
        FileWriter gl = new FileWriter(file1, true);

        // User enter SQL queries, if not "exit" continue to match if it is a SQL statement
        while (sc.hasNext() && !((input = sc.nextLine()).equalsIgnoreCase("exit"))) {
            // Use matchers to match the regex patterns we wrote
            Matcher createTableSQL = CREATE_TABLE_PATTERN.matcher(input);
            Matcher dropTableSQL = DROP_TABLE_PATTERN.matcher(input);
            Matcher selectSQL = SELECT_PATTERN.matcher(input);
            Matcher insertSQL = INSERT_PATTERN.matcher(input);
            Matcher deleteSQL = DELETE_PATTERN.matcher(input);
            Matcher updateSQL = UPDATE_PATTERN.matcher(input);
            Matcher alterPKSQL = ALTER_PK_PATTERN.matcher(input);
            Matcher alterFKSQL = ALTER_FK_PATTERN.matcher(input);

            if (createTableSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.create(createTableSQL, username, el, gl);
            } else if (dropTableSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.drop(dropTableSQL, username, el, gl);
            } else if (selectSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.select(selectSQL, username, el, gl);
            } else if (insertSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.insert(insertSQL, username, el, gl);
            } else if (deleteSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.delete(deleteSQL, username, el, gl);
            } else if (updateSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.update(updateSQL, username, el, gl);
            } else if (alterPKSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.alterPK(alterPKSQL, username, el, gl);
            } else if (alterFKSQL.find()) {
                el.append("[User query][").append(username).append("] ").append(input).append("\n");
                qp.alterFK(alterFKSQL, username, el, gl);
            } else {
                // Nothing matches with Regex patterns
                el.append("[User query error][").append(username).append("] ").append(input).append(" is not in standard SQL format").append("\n");
                System.out.println("Please make sure the input is in standard SQL format.\n" + input + " is not valid.");
            }
            // Link to next input
            System.out.println("Please enter the SQL queries or type 'exit' to quit.");
        }
        el.close();
        gl.close();
    }

}
