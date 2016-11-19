package com.loader.loadi.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Wendy on 10/9/2016.
 */
public class ServerSmsListener extends BroadcastReceiver {

    ArrayList<ReceiveAPI> receiveAPIs = new ArrayList<>();
    ArrayList<ReceiveAPI> receiveLoadAPIs = new ArrayList<>();


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
                        if(cont_msg_array[0].equals("loadi") && cont_msg_array[2].equals("sendto")&& cont_msg_array[4].equals("amount")){
                            ReceiveAPI api = new ReceiveAPI();
                            api.setRef(cont_msg_array[1]);
                            api.setSendto(cont_msg_array[3]);
                            api.setAmount(cont_msg_array[5]);
                            receiveAPIs.add(api);

                            checkifRequested();
                        }else if(msg_from.equals("808")){
                            String[] msg = msgBody.split(" ");
                            Log.e("ref",msg[11].substring(5,msg[11].length()));
                            if(msg.length == 12){
                                ReceiveAPI api = new ReceiveAPI();
                                api.setRef(msg[11].substring(5,msg[11].length()));
                                api.setSendto("");
                                api.setAmount(msg[3].substring(1,msg.length));
                                receiveLoadAPIs.add(api);

                                checkifRequested();
                            }else if(msg.length == 24 && msg[6].equals("Pasaload")){
                                yes();
                            }
                        }

                        abortBroadcast();
                    }
                }catch(Exception e){
                            Log.e("Exception caught",e.getMessage());
                }
            }
        }
    }

    public void checkifRequested(){
        for (int i = 0; i < receiveAPIs.size(); i++) {
            boolean ifbreak = false;
            for (int j = 0; j < receiveLoadAPIs.size(); j++) {
                if(receiveAPIs.get(i).getRef().equals(receiveLoadAPIs.get(j).getRef())){
                    pass_load_to_smart(receiveAPIs.get(i).getSendto(),receiveAPIs.get(i).getAmount());
                    receiveAPIs.remove(i);
                    receiveLoadAPIs.remove(j);
                    ifbreak = true;
                    break;
                }
            }
            if(ifbreak){
                break;
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
    public void yes(){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("808", null, "YES", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void pass_load_to_smart(String sendto,String amount){
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("808", null, sendto + " "+amount, null, null);
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

    private class ReceiveAPI{
        private String ref;
        private String sendto;
        private String amount;

        public ReceiveAPI(){

        }

        public String getRef() {
            return ref;
        }

        public void setRef(String ref) {
            this.ref = ref;
        }

        public String getSendto() {
            return sendto;
        }

        public void setSendto(String sendto) {
            this.sendto = sendto;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }
}