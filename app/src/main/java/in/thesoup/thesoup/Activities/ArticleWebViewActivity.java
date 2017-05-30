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
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;

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
    private String StoryTitle,StoryId;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.article_web_view);


        //application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();


       Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pref = PreferenceManager.getDefaultSharedPreferences(this);


        Bundle extras = getIntent().getExtras();

        URL = extras.getString("ArticleURL");

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
