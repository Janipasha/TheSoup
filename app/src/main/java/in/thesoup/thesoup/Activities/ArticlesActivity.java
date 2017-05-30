package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;

import java.util.List;

import in.thesoup.thesoup.Adapters.ArticlesAdapter;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 13-04-2017.
 */

public class ArticlesActivity extends AppCompatActivity {
    private TextView mtextView;

    private List<Articles> mArticles;
    private RecyclerView ArticlesView;
    private ArticlesAdapter mArticlesAdapter;
    private Tracker mTracker;
   // private AnalyticsApplication application;
    private SharedPreferences pref;
    private String StoryTitle,StoryId;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getarticles);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        pref= PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarArticle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

       // application = (AnalyticsApplication) getApplication();
        //mTracker = application.getDefaultTracker();





        Intent in = getIntent();
        mArticles =(List<Articles>) in.getSerializableExtra("ARTICLELIST");

        for(int i =0;i<mArticles.size();i++) {

            Log.d("Article Json", mArticles.get(i).getArticletitle());

        }

        Bundle extras = getIntent().getExtras();
        StoryId = extras.getString("story_id");
        StoryTitle = extras.getString("StoryTitle");

        Log.d("storyid,story",StoryId);
        Log.d("storyid,story",StoryTitle);

        //gsonConversion articlelist = new gsonConversion();
        //articlelist.ArticleJson(mString,mArticles);


        ArticlesView = (RecyclerView) findViewById(R.id.list_articles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
       ArticlesView.setLayoutManager(layoutManager);

      ArticlesView.setHasFixedSize(true);

        mArticlesAdapter = new ArticlesAdapter(mArticles,this);

        ArticlesView.setAdapter(mArticlesAdapter);





    }


    @Override
    protected void onStart() {
        super.onStart();

        //application.sendScreenName(mTracker, SoupContract.ARTICLES_VIEWED);

       /* if(pref.contains(SoupContract.FB_ID)){

            Log.d("fb_id",pref.getString(SoupContract.FB_ID,null));

            String name = pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null);
            application.sendEventCollectionUser(mTracker, SoupContract.PAGE_VIEW, SoupContract.ARTICLES_VIEWED,
                    SoupContract.ARTICLES_PAGE,StoryId,StoryTitle,pref.getString(SoupContract.FB_ID,null),
                    pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));
        }else {

            application.sendEventCollection(mTracker, SoupContract.PAGE_VIEW, SoupContract.ARTICLES_VIEWED, SoupContract.ARTICLES_PAGE,StoryId,StoryTitle);
        }*/
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
