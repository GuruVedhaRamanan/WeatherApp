package com.example.android.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

   TextView Result;
    EditText editText;



    public void findWeather(View view) {
        String CityName = editText.getText().toString().trim();
        if (TextUtils.isEmpty(CityName)) {
            Toast.makeText(MainActivity.this, "Enter the City Name", Toast.LENGTH_LONG).show();
        } else {
            {
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                try {
                    DownloadContent downloadContent = new DownloadContent();
                    String EncodedCityName = URLEncoder.encode(CityName, "UTF-8");

                    downloadContent.execute("http://api.openweathermap.org/data/2.5/weather?q=" + EncodedCityName + "&appid=63ae2df521994becea54d3de60c4881f");
                } catch (UnsupportedEncodingException e) {

                    Toast.makeText(MainActivity.this, "Could not Find the Weather", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
      public class DownloadContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    char currentdata = (char) data;

                    result += currentdata;

                    data = inputStreamReader.read();
                }

                return result;

            }
            catch (Exception e)
            {

                Toast.makeText(MainActivity.this,"Could not Find the Weather",Toast.LENGTH_LONG).show();
            }
        return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject jsonObject = new JSONObject(result);

                String weatherInfo = jsonObject.getString("weather");


                String  message = "";

                Log.i("WeatherINFO", weatherInfo);

                JSONArray jsonArray = new JSONArray(weatherInfo);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonpart = jsonArray.getJSONObject(i);

                    String main="";String description = "";
                    main =  jsonpart.getString("main");


                  description=  jsonpart.getString("description");

                    Log.i("main", jsonpart.getString("main"));

                    Log.i("Description", jsonpart.getString("description"));
        
                    if(main !="" && description != "")
                    {
                        
                         
                        message += main+ ":"+ description+"\r\n";
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this,"Enter the CityName Properly",Toast.LENGTH_LONG).show();
                    }


                }
                          if(message!="")
                          {
                              Result.setText(message);
                                                          }

            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Enter the CityName Properly",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.cityname);

        Result = (TextView)findViewById(R.id.resultText);



    }

}