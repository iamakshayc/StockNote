package com.example.hp.stocknote;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

/**
 * Created by hp on 9/13/2016.
 */
public class refreshporttask extends AsyncTask<String,Void,String> {
    public static Context con=null;
    public boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }

        return false;
    }
        @Override
    protected String doInBackground(String... urls) {
        StringBuilder str=new StringBuilder();
        allfunc all=new allfunc();
        try {
        all.refreshportfolio();
        }
        catch (Exception e)
        {
        str.append("network error");

        }
        return str.toString();
        }

    @Override
    protected void onPostExecute(String result) {
        // symbols s=symbols.getinstance();
        //  text.setText(result+" "+s.dict.size());
        portfolio p=portfolio.getinstance();
        //text.setText(result+" "+p.toString());
        allfunc all=new allfunc();
        all.notification(con);
        if(isRunning(con))
            all.reloadlist();
        //Toast.makeText(con,"sadsd",Toast.LENGTH_SHORT).show();
        }
}

