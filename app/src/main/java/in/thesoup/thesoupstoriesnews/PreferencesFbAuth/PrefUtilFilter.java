package in.thesoup.thesoupstoriesnews.PreferencesFbAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import in.thesoup.thesoupstoriesnews.GSONclasses.filters1.Filters;


public class PrefUtilFilter {

    private Activity activity;


    public PrefUtilFilter(Activity activity) {
        this.activity = activity;
    }

    public void IDstatus(String ID,String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(ID,value);
        editor.apply();

    }

    public void SaveIDstatus(List<Filters> Data, int position, String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Data.get(position).getId(),value);
        editor.apply();

    }

    public String getStatusofID(String ID){

        if(ID!=null&&!ID.isEmpty()){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
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
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("count",String.valueOf(count));
        editor.apply();

    }

    public String getFilterlistSize(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        return pref.getString("count",null);

    }

    public void SaveFirstFiltername(String firstfiltername){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("firstfiltername",firstfiltername);
        editor.apply();

    }

    public String getFirstFiltername(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        return pref.getString("firstfiltername",null);

    }

    public void SaveTotalFilterCount(String count){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("totalfiltercount",count);
        editor.apply();
    }

    public String TotalFilterCount(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        return pref.getString("totalfiltercount",null);

    }

}
