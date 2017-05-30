package in.thesoup.thesoup.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoup.thesoup.Adapters.StoryFeedAdapter;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoup.thesoup.R;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View RootView = inflater.inflate(R.layout.getstorieslist,container,false);

        StoryView = (RecyclerView) RootView.findViewById(R.id.list_discover);
        mTextView = (TextView) RootView.findViewById(R.id.empty_view);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        params = new HashMap<>();

        mStoryData = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StoryView.setLayoutManager(layoutManager);
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, getActivity());
        StoryView.setAdapter(mStoryfeedAdapter);

        StoryView.setHasFixedSize(true);


        params.put("auth_token", pref.getString("auth_token", null));
        params.put("myfeed", "1"); // 1 is the value required for getting myfeed
        params.put("page", "0");

        Log.d("auth_token1", pref.getString("auth_token", null));

        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


        networkutilsToken.getFeed();




        return RootView;
    }

    public void Nofollowers(){

        StoryView.setVisibility(View.GONE);
        mTextView.setText("You have not followed any SOUPS yet\n");

    }

    public void startAdapter(List<StoryData> mStoryData) {
        mStoryfeedAdapter.refreshData(mStoryData);

    }


    public void demo1(int position, String followstatus) {
        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }
}
