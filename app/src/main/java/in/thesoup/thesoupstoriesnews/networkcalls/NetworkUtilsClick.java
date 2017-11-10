package in.thesoup.thesoupstoriesnews.networkcalls;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.thesoup.thesoupstoriesnews.SoupContract;

/**
 * Created by Jani on 16-06-2017.
 */

public class NetworkUtilsClick {
    private HashMap<String,String> params;
    private Context mcontext;
    private final String BOUNDARY= "whatsnonega";

    public NetworkUtilsClick(Context context,HashMap<String,String> params){
        this.params = params;
        this.mcontext = context;
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


    public void sendClick() throws JSONException {

        Log.d("readrequeststatus",": start");

        MySingleton singleton = MySingleton.getInstance(mcontext);

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, SoupContract.MARKCLICK, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Readresponse", response.toString());
                        Log.d("readrequeststatus",": sucess");
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            NetworkResponse networkResponse = error.networkResponse;
                            //TODO: error response write for fragment
                            if (networkResponse != null) {
                                Log.d("Read error code",String.valueOf(networkResponse.statusCode));
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


}

