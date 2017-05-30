package in.thesoup.thesoup.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class DiscoverFragment extends Fragment {

    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.getstorieslist,container,false);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        params = new HashMap<>();

        mStoryData = new ArrayList<>();
        StoryView = (RecyclerView)RootView.findViewById(R.id.list_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StoryView.setLayoutManager(layoutManager);
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, getActivity());
        StoryView.setAdapter(mStoryfeedAdapter);
        StoryView.setHasFixedSize(true);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        StoryView.addOnScrollListener(scrollListener);


        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {
            params.put("page", "0");
            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
            networkutilsToken.getFeed();
        } else {
            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page", "0");

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
            networkutilsToken.getFeed();

        }



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        return RootView;
    }

    public void loadNextDataFromApi(int offset) {

        String Page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString("auth_token", null))) {


            params.put("page", Page);


            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


            networkutilsToken.getFeed();

        } else {

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("page", Page);

            Log.d("auth_token", pref.getString("auth_token", null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


            networkutilsToken.getFeed();

        }


    }


    public void startAdapter(List<StoryData> mStoryData) {
        //
        //StoryView.setAdapter(mStoryfeedAdapter);
        mStoryfeedAdapter.refreshData(mStoryData);

    }

    public void demo1(int position, String followstatus) {
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }



}
