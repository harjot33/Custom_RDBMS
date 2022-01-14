import java.io.*;
import java.util.List;

/**
 * @author Harjot Singh
 */
public class Registration {
  public static boolean registeruser(List<String> userCredentials) throws IOException {

    try {
      String filepath = "src/main/resources/Credentials.cred";
      File file = new File(filepath);
      FileWriter fw = new FileWriter(file,true);
      String cred = userCredentials.get(0);
      String password = userCredentials.get(1);
      String sec = userCredentials.get(2);
      String combined = cred + ":"+password+":"+sec;
      fw.write(combined+"\n");
      fw.write("NODB");
      fw.write("\n\n\n");
      fw.close();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return true;
  }

  public static boolean checkIfUserExists(String username) throws IOException {
    String filepath = "src/main/resources/Credentials.cred";
    File file = new File(filepath);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line = "";
    while ((line = bufferedReader.readLine()) != null) {
      String[] creds = line.split(":");
      if(creds[0].equals(username)){
        return true;
      }
    }
    return false;
  }
}
