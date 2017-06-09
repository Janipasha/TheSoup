package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import in.thesoup.thesoup.Adapters.FeedFragmentPagerAdapter;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.Fragments.DiscoverFragment;
import in.thesoup.thesoup.Fragments.MyFeedFragment;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private FeedFragmentPagerAdapter adapter;
    private ImageView mimageView;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PrefUtil prefUtil = new PrefUtil(this);

        prefUtil.saveTotalRefresh("0");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarfilter);
        setSupportActionBar(toolbar);


        mTextView = (TextView)findViewById(R.id.filter);
        mimageView = (ImageView)findViewById(R.id.filter_img);
        mimageView.setOnClickListener(this);
        mTextView.setOnClickListener(this);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proxima-nova-black.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );



        // Create an adapter that knows which fragment should be shown on each page
         adapter = new FeedFragmentPagerAdapter(getSupportFragmentManager(),this);


        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);



        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text);
                tab.setText(adapter.getPageTitle(i));
            }





    }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("lol", "Firebase reg id: " + regId);
    }



    public Fragment getFragment(int position){

        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager,position);
        if (fragment instanceof DiscoverFragment){
            DiscoverFragment object = (DiscoverFragment) fragment;

            return object;
        }else if(fragment instanceof MyFeedFragment){
            MyFeedFragment object = (MyFeedFragment) fragment;
            return object;

        }

        return null;

    }

    public void changeFontInViewGroup(ViewGroup viewGroup, String fontPath) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (TextView.class.isAssignableFrom(child.getClass())) {
                CalligraphyUtils.applyFontToTextView(child.getContext(), (TextView) child, fontPath);
            } else if (ViewGroup.class.isAssignableFrom(child.getClass())) {
                changeFontInViewGroup((ViewGroup) viewGroup.getChildAt(i), fontPath);
            }
        }
    }


    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MainActivity.this,FilterActivity.class);
        startActivity(intent);

    }
}
