package in.thesoup.thesoupstoriesnews.preferencesfbauth;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.List;

import in.thesoup.thesoupstoriesnews.gsonclasses.filters1.Filters;
import in.thesoup.thesoupstoriesnews.SoupContract;

/**
 * Created by Jani on 26-09-2017.
 */

public class PrefUtilFilter2 {

    private Activity activity;


    public PrefUtilFilter2(Activity activity) {

        this.activity = activity;
    }

    public void IDstatus(String ID,String value){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ID,value);
        editor.apply();

    }

    public void SaveIDstatus(List<Filters> Data, int position, String value){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Data.get(position).getId(),value);
        editor.apply();

    }

    public String getStatusofID(String ID){

        if(ID!=null&&!ID.isEmpty()){
            SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
            return pref.getString(ID,null);
        }else{
            return null;
        }



        /*if (pref.getString(ID, null) != null && !pref.getString(ID, null).isEmpty()) {
            return pref.getString(ID, null);
        } else {
            return "1";
        }*/

    }

    public void SaveFilterListSize(int count){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("count",String.valueOf(count));
        editor.apply();

    }

    public String getFilterlistSize(){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        return pref.getString("count",null);

    }

    public void SaveFirstFiltername(String firstfiltername){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("firstfiltername",firstfiltername);
        editor.apply();

    }

    public String getFirstFiltername(){
        SharedPreferences pref = activity.getSharedPreferences(SoupContract.FILTERPREFERENCE,0);
        return pref.getString("firstfiltername",null);

    }

    public void AllIDselect(int m){
        for(int i =1;i<=m;i++){
            IDstatus(String.valueOf(i),"1");
        }
    }

    public void AllIDclear(int m){
        for(int i =1;i<=m;i++){
            IDstatus(String.valueOf(i),"0");
        }

    }

}
