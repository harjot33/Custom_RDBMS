import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Harjot Singh
 */
public class Validations {
  public static boolean isUserNameValid(String input) {
    if (input == null || input.isEmpty())
      return false;
    Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(input);
    boolean b = m.find();
    return !b;
  }

  public static boolean isPasswordValid(String input) {
    if (input == null || input.isEmpty())
      return false;
    System.out.println(input);
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
    Pattern p = Pattern.compile(regex);
    Matcher m = p.matcher(input);
    return m.matches();
  }

  public static boolean isAnswerValid(String input) {
    if (input == null || input.isEmpty())
      return false;
    Pattern p = Pattern.compile("^[A-Za-z0-9]", Pattern.CASE_INSENSITIVE);
    Matcher m = p.matcher(input);
    return m.find();
  }

  public static boolean checkTableAvailable(String tableName){
    String db = DatabaseHandler.databaseName;
    return true;
  }




}
