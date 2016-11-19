package com.loader.loadi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loader.loadi.R;
import com.loader.loadi.data.datasource.ServersDataSource;
import com.loader.loadi.data.datasource.UsersDataSource;
import com.loader.loadi.data.objects.Connection;
import com.loader.loadi.data.objects.Servers;
import com.loader.loadi.data.objects.Users;
import com.loader.loadi.manager.LocalStorage;
import com.loader.loadi.manager.WebService;
import com.loader.loadi.manager.WebServiceURL;

public class LoginActivity extends AppCompatActivity {
    final private static String TAG = LoginActivity.class.getSimpleName();

    final private static int LOGIN = 1;


    EditText tv_uname,tv_pword;
    Button b_login,b_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LocalStorage ls = new LocalStorage(this);
//        if(!ls.getIsFirstLoaded()){
            ls.saveStringOnLocalStorage(Connection.HOST, "192.168.43.77");
            ls.saveStringOnLocalStorage(Connection.USERNAME, "root");
            ls.saveStringOnLocalStorage(Connection.PASSWORD, "");
            ls.saveStringOnLocalStorage(Connection.DB_NAME, "loadi");
            ls.saveStringOnLocalStorage(Connection.PORT, "3306");

            ls.saveStringOnLocalStorage(LocalStorage.WEBSERVICE, "192.168.43.77/loadi");
            ls.saveStringOnLocalStorage(LocalStorage.FIRST_LOADED, true);
//        }

        initUI();
        listener();
    }
    public void initUI(){
        tv_uname = (EditText) findViewById(R.id.et_uname);
        tv_pword = (EditText) findViewById(R.id.et_pword);

        b_login = (Button) findViewById(R.id.b_login);
        b_register = (Button) findViewById(R.id.b_register);
    }
    public String getString(EditText text){
        return text.getText().toString().trim();
    }
    public void listener(){
        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(in);
            }
        });
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }

    public void register(){
        String uname = getString(tv_uname);
        String pword = getString(tv_pword);
        get_user_by_uname_pword(uname, pword);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(TAG);
        registerReceiver(bcReceived, intentFilter);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(bcReceived);
        super.onDestroy();
    }

    private BroadcastReceiver bcReceived = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent intent) {
            Bundle bundle = intent.getExtras();

            String response = bundle.getString(WebService.RESPONSE);
            int process = bundle.getInt(WebService.PROCESS);
            int status = bundle.getInt(WebService.STATUS);

            switch (process) {
                case WebService.NETWORK_CONNECTION_FAILED:
                case WebService.SERVER_CONNECTION_FAILED:
                case WebService.HTTP_BAD_REQUEST:
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    break;
                case LOGIN:
                    setLoginCredentials(response, status);
                    break;
            }
        }
    };
    public void setLoginCredentials(String response, int status){
        if( status == WebService.OK ){
            UsersDataSource uds = new UsersDataSource();
            uds.clear();
            uds.insert(uds.parseResponse(response, Users.class));

            ServersDataSource sds = new ServersDataSource();
            sds.clear();
            sds.insert(sds.parseResponse(response, Servers.class));
            new AlertDialog.Builder(this).setTitle("Success").setMessage("Login Success").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).show();
        }else{
            JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
            String message = jsonObject.get(WebService.MESSAGE).getAsString();
            new AlertDialog.Builder(this).setTitle("Error").setMessage(message).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }
    public void get_user_by_uname_pword(String uname,String pword){
        WebServiceURL webServiceURL = new WebServiceURL(this);
        connectPOST(webServiceURL.get_user_by_uname_pword(), "uname=" + uname+"&pword=" + pword, LOGIN);
    }
    public void connectPOST(String url, String params, int process) {
        Intent i = new Intent(this, WebService.class);
        i.putExtra(WebService.URL, url);
        i.putExtra(WebService.PARAMS, params);
        i.putExtra(WebService.METHOD, WebService.POST);
        i.putExtra(WebService.SOURCE, TAG);
        i.putExtra(WebService.PROCESS, process);
        startService(i);
    }
}
