import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcess {
    public static void create(Matcher createTableSQL, String Database, FileWriter el,FileWriter gl) throws IOException {
        Action ac = new Action();
        String tableName = createTableSQL.group(2);
        String columns = createTableSQL.group(3);
        String[] columnArray = columns.split("\\s*,\\s*");
        List<String> columnList = Arrays.asList(columnArray);
        ArrayList<String> columnName = new ArrayList<>();
        ArrayList<String> dataType = new ArrayList<>();
        for(String s:columnList) {
            String[] temp = s.split("\\s+");
            columnName.add(temp[0]);
            dataType.add(temp[1]);
        }
        ac.CreateTable(Database, tableName, columnName, dataType);
    }

    public void drop(Matcher dropTableSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void select(Matcher selectSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void insert(Matcher insertSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void delete(Matcher deleteSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void update(Matcher updateSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void alterPK(Matcher alterPKSQL, String username, FileWriter el, FileWriter gl) {
    }

    public void alterFK(Matcher alterFKSQL, String username, FileWriter el, FileWriter gl) {
    }
}
