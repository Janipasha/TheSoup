package in.thesoupstoriesnews.thesoup.Activities;

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
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoupstoriesnews.thesoup.Adapters.FilterAdapter;
import in.thesoupstoriesnews.thesoup.Adapters.FilterAdapterVersion;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters1.Filters;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsSplash;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtilFilter;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import in.thesoupstoriesnews.thesoup.gsonConversion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.R.attr.filter;


public class FilterActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FilterAdapterVersion mAdapter;
    private List<Filters> getFilters;
    private RecyclerView recyclerView;
    private Button apply, cancel;
    private PrefUtilFilter prefUtilFilter;
    private int count=0;
    private String resetfilter= "0";
    private String firstfiltername ="";
    private ImageButton filterback;
	//CVIPUL Analytics
	private FirebaseAnalytics mFirebaseAnalytics;
	//End Analytics

    private Map<String, String> IDmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		
		//CVIPUL Analytics
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		//End Analytics

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);


            window.setStatusBarColor(ContextCompat.getColor(this,R.color.filter_grey));
        }

        setContentView(R.layout.filterfragment1);

        if(getIntent().getStringExtra("resetfilter")!=null){
            resetfilter = getIntent().getStringExtra("resetfilter");
        }

        apply = (Button) findViewById(R.id.apply);
       // cancel = (Button) findViewById(R.id.cancel);
       // filterback = (ImageButton)findViewById(R.id.backfilter);

        IDmap = new HashMap<>();

        prefUtilFilter = new PrefUtilFilter(this);

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



        for(int i=0;i<getFilters.size();i++){
            if(getFilters.get(i).getId()!=null&&!getFilters.get(i).getId().isEmpty()){
                count = count+1;
            }
        }




            update(getFilters);


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new FilterAdapterVersion(getFilters, this,resetfilter);
        recyclerView.setAdapter(mAdapter);
		
		// CVIPUL Analytics
		// TODO : Verify view Filters screen
		Bundle params = new Bundle();
		params.putString("screen_name", "filters_screen");
		params.putString("category", "screen_view");
		mFirebaseAnalytics.logEvent("viewed_screen_filters",params);
		// End Analytics

    }

    private void update(List<Filters> getFilters) {

        for(int i=0;i<getFilters.size();i++){

            if(getFilters.get(i).getId()!=null&&!getFilters.get(i).getId().isEmpty()){

                String Id = getFilters.get(i).getId();

                if(prefUtilFilter.getStatusofID(Id)!=null&&!prefUtilFilter.getStatusofID(Id).isEmpty()){
                    getFilters.get(i).changeStatus(prefUtilFilter.getStatusofID(Id));
                    Log.d("filter"+Id," :"+getFilters.get(i).getStatus());
                }
            }

        }
    }

    public void onClick(View v){
		// CVIPUL Analytics
		// Moved out of if statement since the info is needed in else also
		int presentFilterCount = 0;
        String filters = "";
        for (int n = 0; n <getFilters.size(); n++) {

                if (IDmap.get(getFilters.get(n).getId())!= null && !IDmap.get(getFilters.get(n).getId()).isEmpty()) {
                    //prefUtilFilter.IDstatus(String.valueOf(n), IDmap.get(String.valueOf(n)));

                      prefUtilFilter.SaveIDstatus(getFilters,n,IDmap.get(getFilters.get(n).getId()));
                }

                if(getFilters.get(n).getStatus().equals("1")){
                    presentFilterCount++;
                    if(firstfiltername.isEmpty()){
                        firstfiltername = getFilters.get(n).getName();
                    }
                    filters= filters+getFilters.get(n).getId()+",";
                }

        }

        if(filters!=null&&!filters.isEmpty()){
            filters = filters.substring(0,filters.length()-1);
        }
		// End Analytics
		
        if(v==apply){

            SharedPreferences.Editor edit = pref.edit();
            edit.putString("filters",filters);
            edit.putString("filter_count",String.valueOf(presentFilterCount));
            edit.apply();
			
			// CVIPUL Analytics
			// TODO : Verify Tap to Remove on Filters screen
			Bundle params = new Bundle();
			params.putString("screen_name", "filters_screen");
			params.putString("category", "tap");
			params.putString("count_filters_selected", String.valueOf(presentFilterCount));
			
			mFirebaseAnalytics.logEvent("tap_filters_accept",params);
			// End Analytics


            if(resetfilter.equals("0")) {
                if (presentFilterCount < 3) {
                    Toast.makeText(this, "Please select atleast 3 categories", Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String> nparams = new HashMap<>();
                    nparams.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    nparams.put(SoupContract.CATEGORIES, filters);

                    NetworkUtilsSplash setfilter = new NetworkUtilsSplash(this, nparams);
                    setfilter.setInterestedCategories();

                    //TODO: add filters , open navigationactivity with home

                    Intent intent = new Intent(FilterActivity.this, NavigationActivity.class);
                    prefUtilFilter.SaveFilterListSize(presentFilterCount);
                    edit.putString("firstfiltername",firstfiltername);
                    edit.apply();
                    finish();
                    startActivity(intent);
                }

            } else if(resetfilter.equals("1")){

                if (presentFilterCount < 1) {
                    Toast.makeText(this, "Please select atleast 1 category", Toast.LENGTH_SHORT).show();
                } else {
                    //TODO: add filters and open discoverfragment

                    Intent intent = new Intent(FilterActivity.this, NavigationActivity.class);
                   prefUtilFilter.SaveFirstFiltername(firstfiltername);
                    prefUtilFilter.SaveFilterListSize(presentFilterCount);
                    intent.putExtra("fragmentPosition","0");
                    edit.apply();
                    finish();
                    startActivity(intent);
                }

            }




        }

       /* if(v==cancel){
			// CVIPUL Analytics
			// TODO : Verify Tap to Remove on Filters screen
			Bundle params = new Bundle();
			params.putString("screen_name", "filters_screen");
			params.putString("category", "tap");
			params.putString("count_filters_selected", String.valueOf(presentFilterCount));
			
			mFirebaseAnalytics.logEvent("tap_filters_cancel",params);
			// End Analytics
			
            finish();
        }*/

       /* if(v==filterback){
            finish();
        }*/
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void changeIDmapValue(String ID, String value) {

        IDmap.put(ID,value);
    }

    @Override
    public void onBackPressed() {
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "filter_screen");
        mparams.putString("category", "tap"); //(only if possible)
        mFirebaseAnalytics.logEvent("tap_back", mparams);
        super.onBackPressed();
    }

    public void goTomainActivity() {

        Intent intent = new Intent(FilterActivity.this,NavigationActivity.class);
        finish();
        startActivity(intent);

    }
}

