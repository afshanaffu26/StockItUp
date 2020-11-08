package com.example.stockitup.utils;

import android.content.Context;
import android.content.SharedPreferences;

/** This class is used to check if the app is launched for the first time,
 so that the on-boarding screens are visible
 */

public class MyPreferences {
    private static final String MY_PREFERENCES = "my_preferences";

    /* This function is used to check if the screen is visible for the first time
     * @return true or false
     * */
    public static boolean isFirst(Context context){
        final SharedPreferences reader = context.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final boolean first = reader.getBoolean("is_first", true);
        if(first){
            final SharedPreferences.Editor editor = reader.edit();
            editor.putBoolean("is_first", false);
            editor.apply();
        }
        return first;
    }

}