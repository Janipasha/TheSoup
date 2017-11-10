package in.thesoup.thesoupstoriesnews.networkcalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.activities.SplashActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.versioncheck.VersionCheck;

/**
 * Created by Jani on 01-11-2017.
 */

public class NetworkUtilsVersionCheck {

    private Context mcontext;
    private final static String BOUNDARY = "khisarner";
    private HashMap<String, String> params;

    public NetworkUtilsVersionCheck (Context context, HashMap<String, String> params) {
        this.mcontext = context;
        this.params = params;
    }

    private String createPostBody(HashMap<String, String> params) {
        StringBuilder sbPost = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                Log.e("param values",key +" "+params.get(key));
                sbPost.append("\r\n" + "--" + BOUNDARY + "\r\n");
                sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
                sbPost.append(params.get(key));
            }
        }

        return sbPost.toString();
    }

    public void CheckVersion() {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.VERSIONVERIFY, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("version response", response.toString());
                        Log.d("version response",": sucess");

                        Gson gson = new Gson();
                        VersionCheck DatafromGson = gson.fromJson(response.toString(),VersionCheck.class);

                        String VersionApiResponse = DatafromGson.getData();

                        SplashActivity activity = (SplashActivity) mcontext;
                        if(VersionApiResponse.equals("0")){
                            NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(mcontext);
                            fetchfilterdata.getFilters();
                        }else if(VersionApiResponse.equals("1")){
                            if(activity!=null){
                                activity.forceUpdate();
                            }
                        }else if(VersionApiResponse.equals("2")) {
                            if (activity != null) {
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
                                if (pref.getString("weeklytimecheck", null) != null && !pref.getString("weeklytimecheck", null).isEmpty()) {
                                    String time = pref.getString("weeklytimecheck", null);

                                    int numberOfDaysSinceCheck =getTime(time);
                                    Log.d("time diffenrence",String.valueOf(numberOfDaysSinceCheck));

                                    if(numberOfDaysSinceCheck>3){
                                        activity.recommendUpdate();
                                        SharedPreferences.Editor edit = pref.edit();
                                        //TODO: Check time is sent in the right way
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String currentTime = sdf.format(new Date());
                                        edit.putString("weeklytimecheck",currentTime);
                                        edit.apply();
                                    }else{
                                        NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(mcontext);
                                        fetchfilterdata.getFilters();
                                    }

                                }else{
                                    SharedPreferences.Editor edit = pref.edit();
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String currentTime = sdf.format(new Date());
                                    edit.putString("weeklytimecheck",currentTime);
                                    edit.apply();
                                    activity.recommendUpdate();
                                }


                            }}}


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment
                            if (networkResponse != null) {
                                Log.e("Authverify code error",String.valueOf(networkResponse.statusCode));
                            }
                        }
                    }}){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headerParam = new HashMap<>();
                headerParam.put("Content-Type","multipart/form-data;boundary="+BOUNDARY+";");
                return headerParam;
            }

            @Override
            public byte[] getBody() {
                String postBody = createPostBody(params);

                return postBody.getBytes();
            }
        };
        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                SoupContract.TIMEOUT_RETRY_TIME,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        singleton.addToRequestQueue(jsObjRequest);




    }

    public int getTime(String timeString){

        String Time = timeString;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());


        Date date1 = null;
        Date date2 = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date1 = format.parse(Time);
            date2 = format.parse(currentTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long Difference = date2.getTime() - date1.getTime();

        Log.d("difference", String.valueOf(Difference));

        int i = (int) (Difference / 86400000);
        Log.d("kya be", String.valueOf(i));


        return i;



    }


}
