package com.example.hp.stocknote;

import java.io.Serializable;

/**
 * Created by hp on 9/11/2016.
 */
public class stock implements Comparable<stock>,Serializable
{
    public String name,symbol,price,change,exchange;
    float lower,upper;
    boolean notify;
    stock()
    {
        name="";
        symbol="";
        price="";
        change="";
        exchange="";
        lower=0.0f;
        upper=Float.MAX_VALUE;
        notify=false;
    }
    public void putstockdetails(String a,String b,String c,String d,String e)
    {
        name=a;
        symbol=b;
        price=c;
        change=d;
        exchange=e;
    }

    public int compareTo(stock o) {
        // TODO Auto-generated method stub
        return this.name.compareTo(o.name);
    }
    public String toString()
    {
        StringBuilder st=new StringBuilder();
        st.append(name.toUpperCase()+"\n"+symbol+"\n"+price+"\n"+change+"\n");
        return st.toString();
    }
}
