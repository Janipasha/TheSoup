package in.thesoup.thesoup.Activities;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.analytics.Tracker;

//import in.thesoup.thesoup.Application.AnalyticsApplication;
import org.json.JSONException;

import java.util.HashMap;

import in.thesoup.thesoup.NetworkCalls.NetworkUtils;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsRead;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;

import static com.squareup.picasso.Picasso.LoadedFrom.NETWORK;

/**
 * Created by Jani on 24-04-2017.
 */

public class ArticleWebViewActivity extends AppCompatActivity {

    private WebView wView;
    private String URL;
    private ProgressBar progress;
    private Tracker mTracker;
   // private AnalyticsApplication application;
    private SharedPreferences pref;
    private String StoryTitle,SubstoryId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.article_web_view);

        Bundle extras = getIntent().getExtras();

        URL = extras.getString("ArticleURL");
        SubstoryId = extras.getString("substory_id");

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



        //application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        wView = (WebView)findViewById(R.id.webview);
        //TODO: progress bar add
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setVisibility(View.GONE);

        wView.setWebViewClient(new MyWebViewClient());

        wView.getSettings().setLoadsImagesAutomatically(true);
        wView.getSettings().setJavaScriptEnabled(true);
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
        }
    }

    /*@Override
    protected void onStart() {
        super.onStart();

        application.sendScreenName(mTracker, SoupContract.ARTICLE_WEB_PAGE_VIEWED);

        if(pref.contains(SoupContract.FB_ID)){

            String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
            application.sendEventUser(mTracker, SoupContract.PAGE_VIEW, SoupContract.ARTICLE_WEB_PAGE_VIEWED,
                    SoupContract.ARTICLEWEB_PAGE, SoupContract.FB_ID,name);
        }else {

            application.sendEvent(mTracker, SoupContract.PAGE_VIEW, SoupContract.ARTICLE_WEB_PAGE_VIEWED, SoupContract.ARTICLEWEB_PAGE);
        }

    }*/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
