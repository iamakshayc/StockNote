package com.example.hp.stocknote;

/**
 * Created by hp on 9/12/2016.
 */
public class selectedstock {
    private static selectedstock s=new selectedstock();
    stock st;
    private selectedstock()
    {
        st=new stock();
    }
    public static selectedstock getinstance()
    {
        return s;
    }
}
