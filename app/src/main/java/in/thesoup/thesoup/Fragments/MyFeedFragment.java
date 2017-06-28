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
import in.thesoup.thesoup.Activities.MainActivity;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.App.Config;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtil;
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
    private String totalrefresh="1";
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

        for (int i = 0; i <=14; i++) {
            String Id = String.valueOf(i);
            if (pref.getString(Id, null) != null && !pref.getString(Id, null).isEmpty()) {
                if (pref.getString(Id, null).equals("1")) {
                    filter = filter + Id + ",";
                }
            }
        }

        if(filter!=null&&!filter.isEmpty()){
           filter = filter.substring(0,filter.length()-1);
        }


        if(pref.getString(SoupContract.TOTAL_REFRESH,null)!=null&&!pref.getString(SoupContract.TOTAL_REFRESH,null).isEmpty()){
            totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);

        }


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

        params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
        params.put("myfeed", "1"); // 1 is the value required for getting myfeed
        params.put("page", "0");
        Log.d("auth_token1", pref.getString(SoupContract.AUTH_TOKEN, null));

        progress.setProgress(0);
        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
        networkutilsToken.getFeed(1, totalrefresh);

        return RootView;
    }

    private void loadNextDataFromApi(int offset) {
        String Page = String.valueOf(offset);
        params.put("myfeed", "1");
        params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
        params.put("page", Page);

        Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
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

    public void startAdapter(List<StoryData> mStoryData,String num_unread) {
        mStoryfeedAdapter.refreshData(mStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);

        if(num_unread!=null&&!num_unread.isEmpty()){
            ((MainActivity)getActivity()).NumberofUnread(num_unread);

        }

                Log.d("mStoryData startAdapter", String.valueOf(mStoryData.size()));
    }

    public void startRefreshAdapter(List<StoryData> nStoryData, String num_unread) {
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);


        if(num_unread!=null&&!num_unread.isEmpty()){
            ((MainActivity)getActivity()).NumberofUnread(num_unread);

        }

        Log.d("mStoryData refresh", String.valueOf(nStoryData.size()));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getActivity() == null) {
                Log.d("context is null", " at fragment");
            } else {
                PrefUtil prefUtil = new PrefUtil(getActivity());
                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh filter",totalrefresh);

                if (totalrefresh.equals("1")) {
                    StoryView.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);
                    params.put("page", "0");
                    for (String name : params.keySet()) {
                        String key = name;
                        String value = params.get(key);
                        Log.d("param values", key + " " + value);
                    }

                    NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
                    networkutilsToken.getFeed(1, totalrefresh);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();
                }
            }
        }

        Log.d("Resume test filter", "Isvisible " + isVisibleToUser);
    }

    public void demo1(int position, String followstatus) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SoupContract.TOTAL_REFRESH, "1");
        edit.apply();

        Log.d("mStoryData demo", String.valueOf(mStoryData.size()));

        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryData.remove(position);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }


    public void stopProgress() {
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }

    public interface Badgeonfilter{
        void NumberofUnread(String num_unread);
    }
}
