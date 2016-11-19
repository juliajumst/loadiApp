package com.loader.loadi.manager;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Wendy on 10/22/2016.
 */
public class LoadiApp extends Application {

    public static final String TAG = LoadiApp.class.getSimpleName();
    private static LoadiApp sInstance;

    public SQLiteHelper mSQLite;


    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        initVariables();
    }

    public void initVariables(){
        mSQLite = SQLiteHelper.getInstance(this);
    }

    public static LoadiApp getInstance() {
        return sInstance;
    }


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag( TextUtils.isEmpty(tag) ? TAG : tag );
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag( TAG );
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}

