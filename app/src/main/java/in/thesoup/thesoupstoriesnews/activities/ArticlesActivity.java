package in.thesoup.thesoupstoriesnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.adapters.ArticlesAdapter;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.ArticlesMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoupstoriesnews.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 08-09-2017.
 */

public class ArticlesActivity extends AppCompatActivity {

    private TextView mtextView;

    private List<ArticlesMain> mArticles;
    private List<ArticlesMainHome> oArticles;
    private List<Articles> nArticles;
    private RecyclerView ArticlesView;
    private ArticlesAdapter mArticlesAdapter;
    private SharedPreferences pref;
    private String StoryTitle,SubstoryID,storyColor;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_articleactivity);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

        pref= PreferenceManager.getDefaultSharedPreferences(this);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        HashMap<String,Object> pparams = new HashMap<>();
        pparams.put("screen_name", "sources_screen");
        pparams.put("category", "screen_view"); //(only if possible)
        cleverTap.event.push("viewed_screen_sources", pparams);




        LinearLayout crossmarklayout = (LinearLayout)findViewById(R.id.crossmark_layout);

        crossmarklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add position of the card in home page, details page
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "articles_screen");
                nparams.putString("category", "tap"); //(only if possible)
                mFirebaseAnalytics.logEvent("tap_modal_close", nparams);

                HashMap<String,Object> pparams = new HashMap<>();
                pparams.put("screen_name", "articles_screen");
                pparams.put("category", "tap"); //(only if possible)
                cleverTap.event.push("tap_modal_close", pparams);



                finish();
            }
        });

        mtextView = (TextView)findViewById(R.id.number_of_articles);
        Intent in = getIntent();

        if(getIntent().getExtras().getSerializable("ARTICLELISTHOME")!=null){
            oArticles = (List<ArticlesMainHome>)in.getSerializableExtra("ARTICLELISTHOME");
        }

        if(getIntent().getExtras().getSerializable("ARTICLELIST")!=null){
            mArticles =(List<ArticlesMain>) in.getSerializableExtra("ARTICLELIST");
        }

        if(getIntent().getExtras().getSerializable("LISTARTICLES")!=null){
            nArticles =(List<Articles>) in.getSerializableExtra("LISTARTICLES");
        }



        if(oArticles!=null){
            int articlesize = oArticles.size();
            mtextView.setText(String.valueOf(articlesize) + " Articles");
        }

        if(mArticles!=null) {
            int articlesize = mArticles.size();
            mtextView.setText(String.valueOf(articlesize) + " Articles");

        }else if(nArticles!=null){
            int articlesize = nArticles.size();
            mtextView.setText(String.valueOf(articlesize) + " Articles");
        }


        Bundle extras = getIntent().getExtras();
        SubstoryID = extras.getString("substory_id");
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

        mArticlesAdapter = new ArticlesAdapter(mArticles,nArticles,oArticles,storyColor,SubstoryID,this);

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
