package com.loader.loadi.data.datasource;

import android.database.Cursor;

import com.loader.loadi.data.objects.TempPhone;
import com.loader.loadi.data.objects.Users;

import java.util.ArrayList;

/**
 * Created by Owner on 5/19/2016.
 */
public class TempPhoneDataSource extends DataSource<TempPhone> {

    public static final String TAG = "TempPhoneDataSource";

    public TempPhoneDataSource() {
        super(TempPhone.TABLE_NAME);
    }

    @Override
    public boolean delete(TempPhone entity) {
        return false;
    }

    @Override
    public boolean update(TempPhone entity) {
        return false;
    }

    @Override
    public TempPhone find(String s) {
        return new TempPhone();
    }

    @Override
    public ArrayList<TempPhone> getList(Cursor cursor) {
        return parseCursorToArrayList(cursor, TempPhone.class);
    }
}
