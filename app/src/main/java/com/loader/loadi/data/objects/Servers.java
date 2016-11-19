package com.loader.loadi.data.objects;

/**
 * Created by Wendy on 10/29/2016.
 */
public class Servers extends Data  {
    public static final String TAG = "Servers";
    public static final String TABLE_NAME = "servers";
    public static final String PRIMARY_KEY = "id";

    private int id;
    private String phone_number;
    private String network;

    public Servers(){
        super(TABLE_NAME);
    }
    public Servers(int id, String phone_number, String network){
        this.id = id;
        this.phone_number = phone_number;
        this.network = network;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
