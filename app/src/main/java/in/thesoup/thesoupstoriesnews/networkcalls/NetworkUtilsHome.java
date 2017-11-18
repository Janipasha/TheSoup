package in.thesoup.thesoupstoriesnews.networkcalls;

import android.content.Context;
import android.net.Uri;
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

import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.activities.EndlessRecyclerView;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment1;
import in.thesoup.thesoupstoriesnews.gsonConversion;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.StoryDataMainHome;

/**
 * Created by Jani on 11-11-2017.
 */

public class NetworkUtilsHome {

        private Context mcontext;
        private List<StoryDataMainHome> nStoryData;
        private final static String BOUNDARY = "khisarner";
        private HashMap<String, String> params;
    private EndlessRecyclerView scrollListener;

        private int statusCode;

        public NetworkUtilsHome(Context context, List<StoryDataMainHome> storyData, HashMap<String, String> params,EndlessRecyclerView scrollListener) {
            this.mcontext = context;
            this.nStoryData = storyData;
            this.scrollListener = scrollListener;
            this.params = params;
        }

        private String createPostBody(HashMap<String, String> params) {
            StringBuilder sbPost = new StringBuilder();
            for (String key : params.keySet()) {
                if (params.get(key) != null) {
                 Log.e("param values", key + " " + params.get(key));
                    sbPost.append("\r\n" + "--" + BOUNDARY + "\r\n");
                    sbPost.append("Content-Disposition: form-data; name=\"" + key + "\"" + "\r\n\r\n");
                    sbPost.append(params.get(key));
                }
            }

            return sbPost.toString();
        }

    public void getFeed2(final int fragmenttag, final String totalrefresh) {

        MySingleton singleton = MySingleton.getInstance(mcontext);



        final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.GETSINGLEFEED, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("khan","great");
                        Log.e("akunamatata home", response.toString());
                        Log.e("vipul asshole","true");
                        gsonConversion mpopulateUI = new gsonConversion();

                        String section = params.get("section");

                        mpopulateUI.fillHome(response,mcontext,fragmenttag,totalrefresh,section,scrollListener);
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
                                        if (params.get("section").equals("0")) {
                                            NavigationActivity activity = (NavigationActivity) mcontext;

                                            if (fragmenttag == 2) {
                                                Fragment f = activity.getFragment(fragmenttag);
                                                ((FollowingFragment) f).Nofollowers();

                                            }
                                        } else {
                                            NavigationActivity activity = (NavigationActivity) mcontext;
                                            if (fragmenttag == 0) {
                                                Fragment f = activity.getFragment(fragmenttag);
                                                ((HomeFragment1) f).stopProgress();
                                            }

                                        }
                                    }
                                }else if(mcontext instanceof NavigationActivity){
                                    NavigationActivity activity = (NavigationActivity) mcontext;
                                    if (fragmenttag == 0) {
                                        Fragment f = activity.getFragment(fragmenttag);
                                        ((HomeFragment1) f).stopProgress();
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


