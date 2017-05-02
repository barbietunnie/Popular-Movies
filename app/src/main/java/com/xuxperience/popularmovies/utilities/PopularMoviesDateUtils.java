package com.xuxperience.popularmovies.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date Utility
 *
 * Created by badeyemi on 5/2/17.
 */

public final class PopularMoviesDateUtils {
    /**
     * Gets the year component of the String date passed
     *
     * @param dateStr The date string with the format yyyy-MM-dd
     * @return The year extracted from the date string
     * @throws ParseException If the date string is invalid, a parse exception is thrown
     */
    public static Integer getYear(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedTime = sdf.parse(dateStr);

        Calendar cal = Calendar.getInstance();
        cal.setTime(parsedTime);

        return cal.get(Calendar.YEAR);
    }
}
