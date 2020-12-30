package com.example.stt_kiosk;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import static com.example.stt_kiosk.MainActivity.sqlString;

import android.util.Log;

public class URLConnector {
    private URL url;

    public URLConnector(String url) throws MalformedURLException { this.url = new URL(url); }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);

        reader.close();
        return jsonHtml.toString();
    }

    public String PhPtest(final String sqlString) {
        Log.d("URLConnector", "hello");
        try {
            String postData = "query=" + sqlString;
            Log.d("URLConnector", "query = " + postData);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            Log.d("URLConnector", "1");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.d("URLConnector", "2");
            conn.setRequestMethod("POST");
            Log.d("URLConnector", "3");
            conn.setConnectTimeout(5000);
            Log.d("URLConnector", "4");
            conn.setDoOutput(true);
            Log.d("URLConnector", "5");
            conn.setDoInput(true);
            Log.d("URLConnector", "6");
            OutputStream outputStream = conn.getOutputStream();
            Log.d("URLConnector", "7");
            outputStream.write(postData.getBytes("UTF-8"));
            Log.d("URLConnector", "8");
            outputStream.flush();
            Log.d("URLConnector", "9");
            outputStream.close();
            Log.d("URLConnector", "10");
            String result = readStream(conn.getInputStream());
            Log.d("URLConnector", "11");
            conn.disconnect();
            return result;
        }
        catch (Exception e) {
            Log.i("PHPRequest", "request was failed.");
            Log.d("URLConnector", "error" + e);
            return null;
        }
    }
}

