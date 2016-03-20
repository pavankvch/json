package com.mobile.json;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv;
    String movieName;
    int releaseDate;
    StringBuffer bufferData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv=(TextView)findViewById(R.id.txt_jsonparse);
        setSupportActionBar(toolbar);

        new ParseAsync().execute();
    }

    private class ParseAsync extends AsyncTask<String, Void, String> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader=null;
        StringBuffer buffer;
        ProgressDialog dialog;
        JSONObject data= null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Please Wait...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
//                URL url = new URL("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");
                URL url = new URL("http://jsonparsing.parseapp.com/jsonData/moviesDemoList.txt");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                reader =new BufferedReader(new InputStreamReader(in));

                buffer =new StringBuffer();

                String line="";
                while ((line= reader.readLine())!=null){
                    buffer.append(line);
                }
                data = new JSONObject(buffer.toString());

                JSONArray jsonArray=data.getJSONArray("movies");

                bufferData=new StringBuffer();
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject object=jsonArray.getJSONObject(i);
                    movieName =object.getString("movie");
                    releaseDate =object.getInt("year");
                    bufferData.append(movieName + " " + releaseDate + "\n");
                }
                reader.close();
            }catch (MalformedURLException mex) {
                mex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }finally{
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();
            tv.setText(bufferData);
        }
    }
}
