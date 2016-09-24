package com.example.hp.stocknote;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
//TextView text;
    customlistadapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

      //  allfunc all=new allfunc();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       // text=(TextView)findViewById(R.id.text);
        symbols s=symbols.getinstance();
        portfolio p=portfolio.getinstance();
        allfunc all=new allfunc();

        if(s.dict.size()==0)
        {
            new loadsymboltask().execute("");
        }
        if(p.stocks.size()==0)
        {
            new loadporttask().execute("");
        }
        refreshporttask.con=getApplicationContext();
        new refreshporttask().execute("");
       // text.setText(p.toString());
       // all.createNotification(getApplicationContext(),"trial","trial sub",1);
       // all.createNotification(getApplicationContext(),"akshay","trial",2);
        all.notification(getApplicationContext());
        adapter = new customlistadapter(p.stocks, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.listview);
        lView.setAdapter(adapter);
        all.adapter=adapter;
        all.reloadlist();
       // startService(new Intent(this, MyService.class));
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pintent = PendingIntent
                .getBroadcast(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        // Start service every 20 seconds
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                10* 1000, pintent);
        //adapter.notifyDataSetChanged();

    }

    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(this)
                .setContentTitle("New mail from " + "test@gmail.com")
                .setContentText("Subject").setSmallIcon(android.R.drawable.btn_radio)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }
    private class loadporttask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder str=new StringBuilder();
            portfolio p=portfolio.getinstance();
            allfunc all=new allfunc();
            p.stocks=all.loadportfolio().stocks;
            str.append(p.toString());
            return str.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            symbols s=symbols.getinstance();
          //  text.setText(result);
            allfunc all=new allfunc();
            all.reloadlist();
        }
    }
    private class loadsymboltask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder str=new StringBuilder();
            symbols s=symbols.getinstance();
            allfunc all=new allfunc();
            s.dict=all.loadsymbol().dict;
            if(s.dict.size()==0)
            {
                try {
                    all.readData("https://dl.dropboxusercontent.com/u/94727116/EQUITY_L.csv");
                    str.append("success");
                }
                catch (Exception e)
                {
                    str.append("error");
                }
            }
            return str.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            symbols s=symbols.getinstance();
          //  text.setText(result+" "+s.dict.size());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(
                new ComponentName(this, MainActivity.class)));
        searchView.setIconifiedByDefault(false);

        return true;
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        return false;
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, "Searching by: "+ query, Toast.LENGTH_SHORT).show();

        } else if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            String uri = intent.getDataString();
           // int num=Integer.parseInt(uri.substring(uri.length()-1));
            cursordata c=cursordata.getinstance();
            symbols s=symbols.getinstance();
           // Toast.makeText(this, "Suggestion: "+ c.cursor.getString(1), Toast.LENGTH_SHORT).show();
            portfolio p=portfolio.getinstance();
            stock st=new stock();
            String sug=c.cursor.getString(1);
            st.putstockdetails(sug,s.dict.get(sug),"","","NSE");
            p.add(st);
           // text.setText(p.toString());

            new refreshporttask().execute("");
            new saveportfoliotask().execute("");
           // adapter.notifyDataSetChanged();
            allfunc all=new allfunc();
            all.reloadlist();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                new refreshporttask().execute("");
                new saveportfoliotask().execute("");
                break;
        }
        return true;
    }
}
