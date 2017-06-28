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
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.Adapters.FilterAdapter;
import in.thesoup.thesoup.GSONclasses.filters1.Filters;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtilFilter;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class FilterActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private FilterAdapter mAdapter;
    private List<Filters> getFilters;
    private RecyclerView recyclerView;
    private Button apply, cancel;
    private PrefUtilFilter prefUtilFilter;
    private int count=0;

    private Map<String, String> IDmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.filterfragment1);

        apply = (Button) findViewById(R.id.apply);
        cancel = (Button) findViewById(R.id.cancel);

        IDmap = new HashMap<>();

        prefUtilFilter = new PrefUtilFilter(this);

        pref = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences preffilter = this.getSharedPreferences(SoupContract.FILTERPREF, 0);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/proxima-nova-black.otf")
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

        prefUtilFilter.SaveFilterListSize(count);

        update(getFilters);



        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        mAdapter = new FilterAdapter(getFilters, this);
        recyclerView.setAdapter(mAdapter);

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
        if(v==apply){

            int presentFilterCount = 0;
            String filters = "";
            for (int n = 0; n <getFilters.size(); n++) {

                if (IDmap.get(getFilters.get(n).getId())!= null && !IDmap.get(getFilters.get(n).getId()).isEmpty()) {
                    //prefUtilFilter.IDstatus(String.valueOf(n), IDmap.get(String.valueOf(n)));

                      prefUtilFilter.SaveIDstatus(getFilters,n,IDmap.get(getFilters.get(n).getId()));
                }

                if(getFilters.get(n).getStatus().equals("1")){
                    presentFilterCount++;
                    filters= filters+getFilters.get(n).getId()+",";
                }

            }

            SharedPreferences.Editor edit = pref.edit();
            edit.putString("filters",filters);
            edit.apply();

            Intent intent = new Intent(FilterActivity.this, MainActivity.class);
            intent.putExtra("filter_count",presentFilterCount);
            startActivity(intent);

        }

        if(v==cancel){
            finish();
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void changeIDmapValue(String ID, String value) {

        IDmap.put(ID,value);}
}

