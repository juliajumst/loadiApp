package com.loader.loadi.data.objects;

/**
 * Created by Wendy on 10/22/2016.
 */
public abstract class Data {

    public String tableName;

    //if primary id is auto incremented in SQLite
    public boolean isAutoIncrement = false;
    public String primaryKey = "";

    public Data(){
        this.tableName = "";
    }

    public Data(String tableName){
        this.tableName = tableName;
    }

    public Data(String tableName, String primaryKey, boolean isAutoIncrement){
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.isAutoIncrement = isAutoIncrement;
    }

    public String getTableName(){
        return tableName;
    }

    public boolean isAutoIncrement(){
        return isAutoIncrement;
    }

    public String getPrimaryKey(){
        return primaryKey;
    }

}
