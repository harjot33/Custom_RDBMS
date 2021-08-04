import java.util.regex.Pattern;

public class ParsingUtil {
    private static final String CREATE_TABLE_REGEX = "(?i)CREATE TABLE (\\w+) \\(((?:\\s?\\w+\\s\\w+\\(?[0-9]*\\)?,?)+)\\)\\s?;";
    private static final String DROP_TABLE_REGEX = "(?i)DROP TABLE (\\w+);";
    private static final String SELECT_REGEX = "(?i)SELECT ([\\s\\S]+) FROM\\s(\\w+)+\\s?(WHERE\\s([\\s\\S]+))?;";
    private static final String INSERT_REGEX = "(?i)INSERT INTO (\\w+) \\(([\\s\\S]+)\\)\\s+VALUES\\s+\\(([\\s\\S]+)\\);";
    private static final String DELETE_REGEX = "(?i)DELETE FROM (\\w+) WHERE ([\\s\\S]+);";
    private static final String UPDATE_REGEX = "(?i)UPDATE (\\w+)\\sSET\\s([\\s\\S]+)\\sWHERE\\s([\\s\\S]+);";
    private static final String ALTER_TABLE_DROP = "(?i)ALTER TABLE (\\w+)\\sDROP " +
        "COLUMN (\\w+);";
    private static final String ALTER_TABLE_ADD = "(?i)ALTER TABLE (\\w+)\\sADD " +
        "COLUMN (\\w+);";

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
        if (Pattern.compile(UPDATE_REGEX).matcher(upperCaseQuery).matches()) {
            return QueryType.UPDATE_TABLE;
        }

        if (Pattern.compile(ALTER_TABLE_DROP).matcher(upperCaseQuery).matches()) {
            return QueryType.ALTER_TABLE_DROP;
        }

        if (Pattern.compile(ALTER_TABLE_ADD).matcher(upperCaseQuery).matches()) {
            return QueryType.ALTER_TABLE_ADD;
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
            case UPDATE_TABLE:
                return Pattern.compile(UPDATE_REGEX);
            case ALTER_TABLE_DROP:
                return Pattern.compile(ALTER_TABLE_DROP);
            case DELETE:
                return Pattern.compile(DELETE_REGEX);
        }
        return null;
    }
}