package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsSplash;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.UTILS;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PagerActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private ViewPager mViewPager;
    private FirebaseAnalytics mFirebaseAnalytics;
    Button mNextBtn;
    Button mBackBtn, mFinishBtn;

    ImageView zero, one, two;
    ImageView[] indicators;


    private UTILS Utils = new UTILS();

    private SharedPreferences pref ;

    static final String TAG = "PagerActivity";

    int page = 0;   //  to track page position

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            window.setStatusBarColor(Color.parseColor("#ffffff"));
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


      if(pref.getString(SoupContract.AUTH_TOKEN,null)!=null&&!pref.getString(SoupContract.AUTH_TOKEN,null).isEmpty()){

          HashMap<String,String> params = new HashMap<>();
          params.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
          params.put("purpose","verify");
          NetworkUtilsSplash verifyauth = new NetworkUtilsSplash(this,params);
          verifyauth.verifyAuthToken();

      } else{

          CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                  .setDefaultFontPath("fonts/proxima-nova-black.otf")
                  .setFontAttrId(R.attr.fontPath)
                  .build()
          );

          setContentView(R.layout.activity_on_boarding2);

          mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

          mNextBtn = (Button) findViewById(R.id.intro_btn_next);

          mBackBtn = (Button) findViewById(R.id.intro_btn_back);
          mBackBtn.setVisibility(View.GONE);
          mFinishBtn = (Button) findViewById(R.id.intro_btn_finish);

          zero = (ImageView) findViewById(R.id.intro_indicator_0);
          one = (ImageView) findViewById(R.id.intro_indicator_1);
          two = (ImageView) findViewById(R.id.intro_indicator_2);


          indicators = new ImageView[]{zero, one, two};

          // Set up the ViewPager with the sections adapter.
          mViewPager = (ViewPager) findViewById(R.id.container);
          mViewPager.setAdapter(mSectionsPagerAdapter);

          mViewPager.setCurrentItem(page);
          updateIndicators(page);



          mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
              @Override
              public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



              }

              @Override
              public void onPageSelected(int position) {

                  mBackBtn.setVisibility(View.GONE);

                  page = position;

                  if(page>0){
                      mBackBtn.setVisibility(View.VISIBLE);
                  }

                  updateIndicators(page);



                  mNextBtn.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                  mFinishBtn.setVisibility(position == 2 ? View.VISIBLE : View.GONE);


              }

              @Override
              public void onPageScrollStateChanged(int state) {

              }
          });

          mNextBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  page += 1;
                  Bundle mparams = new Bundle();
                  mparams.putString("label", "onboarding_screen");
                  mparams.putString("category","tap");
                  mparams.putString("position_scn_onboarding",String.valueOf(page));//(only if possible)
                  mFirebaseAnalytics.logEvent("tap_next",mparams);
                  mViewPager.setCurrentItem(page, true);
              }
          });

          mBackBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  if(page>0){
                      Bundle mparams = new Bundle();
                      mparams.putString("label", "onboarding_screen");
                      mparams.putString("category","tap");
                      mparams.putString("position_scn_onboarding",String.valueOf(page+1));//(only if possible)
                      mFirebaseAnalytics.logEvent("tap_back",mparams);
                      page=page-1;
                      mViewPager.setCurrentItem(page,true);
                  }


              }
          });

          mFinishBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(PagerActivity.this,LoginActivity.class);
                  startActivity(intent);
                  finish();

                  //  update 1st time pref
                  Utils.saveSharedSetting(PagerActivity.this, "FirstTime", "false");

              }
          });


      }

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }

    public void checkauth(String AuthToken) {

        if(AuthToken!=null&&!AuthToken.isEmpty()){
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.AUTH_TOKEN,AuthToken);
            edit.apply();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{

            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.AUTH_TOKEN,AuthToken);
            edit.apply();

            Intent intent = new Intent(PagerActivity.this,LoginActivity.class);
            intent.putExtra("toast","showtoast");
            startActivity(intent);
            finish();

        }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_HEADING = "heading";
        private static final String ARG_HEADING1 = "heading1";
        private static final String ARG_CONTENT = "content";
        private static final String ARG_CONTENT1 ="content1";
        private static final String ARG_DIFFERENTIATOR = "differentiator";
        private static final String ARG_DRAWABLE = "drawable";
        private Button buttn;
        private FirebaseAnalytics mFirebaseAnalytics;

        ImageView img;

        //int[] bgs = new int[]{R.drawable.screen1, R.drawable.screen2, R.drawable.screen3};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String Heading,String Heading2,String Content,String Content1,String differentiater,int drawable) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_HEADING, Heading);
            args.putString( ARG_HEADING1,Heading2);
            args.putString(ARG_CONTENT,Content);
            args.putString(ARG_CONTENT1,Content1);
            args.putString(ARG_DIFFERENTIATOR,differentiater);
            args.putInt(ARG_DRAWABLE,drawable);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            View rootView = inflater.inflate(R.layout.onboardingfragment, container, false);
            ImageView imageView = (ImageView)rootView.findViewById(R.id.onboarding_icon);
            TextView textView = (TextView) rootView.findViewById(R.id.onboardinghero);
            TextView textView1 =(TextView) rootView.findViewById(R.id.onboardingcontent);
            TextView textView2 = (TextView)rootView.findViewById(R.id.onboardinghero2);
            TextView textView3 = (TextView)rootView.findViewById(R.id.onboardingcontent1);
            textView.setText(getArguments().getString(ARG_HEADING));
            textView2.setText(getArguments().getString(ARG_HEADING1));
            textView1.setText(getArguments().getString(ARG_CONTENT));
            textView3.setText(getArguments().getString(ARG_CONTENT1));
            imageView.setImageResource(getArguments().getInt(ARG_DRAWABLE));

           // buttn = (Button)rootView.findViewById(R.id.letsgobuttn);
            //buttn.setVisibility(View.GONE);

            String screenId = getArguments().getString(ARG_DIFFERENTIATOR);

            if(screenId.equals("1")){
               // buttn.setVisibility(View.VISIBLE);

            }

          /*  buttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle mparams = new Bundle();
                    mparams.putString("label", "onboarding_screen");
                    mparams.putString("category","tap");
                    mFirebaseAnalytics.logEvent("tap_get_started",mparams);


                            Intent intent = new Intent(getActivity(),LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();





                }
            });*/



            return rootView;
        }






    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position) {
                case 0:
                    return PlaceholderFragment.newInstance(getString(R.string.onboarding_heading1),getString(R.string.onboarding_heading11),getString(R.string.onboarding_content1),getString(R.string.onboarding_content11),"0", R.drawable.globe);
                case 1:
                    return PlaceholderFragment.newInstance(getString(R.string.onboarding_heading2),getString(R.string.onboarding_heading22),getString(R.string.onboarding_content2),getString(R.string.onboarding_content22),"0",R.drawable.icon_notification);
                case 2:
                    return PlaceholderFragment.newInstance(getString(R.string.onboarding_heading3),getString(R.string.onboarding_heading33),getString(R.string.onboarding_content3),getString(R.string.onboarding_content33),"1",R.drawable.icon_journey);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }

    }

    @Override
    public void onBackPressed() {
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "onboarding_screen");
        mparams.putString("category", "tap"); //(only if possible)
        mFirebaseAnalytics.logEvent("tap_back", mparams);
        super.onBackPressed();
    }


}

