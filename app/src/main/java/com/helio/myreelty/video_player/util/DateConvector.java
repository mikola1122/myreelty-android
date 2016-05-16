package com.helio.myreelty.video_player.util;

import android.util.Log;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Nicolas on 10.03.2016.
 */
public class DateConvector {

    public static final String INPUT_TEMP = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'";
    public static final String OUTPUT_TEMP = "d MMMM yyyy";

    /**
     * Converts a date  "2016-02-25T10:00:57Z" format to "20 days ago" format
     *
     * @return formatted data
     */
    public static String formatDateFromString(String inputDate) {
        Date parsed;
        String outputDate = null;

        SimpleDateFormat df_input = new SimpleDateFormat(INPUT_TEMP,
                java.util.Locale.getDefault());
        SimpleDateFormat dfOutput = new SimpleDateFormat(OUTPUT_TEMP,
                java.util.Locale.getDefault());
        try {
            parsed = df_input.parse(inputDate);
            PrettyTime p = new PrettyTime();
            outputDate = p.format(new Date(parsed.getTime() + 1000 * 60 * 183));
            if (!(outputDate.contains("moments") || outputDate.contains("minute") || outputDate.contains("hour"))) {
                outputDate = dfOutput.format(parsed);
            }
        } catch (ParseException e) {
            Log.d("ParseException", e.toString());
        }
        return outputDate;
    }
}
