package bd.dataAccessor;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormater {

    public static Timestamp convertStringToTimestamp(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Parse the string to Date
            Date parsedDate = dateFormat.parse(dateString);

            // Convert Date to Timestamp
            return new Timestamp(parsedDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("не верно введенная дата");
        }
    }
}
