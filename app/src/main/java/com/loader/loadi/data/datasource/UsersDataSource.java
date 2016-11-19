package com.loader.loadi.data.datasource;

import android.database.Cursor;

import com.loader.loadi.data.objects.Users;

import java.util.ArrayList;

/**
 * Created by Owner on 5/19/2016.
 */
public class UsersDataSource extends DataSource<Users> {

    public static final String TAG = "UsersDataSource";

    public UsersDataSource() {
        super(Users.TABLE_NAME);
    }

    @Override
    public boolean delete(Users entity) {
        return false;
    }

    @Override
    public boolean update(Users entity) {
        return false;
    }

    @Override
    public Users find(String s) {
        return new Users();
    }

    @Override
    public ArrayList<Users> getList(Cursor cursor) {
        return parseCursorToArrayList(cursor, Users.class);
    }
}
