package com.pixelro.nenoons;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class SharedPreferencesManager {

    Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token);
        editor.commit();
    }

    public String getToken(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }

    public void removeToken(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.TOKEN);
        editor.commit();
    }

    public void setName(String name){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.NAME,name);
        editor.commit();
    }

    public String getName(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.NAME,"");
    }

    public void removeName(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.NAME);
        editor.commit();
    }

    public void setEmail(String email){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.EMAIL,email);
        editor.commit();
    }

    public String getEmail(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.EMAIL,"");
    }

    public void removeEmail(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.EMAIL);
        editor.commit();
    }

    public void setLoginning(Boolean bool){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,bool);
        editor.commit();
    }

    public Boolean getLoginning(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,false);
    }

    public void removeLoginning(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.LOGINNING);
        editor.commit();
    }

    public void setColor(int color){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).edit();
        editor.putInt(EYELAB.APPDATA.TEST.LAST_COLOR,color);
        editor.commit();
    }

    public int getColor(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).getInt(EYELAB.APPDATA.TEST.LAST_COLOR,0);
    }

    public void removeColor(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.TEST.LAST_COLOR);
        editor.commit();
    }

    public void setFont(int font){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).edit();
        editor.putInt(EYELAB.APPDATA.TEST.LAST_FONT,font);
        editor.commit();
    }

    public int getFont(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).getInt(EYELAB.APPDATA.TEST.LAST_FONT,0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Typeface getFontTypeface(){

        int font = (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).getInt(EYELAB.APPDATA.TEST.LAST_FONT,0);

        if (font == TestProfile.Font.FONT_2){
            return context.getResources().getFont(R.font.dall01r);
        }
        else if (font == TestProfile.Font.FONT_3){
            return context.getResources().getFont(R.font.dall01b);
        }
        else if (font == TestProfile.Font.FONT_4){
            return context.getResources().getFont(R.font.dall01eb);
        }

        return null;
    }

    public void removeFont(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_TEST, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.TEST.LAST_FONT);
        editor.commit();
    }

}
