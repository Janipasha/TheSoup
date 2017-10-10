package in.thesoup.thesoupstoriesnews.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoupstoriesnews.Activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.Activities.PagerActivity;
import in.thesoup.thesoupstoriesnews.Activities.SplashActivity;
import in.thesoup.thesoupstoriesnews.GSONclasses.filters.Filterdata;
import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.gsonConversion;

/**
 * Created by Jani on 05-06-2017.
 */

public class NetworkUtilsSplash {

    private Context mcontext;
    private List<Filterdata> mFilterData;
    private final String BOUNDARY= "whatsnonega";
    private HashMap<String,String> params;

    public NetworkUtilsSplash(Context context) {
        this.mcontext = context;


    }

    public NetworkUtilsSplash(Context context,HashMap<String ,String > params) {
        this.mcontext = context;
        this.params = params;


    }

    private String createPostBody(Map<String, String> params) {
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

    public void getFilters(){
        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.FILTERURL2, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("filterApi", response.toString());

                        SharedPreferences pref = mcontext.getSharedPreferences(SoupContract.FILTERPREF,0);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("filterobject",response.toString());
                        edit.apply();

                        SplashActivity activity = (SplashActivity) mcontext;
                        ((SplashActivity) mcontext).ToMainActivity();




                    }


                    //mEarthquakedatajsonclass = red;

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment



                            Log.d("Filter call error",": "+String.valueOf(networkResponse.statusCode));

                        }

                    }
                });

        singleton.addToRequestQueue(jsObjRequest);


    }

    public void verifyAuthToken(){

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.AUTHVERIFY, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("VerifyAuthresponse", response.toString());

                        gsonConversion gson = new gsonConversion();
                        String auth_token = gson.getAuthtoken(response.toString());

                        PagerActivity activity= (PagerActivity)mcontext;
                        activity.checkauth(auth_token);





                    }


                    //mEarthquakedatajsonclass = red;

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment



                            Log.d("Filter call error",": "+String.valueOf(networkResponse.statusCode));

                        }

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

    public void setInterestedCategories(){

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.SETCATEGORIES, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("setFilterResponse", response.toString());


                      Log.d("interested categories","sucess");



                    }


                    //mEarthquakedatajsonclass = red;

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment



                            Log.d("setFilercallerror",": "+String.valueOf(networkResponse.statusCode));

                        }

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

