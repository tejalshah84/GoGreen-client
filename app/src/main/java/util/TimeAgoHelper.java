package util;

import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by amanleenpuri on 5/4/16.
 */
public class TimeAgoHelper {

    public static String getTimeAgoForInputString(String input){
        SimpleDateFormat sf;
        String greenEntryDateFormat = "yyyy-MM-dd";
        // Important note. Only ENGLISH Locale works.
        sf = new SimpleDateFormat(greenEntryDateFormat, Locale.ENGLISH);
        sf.setLenient(true);
        try {
            Date date = sf.parse(input);
            CharSequence createdTime = DateUtils.getRelativeTimeSpanString(
                    date.getTime(),
                    Calendar.getInstance().getTimeInMillis(),
                    DateUtils.SECOND_IN_MILLIS,
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_ABBREV_TIME | DateUtils.FORMAT_ABBREV_RELATIVE);
            return createdTime.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
