package in.thesoup.thesoup.PreferencesFbAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.List;

import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.SoupContract;

import static android.R.attr.value;
import static android.os.Build.ID;


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

    public String getStatusofID(String ID){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        if (pref.getString(ID, null) != null && !pref.getString(ID, null).isEmpty()) {
            return pref.getString(ID, null);
        } else {
            return "1";
        }

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



    public void ID1Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("1",value);
        editor.apply();
    }

    public String GetID1Status() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);

        if (pref.getString("1", null) != null && !pref.getString("1", null).isEmpty()) {
            return pref.getString("1", null);
        } else {
            return "1";
        }

    }


    public void ID2Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("2",value);
        editor.apply();
    }


    public String GetID2Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("2", null) != null && !pref.getString("2", null).isEmpty()) {
            return pref.getString("2", null);

        } else {
            return "1";

        }
    }


    public void ID3Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("3",value);
        editor.apply();
    }


    public String GetID3Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("3", null) != null && !pref.getString("3", null).isEmpty()) {
            return pref.getString("3", null);
        } else {
            return "1";

        }
    }


    public void ID4Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("4",value);
        editor.apply();
    }


    public String GetID4Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("4", null) != null && !pref.getString("4", null).isEmpty()) {
            return pref.getString("4", null);
        } else {
            return "1";

        }
    }


    public void ID5Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("5",value);
        editor.apply();
    }


    public String GetID5Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("5", null) != null && !pref.getString("5", null).isEmpty()) {
            return pref.getString("5", null);
        } else {
            return "1";

        }
    }


    public void ID6Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("6",value);
        editor.apply();
    }


    public String GetID6Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("6", null) != null && !pref.getString("6", null).isEmpty()) {
            return pref.getString("6", null);
        } else {
            return "1";

        }
    }


    public void ID7Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("7",value);
        editor.apply();
    }


    public String GetID7Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("7", null) != null && !pref.getString("7", null).isEmpty()) {
            return pref.getString("7", null);
        } else {
            return "1";

        }
    }


    public void ID8Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("8",value);
        editor.apply();
    }


    public String GetID8Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("8", null) != null && !pref.getString("8", null).isEmpty()) {
            return pref.getString("8", null);
        } else {
            return "1";

        }
    }


    public void ID9Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("9",value);
        editor.apply();
    }


    public String GetID9Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("9", null) != null && !pref.getString("9", null).isEmpty()) {
            return pref.getString("9", null);
        } else {
            return "1";

        }
    }


    public void ID10Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("10",value);
        editor.apply();
    }


    public String GetID10Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("10", null) != null && !pref.getString("10", null).isEmpty()) {
            return pref.getString("10", null);
        } else {
            return "1";

        }
    }


    public void ID11Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("11",value);
        editor.apply();
    }


    public String GetID11Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("11", null) != null && !pref.getString("11", null).isEmpty()) {
            return pref.getString("11", null);
        } else {
            return "1";

        }
    }


    public void ID12Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("12",value);
        editor.apply();
    }


    public String GetID12Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("12", null) != null && !pref.getString("12", null).isEmpty()) {
            return pref.getString("12", null);
        } else {
            return "1";

        }
    }


    public void ID13Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("13",value);
        editor.apply();
    }


    public String GetID13Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("13", null) != null && !pref.getString("13", null).isEmpty()) {
            return pref.getString("13", null);
        } else {
            return "1";

        }
    }


    public void ID14Status(String value){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("14",value);
        editor.apply();
    }


    public String GetID14Status(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        if (pref.getString("14", null) != null && !pref.getString("14", null).isEmpty()) {
            return pref.getString("14", null);
        } else {
            return "1";

        }
    }
}
