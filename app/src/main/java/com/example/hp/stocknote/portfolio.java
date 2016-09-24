package com.example.hp.stocknote;

import java.io.Serializable;
import java.util.TreeSet;

/**
 * Created by hp on 9/11/2016.
 */
public class portfolio implements Serializable
{
    public TreeSet<stock> stocks;
    public static portfolio p=new portfolio();
    private portfolio()
    {
        stocks=new TreeSet<stock>();
    }
    public static portfolio getinstance()
    {
        return p;
    }
    public void add(stock s)
    {
        stocks.add(s);
    }
    public void remove(stock s)
    {
        stocks.remove(s);
    }
    public String toString()
    {
        StringBuilder st=new StringBuilder();
        for(stock stu:stocks)
        {
            st.append(stu+"\n");
        }
        return st.toString();
    }
}
