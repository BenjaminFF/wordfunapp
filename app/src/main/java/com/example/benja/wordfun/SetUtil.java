package com.example.benja.wordfun;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by benja on 2018/9/14.
 */

public class SetUtil {
    public static int getRandomColor(int alpha){
        int r=(int)(Math.random()*255);
        int g=(int)(Math.random()*255);
        int b=(int)(Math.random()*255);
        return Color.argb(alpha,r,g,b);
    }

    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager)
                context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager==null){
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if(networkinfo==null||!networkinfo.isAvailable()){
            return false;
        }
        return true;
    }

}
