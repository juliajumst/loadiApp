package com.loader.loadi.data.objects;

/**
 * Created by Wendy on 10/29/2016.
 */
public class Users extends Data  {
    public static final String TAG = "Users";
    public static final String TABLE_NAME = "users";
    public static final String PRIMARY_KEY = "id";

    private int id;
    private String display_name;
    private String email;
    private String photo_url;
    private String id_token;
    private String google_id;
    private String name;
    private String phone_number;
    private int age;
    private String address;
    private String uname;

    public Users(){
        super(TABLE_NAME);
    }
    public Users(int id,String display_name,String email,String photo_url,String id_token,String google_id,String name,String phone_number,int age,String address,String uname){
        this.setId(id);
        this.setDisplay_name(display_name);
        this.setEmail(email);
        this.setPhoto_url(photo_url);
        this.setId_token(id_token);
        this.setGoogle_id(google_id);
        this.setName(name);
        this.setPhone_number(phone_number);
        this.setAge(age);
        this.setAddress(address);
        this.setUname(uname);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
