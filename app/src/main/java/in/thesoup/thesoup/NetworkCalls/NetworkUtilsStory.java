package in.thesoup.thesoup.NetworkCalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;

/**
 * Created by Jani on 13-04-2017.
 */

public class NetworkUtilsStory {

    private Context mcontext;
    private List<Substories> mSubstories;
    private String Storytitle, followstatus,StoryId;
    private final String BOUNDARY= "whatsnonega";
    private HashMap<String,String> params;





    public NetworkUtilsStory(Context context,HashMap<String,String> params){
        this.mcontext = context;
        this.Storytitle = Storytitle;
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


    public void getSingleStory() throws JSONException {

        MySingleton singleton = MySingleton.getInstance(mcontext);
       // String Url = SoupContract.STORYURL +StoryId;



        //RequestQueue queue = singleton.getRequestQueue();


        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.STORYURL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("akunamatata", response.toString());


                        gsonConversion mpopulateUIStory = new gsonConversion();

                        Log.i("gson", mpopulateUIStory.toString());

                        mpopulateUIStory.fillStoryUI(response,mcontext);


                    }


                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Auto-generated method stub

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
        };/* {
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("auth_token", "fa5cfb2eb02b18825ff4d94b81144023");
                params.put("story_id", StoryId);
                return params;
            }

        };*/
            //nee to implement params for post request.


        singleton.addToRequestQueue(jsObjRequest);

        //MySingleton.getInstance(this).addToRequestQueue(stringRequest);



    }


}
