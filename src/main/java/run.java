import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class run {
    public static void main(String[] args) throws IOException {
        QueryProcessor qp = new QueryProcessor();
        String query = "INSERT INTO Customers (CustomerName, ContactName) VALUES ('Cardinal', 'Tom B.');";
        String query1 = "CREATE TABLE Cestomers (CustomerName varchar(20), ContactNumber number(10));";
//        QueryProcessor.processInsertQuery(Pattern.compile(ParsingUtil.INSERT_REGEX),query,"Database1");
        QueryProcessor.processCreateQuery(Pattern.compile(ParsingUtil.CREATE_TABLE_REGEX),query1,"Database1");
//        File table = new File("src/main/resources/Table/hello.tb");
//        table.createNewFile();
    }
}
