package in.thesoup.thesoupstoriesnews.NetworkCalls;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.analytics.Tracker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.thesoup.thesoupstoriesnews.Activities.DetailsActivity;
//import in.thesoup.thesoup.Activities.LoginActivity;
//import in.thesoup.thesoup.Activities.feedActivity;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoupstoriesnews.Activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.Fragments.DiscoverFragmentMain;
import in.thesoup.thesoupstoriesnews.Fragments.HomeFragment;
import in.thesoup.thesoupstoriesnews.SoupContract;

/**
 * Created by Jani on 17-04-2017.
 */

public class NetworkUtilsFollowUnFollow {

    private Context mcontext;
    private Map<String, String> params;
    private final String BOUNDARY = "whatshitisthis";
    // AnalyticsApplication application;
    int statusCode;
    //Tracker mTracker;
    SharedPreferences pref;


    public NetworkUtilsFollowUnFollow(Context context, Map<String, String> params) {
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

    public void followRequest(final int position, final int fragmenttag) {

        //application = AnalyticsApplication.getInstance();
        //mTracker = application.getDefaultTracker();
        pref = PreferenceManager.getDefaultSharedPreferences(mcontext);


        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.FOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("followjsonresponse", response.toString());


                        String Story_id = "";

                        if (mcontext instanceof NavigationActivity) {

                           NavigationActivity activity = (NavigationActivity) mcontext;


                            if (fragmenttag == 0) {

                                Fragment f = activity.getFragment(fragmenttag);


                                try {
                                    Story_id = response.getJSONObject("data").getString("story_id");
                                    String Storytitle = "";

                                    //analytics

                                    // application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.FOLLOW, SoupContract.HOME_PAGE_DISCOVE, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));

                                    ((DiscoverFragmentMain) f).demo1(position, "1");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((DiscoverFragmentMain) f).demo1(position, "0");
                                }
                            } else if (fragmenttag == 1) {

                                Fragment f = activity.getFragment(fragmenttag);


                                try {
                                    Story_id = response.getJSONObject("data").getString("story_id");
                                    String Storytitle = "";

                                    //analytics

                                    // application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.FOLLOW, SoupContract.HOME_PAGE_DISCOVE, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));

                                    ((HomeFragment) f).demo1(position, "1");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((HomeFragment) f).demo1(position, "0");
                                }

                            }
                        }

                        if (mcontext instanceof DetailsActivity) {

                            DetailsActivity activity = (DetailsActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");

                                String Storytitle = "";

                                //analytics

                                //application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.FOLLOW, SoupContract.COLLECTION_PAG, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null),pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));

                                activity.DetailsActivitydemo("1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.DetailsActivitydemo("0");
                            }

                        }


                       /* if (mcontext instanceof feedActivity) {

                            feedActivity activity = (feedActivity) mcontext;

                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                String Storytitle = "";

                                //analytics

                               // application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.FOLLOW, SoupContract.HOME_PAGE_MYFEED, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));


                                activity.demo1(position, "1");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "0");
                            }

                        }*/


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

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


        };

        singleton.addToRequestQueue(jsObjRequest);


    }


    public void unFollowRequest(final int position, final int fragmenttag) {

        //application = AnalyticsApplication.getInstance();
        //mTracker = application.getDefaultTracker();
        pref = PreferenceManager.getDefaultSharedPreferences(mcontext);


        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.UNFOLLOWURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("unfollowjsonresponse", response.toString());


                        String Story_id = "";

                        if (mcontext instanceof NavigationActivity) {
                            NavigationActivity activity = (NavigationActivity) mcontext;
                            if (fragmenttag == 0) {
                                Fragment f = activity.getFragment(fragmenttag);
                                try {
                                    Story_id = response.getJSONObject("data").getString("story_id");
                                    String Storytitle = "";
                                    //analytics
                                    //application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.UNFOLLOW, SoupContract.HOME_PAGE_DISCOVER, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null),pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));
                                    ((DiscoverFragmentMain) f).demo1(position, "0");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((DiscoverFragmentMain) f).demo1(position, "1");
                                }
                            } else if (fragmenttag == 1) {
                                Fragment f = activity.getFragment(fragmenttag);
                                try {
                                    Story_id = response.getJSONObject("data").getString("story_id");
                                    String Storytitle = "";
                                    //analytics
                                    // application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.FOLLOW, SoupContract.HOME_PAGE_DISCOVE, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));
                                    Log.d("position", String.valueOf(position));
                                    ((HomeFragment) f).demo1(position, "0");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    ((HomeFragment) f).demo1(position, "1");
                                }
                            }
                        } else if (mcontext instanceof DetailsActivity) {
                            DetailsActivity activity = (DetailsActivity) mcontext;
                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                String Storytitle = "";
                                //analytics
                                //application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.UNFOLLOW, SoupContract.COLLECTION_PAGE, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));
                                activity.DetailsActivitydemo("0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.DetailsActivitydemo("1");
                            }
                        }/* else if (mcontext instanceof feedActivity) {
                            feedActivity activity = (feedActivity) mcontext;
                            try {
                                Story_id = response.getJSONObject("data").getString("story_id");
                                String Storytitle = "";
                                //analytics
                                //application.sendEventCollectionUser(mTracker, SoupContract.CONVERSION, SoupContract.UNFOLLOW, SoupContract.HOME_PAGE_MYFEED, Storytitle, String.valueOf(Story_id), pref.getString(SoupContract.FB_ID, null), pref.getString(SoupContract.FIRSTNAME, null) + pref.getString(SoupContract.LASTNAME, null));
                                activity.demo1(position, "0");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                activity.demo1(position, "1");
                            }
                        }*/
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

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

        };

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(
               SoupContract.TIMEOUT_RETRY_TIME,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        singleton.addToRequestQueue(jsObjRequest);


    }


}