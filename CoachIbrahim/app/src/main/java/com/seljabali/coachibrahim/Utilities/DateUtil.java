package com.seljabali.coachibrahim.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

import hirondelle.date4j.DateTime;

/**
 * Created by samsoom on 1/15/15.
 */
public class DateUtil {
    public static DateTime dateToDateTime (Date date) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String formatted = format1.format(date.getTime());
        return new DateTime(formatted);
    }
}
