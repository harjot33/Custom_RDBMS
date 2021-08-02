import java.util.regex.Pattern;

public class ParsingUtil {
    private static final String CREATE_TABLE_REGEX = "CREATE TABLE (\\w+) \\(((?:\\s?\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\)\\s?;";
    private static final String DROP_TABLE_REGEX = "DROP TABLE (\\w+);";
    private static final String SELECT_REGEX = "SELECT ([\\s\\S]+) FROM\\s(\\w+)+\\s?(WHERE\\s([\\s\\S]+))?;";
    private static final String INSERT_REGEX = "INSERT INTO (\\w+) \\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);";
    private static final String DELETE_REGEX = "DELETE FROM (\\w+) WHERE ([\\s\\S]+);";
    private static final String UPDATE_REGEX = "UPDATE (\\w+)\\sSET\\s([\\s\\S]+)\\sWHERE\\s([\\s\\S]+);";
    private static final String ALTER_REGEX = "ALTER TABLE (\\w+) ADD PRIMARY KEY\\s\\(([\\s\\S]+)\\);";

    public QueryType identifyQueryType(String query) {
        if (query == null) {
            return QueryType.ERROR;
        }

        String upperCaseQuery = query.toUpperCase();

        if (Pattern.compile(SELECT_REGEX).matcher(upperCaseQuery).matches()) {
            return QueryType.SELECT;
        }

        if (Pattern.compile(CREATE_TABLE_REGEX).matcher(upperCaseQuery).matches()) {
            return QueryType.CREATE;
        }

        if (Pattern.compile(DELETE_REGEX).matcher(upperCaseQuery).matches()) {
            return QueryType.DELETE;
        }

        if (Pattern.compile(DROP_TABLE_REGEX).matcher(upperCaseQuery).matches()) {
            return QueryType.DROP;
        }

        return QueryType.ERROR;
    }


    public Pattern getPatternForQueryType(QueryType queryType) {
        switch (queryType) {
            case CREATE:
                return Pattern.compile(CREATE_TABLE_REGEX);
            case SELECT:
                return Pattern.compile(SELECT_REGEX);
            case INSERT:
                return Pattern.compile(INSERT_REGEX);
            case DROP:
                return Pattern.compile(DROP_TABLE_REGEX);
            case UPDATE:
                return Pattern.compile(UPDATE_REGEX);
            case ALTER:
                return Pattern.compile(ALTER_REGEX);
            case DELETE:
                return Pattern.compile(DELETE_REGEX);
        }
        return null;
    }
}