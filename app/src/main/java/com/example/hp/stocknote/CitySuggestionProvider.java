package com.example.hp.stocknote;

import android.app.DownloadManager;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 9/8/2016.
 */
public class CitySuggestionProvider extends ContentProvider {

    ArrayList<String> cities;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {


        cursordata c=cursordata.getinstance();
        c.cursor = new MatrixCursor(
                new String[] {
                        BaseColumns._ID,
                        SearchManager.SUGGEST_COLUMN_TEXT_1,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
                }
        );
        String query = uri.getLastPathSegment().toLowerCase();
        allfunc all=new allfunc();

        cities=all.search(query);
       // cities.add("akshay");
       // cities.add("amal");
       // cities.add("akhil");
        if (cities != null) {

            int limit = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            int lenght = cities.size();
            for (int i = 0; i < lenght && c.cursor.getCount() < limit; i++) {
                String city = cities.get(i);
                   // if(city.contains(query.toLowerCase()))
                    c.cursor.addRow(new Object[]{ i, city, i });

            }
        }

        return c.cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    //

}