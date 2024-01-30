package com.example.ubinone.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;

import com.example.ubinone.recyclers.agenda.RecyclerAgendaBase;
import com.example.ubinone.recyclers.agenda.RecyclerAgendaItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Utils {

    private static final String TAG = "Utils";

    public static int invertedColor(int color) {
        return color != Color.parseColor("#808080") ? color ^ 0x00ffffff : Color.parseColor("#000000");
    }

    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();

        config.setLocale(locale);
        Locale.setDefault(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static List<RecyclerAgendaBase> resultInterpreter(HTMLRequester htmlRequester) {
        List<RecyclerAgendaBase> items = new ArrayList<>();

        if (htmlRequester.getRequestResult() != null)
            htmlRequester.getRequestResult().forEach(map -> {

                String start = (String) map.get("start");
                String end = (String) map.get("end");
                boolean allDay = (boolean) map.get("allDay");
                String backgroundColor = (String) map.get("backgroundColor");

                items.add(new RecyclerAgendaItem(start, end, allDay, String.valueOf(Html.fromHtml((String) map.get("description"), Html.FROM_HTML_MODE_LEGACY)), backgroundColor));
            });
        return items;
    }

    public static String getDateTime(String start, String end) {
        Log.d(TAG, "getDateTime: " + start + " / " + end);
        StringBuilder builder = new StringBuilder();
        String[] splitStart = start.split(" ");
        builder.append(splitStart[0]).append(" ").append(splitStart[1]);

        if (end != null) {
            String[] splitEnd = end.split(" ");
            builder.append("-").append(splitEnd[1]);
        } else {
            builder.append(" | ").append("All Day");
        }
        return builder.toString();
    }
}
