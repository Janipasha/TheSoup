package in.thesoup.thesoupstoriesnews.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoupstoriesnews.adapters.FilterAdapterVersion;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters1.Filters;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsSplash;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter2;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.gsonConversion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class FilterActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FilterAdapterVersion mAdapter;
    private List<Filters> getFilters;
    private RecyclerView recyclerView;
    private Button apply, cancel,next;
    private PrefUtilFilter prefUtilFilter;
    private PrefUtilFilter2 prefUtilFilterManage;
    private int count = 0;
    private String resetfilter = "";
    private String firstfiltername = "";
    private ImageButton filterback;
    //CVIPUL Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;
    private LinearLayout applybackbutton;
    //End Analytics

    private Map<String, String> IDmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            window.setStatusBarColor(ContextCompat.getColor(this, R.color.filter_grey));
        }

        setContentView(R.layout.filterfragment1);

        if (getIntent().getStringExtra("resetfilter") != null) {
            resetfilter = getIntent().getStringExtra("resetfilter");
        }

        apply = (Button) findViewById(R.id.apply);
        next = (Button) findViewById(R.id.next);
        cancel = (Button) findViewById(R.id.cancel);
        applybackbutton = (LinearLayout) findViewById(R.id.applycancelbutton);
        // filterback = (ImageButton)findViewById(R.id.backfilter);

        if(resetfilter.isEmpty()){
            applybackbutton.setVisibility(View.GONE);
        }else if(resetfilter.equals("0")||resetfilter.equals("1")){
            next.setVisibility(View.GONE);
        }

        IDmap = new HashMap<>();

        prefUtilFilter = new PrefUtilFilter(this);
        prefUtilFilterManage = new PrefUtilFilter2(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences preffilter = this.getSharedPreferences(SoupContract.FILTERPREF, 0);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/montserrat-bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        String filterJson = preffilter.getString(SoupContract.FILTERJSON, null);
        Log.d("filterJSON", filterJson);
        gsonConversion getfilterObject = new gsonConversion();
        getFilters = getfilterObject.getFilters(filterJson);


        for (int i = 0; i < getFilters.size(); i++) {
            if (getFilters.get(i).getId() != null && !getFilters.get(i).getId().isEmpty()) {
                count = count + 1;
            }
        }

      //  prefUtilFilter.SaveTotalFilterCount(String.valueOf(getFilters.size()));

        update(getFilters);



        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new FilterAdapterVersion(getFilters, this, resetfilter);
        recyclerView.setAdapter(mAdapter);

        // CVIPUL Analytics
        // TODO : Verify view Filters screen
        Bundle params = new Bundle();
        params.putString("screen_name", "filters_screen");
        params.putString("category", "screen_view");
        mFirebaseAnalytics.logEvent("viewed_screen_managehome", params);

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "filters_screen");
        nparams.put("category", "screen_view");
        if(resetfilter.isEmpty()){
            nparams.put("from","onboarding");
        }else if(resetfilter.equals("0")){
            nparams.put("from","home");
        }else if(resetfilter.equals("1")){
            nparams.put("from","discover");
        }
        cleverTap.event.push("viewed_screen_managehome", nparams);

        // End Analytics

    }

    private void update(List<Filters> getFilters) {

        for (int i = 0; i < getFilters.size(); i++) {

            if (getFilters.get(i).getId() != null && !getFilters.get(i).getId().isEmpty()) {

                String Id = getFilters.get(i).getId();

                if (resetfilter.equals("0")) {
                    if (prefUtilFilterManage.getStatusofID(Id) != null && !prefUtilFilterManage.getStatusofID(Id).isEmpty()) {
                        getFilters.get(i).changeStatus(prefUtilFilterManage.getStatusofID(Id));
                        Log.d("filter" + Id, " :" + getFilters.get(i).getStatus());
                    }

                } else if (resetfilter.equals("1")) {
                    if (prefUtilFilter.getStatusofID(Id) != null && !prefUtilFilter.getStatusofID(Id).isEmpty()) {
                        getFilters.get(i).changeStatus(prefUtilFilter.getStatusofID(Id));
                        Log.d("filter" + Id, " :" + getFilters.get(i).getStatus());
                    }
                }


            }

        }
    }

    public void onClick(View v) {
        // CVIPUL Analytics
        // Moved out of if statement since the info is needed in else also
        int presentFilterCount = 0;
        String filters = "";

        for (int n = 0; n < getFilters.size(); n++) {

            if (IDmap.get(getFilters.get(n).getId()) != null && !IDmap.get(getFilters.get(n).getId()).isEmpty()) {
                //prefUtilFilter.IDstatus(String.valueOf(n), IDmap.get(String.valueOf(n)));
                if (resetfilter.equals("0")||resetfilter.isEmpty()) {
                    prefUtilFilterManage.SaveIDstatus(getFilters, n, IDmap.get(getFilters.get(n).getId()));
                } else if (resetfilter.equals("1")) {
                    prefUtilFilter.SaveIDstatus(getFilters, n, IDmap.get(getFilters.get(n).getId()));
                }

            }

            if (getFilters.get(n).getStatus().equals("1")) {
                presentFilterCount++;
                if (firstfiltername.isEmpty()) {
                    firstfiltername = getFilters.get(n).getName();
                }
                filters = filters + getFilters.get(n).getId() + ",";
            }

        }

        if (filters != null && !filters.isEmpty()) {
            filters = filters.substring(0, filters.length() - 1);
        }
        // End Analytics


        if (v == apply||v==next) {

            // CVIPUL Analytics
            // TODO : Verify Tap to Remove on Filters screen
            Bundle params = new Bundle();
            params.putString("screen_name", "filters_screen");
            params.putString("category", "tap");
            params.putString("count_filters_selected", String.valueOf(presentFilterCount));

            mFirebaseAnalytics.logEvent("tap_filters_accept", params);

            HashMap<String,Object> oparams = new HashMap<>();
            oparams.put("screen_name", "filters_screen");
            oparams.put("category", "tap");
            oparams.put("count_filters_selected", String.valueOf(presentFilterCount));

            if(v==apply){
                cleverTap.event.push("tap_filters_apply", oparams);
            }else if(v==next){
                cleverTap.event.push("tap_filters_next", oparams);
            }



            // End Analytics


            if (resetfilter.equals("0")||resetfilter.isEmpty()) {
                if (presentFilterCount < 3) {
                    Toast.makeText(this, "Please select atleast 3 categories", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("filters", filters);
                    edit.putString("filter_count", String.valueOf(presentFilterCount));
                    edit.apply();

                    HashMap<String, String> nparams = new HashMap<>();
                    nparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    nparams.put(SoupContract.CATEGORIES, filters);

                    NetworkUtilsSplash setfilter = new NetworkUtilsSplash(this, nparams);
                    setfilter.setInterestedCategories();

                    //TODO: add filters , open navigationactivity with home



                    Intent intent = new Intent(FilterActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    prefUtilFilterManage.SaveFilterListSize(presentFilterCount);
                    edit.putString("firstfiltername", firstfiltername);
                    edit.apply();
                    finish();
                    startActivity(intent);
                }

            } else if (resetfilter.equals("1")) {

                if (presentFilterCount < 1) {
                    Toast.makeText(this, "Please select atleast 1 category", Toast.LENGTH_SHORT).show();
                } else {

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("filtersdiscover", filters);
                    edit.putString("filter_count", String.valueOf(presentFilterCount));
                    edit.apply();
                    //TODO: add filters and open discoverfragment

                    Intent intent = new Intent(FilterActivity.this, NavigationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    prefUtilFilter.SaveFirstFiltername(firstfiltername);
                    prefUtilFilter.SaveFilterListSize(presentFilterCount);
                    intent.putExtra("fragmentPosition", "0");
                    edit.apply();
                    finish();
                    startActivity(intent);
                }

            }


        }

        if(v==cancel){

			Bundle params = new Bundle();
			params.putString("screen_name", "filters_screen");
			params.putString("category", "tap");
			params.putString("count_filters_selected", String.valueOf(presentFilterCount));
			
			mFirebaseAnalytics.logEvent("tap_filters_cancel",params);


            HashMap<String,Object> nparams= new HashMap<>();
            nparams.put("screen_name", "filters_screen");
            nparams.put("category", "tap");
            nparams.put("count_filters_selected", String.valueOf(presentFilterCount));

            cleverTap.event.push("tap_filters_cancel",nparams);
			
            finish();
        }


    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void changeIDmapValue(String ID, String value) {

        IDmap.put(ID, value);
    }

    @Override
    public void onBackPressed() {
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "filter_screen");
        mparams.putString("category", "tap"); //(only if possible)
        mFirebaseAnalytics.logEvent("tap_back", mparams);

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "filter_screen");
        nparams.put("category", "tap"); //(only if possible)

        cleverTap.event.push("tap_back", nparams);
        super.onBackPressed();
    }

    public void goTomainActivity() {

        Intent intent = new Intent(FilterActivity.this, NavigationActivity.class);
        finish();
        startActivity(intent);

    }
}

