package in.thesoup.thesoupstoriesnews.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

//import com.google.android.gms.analytics.Tracker;
import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

//import in.thesoup.thesoup.Application.AnalyticsApplication;
import org.json.JSONException;
import org.jsoup.safety.Cleaner;

import java.util.HashMap;

import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsRead;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 24-04-2017.
 */

public class ArticleWebViewActivity extends AppCompatActivity {

    private WebView wView;
    private String URL;
    private ProgressBar progress;
   // private Tracker mTracker;
   // private AnalyticsApplication application;
    private SharedPreferences pref;
    private String StoryTitle,SubstoryId;
	//CVIPUL Analytics
	private Bundle mExtras;
	private Bundle mParams;	//what we send to firebase
	private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;
    private String storyColor;
	//End Analytics


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
        mParams = new Bundle();
		

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        setContentView(R.layout.article_web_view);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar1);

        mExtras = getIntent().getExtras();

        URL = mExtras.getString("ArticleURL");
       SubstoryId = mExtras.getString("substory_id");
        storyColor = mExtras.getString("storycolor");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#"+storyColor));
        }


        HashMap<String,Object> oparams = new HashMap<>();
        oparams.put("screen_name", "browser_screen");
        oparams.put("category", "screen_view"); //(only if possible)
        oparams.put("article_url",URL);
        cleverTap.event.push("viewed_browser_screen", oparams);




        toolbar.setBackgroundColor(Color.parseColor("#"+storyColor));



        pref = PreferenceManager.getDefaultSharedPreferences(ArticleWebViewActivity.this);
        String auth_token = pref.getString(SoupContract.AUTH_TOKEN,null);

        HashMap<String,String> params = new HashMap<>();
        params.put("auth_token",auth_token);
        params.put("type","substories");
        params.put("id",SubstoryId);

        NetworkUtilsRead readRequest = new NetworkUtilsRead(ArticleWebViewActivity.this,params);
        try {
            readRequest.sendReadRequest();
        } catch (JSONException e) {
            e.printStackTrace();
        }




        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        wView = (WebView)findViewById(R.id.webview);
        //TODO: progress bar add
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.VISIBLE);

        wView.setWebViewClient(new MyWebViewClient());

        wView.getSettings().setLoadsImagesAutomatically(true);
        wView.getSettings().setLoadsImagesAutomatically(true);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.setWebChromeClient(new WebChromeClient());
        wView.getSettings().setDomStorageEnabled(true);
        wView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        wView.loadUrl(URL);

    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            progress.setVisibility(View.GONE);
            ArticleWebViewActivity.this.progress.setProgress(100);
            super.onPageFinished(view, url);
            //TODO: read API call



        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //TODO: progress bar
            progress.setVisibility(View.VISIBLE);
            ArticleWebViewActivity.this.progress.setProgress(0);
            super.onPageStarted(view, url, favicon);
			
			// CVIPUL Analytics
			// TODO : verify Entity(article) click event
			mParams.putString("url_browsing", url);
			Bundle params = mParams;
			params.putString("category", "conversion");
			if(url.equals(mParams.getString("url_entity"))){
				mFirebaseAnalytics.logEvent("read", params);
			} else {
				mFirebaseAnalytics.logEvent("viewed_screen_browser_hyperlink",params);
			}
			// End Analytics
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public boolean onSupportNavigateUp() {
			// CVIPUL Analytics
			// TODO : verify Entity(article) click event			
			Bundle mparams = mParams;
            mparams.putString("screen_name", "browser_screen");
			mparams.putString("category", "tap");
			mFirebaseAnalytics.logEvent("tap_browser_backward",mparams);
			// End Analytics

        finish();
		
        onBackPressed();
        return true;
    }



}
