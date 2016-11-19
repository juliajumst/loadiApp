package com.loader.loadi.data.datasource;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.loader.loadi.data.objects.Data;
import com.loader.loadi.manager.LoadiApp;
import com.loader.loadi.manager.SQLiteHelper;
import com.loader.loadi.manager.SQLiteTableCreator;
import com.loader.loadi.manager.WebService;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by keneth.p on 7/14/2015.
 * extend this class if
 */
public abstract class DataSource<T extends Data> {

    private String table_name = "";

    private SQLiteHelper sqLiteHelper;
    Context context;

    public DataSource(String table_name){
        sqLiteHelper = LoadiApp.getInstance().mSQLite;
        this.table_name = table_name;
    }
    public DataSource(String table_name,Context context){
        sqLiteHelper = LoadiApp.getInstance().mSQLite;
        this.table_name = table_name;
        this.context = context;
    }

    public String getTableName(){
        return table_name;
    }

    protected SQLiteDatabase getWritableDatabase(){
        return sqLiteHelper.getWritableDatabase();
    }

    protected SQLiteDatabase getReadableDatabase(){
        return sqLiteHelper.getReadableDatabase();
    }

    public boolean insert(T entity){
        if (entity == null) {
            return false;
        }

        long result = getWritableDatabase().insert(getTableName(), null, getContentValues(entity));

        return result != -1;
    }

    public boolean insert(ArrayList<T> entity){
        if (entity == null) {
            return false;
        }

        long result = -1;

        for(T e : entity)
        result = getWritableDatabase().insert(getTableName(), null, getContentValues(e));

        return result != -1;
    }

    public ArrayList<T> read(){

        String query = "SELECT  * FROM " + table_name;
        Cursor cursor = getReadableDatabase().rawQuery(query, null);

        return getList(cursor);
    }
    public void showTables(){
        Cursor c = getReadableDatabase().rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.e("tables" , ""+"Table Name=> "+c.getString(0));
                c.moveToNext();
            }
        }
    }

    public T getFirst(){
        String query = "SELECT  * FROM " + table_name;
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        ArrayList<T> list = getList(cursor);
        if( list.isEmpty() )
            return null;
        else
            return list.get(0);
    }
    public boolean clear() {
        int result =  getWritableDatabase().delete(table_name, null, null);
        return result != 0;
    }

    public abstract boolean delete(T entity);
    public abstract boolean update(T entity);
    public abstract T find (String s);
    //public abstract ContentValues getContentValues(T entity);
    //public abstract ArrayList<T> parseResponse(String response);
    public abstract ArrayList<T> getList(Cursor cursor);

    public ArrayList<T> parseResponse(String response,Class<T> clazz) {

        if(!response.equalsIgnoreCase("")) {
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            int status = jsonObject.get(WebService.STATUS).getAsInt();

            if (status == WebService.OK) {

                //System.out.println("Table :: " + table_name);

                JsonArray jsonArray = jsonObject.getAsJsonArray(table_name).getAsJsonArray();

                ArrayList<T> list = new ArrayList<>();

                for (JsonElement element : jsonArray)
                    list.add(new Gson().fromJson(element, clazz));

                return list;
            }
        }

        return new ArrayList<T>();
    }

    public T getInstanceOfT(Class<T> aClass)
    {
        try {
            return aClass.newInstance();
        } catch (InstantiationException e) {
            System.out.println("DataSource Exception: IE");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("DataSource Exception: IAE");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<T> parseCursorToArrayList(Cursor cursor, Class<T> clazz) {

        ArrayList<T> elements = new ArrayList();

        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                T element = getInstanceOfT(clazz);

                //System.out.println("Element: " + element.getClass());

                for (int x = 0; x < cursor.getColumnCount(); x++){

                    String field_name = cursor.getColumnName(x);

                    try {

                        Field field = element.getClass().getDeclaredField(field_name);
                        field.setAccessible(true);
                        if (field.getType() == int.class)
                            field.set(element, cursor.getInt(cursor.getColumnIndex(field_name)));
                        else if (field.getType() == String.class)
                            field.set(element, cursor.getString(cursor.getColumnIndex(field_name)));
                        else if (field.getType() == Double.class)
                            field.set(element, cursor.getDouble(cursor.getColumnIndex(field_name)));

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

                elements.add(element);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return elements;
    }

    public T parseCursorToObject(Cursor cursor, Class<T> clazz){

        T element = getInstanceOfT(clazz);

        if (cursor != null && cursor.moveToFirst()) {

                for (int x = 0; x < cursor.getColumnCount(); x++){

                    String field_name = cursor.getColumnName(x);

                    try {
                        Field field = element.getClass().getDeclaredField(field_name);
                        field.setAccessible(true);
                        if (field.getType() == int.class)
                            field.set(element, cursor.getInt(cursor.getColumnIndex(field_name)));
                        else if (field.getType() == String.class)
                            field.set(element, cursor.getString(cursor.getColumnIndex(field_name)));
                        else if (field.getType() == Double.class)
                            field.set(element, cursor.getDouble(cursor.getColumnIndex(field_name)));

                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

            cursor.close();
        }

        return element;
    }

    public ContentValues getContentValues(T entity){
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        SQLiteTableCreator sqltc = new SQLiteTableCreator();

        Field[] fields = entity.getClass().getDeclaredFields();

        for(Field field : fields){

            field.setAccessible(true);

            if( (java.lang.reflect.Modifier.isPublic(field.getModifiers()) || java.lang.reflect.Modifier.isPrivate(field.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) ){

                String column_name = field.getName();

                if(sqltc.isReservedWord(column_name)){
                    column_name = "`" + column_name + "`";
                }

                try {
                    if( entity.isAutoIncrement() && column_name.equalsIgnoreCase(entity.getPrimaryKey()) ){
                        //prevent id from being included in adding
                    }
                    else{
                        if (field.getType() == int.class)
                            values.put(column_name, field.getInt(entity));
                        if (field.getType() == String.class)
                            values.put(column_name, (String) field.get(entity));
                        if (field.getType() == double.class)
                            values.put(column_name, field.getDouble(entity));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }

            }

        }

        return values;
    }

}
