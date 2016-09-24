package com.example.hp.stocknote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by hp on 9/14/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message

        allfunc all=new allfunc();
        //all.notification(arg0);
        portfolio p=portfolio.getinstance();
        if(p.stocks.size()==0)
            p.stocks=all.loadportfolio().stocks;
       refreshporttask.con=arg0;
        new refreshporttask().execute("");
    }

}
