package com.example.userdatatask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    int userId,id;
   String title,body;
    RecyclerView recyclerview;
    UserAdapter userAdapter;
    ArrayList<UserModel> userModels;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userModels = new ArrayList<>();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(MainActivity.this, 1, LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        userAdapter = new UserAdapter(userModels,MainActivity.this);
        recyclerview.setAdapter(userAdapter);

        new GetUserAction().execute();
    }

    private class GetUserAction extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String json, pass, jsonStr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Loading...", "Please wait!", true, false);
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();


            JSONArray root = new JSONArray();
            JSONObject ob = new JSONObject();

            try {

                final String url = "https://jsonplaceholder.typicode.com/posts";


                pass = url;

                // Making a request to url and getting response
                jsonStr = sh.makeServiceCall(pass);
                Log.i(TAG, "doInBackground: RE:- " + jsonStr
                        + pass);
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }


            Log.i(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {

                    // Getting JSON Array node
                    JSONArray grp_mst =new JSONArray(jsonStr);

                    // JSONArray jsonArray = new JSONArray(jsonStr);
                    // JSONObject config = jsonArray.getJSONObject(0);

                    if (!grp_mst.isNull(0)) {
                        Log.i(TAG, "doInBackground: It  is null");

                        // looping through All Contacts
                        for (int i = 0; i < grp_mst.length(); i++) {
                            JSONObject c = grp_mst.getJSONObject(i);
                            // GrpItem grpItem1 = new GrpItem(c.getString("grp_code"), dir + "/" +  c.getString("image_name") + ".jpg", c.getString("invcod"));
                            // movieList.add(grpItem1);

                            String  userId = c.getString("userId");
                            String  id = c.getString("id");
                            String title = c.getString("title");
                            String body = c.getString("body");

                            userModel = new UserModel(userId,id,title,body);

                            userModels.add(userModel);

                            //  GrpItem grpItem = new GrpItem(grpcode[k], dir + "/" + image_name[k] + ".jpg", invcode[k]);

                        }


                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {



                            }
                        });
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                                Toast.makeText(getApplicationContext(),
//                                        "Json parsing error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG)
//                                        .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            userAdapter.notifyDataSetChanged();


            // super.onPostExecute(result);
//            for (int i = 0; i < invcode.length; i++) {
//                    GrpItem grpItem = new GrpItem(grpcode[i], dir + "/" + image_name[i] + ".jpg", invcode[i]);
//                    movieList.add(grpItem);
//
//                }
//
//                mAdapter.notifyDataSetChanged();
        }

    }

}