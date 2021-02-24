package com.example.userdatatask;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpHandler {


        private static final String TAG = HttpHandler.class.getSimpleName();
        long startTime;
        long endTime;
        long fileSize;
        Context context;
        isSlowNW isSlowNW;
        // bandwidth in kbps
        private int POOR_BANDWIDTH = 150;
        private int AVERAGE_BANDWIDTH = 550;
        private int GOOD_BANDWIDTH = 2000;

        public HttpHandler() {
        }

        public HttpHandler(Context context) {
            this.context = context;
            isSlowNW = (HttpHandler.isSlowNW) ((Activity) context);
        }

        public String makeServiceCall(String reqUrl) {
            String response = null;
            try {
                reqUrl = reqUrl.replaceAll(" ", "%20");
                URL url = new URL(reqUrl);
                startTime = System.currentTimeMillis();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");
                conn.setRequestProperty("Connection", "Keep-Alive");

                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                response = convertStreamToString(in);
                if (context != null) {

                    speedCal(context);
                }


            } catch (MalformedURLException e) {
                Log.e(TAG, "MalformedURLException: " + e.getMessage());
            } catch (ProtocolException e) {
                Log.e(TAG, "ProtocolException: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "IOException: " + e.getMessage());
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }

            if (response != null) {


                try {
                    JSONObject object = new JSONObject(response);
                    String error = object.optString("Error");

                    if (error.contains("Session")) {

                        if (context != null) {

                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    NetworkConnectionUtil.showSessionexpire(context);

                                }
                            });


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
            return response;
        }

        private String convertStreamToString(InputStream is) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append('\n');
                }


                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                while (is.read(buffer) != -1) {

                    bos.write(buffer);
                    byte[] docBuffer = bos.toByteArray();
                    fileSize = bos.size();
                }
                Log.i(TAG, "convertStreamToString: fileSize:- " + fileSize);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            endTime = System.currentTimeMillis();
            return sb.toString();
        }


        public void speedCal(Context context) {

            // calculate how long it took by subtracting endtime from starttime

            double timeTakenMills = Math.floor(endTime - startTime);  // time taken in milliseconds
            double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
            final int kilobytePerSec = (int) Math.round(1024 / timeTakenSecs);

            if (kilobytePerSec <= POOR_BANDWIDTH) {
                // slow connection
                Log.i(TAG, "speedCal: slow connection");

                if (context != null) {
                    isSlowNW.slowNwDetected(context, "slow connection");
                }

            }
            // get the download speed by dividing the file size by time taken to download
            double speed = fileSize / timeTakenMills;

            Log.d(TAG, "speedCal: Time taken in secs: " + timeTakenSecs);
            Log.d(TAG, "speedCal: kilobyte per sec: " + kilobytePerSec);
            Log.d(TAG, "speedCal: Download Speed: " + speed);
            Log.d(TAG, "speedCal: File size: " + fileSize);


        }


        public interface isSlowNW {
            void slowNwDetected(Context context, String errorMess);


        }


}
