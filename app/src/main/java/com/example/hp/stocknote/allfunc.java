package com.example.hp.stocknote;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Reader;
import java.io.StreamCorruptedException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by hp on 9/8/2016.
 */
public class allfunc {
    public static customlistadapter adapter;
    public void readData(String url) throws MalformedURLException, IOException
    {
        symbols s=symbols.getinstance();
        InputStream is = new URL(url).openStream();
        try {
            System.out.println("akshay");
            String line=null;
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            while ((line = rd.readLine()) != null) {
                //Toast.makeText(,s.dict.size(), Toast.LENGTH_SHORT).show();
              //  System.out.println(line);
                String words[]=line.split(",");
                String key=words[1].toLowerCase();
                if(s.dict.containsKey(key))
                {
                    String symb=s.dict.get(key);
                    s.dict.remove(key);
                    String newkey=key+" - "+symb;
                    s.dict.put(newkey,symb);
                    key=key+" - "+words[0];
                    s.dict.put(key,words[0]);
                }
                else
                    s.dict.put(key,words[0]);
            }
        }
        finally {
            is.close();
        }
        savesymbol(s);
    }
    public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    public void reloadlist()
    {
        if(adapter!=null) {
            adapter.getData().clear();
            portfolio p = portfolio.getinstance();
            adapter.getData().addAll(p.stocks);
            adapter.notifyDataSetChanged();
        }
    }
    public void notification(Context con)
    {
        portfolio p=portfolio.getinstance();
        int count=0;
        for(stock st:p.stocks)
        {
            if(notifygen(st))
            {
              createNotification(con,st.name.toUpperCase(),st.price+" "+st.change,count);
            }
            count++;
        }
    }
    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            jsonText=jsonText.substring(5,jsonText.length()-2);
            //System.out.println(jsonText);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public JSONObject getsharevalue(String sym) throws IOException, JSONException
    {
        JSONObject json = readJsonFromUrl("https://finance.google.com/finance/info?client=ig&q=NSE:"+sym);
        return json;
    }
    public void createNotification(Context con,String title,String sub,int id) {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(con,selectstock.class);
        intent.putExtra("key",id);
        PendingIntent pIntent = PendingIntent.getActivity(con, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake
        Notification noti = new Notification.Builder(con)
                .setContentTitle(title)
                .setContentText(sub).setSmallIcon(android.R.drawable.btn_radio)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager)con.getSystemService(Context.NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;
        noti.defaults |= Notification.DEFAULT_SOUND;
        notificationManager.notify(id , noti);
    }
    public boolean notifygen(stock temp)
    {
        String st=temp.price.replace(",","");
        float price=1.0f;
        try {
            price = Float.parseFloat(st);
        }catch (Exception e)
        {

        }
        return (((price<=temp.lower)||(price>=temp.upper))&&(temp.notify));
    }
    public  void refresh(stock temp) throws IOException, JSONException
    {
        String sym=temp.symbol;
        JSONObject json = getsharevalue(sym);
        //temp.name=json.getString("name");
        temp.price=json.getString("l_cur").replace("&#8377;","");
        temp.change=json.getString("c");

    }
    public void refreshportfolio() throws IOException, JSONException
    {
        portfolio p=portfolio.getinstance();
        for(stock st:p.stocks)
        {
            refresh(st);
        }
    }
    public int minDistance(String word1, String word2) {
        // DP talbe, i is the position in word1, and j is the position in word2
        // vector<vector<int>> distance(word1.length() + 1, vector<int>(word2.length() + 1, 0));
        int m=word1.length()+1,n=word2.length()+1;
        int distance[][]=new int[m][n];
        // when i or j=0 means empty string, the distance is the length of another string
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0)
                    distance[i][j] = j;
                else if (j == 0)
                    distance[i][j] = i;
            }
        }

        // if word1[i] == word2[j], then the distance of i and j is the previous i and j
        // otherwise we either replace, insert or delete a char
        // when insert a char to word1 it means we are trying to match word1 at i-1 to word2 at j
        // when delete a char from word1 it equals to insert a char to word2, which
        // means we are trying to match word1 at i to word2 at j-1
        // when replace a char to word1, then we add one step to previous i and j
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (word1.charAt(i - 1)== word2.charAt(j - 1))
                    distance[i][j] = distance[i - 1][j - 1];
                else
                    distance[i][j] = 1 +Math.min(distance[i - 1][j - 1],Math.min(distance[i - 1][j], distance[i][j - 1]));
            }
        }

        return distance[word1.length()][word2.length()];
    }
    public ArrayList<String> search(String term)
    {
        // String res="";
        symbols s=symbols.getinstance();
        TreeMap<Integer,ArrayList<String>> res=new TreeMap<Integer,ArrayList<String>>();
        int min=Integer.MAX_VALUE;
        for(String key:s.dict.keySet())
        {
            int getmin=minDistance(term,key);
            if(key.contains(term))
            {
                getmin=0;
            }
            if(res.containsKey(getmin))
            {
                res.get(getmin).add(key);
            }
            else
            {
                ArrayList<String> arr=new ArrayList<String>();
                arr.add(key);
                res.put(getmin,arr);
                if(res.size()>5)
                {
                    res.remove(res.lastEntry().getKey());
                }
            }
        }
        ArrayList<String> resultarr=new ArrayList<String>();
        for(int i:res.keySet())
        {
            resultarr.addAll(res.get(i));
        }
        return resultarr;
    }
    public void savesymbol(symbols s) {
        FileOutputStream outStream = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"StockNote");
            dir.mkdirs();
            File f=new File(dir.getAbsolutePath()+File.separator+"symbols.st");
            outStream = new FileOutputStream(f);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);


            objectOutStream.writeObject(s);
            objectOutStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public symbols loadsymbol()
    {
        symbols s=symbols.getinstance();
        FileInputStream inStream = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory()+File.separator+"StockNote"+File.separator+"symbols.st");
            inStream = new FileInputStream(f);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);

            s = ((symbols) objectInStream.readObject());
            //System.out.println(s);
            objectInStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (OptionalDataException e1) {
            e1.printStackTrace();
        } catch (StreamCorruptedException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return s;
    }
    public void saveportfolio(portfolio s) {
        FileOutputStream outStream = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"StockNote");
            dir.mkdirs();
            File f=new File(dir.getAbsolutePath()+File.separator+"portfolio.st");
            outStream = new FileOutputStream(f);
            ObjectOutputStream objectOutStream = new ObjectOutputStream(outStream);


            objectOutStream.writeObject(s);
            objectOutStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    public portfolio loadportfolio()
    {
        portfolio s=portfolio.getinstance();
        FileInputStream inStream = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory()+File.separator+"StockNote"+File.separator+"portfolio.st");
            inStream = new FileInputStream(f);
            ObjectInputStream objectInStream = new ObjectInputStream(inStream);

            s = ((portfolio) objectInStream.readObject());
            //System.out.println(s);
            objectInStream.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (OptionalDataException e1) {
            e1.printStackTrace();
        } catch (StreamCorruptedException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return s;
    }
}
