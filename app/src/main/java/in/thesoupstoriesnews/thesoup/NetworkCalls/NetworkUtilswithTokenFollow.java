package in.thesoupstoriesnews.thesoup.NetworkCalls;

import android.content.Context;
import android.support.v4.app.Fragment;
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

import in.thesoupstoriesnews.thesoup.Activities.NavigationActivity;
import in.thesoupstoriesnews.thesoup.Fragments.FollowingFragment;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoupstoriesnews.thesoup.SoupContract;
import in.thesoupstoriesnews.thesoup.gsonConversion;

/**
 * Created by Jani on 06-09-2017.
 */

public class NetworkUtilswithTokenFollow {

    private Context mcontext;
    private List<StoryDataMain> nStoryData;
    private final static String BOUNDARY = "khisarner";
    private HashMap<String, String> params;

    private int statusCode;

    public NetworkUtilswithTokenFollow(Context context, HashMap<String, String> params) {
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
    public void getFeedFollow(final int fragmenttag, final String totalrefresh) {

        MySingleton singleton = MySingleton.getInstance(mcontext);

        //RequestQueue queue = singleton.getRequestQueue();


        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.GETFOLLOWING, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("akunamatata", response.toString());
                        gsonConversion mpopulateUI = new gsonConversion();
                        mpopulateUI.fillUiFollow(response, mcontext, fragmenttag, totalrefresh);
                    }
                    //mEarthquakedatajsonclass = red;
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment

                            if (networkResponse != null) {
                                if (networkResponse.statusCode == 404) {
                                    if (mcontext instanceof NavigationActivity) {
                                        if (params.get("page").equals("0")) {
                                            NavigationActivity activity = (NavigationActivity) mcontext;

                                            if (fragmenttag == 2) {
                                                Fragment f = activity.getFragment(fragmenttag);
                                                ((FollowingFragment) f).Nofollowers();

                                            }
                                        } else {
                                           NavigationActivity activity = (NavigationActivity) mcontext;
                                            if (fragmenttag == 2) {
                                                Fragment f = activity.getFragment(fragmenttag);
                                                ((FollowingFragment) f).stopProgress();
                                            }

                                        }
                                    }
                                }

                                Log.d("asdfghj", String.valueOf(networkResponse.statusCode));
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headerParam = new HashMap<>();
                headerParam.put("Content-Type", "multipart/form-data;boundary=" + BOUNDARY + ";");
                return headerParam;
            }

            @Override
            public byte[] getBody() {
                String postBody = createPostBody(params);

                return postBody.getBytes();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                   /* if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
                        VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
                        volleyError = error;
                    }

                    return volleyError;*/

                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }
        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                SoupContract.TIMEOUT_RETRY_TIME,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        singleton.addToRequestQueue(jsObjRequest); //


    }

}
