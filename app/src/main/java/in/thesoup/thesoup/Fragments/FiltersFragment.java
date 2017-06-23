package in.thesoup.thesoup.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.thesoup.thesoup.GSONclasses.filters.FilterJson;
import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;
import in.thesoup.thesoup.gsonConversion;

import static android.R.attr.filter;

/**
 * Created by Jani on 06-06-2017.
 */

public class FiltersFragment extends Fragment {

    private SharedPreferences pref;
    private FilterJson mFilterdata;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.filters,container,false);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String filterJson = pref.getString(SoupContract.FILTERJSON,null);

         gsonConversion getfilterObject = new gsonConversion();
        //mFilterdata= getfilterObject.filterConversion(filterJson);

        ArrayList<List<Filterdata>> testArry = new ArrayList<>();

        /*testArry.add(mFilterdata.getGovernance());
        testArry.add(mFilterdata.getWorld());
        testArry.add(mFilterdata.getFuture());
        testArry.add(mFilterdata.getProfessional());
        testArry.add(mFilterdata.getLeisure());
        testArry.add(mFilterdata.getSociety());*/


        return RootView;


    }
}
