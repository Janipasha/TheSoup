package in.thesoup.thesoup.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.Adapters.FilterAdapter;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.GSONclasses.filters.GetFilters;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtilFilter;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.os.Build.ID;
import static com.facebook.FacebookSdk.getApplicationContext;


public class FilterActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FilterAdapter mAdapter;
    private GetFilters mGetFilters;
    private RecyclerView recyclerView;
    private PrefUtilFilter prefUtilFilter;
    private Button selectall, clear, tick, crossback;
    private int count;
    private Map<String, String> IDmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        setContentView(R.layout.fragmentfilter);
        IDmap = new HashMap<>();
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences preffilter = this.getSharedPreferences(SoupContract.FILTERPREF, 0);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proxima-nova-black.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        //Test to check prefvalue


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectall = (Button) findViewById(R.id.selectall);
        clear = (Button) findViewById(R.id.clear);
        tick = (Button) findViewById(R.id.tick);
        crossback =(Button)findViewById(R.id.crossback);


        prefUtilFilter = new PrefUtilFilter(this);
        String filterJson = preffilter.getString(SoupContract.FILTERJSON, null);
        Log.d("filterJSON", filterJson);
        gsonConversion getfilterObject = new gsonConversion();
        mGetFilters = getfilterObject.filterConversion(filterJson);

        getcount();

        for (int i = 1; i <= count; i++) {
            if (pref.getString(String.valueOf(i), null) != null && !pref.getString(String.valueOf(i), null).isEmpty()) {
                Log.d(String.valueOf(i), ": " + pref.getString(String.valueOf(i), null));
            }
        }

        updateFilters(mGetFilters);



        List<List<Filterdata>> ListFilters = new ArrayList<>();
        ListFilters.addAll(ArrayCreate(mGetFilters));


        Log.d("filterJSON", ListFilters.toString());

        recyclerView = (RecyclerView) findViewById(R.id.filerlist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new FilterAdapter(ListFilters, this);
        recyclerView.setAdapter(mAdapter);

    }

    private void getcount() {
        count = mGetFilters.getdata().getGovernance().size() + mGetFilters.getdata().getSociety().size() +
                mGetFilters.getdata().getLeisure().size() + mGetFilters.getdata().getWorld().size() +
                mGetFilters.getdata().getProfessional().size() + mGetFilters.getdata().getFuture().size();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void updateFilters(GetFilters mGetFilters) {

        update(mGetFilters.getdata().getGovernance());
        update(mGetFilters.getdata().getWorld());
        update(mGetFilters.getdata().getFuture());
        update(mGetFilters.getdata().getProfessional());
        update(mGetFilters.getdata().getLeisure());
        update(mGetFilters.getdata().getSociety());


    }

    public void update(List<Filterdata> list) {

        for (int i = 0; i < list.size(); i++) {
            String ID = list.get(i).getId();
            list.get(i).changeStatus(prefUtilFilter.getStatusofID(ID));
        }

    }

    public void ClearFilters(List<Filterdata> list) {

        for (int i = 0; i < list.size(); i++) {
            list.get(i).changeStatus("0");
        }

    }

    public void SelectAll(List<Filterdata> list) {

        for (int i = 0; i < list.size(); i++) {
            list.get(i).changeStatus("1");
        }


    }


    public void onClick(View v) {

        getcount();

        if (v == clear) {

            ClearFilters(mGetFilters.getdata().getGovernance());
            ClearFilters(mGetFilters.getdata().getSociety());
            ClearFilters(mGetFilters.getdata().getLeisure());
            ClearFilters(mGetFilters.getdata().getWorld());
            ClearFilters(mGetFilters.getdata().getProfessional());
            ClearFilters(mGetFilters.getdata().getFuture());

            List<List<Filterdata>> filterList = new ArrayList<>();
                    filterList.addAll(ArrayCreate(mGetFilters));

            for (int n = 1; n <= count; n++) {
                IDmap.put(String.valueOf((n)), "0");
            }

            mAdapter.refreshAdapter(filterList);


        }

        if (v == selectall) {

            SelectAll(mGetFilters.getdata().getGovernance());
            SelectAll(mGetFilters.getdata().getSociety());
            SelectAll(mGetFilters.getdata().getLeisure());
            SelectAll(mGetFilters.getdata().getWorld());
            SelectAll(mGetFilters.getdata().getProfessional());
            SelectAll(mGetFilters.getdata().getFuture());


            for (int n = 1; n <= count; n++) {
                IDmap.put(String.valueOf(n), "1");
            }
            List<List<Filterdata>> filterList = new ArrayList<>();
            filterList.addAll(ArrayCreate(mGetFilters));

            mAdapter.refreshAdapter(filterList);


        }

        if (v == tick) {


            for (int n = 1; n <= count; n++) {
                if (IDmap.get(String.valueOf(n)) != null && !IDmap.get(String.valueOf(n)).isEmpty()) {
                    prefUtilFilter.IDstatus(String.valueOf(n), IDmap.get(String.valueOf(n)));
                }

                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                startActivity(intent);
            }

            //Todo:add intent


        }

        if(v==crossback){
            finish();
        }
    }


    public List<List<Filterdata>> ArrayCreate(GetFilters mGetFilters) {

        ArrayList<List<Filterdata>> testArry = new ArrayList<>();

        testArry.add(mGetFilters.getdata().getGovernance());
        testArry.add(mGetFilters.getdata().getWorld());
        testArry.add(mGetFilters.getdata().getSociety());
        testArry.add(mGetFilters.getdata().getLeisure());
        testArry.add(mGetFilters.getdata().getFuture());
        testArry.add(mGetFilters.getdata().getProfessional());

        return testArry;


    }


    public void changeIDmapValue(String ID, String value) {

        IDmap.put(ID,value);}
}
