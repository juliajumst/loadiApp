package com.loader.loadi.manager;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loader.loadi.data.objects.Connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.net.URL;
/**
 * Created by Wendy on 10/22/2016.
 */
public class WebService extends IntentService {

    public static final String TAG = "WebService";
    public static final String URL = "url";
    public static final String METHOD = "method";
    public static final String PARAMS = "params";
    public static final String SOURCE = "source";
    public static final String PROCESS = "process";
    public static final String CONNECT_ACCOUNT = "is_connect_to_account";
    public static final String RESPONSE = "response";
    public static final String STATUS = "status";
    public static final String MESSAGE = "message";
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String GOOGLE_DEVELOPER_KEY = "AIzaSyByEb8fYpeymZu9UlcoiL9tuRwVSwpcwOQ";

    public static final int NETWORK_CONNECTION_FAILED = 100;
    public static final int SERVER_CONNECTION_FAILED = 101;
    public static final int NO_RESPONSE = 102;
    public static final int OK = 1;
    public static final int FAILED = 2;

    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_NOT_MODIFIED = 304;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int HTTP_FORBIDDEN = 403;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_UNPROCESSABLE_ENTITY = 422;
    public static final int HTTP_INTERNAL_SERVER_ERROR = 500;

    boolean isConnectToAccount = false;


    public WebService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Initializing Webservice...");

        Bundle extra = intent.getExtras();
        String url = extra.getString(URL);
        String params = extra.getString(PARAMS);
        String method = extra.getString(METHOD);
        String source = extra.getString(SOURCE);
        int process = extra.getInt(PROCESS);
        isConnectToAccount = extra.getBoolean(CONNECT_ACCOUNT);

