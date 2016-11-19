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
import com.loader.loadi.data.datasource.UsersDataSource;
import com.loader.loadi.data.objects.Users;
import com.loader.loadi.manager.WebService;
import com.loader.loadi.manager.WebServiceURL;

public class RegistrationActivity extends AppCompatActivity{

    final static String TAG = RegistrationActivity.class.getSimpleName();

    final static int CREATE_USER = 1;

    EditText tv_fname, tv_lname, tv_age, tv_address, tv_phone, tv_email, tv_uname, tv_pword;
    Button b_cancel, b_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_layout);

        initUI();
        listener();
    }

    public void initUI() {
        tv_fname = (EditText) findViewById(R.id.tv_fname);
        tv_lname = (EditText) findViewById(R.id.tv_lname);
        tv_age = (EditText) findViewById(R.id.tv_age);
        tv_address = (EditText) findViewById(R.id.tv_address);
        tv_phone = (EditText) findViewById(R.id.tv_phone);
        tv_email = (EditText) findViewById(R.id.tv_email);
        tv_uname = (EditText) findViewById(R.id.tv_uname);
        tv_pword = (EditText) findViewById(R.id.tv_pword);

        b_cancel = (Button) findViewById(R.id.b_cancel);
        b_register = (Button) findViewById(R.id.b_register);
    }

    public String getString(EditText text) {
        return text.getText().toString().trim();
    }

    public void listener() {
        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void register() {
        String fname = getString(tv_fname);
        String lname = getString(tv_lname);
        String age = getString(tv_age);
        String address = getString(tv_address);
        String email = getString(tv_email);
        String phone = getString(tv_phone);
        String uname = getString(tv_uname);
        String pword = getString(tv_pword);

        WebServiceURL webServiceURL = new WebServiceURL(this);

        connectPOST(webServiceURL.create_user(), "display_name=" + fname+"&email=" + email+"&name=" + (fname+ " " + lname)+"&phone_number=" + phone+"&age=" + age+"&address=" + address+"&uname=" + uname+"&pword=" + pword, CREATE_USER);
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
                    Toast.makeText(RegistrationActivity.this, response, Toast.LENGTH_SHORT).show();
                    break;
                case CREATE_USER:
                    setUser(response, status);
                    break;
            }
        }

    };

    public void setUser(String response, int status){
        if( status == WebService.OK ){
            UsersDataSource uds = new UsersDataSource();
            uds.clear();
            uds.insert(uds.parseResponse(response, Users.class));
            new AlertDialog.Builder(this).setTitle("Success").setMessage("Successfully Created Account Please Log in").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
}
