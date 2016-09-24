package com.example.hp.stocknote;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.TreeSet;



/**
 * Created by hp on 9/12/2016.
 */
public class customlistadapter extends BaseAdapter implements ListAdapter {
    private ArrayList<stock> list;
    private Context context;



    public customlistadapter(TreeSet<stock> list, Context context) {
        this.list=new ArrayList<stock>(list);
        this.context = context;
    }
    public void notifychange()
    {
        notifyDataSetChanged();
    }
    public ArrayList<stock> getData()
    {
        return list;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.listlayout, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.listtext);
        listItemText.setText(list.get(position).toString());

        //Handle buttons and add onClickListeners
        ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.delete);
        listItemText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                selectedstock st=selectedstock.getinstance();
                st.st=list.get(position);
                Intent intent = new Intent(v.getContext(), selectstock.class);
                v.getContext().startActivity(intent);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                portfolio p=portfolio.getinstance();
                p.stocks.remove(list.get(position));
                list.remove(position); //or some other task
                notifyDataSetChanged();
                new saveportfoliotask().execute("");
            }
        });


        return view;
    }
}