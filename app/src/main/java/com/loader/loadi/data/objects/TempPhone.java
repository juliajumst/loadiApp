package com.loader.loadi.data.objects;

/**
 * Created by Wendy on 10/29/2016.
 */
public class TempPhone extends Data  {
    public static final String TAG = "TempPhone";
    public static final String TABLE_NAME = "tempphone";
    public static final String PRIMARY_KEY = "id";

    private int id;
    private String phone_number;
    private String ref;

    public TempPhone(){
        super(TABLE_NAME);
    }
    public TempPhone(int id, String phone_number, String ref){
        this.id = id;
        this.phone_number = phone_number;
        this.ref = ref;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