        if(isNetworkConnected()){
            Log.d("network", "true");
            if(isServerConnected()){
                Log.d("server","true");
                connect(url, params, method, source, process);
            }else{
                Log.e("BroadCast","Server Connection Failed");
                sendBroadcast("Server Connection Failed", source, SERVER_CONNECTION_FAILED,process);
            }
        }else{
            Log.e("BroadCast","Network Connection Failed");
            sendBroadcast("Network Connection Failed", source, NETWORK_CONNECTION_FAILED,process);
        }

    }

    public void connect(String url, String params, String method, String source, int process){
        Log.i(TAG, "Connecting...");

        String response = sendRequest(url, params, method);
        /*if( method.equalsIgnoreCase(GET) )
            response = sendGetRequest(url, params);
        else if( method.equalsIgnoreCase(POST) )
            response = sendRequest(url, params, method);
        */
        Log.w("Response: ", response);
        Log.w("Process: ", process + "");
        if(TextUtils.equals(response, String.valueOf(WebService.HTTP_BAD_REQUEST)) || TextUtils.equals(response, String.valueOf(WebService.HTTP_NOT_FOUND))){
            sendBroadcast(response, source, HTTP_BAD_REQUEST, process);
        }else{
            if(!response.equalsIgnoreCase("")) {
                try {
                    JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                    int status = jsonObject.get(WebService.STATUS).getAsInt();
                    sendBroadcast(response, source, process, status);
                }catch (Exception e){
//                    uncaughtException("params : "+params+" \n"+e.getMessage() + "\n" + response);
                    sendBroadcast(response, source, HTTP_BAD_REQUEST, process);
                }
            }else{
                sendBroadcast(response, source, process, NO_RESPONSE);
            }
        }
    }
    public void uncaughtException(String result) {
        Long tsLong = System.currentTimeMillis()/1000;
        String timestamp = "timestamp"+tsLong.toString();
        String stacktrace = result;
        String filename = timestamp + ".text";
        writeToFile(stacktrace, filename);
    }
    private void writeToFile(String stacktrace, String filename) {
        try {
            File directory = new File(Environment.getExternalStorageDirectory()+"/kmobile_files/logcat");
            if (!directory.exists()){
                directory.mkdirs();
            }
            String localPath = directory.getAbsolutePath();
            Log.e("localPath",localPath);

            BufferedWriter bos = new BufferedWriter(new FileWriter(
                    localPath + "/" + filename));
            bos.write(stacktrace);
            bos.flush();
            bos.close();
        } catch (Exception e) {
        }
    }
    public void sendBroadcast(String response, String source,int process, int status){
        Log.i(TAG,"Sending Broadcast...");
        Intent mintent = new Intent(source);
        mintent.putExtra(RESPONSE, response);
        mintent.putExtra(PROCESS, process);
        mintent.putExtra(STATUS, status);
        sendBroadcast(mintent);
    }

    public String sendRequest(String requestURL, String requestParams, String requestMethod) {

        String response = "";
        HttpURLConnection conn = null;


        if( requestMethod.equalsIgnoreCase(GET) ) {
            if (requestParams.equalsIgnoreCase("")) {
                requestURL += "?" + getDefaultConnectionAsUrlParameters();
            }else
                requestURL += "?" + getDefaultConnectionAsUrlParameters() +  "&" + requestParams;
        }

        Log.w(TAG, requestURL);

        if( requestMethod.equalsIgnoreCase(POST) ) {
            if (requestParams.equalsIgnoreCase("")){
                requestParams += getDefaultConnectionAsUrlParameters();
            }else {
                requestParams = getDefaultConnectionAsUrlParameters() + "&" + requestParams;
            }
        }

        Log.i(TAG,"Sending " + requestMethod + " Request... To: " + requestURL );
        Log.i(TAG, "Params " + requestParams);

        try {
            java.net.URL url = new URL(requestURL);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);

            if( requestMethod.equalsIgnoreCase(GET) )
                conn.setDoOutput(false);
            if( requestMethod.equalsIgnoreCase(POST) ) {
                conn.setDoOutput(true);
                conn.getOutputStream().write(requestParams.getBytes(Charset.forName("UTF-8")));
            }

            int responseCode = conn.getResponseCode();
            Log.i(TAG,"Method " + conn.getRequestMethod() );
            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
                br.close();
            } else {
                Log.w(TAG, "Response Code: " + responseCode + ":" + conn.getResponseMessage());
                response = HTTP_BAD_REQUEST+"";
            }
            return response;
        }
        catch(IOException ioe){
            Log.w(TAG,"IO Exception: " + ioe.getMessage());
            response = HTTP_BAD_REQUEST+"";
        }
        finally {
            if(conn != null)
                conn.disconnect();
        }

        return response;

    }

    public String getDefaultConnectionAsUrlParameters(){

        LocalStorage ls = new LocalStorage(this);

        if(isConnectToAccount){

            return "DB_HOST" + "=" + ls.getLocalStorage().getString(Connection.HOST, "") + "&" +
                    "DB_USER" + "=" + ls.getLocalStorage().getString(Connection.USERNAME, "") + "&" +
                    "DB_PASS" + "=" + ls.getLocalStorage().getString(Connection.PASSWORD, "") + "&" +
                    "DB_NAME" + "=" + ls.getLocalStorage().getString(Connection.DB_NAME, "") + "&" +
                    "DB_PORT" + "=" + ls.getLocalStorage().getString(Connection.PORT, "");
        }
        else{

            return Connection.HOST + "=" + ls.getLocalStorage().getString(Connection.HOST, "") + "&" +
                    Connection.USERNAME + "=" + ls.getLocalStorage().getString(Connection.USERNAME, "") + "&" +
                    Connection.PASSWORD + "=" + ls.getLocalStorage().getString(Connection.PASSWORD, "") + "&" +
                    Connection.DB_NAME + "=" + ls.getLocalStorage().getString(Connection.DB_NAME, "") + "&" +
                    Connection.PORT + "=" + ls.getLocalStorage().getString(Connection.PORT, "");
        }

    }

    public boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return (cm.getActiveNetworkInfo() != null);
    }

    public boolean isServerConnected(){
        try {
            String host = new WebServiceURL(this).getHost().split(":").length != 1 ? new WebServiceURL(this).getHost().split(":")[0] : new WebServiceURL(this).getHost();
            Log.w("server",host);
            InetAddress ipAddr = InetAddress.getByName(host);
            return !ipAddr.equals("");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
