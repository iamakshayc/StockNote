package com.example.hp.stocknote;

import android.database.MatrixCursor;

/**
 * Created by hp on 9/8/2016.
 */
public class cursordata {
    private static cursordata c=new cursordata();
    MatrixCursor cursor;
    private cursordata()
    {
        cursor=null;
    }
    public static cursordata getinstance()
    {
        return c;
    }
}
