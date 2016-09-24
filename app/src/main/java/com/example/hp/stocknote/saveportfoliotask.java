package com.example.hp.stocknote;

import android.os.AsyncTask;

/**
 * Created by hp on 9/12/2016.
 */
public class saveportfoliotask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... urls) {
        StringBuilder str=new StringBuilder();
        allfunc all=new allfunc();
        portfolio p=portfolio.getinstance();
        all.saveportfolio(p);
        return str.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        // symbols s=symbols.getinstance();
        //  text.setText(result+" "+s.dict.size());
    }
}