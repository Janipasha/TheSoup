package in.thesoup.thesoup.PreferencesFbAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import in.thesoup.thesoup.SoupContract;


public class PrefUtil {

    private Activity activity;

    // Constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }

    public void saveTotalRefresh(String totalrefresh){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SoupContract.TOTAL_REFRESH, totalrefresh);
        editor.apply();


    }

    public String getTotalRefresh(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.TOTAL_REFRESH,null);
    }

    public void saveAccessTokenPermissions(String token,String permissions) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SoupContract.FB_ID, token);
        editor.putString("grantedScope", permissions);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveGeneratedUserToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("auth_token", token);
        editor.apply();
    }

    public String getGeneratedUserToken(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("auth_token", null);

    }


    public String getToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.FB_ID, null);
    }

    public String getPermissions() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("grantedScope", null);
    }

    public void clearToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveFacebookUserInfo(String first_name,
                                     String last_name,
                                     String email,
                                     String gender,
                                     String profileURL,
                                     String id,
                                     String age_min,
                                     String age_max) {

        Log.d("pref_use", age_min);
        //Log.d("pref_max",age_max);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(SoupContract.FIRSTNAME, first_name);
        editor.putString(SoupContract.LASTNAME, last_name);
        editor.putString("email_id", email);
        editor.putString("gender", gender);
        editor.putString("image_url", profileURL);
        editor.putString(SoupContract.FB_ID, id);
        editor.putString("age_min", age_min);
        editor.putString("age_max", age_max);

        editor.apply(); // This line is IMPORTANT !!!
        Log.d("MyApp", "Shared Name : " + first_name + "\nLast Name : " +
                last_name + "\nEmail : " + email/*"\nGender : "+gender**/ + "\nProfile Pic : " +
                "" + profileURL + "\n age_min:" + age_min);

    }

    public void getFacebookUserInfo(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        Log.d("MyApp", "Name : "+prefs.getString("fb_name",null)+"\nEmail : "+prefs.getString("fb_email",null));
    }

    public String getEmail(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("email_id",null);
    }

    public String getFirstname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("first_name",null);
    }


    public String getLastname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("last_name",null);
    }


    public String getGender(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("gender",null);
    }


    public String getId(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.FB_ID,null);
    }

    public String getPictureUrl(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("image_url",null);}

    public String getAgeMin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("age_min",null);
    }

    public String getAgeMax(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString("age_max",null);
    }





}
