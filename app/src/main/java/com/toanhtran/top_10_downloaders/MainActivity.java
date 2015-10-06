package com.toanhtran.top_10_downloaders;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView xmlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xmlTextView = (TextView) findViewById(R.id.xmlTextView);
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class DownloadData extends AsyncTask<String, Void, String> {

        private String mFilesContents;

        @Override
        protected String doInBackground(String... params) {
            mFilesContents = downloadXMLFiles(params[0]);//pass the first element in the array
            if (mFilesContents == null){
                Log.d("DownloadData", "Error downloading");
            }
            return mFilesContents;
        }

        /**
         * Method onPostExecute post out of of URL to screen
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("DownloadData", "Result was: " + result);
            xmlTextView.setText(mFilesContents);//shows xml content to screen
        }

        private String downloadXMLFiles(String urlPath) {
            StringBuilder tempBuffer = new StringBuilder();
            try {
                URL url = new URL(urlPath);//if URL is vaild will open Top 10 RSS feed
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();//opens connection
                int response = connection.getResponseCode();
                Log.d("DownloadData", "The response code was " + response);
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);//Start processing data

                int charRead;//var to read char
                char[] inputBuffer = new char[500];//reads 500 bytes
                while (true){
                    charRead = isr.read(inputBuffer);//tries to read input buffer
                    if(charRead <= 0){
                        break;//Loops to read 500 char when it read 0 char then it will exit
                    }
                    tempBuffer.append(String.copyValueOf(inputBuffer, 0, charRead));//reads up to 0 to charRead
                }
                return tempBuffer.toString();//convert buffer to string
            } catch(IOException e){
                Log.d("DownloadData", "IO Exception reading data: " + e.getMessage());
                e.printStackTrace();
            } catch(SecurityException e) {
                Log.d("DownloadData", "Security exception. Need permissions? " + e.getMessage());
            }//if there is a error log and try to debug error

            return null;//return If there is error downloading
        }


    }






















}
