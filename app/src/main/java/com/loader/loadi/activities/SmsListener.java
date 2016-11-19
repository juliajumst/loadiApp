package com.loader.loadi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.loader.loadi.data.datasource.TempPhoneDataSource;
import com.loader.loadi.data.objects.TempPhone;
import com.loader.loadi.manager.LocalStorage;

/**
 * Created by Wendy on 10/9/2016.
 */
public class SmsListener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
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
                        Log.e("no",""+ msg_from);
                        Log.e("messages", "" + msgBody);

                        String[] cont_msg_array = msgBody.split("--");

//                        if(msg_from.equals("808")){
//                            String[] msg = msgBody.split(" ");
//                            Log.e("msg[23]",msg[23]);
//
//                            TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
//                            TempPhone phone = tempPhoneDataSource.getFirst();
//                            if(msg.length == 24){
//                                String ref = msg[23].substring(8, msg[23].length());
//                                String amount = msg[7].substring(1, msg[7].length());
//                                Log.e("ref", ref);
//                                pass_again_to(ref + "--sendto--"+phone.getPhone_number()+"--amount--"+amount);
//                                pass_load_to_smart();
//                                tempPhoneDataSource.clear();
//                            }
//                        }else
//                        if(msg_from.substring(0,1).equals("2")){
//                            String[] msg = msgBody.split(" ");
//                            Log.e("msg[23]", msg[23]);
//                            TempPhoneDataSource tempPhoneDataSource = new TempPhoneDataSource();
//                            TempPhone phone = tempPhoneDataSource.getFirst();
//                            if(msg.length == 25 && msg[7].equals(phone.getPhone_number())){
//                                String ref = msg[23].substring(8, msg[23].length());
//                                Log.e("ref", ref);
////                                pass_again_to(ref + "--number--"+phone.getPhone_number());
//                                pass_load_to_globe(msg_from);
//                                tempPhoneDataSource.clear();
//                            }
//                        }
//                        else if(cont_msg_array[0].equals("loadi") && cont_msg_array[2].equals("number")){
//                        }
//                        abortBroadcast();
                    }
                }catch(Exception e){
                            Log.e("Exception caught",e.getMessage());
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
    public void pass_load_to_smart(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("808", null, "YES", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pass_again_to(String ref){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("09078840018", null, "loadi--" + ref, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}