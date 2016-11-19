package com.loader.loadi.data.datasource;

import android.database.Cursor;

import com.loader.loadi.data.objects.Servers;

import java.util.ArrayList;

/**
 * Created by Owner on 5/19/2016.
 */
public class ServersDataSource extends DataSource<Servers> {

    public static final String TAG = "ServersDataSource";

    public ServersDataSource() {
        super(Servers.TABLE_NAME);
    }

    @Override
    public boolean delete(Servers entity) {
        return false;
    }

    @Override
    public boolean update(Servers entity) {
        return false;
    }

    @Override
    public Servers find(String s) {
        return new Servers();
    }

    @Override
    public ArrayList<Servers> getList(Cursor cursor) {
        return parseCursorToArrayList(cursor, Servers.class);
    }
}
