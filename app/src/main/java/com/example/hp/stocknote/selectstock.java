package com.example.hp.stocknote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class selectstock extends AppCompatActivity {
    TextView stocktext;
    EditText lower,upper;
    Button save,cancel;
    stock st;
    CheckBox notify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectstock);
        Bundle extras = getIntent().getExtras();
        st=selectedstock.getinstance().st;
        if (extras != null) {
            int value = extras.getInt("key");
            portfolio p=portfolio.getinstance();
            ArrayList<stock> list=new ArrayList<stock>(p.stocks);
            st=list.get(value);
            //The key argument here must match that used in the other activity
        }
        stocktext=(TextView)findViewById(R.id.stocktext);
        lower=(EditText)findViewById(R.id.lower);
        upper=(EditText)findViewById(R.id.upper);
        save=(Button) findViewById(R.id.save);
        cancel=(Button) findViewById(R.id.cancel);
        notify=(CheckBox)findViewById(R.id.notify);
        stocktext.setText(st.toString());
        lower.setText(""+st.lower+"");
        upper.setText(""+st.upper+"");
        notify.setChecked(st.notify);
        notify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                st.notify=notify.isChecked();
            }
        });
        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                try {
                    float a = Float.parseFloat(lower.getText().toString());
                    float b = Float.parseFloat(upper.getText().toString());
                    st.lower=a;
                    st.upper=b;
                    new saveportfoliotask().execute("");
                }
                catch (Exception e)
                {

                }
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                finish();
            }
        });
    }
}
