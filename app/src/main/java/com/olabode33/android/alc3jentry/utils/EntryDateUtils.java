package com.olabode33.android.alc3jentry.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by obello004 on 7/1/2018.
 */

public class EntryDateUtils {

    private SimpleDateFormat fullDateStringFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    private Date convertedToDate;

    private SimpleDateFormat slimDateFormat = new SimpleDateFormat("MMM dd, yyyy");
    private SimpleDateFormat dayTimeDateFormat = new SimpleDateFormat("EEE, hh:mm:ss a");

    public EntryDateUtils(String fullDate) {
        try {
            convertedToDate = fullDateStringFormat.parse(fullDate);
        } catch (Exception e) {

        }
    }

    public String getSlimDateFormat() {
        String slimDate = slimDateFormat.format(convertedToDate);

        return slimDate;
    }

    public String getDayTimeDateFormat() {
        String dayTime = dayTimeDateFormat.format(convertedToDate);

        return dayTime;
    }
}
