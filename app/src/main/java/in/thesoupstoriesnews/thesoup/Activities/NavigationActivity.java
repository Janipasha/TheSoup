package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import in.thesoupstoriesnews.thesoup.Adapters.FragmentPagerNavigation;
import in.thesoupstoriesnews.thesoup.Fragments.DiscoverFragment;
import in.thesoupstoriesnews.thesoup.Fragments.DiscoverFragmentMain;
import in.thesoupstoriesnews.thesoup.Fragments.FollowingFragment;
import in.thesoupstoriesnews.thesoup.Fragments.HomeFragment;
import in.thesoupstoriesnews.thesoup.Fragments.MyFeedFragment;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Jani on 02-09-2017.
 */

public class NavigationActivity extends FragmentActivity {

    private CustomViewPager viewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FragmentPagerNavigation fragmentPagerNavigation;
    private TabLayout tabLayout;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f2f2f2"));
        }


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_mainversion);


        String fragmentPosition = "1";
        Intent startingIntent = getIntent();
        if (getIntent().getStringExtra("fragmentPosition") != null && !getIntent().getStringExtra("fragmentPosition").isEmpty()) {
            fragmentPosition = startingIntent.getStringExtra("fragmentPosition");
        }

        viewPager = (CustomViewPager) findViewById(R.id.viewpager_main);
        viewPager.setOffscreenPageLimit(3);


        fragmentPagerNavigation = new FragmentPagerNavigation(getSupportFragmentManager(), this);

        viewPager.setAdapter(fragmentPagerNavigation);
        viewPager.setPagingEnabled(false);


        PrefUtil prefUtil = new PrefUtil(this);
        prefUtil.saveTotalRefresh("0");


        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorHeight(0);
        tabLayout.setupWithViewPager(viewPager);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        for (int i = 0; i < fragmentPagerNavigation.getCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(R.layout.tab_text_new);
                ImageView imageView = (ImageView) tab.getCustomView().findViewById(R.id.globe1);
                ImageView imageView1 = (ImageView) tab.getCustomView().findViewById(R.id.notificationbell);
                imageView.setVisibility(View.GONE);
                imageView1.setVisibility(View.GONE);

                TextView textView = (TextView) tab.getCustomView().findViewById(R.id.tabtext);

                textView.setText(fragmentPagerNavigation.getPageTitle(i));
                Typeface face = Typeface.createFromAsset(this.getAssets(),
                        "fonts/montserrat-bold.ttf");
                textView.setTypeface(face);

                ColorStateList colors;
                if (Build.VERSION.SDK_INT >= 23) {
                    colors = getResources().getColorStateList(R.color.tab_icon_color, getTheme());
                } else {
                    colors = getResources().getColorStateList(R.color.tab_icon_color);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    DrawableCompat.setTintList(getDrawable(R.drawable.globe), colors);
                    DrawableCompat.setTintList(getDrawable(R.drawable.icons8_home_page_filled), colors);
                    DrawableCompat.setTintList(getDrawable(R.drawable.icons8_report_card_filled), colors);
                } else {
                    DrawableCompat.setTintList(getResources().getDrawable(R.drawable.globe), colors);
                    DrawableCompat.setTintList(getResources().getDrawable(R.drawable.icons8_home_page_filled), colors);
                    DrawableCompat.setTintList(getResources().getDrawable(R.drawable.icons8_report_card_filled), colors);
                }


                if (i == 0) {
                    imageView.setVisibility(View.VISIBLE);

                } else if (i == 1) {
                    imageView1.setImageResource(R.drawable.icons8_home_page_filled);
                    imageView1.setVisibility(View.VISIBLE);
                    // int color = Color.parseColor("#b3ffffff");
                    //  imageView1.setColorFilter(R.drawable.tab_icon_color);
                    /*
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_attention, 0, 0);
                    tabLayout.getTabAt(1).setCustomView(textView);*/

                } else if (i == 2) {
                    imageView1.setImageResource(R.drawable.icons8_report_card_filled);
                    imageView1.setVisibility(View.VISIBLE);
                }
            }
        }

        if (fragmentPosition != null && !fragmentPosition.isEmpty()) {
            viewPager.setCurrentItem(Integer.valueOf(fragmentPosition));
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        Bundle mparams = new Bundle();
                        mparams.putString("screen_name", "discover_screen");
                        mparams.putString("category", "tap"); //(only if possible)
                        mFirebaseAnalytics.logEvent("tap_discover", mparams);
                    case 1:
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        mFirebaseAnalytics.logEvent("tap_notifications", nparams);
                    case 2:
                        Bundle pparams = new Bundle();
                        pparams.putString("screen_name", "following_screen");
                        pparams.putString("category", "tap"); //(only if possible)
                        mFirebaseAnalytics.logEvent("tap_notifications", pparams);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }




    public void gotoFragment(int i){
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onBackPressed() {

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "home_screen");
        mparams.putString("category", "tap"); //(only if possible)
        mFirebaseAnalytics.logEvent("tap_back", mparams);

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public Fragment getFragment(int position) {

        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, position);
        if (fragment instanceof HomeFragment) {
            HomeFragment object = (HomeFragment) fragment;

            return object;
        } else if (fragment instanceof FollowingFragment) {
            FollowingFragment object = (FollowingFragment) fragment;
            return object;

        }else if(fragment instanceof DiscoverFragmentMain){
            DiscoverFragmentMain object = (DiscoverFragmentMain)fragment;
            return object;
        }

        return null;

    }

    }

