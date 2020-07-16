package com.pixelro.nenoons;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public String getToken(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getString(EYELAB.APPDATA.ACCOUNT.TOKEN,"");
    }

    public void setToken(String token){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putString(EYELAB.APPDATA.ACCOUNT.TOKEN,token);
        editor.commit();
    }

    public void removeToken(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.TOKEN);
        editor.commit();
    }

    public Boolean getLoginning(){
        return (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).getBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,false);
    }

    public void setLoginning(Boolean bool){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.putBoolean(EYELAB.APPDATA.ACCOUNT.LOGINNING,bool);
        editor.commit();
    }

    public void removeLoginning(){
        SharedPreferences.Editor editor = (context.getSharedPreferences(EYELAB.APPDATA.NAME_ACCOUNT, Context.MODE_PRIVATE)).edit();
        editor.remove(EYELAB.APPDATA.ACCOUNT.LOGINNING);
        editor.commit();
    }

}
