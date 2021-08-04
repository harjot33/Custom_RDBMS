import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @author Harjot Singh
 */
public class Login {

  public boolean loginAuth(List<String> credentials) throws IOException {
    String filepath = "src/main/resources/Credentials.cred";
    File file = new File(filepath);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    String line = "";
    String username = credentials.get(0);
    String password = credentials.get(1);
    while ((line = bufferedReader.readLine()) != null) {
      String[] creds = line.split(":");
      if(creds[0].equals(username)){
        if(creds[1].equals(password)){
          System.out.println("Please answer the security question");
          Scanner s = new Scanner(System.in);
          System.out.println(creds[2]);
          String userinput = s.next();
          if(userinput.equals(creds[3])){
            return true;
          }
        }
      }
    }
    return false;
  }
  }

