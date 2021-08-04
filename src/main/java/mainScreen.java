import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Harjot Singh
 */
public class mainScreen {
  public static final int LOW_LIMIT = 0;
  public static final int HIGH_LIMIT = 3;

  public static void main(String[] args) {
    mainScreen obj = new mainScreen();
    obj.mainsscreen();
  }

  public void mainsscreen() {
    System.out.println("Welcome to 5408_RDbMS_Custom(RDb)");
    int userinput = 0;
    boolean correctinput = false;
    int tries = 0;
    Scanner scanner = new Scanner(System.in);
    System.out.println("Choose the following options from the options. \n" +
        "1. Login. \n" +
        "2. Register. \n" +
        "3. Exit Application. \n");

    try {
      userinput = scanner.nextInt();
      if (userinput > LOW_LIMIT && userinput <= HIGH_LIMIT) {
        correctinput = true;
      } else {
        System.out.println("Wrong Input Choice.");
        mainsscreen();
      }
    } catch (InputMismatchException inputMismatchException) {
      System.out.println("You can only enter an integer value");
    }

    if (correctinput) {
      if (userinput == 1) {
        loginUser(scanner);

      } else if (userinput == 2) {
        List<String> credentials = null;
        try {
          credentials = registerUser();
        } catch (IOException e) {
          System.out.println("An Error was encountered, try again.");
          mainsscreen();
        }
        Map<String, String> secQA = new HashMap<>();
        if(credentials != null){
          secQA = setSecQuestions(credentials);
        }
        if(secQA != null){
          String question = "";
          String answer = "";
          String combined = "";
          Set<String> secQAKeys = secQA.keySet();
          for(String S : secQAKeys){
             answer = secQA.get(S);
             question = S;
             combined  = question + ":" + answer;
          }
          credentials.add(combined);
          try {
            Registration.registeruser(credentials);
          } catch (IOException e) {
            System.out.println("Problem Encountered while registering");
            mainsscreen();
          }
        }
        System.out.println("You have been successfully registered");
        mainsscreen();
      } else if (userinput == 3) {
        System.out.println("Application is exiting.");
      }
    }
  }


  public List<String> registerUser() throws IOException {
    System.out.println("Enter the user name to sign up.");
    List<String> credentials = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    String username = sc.next();
    if (!Validations.isStringvalid(username)) {
      System.out.println("You have entered an invalid username");
      registerUser();
    }
    if(Registration.checkIfUserExists(username)){
      System.out.println("Username already exists! Please choose another " +
          "username");
      registerUser();
    }
    credentials.add(username);
    System.out.println("Enter the password that you want to set.");
    String password = sc.next();
    if (password.isEmpty() || password == null || !Validations.isPassValid(password)) {
      System.out.println("Invalid Password");
      System.out.println("Enter atleast 1 small case, capital case, special " +
          "character, digit, no white spaces are allowed.");
      registerUser();
    }
    credentials.add(password);
    System.out.println("You have successfully chosen your username and " +
        "password, now you will be setting your security questions.");

    return credentials;
  }

  public void loginUser(Scanner scanner){
    System.out.println("Enter your username");
    String username = scanner.next();
    System.out.println("Enter your password");
    String password = scanner.next();
    Login login = new Login();
    List<String> usercredentials = new ArrayList<>();
    usercredentials.add(username);
    usercredentials.add(password);
    try {
      boolean auth = login.loginAuth(usercredentials);
      if(auth){
        System.out.println("You have been logged in successfully! " +
            "Redirecting to the user screen.");
        File file = new File("src/main/resources/GeneralRecord.gr");
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line=fileReader.readLine())!=null){
          String[] vals = line.split(":");
          if(vals[0].equals(username)){
            QueryScreen queryScreen = new QueryScreen();
            queryScreen.queryScreenOptions(vals[1]);
          }
        }

      }else{
        System.out.println("You have entered wrong credentials, please enter " +
            "them again. ");
        loginUser(scanner);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> setSecQuestions(List<String> crdentials) {
    System.out.println("Okay, so, " + crdentials.get(0) + " -  Choose from " +
        "the " +
        "following security questions.");
    int userinput = 0;
    boolean correctinput = false;
    int tries = 0;
    Scanner scanner = new Scanner(System.in);
    Map<String, String> secquestions = new HashMap<>();
    System.out.println("Choose the following options from the options. \n" +
        "1. What is my favourite color? \n" +
        "2. What is my first pet's name? \n" +
        "3. What is my greatest fear? \n");

    try {
      userinput = scanner.nextInt();
      if (userinput > LOW_LIMIT && userinput <= HIGH_LIMIT) {
        correctinput = true;
      } else {
        System.out.println("Wrong Input Choice.");
        mainsscreen();
      }
    } catch (InputMismatchException inputMismatchException) {
      System.out.println("You can only enter an integer value");
    }

    if (correctinput) {
      if (userinput == 1) {
        System.out.println("You have chosen option 1.");
        System.out.println("Enter your answer to the Question 1.");
        String sec1 = scanner.next();
        if (Validations.isSecAns(sec1)) {
          List<String> secans = new ArrayList<>();
          secans.add("What is my favourite color?");
          secans.add(sec1);
          secquestions.put(secans.get(0),secans.get(1));
          System.out.println("You have successfully set your security " +
              "question.");
        } else {
          System.out.println("You can only enter letters in this answer, no " +
              "spaces are allowed.");
          setSecQuestions(crdentials);
        }
      } else if (userinput == 2) {
        System.out.println("You have chosen option 2.");
        System.out.println("Enter your answer to the Question 2.");
        String sec2 = scanner.next();
        if (Validations.isSecAns(sec2)) {
          List<String> secans = new ArrayList<>();
          secans.add("What is my first pet's name?");
          secans.add(sec2);
          secquestions.put(secans.get(0),secans.get(1));
          System.out.println("You have successfully set your security " +
              "question.");

        } else {
          System.out.println("You can only enter letters in this answer, no " +
              "spaces are allowed.");
          setSecQuestions(crdentials);
        }
      } else if (userinput == 3) {
        System.out.println("You have chosen option 3.");
        System.out.println("Enter your answer to this question");
        String sec3 = scanner.next();
        if (Validations.isSecAns(sec3)) {
          List<String> secans = new ArrayList<>();
          secans.add("3. What is my greatest fear?");
          secans.add(sec3);
          secquestions.put(secans.get(0),secans.get(1));
          System.out.println("You have successfully set your security " +
              "question.");

        } else {
          System.out.println("You can only enter letters in this answer, no " +
              "spaces are allowed.");
          setSecQuestions(crdentials);
        }

      }
    }
    return  secquestions;
    }
  }


