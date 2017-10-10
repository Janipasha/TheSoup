package in.thesoup.thesoupstoriesnews.PreferencesFbAuth;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import in.thesoup.thesoupstoriesnews.App.Config;
import in.thesoup.thesoupstoriesnews.SoupContract;

import static com.facebook.FacebookSdk.getApplicationContext;


public class PrefUtil {

    private Activity activity;

    // Constructor
    public PrefUtil(Activity activity) {
        this.activity = activity;
    }


    public String getFirebaseID(){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        return  prefs.getString(SoupContract.FIREBASEID,null);
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

    public void saveAccessToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SoupContract.SOCIAL_TOKEN, token);
        editor.apply(); // This line is IMPORTANT !!!
    }

    public void saveGeneratedUserToken(String token) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SoupContract.SOCIAL_TOKEN, token);
        editor.apply();
    }

    public String getGeneratedUserToken(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.SOCIAL_TOKEN, null);

    }


    public String getToken() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.SOCIAL_TOKEN, null);
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

    public void saveUserInfo(String first_name,
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

        editor.putString(SoupContract.FIRST_NAME, first_name);
        editor.putString(SoupContract.LAST_NAME, last_name);
        editor.putString(SoupContract.EMAIL_ID, email);
        editor.putString(SoupContract.GENDER, gender);
        editor.putString(SoupContract.IMAGE_URL, profileURL);
        editor.putString(SoupContract.SOCIAL_ID, id);
        editor.putString(SoupContract.AGE_MIN, age_min);
        editor.putString(SoupContract.AGE_MAX, age_max);

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
        return prefs.getString(SoupContract.EMAIL_ID,null);
    }

    public String getFirstname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.FIRST_NAME,null);
    }


    public String getLastname(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.LAST_NAME,null);
    }


    public String getGender(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.GENDER,null);
    }


    public String getId(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.SOCIAL_ID,null);
    }

    public String getPictureUrl(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.IMAGE_URL,null);}

    public String getAgeMin(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.AGE_MIN,null);
    }

    public String getAgeMax(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        return prefs.getString(SoupContract.AGE_MAX,null);
    }





}
