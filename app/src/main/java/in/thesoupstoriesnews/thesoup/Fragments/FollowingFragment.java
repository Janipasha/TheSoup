package in.thesoupstoriesnews.thesoup.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerView;
import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoupstoriesnews.thesoup.Activities.NavigationActivity;
import in.thesoupstoriesnews.thesoup.Adapters.FollowFeedAdapter;
import in.thesoupstoriesnews.thesoup.GSONclasses.FollowingGSON.StoryDataFollowing;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsClick;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithTokenFollow;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithTokenMain;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

/**
 * Created by Jani on 06-09-2017.
 */

public class FollowingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        private FollowFeedAdapter mStoryfeedAdapter;
        private List<StoryDataFollowing> mStoryData;
        private RecyclerView StoryView;
        private HashMap<String, String> params;
        private SharedPreferences pref;
        private TextView mTextView;
        private ProgressBar progress;
        private String totalrefresh="1",value ="latest_update";
        private Button gotodiscover;
        private ImageView warningImage;
        private String filter = "";
        private EndlessRecyclerView scrollListener;
        //CVIPUL Analytics
        private FirebaseAnalytics mFirebaseAnalytics;
        private int mLastFirstVisibleItem;
        private boolean mIsScrollingUp;
        private String unread = "";
        private SwipeRefreshLayout swipeRefreshLayout;
        private int seenStatus = 0;
        // Analytics end


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View RootView = inflater.inflate(R.layout.getstorieslist, container, false);

            StoryView = (RecyclerView) RootView.findViewById(R.id.list_discover);

            mTextView = (TextView) RootView.findViewById(R.id.warningtext);
            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            gotodiscover = (Button)RootView.findViewById(R.id.go_to_discover);

            gotodiscover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NavigationActivity activity = (NavigationActivity)getActivity();
                    activity.gotoFragment(0);
                }
            });
            warningImage = (ImageView)RootView.findViewById(R.id.attention_drawable);
            //CVIPUL Analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());


            //End Analytics

            //Todo:hardcode remove

            if(pref.getString("filters",null)!=null&&!pref.getString("filters",null).isEmpty()) {
                filter = pref.getString("filters", null);

                Log.d("filters"," :"+filter);
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
            mStoryfeedAdapter = new FollowFeedAdapter(mStoryData, getActivity(), 1,value);
            StoryView.setAdapter(mStoryfeedAdapter);
            StoryView.setHasFixedSize(true);

            scrollListener = new EndlessRecyclerView(layoutManager) {

                @Override
                public void onLoadMore(int current_page) {
                    loadNextDataFromApi(current_page);
                }

            };
            StoryView.addOnScrollListener(scrollListener);


            swipeRefreshLayout= (SwipeRefreshLayout)RootView.findViewById(R.id.container_discover);
            swipeRefreshLayout.setOnRefreshListener(FollowingFragment.this);

           // NetworkCallFollowing("latest_update");

            // CVIPUL Analytics
            // TODO : Verify view MyFeed screen


            return RootView;
        }

        public void NetworkCallFollowing(String value_fromAdapter) {
            value= value_fromAdapter;

            totalrefresh = "1";
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("myfeed", "1"); // 1 is the value required for getting myfeed
            params.put("page", "0");
            params.put("sortby",value);
            //  Log.d("auth_token1", pref.getString(SoupContract.AUTH_TOKEN, null));
            if(swipeRefreshLayout.isRefreshing()){
                progress.setVisibility(View.GONE);

            }else{
                progress.setVisibility(View.VISIBLE);

            }


            progress.setProgress(0);
            NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
            networkutilsToken.getFeedFollow(2, totalrefresh);

        }

        private void loadNextDataFromApi(int offset) {
            totalrefresh="0";
            String Page = String.valueOf(offset);
            params.put("myfeed", "1");
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", Page);
            params.put("sortby",value);

            Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);

            NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
            networkutilsToken.getFeedFollow(2, totalrefresh);


        }

        public void Nofollowers() {

            StoryView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            // warningImage.setVisibility(View.VISIBLE);
            gotodiscover.setVisibility(View.VISIBLE);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);

        }

        public void startAdapter(List<StoryDataFollowing> mStoryData) {

            mStoryfeedAdapter.refreshData(mStoryData,value);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);


            swipeRefreshLayout.setRefreshing(false);

            Log.d("mStoryData startAdapter", String.valueOf(mStoryData.size()));
        }

        public void startRefreshAdapter(List<StoryDataFollowing> nStoryData) {
            mStoryData = nStoryData;
            mStoryfeedAdapter.totalRefreshData(nStoryData,value);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);

            Log.d("mStoryData refresh", String.valueOf(nStoryData.size()));
        }



        public void demo1(int position, String followstatus) {

            // CVIPUL Analytics
            // TODO : Verify follow/unfollow event
            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "notification_screen"); // "myfeed / discover"
            mparams.putString("collection_id", mStoryData.get(position).getStoryId());
          //  mparams.putString("follow_status", mStoryData.get(position).getFollowStatus());
            mparams.putString("collection_name", mStoryData.get(position).getStoryName());
            mparams.putString("category", "conversion");
            if(followstatus.equals( "1")){
                mFirebaseAnalytics.logEvent("add",mparams);
            }
            else if(followstatus.equals( "0")){
                mFirebaseAnalytics.logEvent("remove",mparams);
            }
            //End Analytics


            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.TOTAL_REFRESH, "1");
            edit.apply();

            Log.d("mStoryData demo", String.valueOf(mStoryData.size()));




            mStoryData.remove(position);

            if(mStoryData.size()==0){

                StoryView.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);
                //warningImage.setVisibility(View.VISIBLE);
                // gotodiscover.setVisibility(View.VISIBLE);
                progress.setProgress(100);
                progress.setVisibility(View.GONE);
            }



            mStoryfeedAdapter.refreshfollowstatus(mStoryData);
        }


        public void stopProgress() {
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
        }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null", " at fragment");
            } else {


                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "following_screen");
                mparams.putString("category", "screen_view");
                if(mStoryData!=null){
                mparams.putString("count_subscribed",String.valueOf(mStoryData.size()));
                }
                mFirebaseAnalytics.logEvent("viewed_screen_following", mparams);


                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover", totalrefresh);

                if (totalrefresh.equals("1")) {

                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    params.put("sortby", value);
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);

                    for (String name : params.keySet()) {
                        String key = name;
                        String value = params.get(key);
                        Log.d("param values", key + " " + value);
                    }


                    Log.d("filter", filter);

                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
                    networkutilsToken.getFeedFollow(2, totalrefresh);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();

                    prefUtil.saveTotalRefresh("0");
                    scrollListener.changeoffset(0);

                }
            }
        }
    }


        @Override
        public void onRefresh () {
            seenStatus = 0;
            scrollListener.changeoffset(0);
            swipeRefreshLayout.setRefreshing(true);
            NetworkCallFollowing(value);
        }

        public interface Badgeonfilter {
            void NumberofUnread(String num_unread);
        }


    }


