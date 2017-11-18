package in.thesoup.thesoupstoriesnews.activities;

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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

import in.thesoup.thesoupstoriesnews.adapters.FragmentPagerNavigation;
import in.thesoup.thesoupstoriesnews.fragments.DiscoverFragmentMain;
import in.thesoup.thesoupstoriesnews.fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment;
import in.thesoup.thesoupstoriesnews.fragments.HomeFragment1;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtil;
import in.thesoup.thesoupstoriesnews.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static in.thesoup.thesoupstoriesnews.R.id.follow;

/**
 * Created by Jani on 02-09-2017.
 */

public class NavigationActivity extends FragmentActivity implements FollowingFragment.Badgeonfilter {

    private CustomViewPager viewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FragmentPagerNavigation fragmentPagerNavigation;
    private TabLayout tabLayout;
    boolean doubleBackToExitPressedOnce = false;
    private CleverTapAPI cleverTap = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#f2f2f2"));
        }

        updateProfile();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Log.e("firebase",FirebaseInstanceId.getInstance().getToken());
        PrefUtil prefUtil = new PrefUtil(this);
        mFirebaseAnalytics.setUserProperty("user_name",prefUtil.getFirstname()+" "+prefUtil.getLastname());
        mFirebaseAnalytics.setUserProperty("user_email",prefUtil.getEmail());
        setContentView(R.layout.activity_mainversion);


        String fragmentPosition = "0";
        Intent startingIntent = getIntent();
        if (getIntent().getStringExtra("fragmentPosition") != null && !getIntent().getStringExtra("fragmentPosition").isEmpty()) {
            fragmentPosition = startingIntent.getStringExtra("fragmentPosition");
        }else if (getIntent().getAction()!=null){
                fragmentPosition = "1";

        }

        viewPager = (CustomViewPager) findViewById(R.id.viewpager_main);
        viewPager.setOffscreenPageLimit(2);


        fragmentPagerNavigation = new FragmentPagerNavigation(getSupportFragmentManager(), this);

        viewPager.setAdapter(fragmentPagerNavigation);
        viewPager.setPagingEnabled(false);


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
                        "fonts/montserrat-light.ttf");
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
                    imageView1.setImageResource(R.drawable.icons8_home_page_filled);
                    imageView1.setVisibility(View.VISIBLE);
                    // int color = Color.parseColor("#b3ffffff");
                    //  imageView1.setColorFilter(R.drawable.tab_icon_color);
                    /*
                    textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.icon_attention, 0, 0);
                    tabLayout.getTabAt(1).setCustomView(textView);*/

                } else if (i == 1) {
                    imageView1.setImageResource(R.drawable.icons8_report_card_filled);
                    imageView1.setVisibility(View.VISIBLE);
                }
            }
        }

        if (fragmentPosition != null && !fragmentPosition.isEmpty()) {
            viewPager.setCurrentItem(Integer.valueOf(fragmentPosition));
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        Bundle mparams = new Bundle();
                        mparams.putString("screen_name", "home_screen");
                        mparams.putString("category", "tap"); //(only if possible)
                        mFirebaseAnalytics.logEvent("tap_home", mparams);

                        HashMap<String,Object> clickaction = new HashMap<>();
                        clickaction.put("screen_name", "home_screen");
                        clickaction.put("category", "tap"); //(only if possible)
                        cleverTap.event.push("tap_home",clickaction);
                        return;
                    case 1:
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "following_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        mFirebaseAnalytics.logEvent("tap_following", nparams);

                        HashMap<String,Object> nclickaction = new HashMap<>();
                        nclickaction.put("screen_name", "following_screen");
                        nclickaction.put("category", "tap"); //(only if possible)
                        cleverTap.event.push("tap_following",nclickaction);


                        return;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, tab.getPosition());
                if (fragment instanceof HomeFragment1) {
                    HomeFragment1 object = (HomeFragment1) fragment;
                    object.goToTop();

                } else if (fragment instanceof FollowingFragment) {
                    FollowingFragment object = (FollowingFragment) fragment;
                    object.goToTop();

                } else if (fragment instanceof DiscoverFragmentMain) {
                    DiscoverFragmentMain object = (DiscoverFragmentMain) fragment;
                    object.goToTop();
                }
            }
        });



    }




    public void gotoFragment(int i) {
        viewPager.setCurrentItem(i);
    }

    @Override
    public void onBackPressed() {

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "home_screen");
        mparams.putString("category", "tap"); //(only if possible)
        mFirebaseAnalytics.logEvent("tap_back", mparams);

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "home_screen");
        nparams.put("category", "tap"); //(only if possible)
        cleverTap.event.push("tap_back", nparams);

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public Fragment getFragment(int position) {

        Fragment fragment = (Fragment) viewPager.getAdapter().instantiateItem(viewPager, position);
        if (fragment instanceof HomeFragment1) {
            HomeFragment1 object = (HomeFragment1) fragment;

            return object;
        } else if (fragment instanceof FollowingFragment) {
            FollowingFragment object = (FollowingFragment) fragment;
            return object;

        } else if (fragment instanceof DiscoverFragmentMain) {
            DiscoverFragmentMain object = (DiscoverFragmentMain) fragment;
            return object;
        }

        return null;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void NumberofUnread(String num_unread) {
        if (num_unread != null && !num_unread.isEmpty()) {

            if (num_unread.equals("0")) {

                    View view = tabLayout.getTabAt(1).getCustomView();
                    TextView textview = (TextView) view.findViewById(R.id.badge);
                    textview.setVisibility(View.GONE);


            } else {

                View view = tabLayout.getTabAt(1).getCustomView();
                TextView textview = (TextView) view.findViewById(R.id.badge);
                textview.setVisibility(View.VISIBLE);

                Typeface face = Typeface.createFromAsset(this.getAssets(),
                        "fonts/montserrat-bold.ttf");
                textview.setTypeface(face);
                textview.setText(num_unread);

            }

        }

    }

    public void updateProfile(){

        PrefUtil prefUtil = new PrefUtil(this);

        HashMap<String, Object> profileUpdate = new HashMap<>();


        profileUpdate.put("Name", prefUtil.getFirstname());                  // String
        profileUpdate.put("Identity", prefUtil.getId());                    // String or number
        profileUpdate.put("Email", prefUtil.getEmail());               // Email address of the user
        profileUpdate.put("Gender", prefUtil.getGender());                           // Can be either M or F
        profileUpdate.put("Age", prefUtil.getAgeMin());                               // Not required if DOB is set
        profileUpdate.put("Photo", prefUtil.getPictureUrl());    // URL to the Image

        cleverTap.profile.push(profileUpdate);
    }
}



