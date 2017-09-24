package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.Serializable;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Adapters.ArticlesAdapter;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.ArticlesMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 08-09-2017.
 */

public class ArticlesActivity extends AppCompatActivity {

    private TextView mtextView;

    private List<ArticlesMain> mArticles;
    private List<Articles> nArticles;
    private RecyclerView ArticlesView;
    private ArticlesAdapter mArticlesAdapter;
    private SharedPreferences pref;
    private String StoryTitle,StoryId,storyColor;
    private FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_articleactivity);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        pref= PreferenceManager.getDefaultSharedPreferences(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );




        LinearLayout crossmarklayout = (LinearLayout)findViewById(R.id.crossmark_layout);

        crossmarklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add position of the card in home page, details page
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "collection_screen");
                nparams.putString("category", "tap"); //(only if possible)
                mFirebaseAnalytics.logEvent("tap_modal_close", nparams);

                finish();
            }
        });

        mtextView = (TextView)findViewById(R.id.number_of_articles);
        Intent in = getIntent();

        if(getIntent().getExtras().getSerializable("ARTICLELIST")!=null){
            mArticles =(List<ArticlesMain>) in.getSerializableExtra("ARTICLELIST");
        }

        if(getIntent().getExtras().getSerializable("LISTARTICLES")!=null){
            nArticles =(List<Articles>) in.getSerializableExtra("LISTARTICLES");
        }





        if(mArticles!=null) {
            int articlesize = mArticles.size();
            mtextView.setText(String.valueOf(articlesize) + " Articles");

        }else if(nArticles!=null){
            int articlesize = nArticles.size();
            mtextView.setText(String.valueOf(articlesize) + " Articles");
        }


        Bundle extras = getIntent().getExtras();
        //StoryId = extras.getString("story_id");
       // StoryTitle = extras.getString("StoryTitle");
        storyColor = extras.getString("story_color");

       // Log.d("storyid,story",StoryId);
       // Log.d("storyid,story",StoryTitle);

        //gsonConversion articlelist = new gsonConversion();
        //articlelist.ArticleJson(mString,mArticles);


        ArticlesView = (RecyclerView) findViewById(R.id.article_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        ArticlesView.setLayoutManager(layoutManager);

        ArticlesView.setHasFixedSize(true);

        mArticlesAdapter = new ArticlesAdapter(mArticles,nArticles,storyColor,this);

        ArticlesView.setAdapter(mArticlesAdapter);





    }


    @Override
    protected void onStart() {
        super.onStart();

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
