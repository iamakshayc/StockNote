package com.example.hp.stocknote;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by hp on 9/8/2016.
 */
public class symbols implements Serializable
{
    public HashMap<String,String> dict;
    private static symbols s=new symbols();
    private symbols()
    {
        dict=new HashMap<String,String>();
    }
    public static symbols getinstance()
    {
        return s;
    }
}
