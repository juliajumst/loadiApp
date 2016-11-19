package com.loader.loadi.manager;

import android.content.Context;

import java.util.StringTokenizer;

/**
 * Created by Wendy on 10/22/2016.
 */
public class WebServiceURL {

    private String webService;

    public WebServiceURL(Context context) {

        LocalStorage ls = new LocalStorage(context);
        webService = ls.getLocalStorage().getString(LocalStorage.WEBSERVICE, "");

    }
    public String getWebService(){
        return webService;
    }

    public String getHost(){
        return new StringTokenizer(webService).nextToken("/");
    }

    public String create_user() {
        return "http://" + webService + "/create_user.php";
    }
    public String get_user_by_uname_pword() {
        return "http://" + webService + "/get_user_by_uname_pword.php";
    }
    public String get_all_servers() {
        return "http://" + webService + "/get_user_by_uname_pword.php";
    }
}