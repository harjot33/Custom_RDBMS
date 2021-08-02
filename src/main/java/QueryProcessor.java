import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryProcessor {
    static QueryExecutor executor = new QueryExecutor();
    public static void processQuery(QueryType queryType, Pattern pattern, String originalQuery) {
        if (queryType == QueryType.ERROR || pattern == null || originalQuery.isEmpty()) {
            return;
        }

        switch (queryType) {
            case CREATE:
                processCreateQuery(pattern, originalQuery);
                break;
            case SELECT:
                processSelectQuery(pattern, originalQuery);
                break;
            case INSERT:
                processInsertQuery(pattern, originalQuery);
                break;
            case UPDATE:
                processUpdateQuery(pattern, originalQuery);
                break;
            case ALTER:
                processAlterQuery(pattern, originalQuery);
                break;
            case DELETE:
                processDeleteQuery(pattern, originalQuery);
                break;
            case DROP:
                processDropQuery(pattern, originalQuery);
                break;
        }
    }

    private static void processCreateQuery(Pattern pattern, String originalQuery) {
        // create query.
    }

    private static void processSelectQuery(Pattern pattern, String originalQuery) {
    }

    private static void processInsertQuery(Pattern pattern, String originalQuery) {
    }


    private static void processUpdateQuery(Pattern pattern, String originalQuery) {
    }

    private static void processAlterQuery(Pattern pattern, String originalQuery) {
    }

    private static void processDeleteQuery(Pattern pattern, String originalQuery) {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        if (matcher.groupCount() > 0) {
            String tableName = matcher.group(1);
            String condition = matcher.group(2);
            if (condition != null && condition.indexOf('=') != -1) {
                String[] conditionArray = condition.split("=");
                String columnName = conditionArray[0].trim();
                String columnValue = conditionArray[1].trim();
                System.out.println("");
                executor.executeDeleteQuery(tableName, columnName, columnValue);
            }
            System.out.println("");
        }
    }

    private static void processDropQuery(Pattern pattern, String originalQuery) {
        String upperCaseQuery = originalQuery.toUpperCase();
        Matcher matcher = pattern.matcher(upperCaseQuery);
        matcher.matches();
        String tableName = matcher.group(1);
        executor.executeDropQuery(tableName);
    }
}

