package com.loader.loadi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
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
    String owner_number = "09091486089";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_pass_activity);
        initUI();
    }
    @Override
    public void onResume(){
        super.onResume();
        IntentFilter in = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsListener,in);
    }
    boolean isDestroy = false;
    @Override
    public void onDestroy(){
        super.onDestroy();
        isDestroy = true;
        unregisterReceiver(smsListener);
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

    private BroadcastReceiver smsListener = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                if(!isDestroy){
                    Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                    SmsMessage[] msgs = null;
                    String msg_from;
                    if (bundle != null){
                        //---retrieve the SMS message received---
                        try{
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = new SmsMessage[pdus.length];
                            for(int i=0; i<msgs.length; i++){
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                msg_from = msgs[i].getOriginatingAddress();
                                String msgBody = msgs[i].getMessageBody();
                                Log.e("no1",""+ msg_from);
                                Log.e("messages1", "" + msgBody);

                                String[] cont_msg_array = msgBody.split("--");

                                if(msg_from.equals("808")){
                                    String[] msg = msgBody.split(" ");
                                    Log.e("msg[23]",msg[23]);
                                    if(msg.length == 24){
                                        String ref = msg[23].substring(8, msg[23].length());
                                        String amount = msg[7].substring(1, msg[7].length());
                                        yes_or_no_prompt_smart(amount,ref);
                                    }
                                }else if(msg_from.substring(0,1).equals("2")){
                                    String[] msg = msgBody.split(" ");
                                    Log.e("msg[23]", msg[23]);
                                    TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
                                    TempPhone phone = tempPhoneDataSource.getFirst();
                                    if(msg.length == 25 && msg[7].equals(phone.getPhone_number())){
                                        String ref = msg[23].substring(8, msg[23].length());
                                        Log.e("ref", ref);
//                                pass_again_to(ref + "--number--"+phone.getPhone_number());
                                        pass_load_to_globe(msg_from);
                                        tempPhoneDataSource.clear();
                                    }
                                }
//                        else if(cont_msg_array[0].equals("loadi") && cont_msg_array[2].equals("number")){
//                        }
                                abortBroadcast();
                            }
                        }catch(Exception e){
                            Log.e("Exception caught",e.getMessage());
                        }
                    }
                }
            }
        }

        public void pass_load_to_globe(String send_to){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(send_to, null, "YES", null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void yes_or_no_prompt_smart(final String amount,final String ref){
            new AlertDialog.Builder(PasaLoadActivity.this)
                    .setTitle("Warning")
                    .setMessage("Are you sure you want to pass "+amount+" load")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
                            TempPhone phone = tempPhoneDataSource.getFirst();
                            tempPhoneDataSource.clear();
                            pass_again_to(ref + "--sendto--"+phone.getPhone_number()+"--amount--"+amount);
                            pass_load_to_smart(true);
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
                            tempPhoneDataSource.clear();
                            pass_load_to_smart(false);
                            dialogInterface.dismiss();
                        }
                    }).show();
        }
        public void pass_load_to_smart(boolean yes_no){
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("808", null, yes_no ? "YES" : "NO", null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void pass_again_to(String ref){
            //server numbers smart
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("09078840018", null, "loadi--" + ref, null, null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    };
}
