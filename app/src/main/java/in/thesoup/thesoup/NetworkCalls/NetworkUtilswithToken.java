package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.Fragments.MyFeedFragment;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 20-04-2017.
 */

public class NetworkUtilswithToken {

        private Context mcontext;
        private List<StoryData> mStoryData;
        private final static String BOUNDARY ="khisarner";
        private HashMap<String, String> params;

       private  int statusCode;

        public NetworkUtilswithToken(Context context, List<StoryData> storyData, HashMap<String,String> params) {
            this.mcontext = context;
            this.mStoryData = storyData;
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



        public void getFeed() {

            MySingleton singleton = MySingleton.getInstance(mcontext);

            //RequestQueue queue = singleton.getRequestQueue();


            final JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, SoupContract.URL, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i("akunamatata", response.toString());



                            gsonConversion mpopulateUI = new gsonConversion();

                            mpopulateUI.fillUI(response,mcontext);


                        }


                        //mEarthquakedatajsonclass = red;

                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {


                           NetworkResponse response = error.networkResponse;


                            if (response != null && response.data != null) {

                            NetworkResponse networkResponse = error.networkResponse;

                            //TODO: error response write for fragment

                          if(networkResponse!=null){
                                if(networkResponse.statusCode ==404){
                                    if (mcontext instanceof MainActivity) {
                                        if (params.get("page").equals("0")) {

                                        MainActivity activity = (MainActivity)mcontext;

                                            int position = activity.getFragmentPosition();
                                            if(position==1){

                                            Fragment f = activity.getFragment(position);

                                            ((MyFeedFragment)f).Nofollowers();
                                        }}

                                    }
                            }





                            Log.d("asdfghj",error.toString());




                                }
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

            singleton.addToRequestQueue(jsObjRequest); //


}
}
