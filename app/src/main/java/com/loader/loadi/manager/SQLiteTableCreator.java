package com.loader.loadi.manager;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Wendy on 10/22/2016.
 */
public class SQLiteTableCreator<T> {

    /**
     This class creates a table from a specific object class.
     Object class variables which are declared int,String,double are used for columns other are not accepted (You can modify the createTable function to add other types)
     Table columns are derived from variable names.
     **/

    private String table;
    private String table_name;
    private Class<T> clazz;
    private String primary_key;
    boolean isAutoIncrement;

    private ArrayList<String> notNull_columns = new ArrayList<>();

    private String[] reservedWords = {"ABORT", "ACTION", "ADD", "AFTER", "ALL", "ALTER", "ANALYZE", "AND", "AS", "ASC", "ATTACH", "AUTOINCREMENT",
            "BEFORE", "BEGIN", "BETWEEN", "BY", "CASCADE", "CASE", "CAST", "CHECK", "COLLATE", "COLUMN", "COMMIT", "CONFLICT", "CONSTRAINT",
            "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "DATABASE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE",
            "DESC", "DETACH", "DISTINCT", "DROP", "EACH", "ELSE", "END", "ESCAPE", "EXCEPT", "EXCLUSIVE", "EXISTS", "EXPLAIN", "FAIL", "FOR",
            "FOREIGN", "FROM", "FULL", "GLOB", "GROUP", "HAVING", "IF", "IGNORE", "IMMEDIATE", "IN", "INDEX", "INDEXED", "INITIALLY", "INNER",
            "INSERT", "INSTEAD", "INTERSECT", "INTO", "IS", "ISNULL", "JOIN", "KEY", "LEFT", "LIKE", "LIMIT", "MATCH", "NATURAL", "NO", "NOT",
            "NOTNULL", "NULL", "OF", "OFFSET", "ON", "OR", "ORDER", "OUTER", "PLAN", "PRAGMA", "PRIMARY", "QUERY", "RAISE", "RECURSIVE", "REFERENCES",
            "REGEXP", "REINDEX", "RELEASE", "RENAME", "REPLACE", "RESTRICT", "RIGHT", "ROLLBACK", "ROW", "SAVEPOINT", "SELECT", "SET", "TABLE",
            "TEMP", "TEMPORARY", "THEN", "TO", "TRANSACTION", "TRIGGER", "UNION", "UNIQUE", "UPDATE", "USING", "VACUUM", "VALUES", "VIEW", "VIRTUAL",
            "WHEN", "WHERE", "WITH", "WITHOUT"};

    public SQLiteTableCreator(){

    }


    public SQLiteTableCreator(String table_name, Class<T> clazz, String primary_key, boolean isAutoIncrement){
        this.table_name = table_name;
        this.clazz = clazz;
        this.primary_key = primary_key;
        this.isAutoIncrement = isAutoIncrement;
        createTable();
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTable(){
        return table;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     *
     * @param column_name
     * Set a specific column as not null
     */
    public void setAsNotNull(String column_name){
        notNull_columns.add(column_name);
    }

    /**
     *
     * @param column_name Column name to be search in the list of column names which are to be set not null
     * @return
     */

    //check if column name is the the list of isNotNull
    public boolean isNotNull(String column_name){
        for(String s : notNull_columns)
            if(s.equalsIgnoreCase(column_name))
                return true;
        return false;
    }

    //check if column_name is reserved word
    public boolean isReservedWord(String column_name){
        for(String s : reservedWords)
            if(s.equalsIgnoreCase(column_name))
                return true;
        return false;
    }

    /**
     * Creates a create table statement for a specified class.
     * only uses publicly or privately declared variables as table columns ( static,final declarations are not included)
     * all columns will be set with no modifiers as default(ex. auto increment, not null)
     */

    /*public static final String CREATE_TABLE = "create table " + TABLE_NAME
            + "(" + ID + " integer primary key not null, "
            + DATE_TIME_OPENED + " text,"
            + DATE_TIME_CLOSED + " text,"
            + SESSION_NOTE_ID + " integer);";*/

    public void createTable(){

        table = "create table if not exists " + table_name + "(";

        if(!primary_key.equalsIgnoreCase("")){
            if (isAutoIncrement)
                table += primary_key + " integer primary key autoincrement not null";
            else
                table += primary_key + " integer primary key not null";
        }

        T object = getInstanceOfT(clazz);

        Field[] fields = object.getClass().getDeclaredFields();

        for( int x = 0 ; x < fields.length ; x++ ){

            Field field = fields[x];

            if( (java.lang.reflect.Modifier.isPublic(field.getModifiers()) || java.lang.reflect.Modifier.isPrivate(field.getModifiers())) && (!java.lang.reflect.Modifier.isStatic(field.getModifiers()) && !java.lang.reflect.Modifier.isFinal(field.getModifiers())) ){
                if(!primary_key.equalsIgnoreCase(field.getName())) {

                    String column_name = field.getName();

                    if(column_name != ""){
                        if(isReservedWord(column_name)){
                            column_name = "`" + column_name + "`";
                        }
                        table += ",";

                        if (field.getType() == int.class)
                            table += column_name + " integer";
                        if (field.getType() == String.class)
                            table += column_name + " text";
                        if (field.getType() == double.class)
                            table += column_name + " double";
                        if (field.getType() == boolean.class)
                            table += column_name + " boolean";

                        if (!notNull_columns.isEmpty())
                            if (isNotNull(column_name)) {
                                table += " not null";
                            }
                    }
                    //table += ",";
                }
            }


        }

        table += ")";

    }

    public T getInstanceOfT(Class<T> aClass)
    {
        try {
            return aClass.newInstance();
        } catch (InstantiationException e) {
            System.out.println("SQLiteTableCreator Exception: IE");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("SQLiteTableCreator Exception: IAE");
            e.printStackTrace();
        }
        return null;
    }

}
