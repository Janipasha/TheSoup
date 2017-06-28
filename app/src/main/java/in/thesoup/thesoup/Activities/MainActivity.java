package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import in.thesoup.thesoup.Adapters.FeedFragmentPagerAdapter;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.Fragments.DiscoverFragment;
import in.thesoup.thesoup.Fragments.MyFeedFragment;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MyFeedFragment.Badgeonfilter {

    private ViewPager viewPager;
    private FeedFragmentPagerAdapter adapter;
    private ImageView mimageView,tickImage;
    private TextView mTextView;
    private LinearLayout mlinearlayout;
    private SharedPreferences pref;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        pref = PreferenceManager.getDefaultSharedPreferences(this);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_main);

        String fragmentPosition= "";

        Intent startingIntent = getIntent();

        if(startingIntent!=null){
            fragmentPosition = startingIntent.getStringExtra("fragmentPosition");
        }



        PrefUtil prefUtil = new PrefUtil(this);

        prefUtil.saveTotalRefresh("0");

       // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarfilter);
        //setSupportActionBar(toolbar);


        mTextView = (TextView)findViewById(R.id.filter);
        //mimageView = (ImageView)findViewById(R.id.filter_img);
        tickImage = (ImageView)findViewById(R.id.filter_tick);
        tickImage.setVisibility(View.GONE);
       // mimageView.setOnClickListener(this);
        mTextView.setOnClickListener(this);
        mlinearlayout = (LinearLayout)findViewById(R.id.filtertab);
        mlinearlayout.setOnClickListener(this);

        int filter_count = 0;

        if(pref.getString("count",null)!=null && !pref.getString("count",null).isEmpty()) {
            int count = Integer.valueOf(pref.getString("count", null));
            Log.d("filter_count",':'+String.valueOf(count));
            filter_count = getIntent().getIntExtra("filter_count", 0);
            Log.d("filtercount",String.valueOf(filter_count));
            if (filter_count > 0 && filter_count < count) {

                tickImage.setVisibility(View.VISIBLE);
            }

        }

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

        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < adapter.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text);
                tab.setText(adapter.getPageTitle(i));
            }
        }

        if(fragmentPosition!=null&&!fragmentPosition.isEmpty()){
            viewPager.setCurrentItem(Integer.valueOf(fragmentPosition));
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
        String regId = pref.getString(SoupContract.FIREBASEID, null);

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



    @Override
    public void onClick(View view) {

        Intent intent = new Intent(MainActivity.this,FilterActivity.class);
        startActivity(intent);

    }


    @Override
    public void NumberofUnread(String num_unread) {

        if(num_unread!=null||!num_unread.isEmpty()){

       View view = tabLayout.getTabAt(1).getCustomView();
        TextView textview = (TextView) view.findViewById(R.id.badge);
        textview.setText(num_unread);
        textview.setVisibility(View.VISIBLE);
        }


    }
}
