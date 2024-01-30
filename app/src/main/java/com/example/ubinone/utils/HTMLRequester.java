package com.example.ubinone.utils;

import android.content.Context;
import android.net.Uri;

import com.example.ubinone.R;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HTMLRequester {

    private final URL urlRooms;
    private final URL urlClass;
    private final URL url;
    private final Map<String, String> forcedContent;
    private String TAG = "HTMLRequester";
    private Context context;
    private List<String> rooms = null;
    private List<String> classes = null;
    private List<HashMap<String, Object>> requestResult = null; // String unescaped = String.valueOf(Html.fromHtml(STRING, Html.FROM_HTML_MODE_LEGACY));
    private String day;

    public HTMLRequester(Context context) throws IOException {
        this.context = context;
        forcedContent = new HashMap<>();
        urlRooms = new URL("https://celcat.u-bordeaux.fr/calendar/Home/ReadResourceListItems?myResources=false&searchTerm=_&pageSize=4000&resType=102");
        urlClass = new URL("https://celcat.u-bordeaux.fr/calendar/Home/ReadResourceListItems?myResources=false&searchTerm=_&pageSize=4000&resType=103");
        url = new URL("https://celcat.u-bordeaux.fr/calendar/Home/GetCalendarData");
//        forcedContent.put("resType", "103");
        forcedContent.put("calView", "agendaDay");
        forcedContent.put("colourScheme", "3");
    }

    public List<String> getRooms() {
        return rooms;
    }

    public List<String> getClasses() {
        return classes;
    }

    public List<HashMap<String, Object>> getRequestResult() {
        return requestResult;
    }

    public void getDates(Date date) {
        Date today;

        if (date != null) {
            today = date;
        } else {
            Calendar calendar = Calendar.getInstance();
            day = context.getString(R.string.today);
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 19) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                day = context.getString(R.string.tomorrow);
            }
            today = calendar.getTime();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", context.getResources().getConfiguration().getLocales().get(0));
        forcedContent.put("start", dateFormat.format(today));
        forcedContent.put("end", dateFormat.format(today));
    }

    public void requestData() throws IOException {
        String r = connection(urlRooms, null);
        HashMap<String, List<HashMap<String, String>>> mapping = new ObjectMapper().readValue(r, HashMap.class);
        List<HashMap<String, String>> lst = mapping.get("results");
        rooms = lst.stream().map(m -> m.get("id")).collect(Collectors.toList());

        String c = connection(urlClass, null);
        HashMap<String, List<HashMap<String, String>>> mapping2 = new ObjectMapper().readValue(c, HashMap.class);
        List<HashMap<String, String>> lst2 = mapping2.get("results");
        classes = lst2.stream().map(m -> m.get("id")).collect(Collectors.toList());
    }

    public void requestForClass(String query, Date date) throws IOException {
        if ("None".equals(query)) return;

        processDates(date);
        StringBuilder params = buildParams("103");

        params.append("&federationIds%5B%5D=").append(Uri.encode(query));
        String result = makeConnection(url, params);

        handleResponse(result);
    }

    public void requestForRoom(String query) throws IOException {
        if (query == null || query.isEmpty()) return;

        processDates(null);
        StringBuilder params = buildParams("102");

        params.append("&federationIds%5B%5D=").append(Uri.encode(query));

        String result = makeConnection(url, params);

        handleResponse(result);
    }

    private StringBuilder buildParams(String resType) {
        StringBuilder sbParams = new StringBuilder();
        sbParams.append("resType=").append(resType);

        for (Map.Entry<String, String> entry : forcedContent.entrySet()) {
            sbParams.append("&").append(Uri.encode(entry.getKey())).append("=").append(Uri.encode(entry.getValue()));
        }

        return sbParams;
    }

    private void processDates(Date date) {
        getDates(date);
    }

    private String makeConnection(URL url, StringBuilder params) throws IOException {
        return connection(url, params);
    }

    private void handleResponse(String result) throws IOException {
        HashMap<String, List<HashMap<String, Object>>> mapping = new ObjectMapper().readValue(result, HashMap.class);
        requestResult = mapping.get("results");
//        Log.v(TAG, requestResult.toString());
    }


    private String connection(URL target, StringBuilder sbParams) throws IOException {
        HttpURLConnection client = (HttpURLConnection) target.openConnection();
        client.setDoOutput(true);
        client.setRequestMethod("POST");
        client.setReadTimeout(10000);
        client.setConnectTimeout(15000);
        client.connect();

        if (sbParams != null) {
            String paramsString = sbParams.toString();

            DataOutputStream wr = new DataOutputStream(client.getOutputStream());
            wr.writeBytes(paramsString);
            wr.flush();
            wr.close();
        }

        InputStream in = new BufferedInputStream(client.getInputStream());

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        if (sbParams != null) result.append("{\"results\":");
        String line;
        while ((line = reader.readLine()) != null) {
//            String unescaped = String.valueOf(Html.fromHtml(line, Html.FROM_HTML_MODE_LEGACY));
            result.append(line);
        }
        if (sbParams != null) result.append("}");

        client.disconnect();
//        Log.v(TAG, result.toString());
        return result.toString();
    }

    public String getDay() {
        return day;
    }
}
