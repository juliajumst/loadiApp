package com.loader.loadi.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.loader.loadi.data.objects.Connection;
import com.loader.loadi.data.objects.Servers;
import com.loader.loadi.data.objects.TempPhone;
import com.loader.loadi.data.objects.Users;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static SQLiteHelper sInstance;

    private static final String DATABASE_NAME = "loadi";
    private static final int DATABASE_VERSION = 55;

    public static SQLiteHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SQLiteHelper(context);
        }

        return sInstance;
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        boolean isAutoIncrement = true;
        database.execSQL(new SQLiteTableCreator<Connection>(Connection.TABLE_NAME,Connection.class, Connection.PRIMARY_KEY,isAutoIncrement).getTable());
        database.execSQL(new SQLiteTableCreator<Users>(Users.TABLE_NAME,Users.class, Users.PRIMARY_KEY,isAutoIncrement).getTable());
        database.execSQL(new SQLiteTableCreator<Servers>(Servers.TABLE_NAME,Servers.class, Servers.PRIMARY_KEY,isAutoIncrement).getTable());
        database.execSQL(new SQLiteTableCreator<TempPhone>(TempPhone.TABLE_NAME,TempPhone.class, TempPhone.PRIMARY_KEY,isAutoIncrement).getTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + Users.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Connection.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Servers.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TempPhone.TABLE_NAME);
        onCreate(db);
    }
    public boolean clear(String table_name) {
        int result =  getWritableDatabase().delete(table_name, null, null);
        close();
        return result != 0;
    }

}
