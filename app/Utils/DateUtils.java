package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

    private static final String FORMAT = "MMM-dd-yyyy HH:mm EEE"; //Ex: jun-24-2016 14:30 qui

    /**
     * Retorna a string contendo a data de acordo com o FORMAT
     */
    public static String todayFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT, Locale.getDefault());
        Date today = new Date();
        return formatter.format(today);
    }

}
