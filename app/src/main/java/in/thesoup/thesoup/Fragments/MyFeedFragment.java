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
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;
import in.thesoup.thesoup.SoupContract;

import static android.R.attr.offset;
import static android.os.Build.ID;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Jani on 22-05-2017.
 */

public class MyFeedFragment extends Fragment {

    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private TextView mTextView;
    private ProgressBar progress;
    private String totalrefresh;
    private String filter = "";
    private EndlessRecyclerViewScrollListener scrollListener;

    public MyFeedFragment newinstance(int position) {
        MyFeedFragment f = new MyFeedFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", position);
        f.setArguments(args);

        return f;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.getstorieslist, container, false);

        StoryView = (RecyclerView) RootView.findViewById(R.id.list_discover);
        mTextView = (TextView) RootView.findViewById(R.id.empty_view);

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //Todo:hardcode remove

        for (int i = 0; i < 14; i++) {
            String Id = String.valueOf(i);

            if (pref.getString(Id, null) != null && !pref.getString(Id, null).isEmpty()) {

                if (pref.getString(Id, null).equals("1")) {
                    filter = filter + Id + ",";
                }

            }
        }

        totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH, null);


        params = new HashMap<>();

        params.put("filters", filter);

        progress = (ProgressBar) RootView.findViewById(R.id.progressBar2);
        progress.setVisibility(View.GONE);


        mStoryData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StoryView.setLayoutManager(layoutManager);
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, getActivity(), 1);
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


        params.put("auth_token", pref.getString("auth_token", null));
        params.put("myfeed", "1"); // 1 is the value required for getting myfeed
        params.put("page", "0");

        Log.d("auth_token1", pref.getString("auth_token", null));

        progress.setProgress(0);

        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


        networkutilsToken.getFeed(1, totalrefresh);


        return RootView;
    }

    private void loadNextDataFromApi(int page) {
        String Page = String.valueOf(offset);


        params.put("myfeed", "1");
        params.put("auth_token", pref.getString("auth_token", null));
        params.put("page", Page);

        Log.d("auth_token", pref.getString("auth_token", null));
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(0);

        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


        networkutilsToken.getFeed(1, totalrefresh);


    }

    public void Nofollowers() {

        StoryView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("You have not followed any SOUPS yet\n");
        progress.setProgress(100);
        progress.setVisibility(View.GONE);

    }

    public void startAdapter(List<StoryData> mStoryData) {
        mStoryfeedAdapter.refreshData(mStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        Log.d("mStoryData startAdapter", String.valueOf(mStoryData.size()));


    }

    public void startRefreshAdapter(List<StoryData> nStoryData) {
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);
        Log.d("mStoryData refresh", String.valueOf(nStoryData.size()));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);


        if (isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null", " at fragment");
            } else {
                totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH, null);


                if (totalrefresh.equals("1")) {

                    StoryView.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);


                    //mStoryData = new ArrayList<>();

                    NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


                    networkutilsToken.getFeed(1, totalrefresh);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();


                }
            }
        }

        Log.d("Resume test", "Isvisible " + isVisibleToUser);


    }

    @Override
    public void onResume() {
        super.onResume();


    }

    public void demo1(int position, String followstatus) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SoupContract.TOTAL_REFRESH, "1");
        edit.apply();

        Log.d("mStoryData demo", String.valueOf(mStoryData.size()));

        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }


    public void stopProgress() {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }
}
