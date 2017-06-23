package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.android.volley.VolleyLog.d;
import static in.thesoup.thesoup.R.id.view;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,MyFeedFragment.Badgeonfilter {

    private ViewPager viewPager;
    private FeedFragmentPagerAdapter adapter;
    private ImageView mimageView,tickImage;
    private TextView mTextView;
    private RelativeLayout mlinearlayout;
    private SharedPreferences pref;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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



        pref = PreferenceManager.getDefaultSharedPreferences(this);
        PrefUtil prefUtil = new PrefUtil(this);

        prefUtil.saveTotalRefresh("0");

       // Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarfilter);
        //setSupportActionBar(toolbar);


        mTextView = (TextView)findViewById(R.id.filter);
        mimageView = (ImageView)findViewById(R.id.filter_img);
        tickImage = (ImageView)findViewById(R.id.filter_tick);
        tickImage.setVisibility(View.GONE);
        mimageView.setOnClickListener(this);
        mTextView.setOnClickListener(this);
        mlinearlayout = (RelativeLayout)findViewById(R.id.filtertab);
        mlinearlayout.setOnClickListener(this);

        int filter_count = 0;


        for(int i=1;i<=14;i++) {
            String Id = String.valueOf(i);

            if (pref.getString(Id, null) != null && !pref.getString(Id, null).isEmpty()) {
                if(pref.getString(Id,null).equals("1")){
                    filter_count = filter_count + 1;
                }
            }else{
                filter_count=filter_count+1;
            }
        }

        Log.d("filter_count",':'+String.valueOf(filter_count));

        if(filter_count>0&&filter_count<14){

            tickImage.setVisibility(View.VISIBLE);
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


       /* tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        TabLayout.Tab tab = tabLayout.getTabAt(0);
        View view = LayoutInflater.from(this).inflate(R.layout.tab_text,null);
       // TextView tabtext = (TextView)view.findViewById(R.id.tabtext);
        //tabtext.setText(adapter.getPageTitle(0).toString());
        tab.setCustomView(R.layout.tab_text);

        TabLayout.Tab tab1 = tabLayout.getTabAt(1);
        View view1 = LayoutInflater.from(this).inflate(R.layout.tab_text,null);
        //TextView tabtext1 = (TextView)view1.findViewById(R.id.tabtext);
        //tabtext1.setText(adapter.getPageTitle(1).toString());
        tab1.setCustomView(R.layout.tab_text);

        Log.d("tab2",adapter.getPageTitle(0).toString());



        /*for (int i = 0; i < adapter.getCount(); i++) {


            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (i==0) {



            } else if(i==1){

                View view = LayoutInflater.from(this).inflate(R.layout.tab_text,null);
                TextView badge = (TextView)view.findViewById(R.id.badge);
                badge.setText(num_unread);
                badge.setVisibility(View.VISIBLE);
                TextView tabtext = (TextView)view.findViewById(R.id.tabtext);
                tabtext.setText(adapter.getPageTitle(i).toString());
                tab.setCustomView(R.layout.tab_text);


            }


        }*/


    }
}
