package in.thesoupstoriesnews.thesoup.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.thesoupstoriesnews.thesoup.Activities.EmailActivity;
import in.thesoupstoriesnews.thesoup.Activities.LoginActivity;
import in.thesoupstoriesnews.thesoup.SoupContract;

/**
 * Created by Jani on 18-04-2017.
 */

public class NetworkUtilsLogin {

    private Context mcontext;
    private HashMap<String,String> params;
    private final String BOUNDARY = "whagtstheaek";
    private FirebaseAnalytics mFirebaseAnalytics;


    public NetworkUtilsLogin(Context context, HashMap<String,String> params){
        this.mcontext =  context;
        this.params = params;

    }


    private String createPostBody(HashMap<String, String> params) {
        StringBuilder sbPost = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                sbPost.append("\r\n" + "--" + BOUNDARY + "\r\n");
                sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
                sbPost.append(params.get(key));
            }
        }

        return sbPost.toString();
    }

    public void loginvolleyRequest(){



        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.LOGINURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginjsonresponse", response.toString());

                        String outh_token= "" ;

                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);

                        Bundle mparams = new Bundle();
                        mparams.putString("label", "login_screen");
                        mparams.putString("category", "conversion");
                        mFirebaseAnalytics.logEvent("login", mparams);




                        try {
                            outh_token = response.getJSONObject("data").getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        EmailActivity Object = (EmailActivity) mcontext;


                      SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(SoupContract.AUTH_TOKEN,outh_token);
                        editor.apply();


                        Log.d("prefvalue 1",pref.getString("auth_token",null)+"-----");

                       Object.main();
                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
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


    public void loginvolleyRequestfromMain(){



        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.LOGINURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginjsonresponse", response.toString());

                        String outh_token= "" ;

                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);

                        Bundle mparams = new Bundle();
                        mparams.putString("label", "login_screen");
                        mparams.putString("category", "conversion");
                        mFirebaseAnalytics.logEvent("login", mparams);

                        try {
                            outh_token = response.getJSONObject("data").getString("token");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        LoginActivity Object = (LoginActivity) mcontext;


                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString(SoupContract.AUTH_TOKEN,outh_token);
                        editor.apply();


                        Log.d("prefvalue 1", pref.getString("auth_token",null)+"-----");

                        Object.startActivityMD();

                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
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

        singleton.addToRequestQueue(jsObjRequest);



    }

    public void logoutVolleyRequest(){



        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.LOGOUT_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("loginjsonresponse", response.toString());

                        String outh_token= "" ;

                        mFirebaseAnalytics = FirebaseAnalytics.getInstance(mcontext);

                        Bundle mparams = new Bundle();
                        mparams.putString("label", "login_screen");
                        mparams.putString("category", "conversion");
                        mFirebaseAnalytics.logEvent("login", mparams);





                    }



                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                }){
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
}
