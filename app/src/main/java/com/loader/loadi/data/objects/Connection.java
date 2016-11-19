package com.loader.loadi.data.objects;

/**
 * Created by Wendy on 10/22/2016.
 */
public class Connection extends Data{

    public static final String ACCOUNT_TYPE_SERVER = "SERVER";
    public static final String ACCOUNT_TYPE_WAITER = "WAITER";

    public static final String TAG = "Connection";
    public static final String TABLE_NAME = "connection";
    public static final String PRIMARY_KEY = "id";
    public static final String HOST = "host";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String DB_NAME = "db_name";
    public static final String PORT = "port";
    public static final String ACCOUNT_TYPE = "account_type";
    public static final String WEBSEVICE = "webservice";

    private int id;
    private String host;
    private String username;
    private String password;
    private String db_name;
    private String port;
    private String webservice;

    public Connection() {
        super(TABLE_NAME);
    }

    public Connection(int id, String host, String username, String password, String db_name, String port,String webservice){
        super(TABLE_NAME,PRIMARY_KEY,true);
        this.id = id;
        this.host = host;
        this.username = username;
        this.password = password;
        this.db_name = db_name;
        this.port = port;
        this.webservice = webservice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDb_Name() {
        return db_name;
    }

    public void setDb_Name(String db_name) {
        this.db_name = db_name;
    }

    public String getPort() {return port;}

    public void setPort(String port) {this.port = port;}

    public String getWebservice() {
        return webservice;
    }

    public void setWebservice(String webservice) {
        this.webservice = webservice;
    }
}
