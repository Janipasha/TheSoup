package in.thesoup.thesoupstoriesnews.networkcalls;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jani on 06-04-2017.
 */


    public class MySingleton {

        private static MySingleton mInstance;
        private RequestQueue mRequestQueue;


      private MySingleton(Context context) {
            mRequestQueue = getRequestQueue(context);
      }


        public static synchronized MySingleton getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new MySingleton(context);
            }
            return mInstance;
        }

        public RequestQueue getRequestQueue(Context context) {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            mRequestQueue.add(req);
        }


    }

