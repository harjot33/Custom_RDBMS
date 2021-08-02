import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Harjot Singh
 */
public class Login {

  public boolean loginAuth(List<String> credentials) throws IOException {
    String filepath = "dbfiles/Credentials.cdb";
    File file = new File(filepath);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line = "";
    String username = credentials.get(0);
    String password = credentials.get(1);
    while ((line = bufferedReader.readLine()) != null) {
      String[] creds = line.split(":");
      if(creds[0].equals(username)){
        if(creds[1].equals(password)){
          return true;
        }
      }
    }
    return false;
  }
  }

