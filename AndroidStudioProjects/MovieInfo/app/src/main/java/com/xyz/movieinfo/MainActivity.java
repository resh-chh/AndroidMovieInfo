package com.xyz.movieinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    EditText etQuery;
    Button btnSearch;
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etQuery = (EditText) findViewById(R.id.etQuery);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        tvData = (TextView) findViewById(R.id.tvData);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = etQuery.getText().toString();
                if (movieName.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please Enter Movie Name", Toast.LENGTH_LONG).show();
                    etQuery.requestFocus();
                    return;
                } else {
                    Task1 t1 = new Task1();
                    t1.execute("http://www.omdbapi.com/?s=" + movieName);
                }
            }
        });
    }

    class Task1 extends AsyncTask<String, Void, String> {

        String jsonData = "", data = "", line = "";

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    jsonData += line + "\n";

                }

                if (jsonData != null) {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray movieArray = jsonObject.getJSONArray("Search");
                    for (int i = 0; i < movieArray.length(); i++) {
                        JSONObject movie = movieArray.getJSONObject(i);
                        String title = movie.getString("Title");
                        String year = movie.getString("Year");
                        data += title + " " + year + "\n";
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tvData.setText(s);
        }
    }


}
