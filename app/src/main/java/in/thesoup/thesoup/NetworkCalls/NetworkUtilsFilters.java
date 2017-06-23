package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.List;

import in.thesoup.thesoup.Activities.SplashActivity;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;

import static android.R.id.edit;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Jani on 05-06-2017.
 */

public class NetworkUtilsFilters {

    private Context mcontext;
    private List<Filterdata> mFilterData;

    public NetworkUtilsFilters(Context context) {
        this.mcontext = context;


    }

    public void getFilters(){
        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.FILTERURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("filterApi", response.toString());

                        SharedPreferences pref = mcontext.getSharedPreferences(SoupContract.FILTERPREF,0);
                        SharedPreferences.Editor edit = pref.edit();
                        edit.putString("filterobject",response.toString());
                        edit.apply();




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





}

