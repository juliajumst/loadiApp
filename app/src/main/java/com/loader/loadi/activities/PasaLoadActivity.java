package com.loader.loadi.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loader.loadi.R;
import com.loader.loadi.data.datasource.TempPhoneDataSource;
import com.loader.loadi.data.objects.TempPhone;
import com.loader.loadi.manager.LocalStorage;
import com.loader.loadi.utilities.utilities;

public class PasaLoadActivity extends AppCompatActivity {

    EditText et_phone_no,et_amount;
    Button b_pass_load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_pass_activity);
        initUI();
    }
    public void initUI(){
        et_phone_no = (EditText) findViewById(R.id.et_phone_no);
        et_amount = (EditText) findViewById(R.id.et_amount);
        b_pass_load = (Button) findViewById(R.id.b_pass_load);

        b_pass_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = getString(et_phone_no);
                String amount = getString(et_amount);
                if (phone.length() == 11) {
                    int network = utilities.getNetwork(phone);
                    if (network != 0) {
                        switch (network) {
                            case utilities.SMART:
                                sendSmart(phone, amount);
                                break;
                            case utilities.GLOBE:
                                sendGlobe(phone, amount);
                                break;
                            case utilities.SUN:

                                break;
                        }
                    } else {
                        promptErrorNetwork();
                    }
                } else {
                    promptErrorLength();
                }
            }
        });
    }
    public void sendGlobe(String phone,String amount){
        TempPhone temp = new TempPhone();
        temp.setPhone_number(phone);

        TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
        tempPhoneDataSource.clear();
        tempPhoneDataSource.insert(temp);

        pass_load_to_globe(amount);
    }
    public void pass_load_to_globe(String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("209757115320", null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again later! "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void sendSmart(String phone,String amount){
        TempPhone temp = new TempPhone();
        temp.setPhone_number(phone);

        TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
        tempPhoneDataSource.clear();
        tempPhoneDataSource.insert(temp);

        pass_load_to_smart("09078840018 " + amount);
    }
    public void pass_load_to_smart(String message){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("808", null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again later! "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    public void promptErrorLength(){
        new AlertDialog.Builder(PasaLoadActivity.this).setTitle("Error").setMessage("should be only 11 numbers").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    public void promptErrorNetwork(){
        new AlertDialog.Builder(PasaLoadActivity.this).setTitle("Error").setMessage("Invalid Prefix").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
    public String getString(EditText et){
        return et.getText().toString().trim();
    }
}
