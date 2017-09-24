package in.thesoupstoriesnews.thesoup.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoupstoriesnews.thesoup.Adapters.HomeFeedAdapter;
import in.thesoupstoriesnews.thesoup.Adapters.StoryFeedAdapterMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithTokenMain;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Jani on 08-09-2017.
 */

public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        private HomeFeedAdapter mStoryfeedAdapter;
        private List<StoryDataMain> mStoryData,nStoryData;
        private RecyclerView StoryView;
        private HashMap<String, String> params;
        private SharedPreferences pref;
        private Button Discover, MyFeed;
        private EndlessRecyclerViewScrollListener scrollListener;
        private ProgressBar progress;
        private String totalrefresh="0";
        private Context context;
        private String filter="";
        private FirebaseAnalytics mFirebaseAnalytics;
        private SwipeRefreshLayout swipeRefreshLayout;
        private int mLastFirstVisibleItem;
        private MotionEvent motionEvent;
        //private boolean mIsScrollingUp;
        // Analytics end





    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View RootView = inflater.inflate(R.layout.getstorieslist,container,false);

            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            //CVIPUL Analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            //End Analytics

            //hardcoding of filters can change may be with SQL database



            if(pref.getString("filters",null)!=null&&!pref.getString("filters",null).isEmpty()) {
                filter = pref.getString("filters", null);

                Log.d("filters"," :"+filter);
            }



            //removing coma at the end

            if(filter!=null&&!filter.isEmpty()){
                filter = filter.substring(0,filter.length()-1);
            }



            if(pref.getString(SoupContract.TOTAL_REFRESH,null)!=null&&!pref.getString(SoupContract.TOTAL_REFRESH,null).isEmpty()){
                totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);}

            Log.d("totalRefresh Discover",totalrefresh);
            params = new HashMap<>();
            params.put("purpose","followed");

           // params.put("filters",filter);

            progress = (ProgressBar) RootView.findViewById(R.id.progressBar2);
            progress.setVisibility(View.GONE);

            mStoryData = new ArrayList<>();
            nStoryData = new ArrayList<>();
            StoryView = (RecyclerView)RootView.findViewById(R.id.list_discover);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            StoryView.setLayoutManager(layoutManager);
            mStoryfeedAdapter = new HomeFeedAdapter(mStoryData,nStoryData, getActivity(),1);
            StoryView.setAdapter(mStoryfeedAdapter);
            StoryView.setHasFixedSize(true);


            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

                    loadNextDataFromApi(page);
                }
            };

            StoryView.addOnScrollListener(scrollListener);
            swipeRefreshLayout = (SwipeRefreshLayout)RootView.findViewById(R.id.container_discover);


            NetworkCall();


            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/montserrat-bold.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );

            // CVIPUL Analytics
            // TODO : Verify view Discover feed screen
            Bundle params = new Bundle();
            params.putString("screen_name", "discover_screen");
            params.putString("category", "screen_view");
            mFirebaseAnalytics.logEvent("viewed_screen_discover",params);

            swipeRefreshLayout.setOnRefreshListener(HomeFragment.this);


            return RootView;
        }

        public void NetworkCall(){



            if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
                params.put("page", "0");
                params.put("purpose","followed");

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed2(1,totalrefresh);
            } else {
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("page", "0");
                params.put("purpose","discover");
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);

                for (String name : params.keySet()) {
                    String key = name;
                    String value = params.get(key);
                    Log.d("param values", key + " " + value);
                }

                Log.d("auth_token", pref.getString(SoupContract.AUTH_TOKEN, null));

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed2(1,totalrefresh);





            }
        }

        @Override
        public void onRefresh() {
            totalrefresh = "1";
            swipeRefreshLayout.setRefreshing(true);

            NetworkCall();

        }



        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            context = getActivity();
        }

        public void loadNextDataFromApi(int offset) {

            totalrefresh="0";

            String Page = String.valueOf(offset);

            if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {

                params.put("page", Page);
                params.put("purpose","foryou");

                NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
                networkUtilswithTokenMain.getFeed3(1,totalrefresh);

            } else {
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("page", Page);
                Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(0);

                params.put("purpose","foryou");

                NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
                networkUtilswithTokenMain.getFeed3(1,totalrefresh);

            }


        }


        public void startAdapterfirsttime(List<StoryDataMain> mStoryData1,List<StoryDataMain> nStoryData,String followcount) {
            //StoryView.setAdapter(mStoryfeedAdapter);
            mStoryData = mStoryData1;
            mStoryfeedAdapter.refreshData(mStoryData,nStoryData,followcount);

            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

            params.put("purpose","foryou");

            NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
            networkUtilswithTokenMain.getFeed3(1,totalrefresh);


// Dispatch touch event to view



        }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null"," at fragment");
            }else {


                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "Home_screen");
                mparams.putString("category", "screen_view");
                mFirebaseAnalytics.logEvent("viewed_screen_home", mparams);


                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover",totalrefresh);

                if (totalrefresh.equals("1")) {

                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    params.put("purpose","followed");
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);

                    for (String name : params.keySet()) {
                        String key = name;
                        String value = params.get(key);
                        Log.d("param values", key + " " + value);
                    }


                    Log.d("filter",filter);

                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                    networkutilsToken.getFeed2(1,totalrefresh);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();

                    //prefUtil.saveTotalRefresh("0");

                }
            }
        }




    }

        @Override
        public void onResume() {
            super.onResume();


        }

        public void startRefreshAdapter(List<StoryDataMain> mStoryData,List<StoryDataMain> nStoryData ){

            this.mStoryData = mStoryData;
            this.nStoryData = nStoryData;
            mStoryfeedAdapter.totalRefreshData(mStoryData,nStoryData);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);


        }



        public void demo1(int position, String followstatus) {

            // CVIPUL Analytics
            // TODO : Verify follow/unfollow event
            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "discover_screen"); // "myfeed / discover"
		/*
		String Storyname = StoryDataList.get(mposition).getStoryName();
        String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
        String mString = StoryDataList.get(mposition).getStoryId();
		*/
           // mparams.putString("collection_id", mStoryData.get(position).getStoryIdMain());
            // mparams.putString("follow_status", mStoryData.get(position).getFollowStatus());
            //mparams.putString("collection_name", mStoryData.get(position).getStoryNameMain());
            //mparams.putString("category", "conversion");
            /*if(followstatus.equals("1")){
                mFirebaseAnalytics.logEvent("add",mparams);
            }
            else if(followstatus.equals("0")){
                mFirebaseAnalytics.logEvent("remove",mparams);
            }
            //End Analytics*/


            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.TOTAL_REFRESH,"1");
            edit.apply();

            totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);



            if (position < mStoryData.size()) {
                StoryDataMain aStoryData = mStoryData.get(position);

             aStoryData.changeFollowStatus(followstatus);

            } else if (position > mStoryData.size()) {
                position = position - mStoryData.size() - 1;
                StoryDataMain aStoryData = nStoryData.get(position);

                aStoryData.changeFollowStatus(followstatus);
            }

            mStoryfeedAdapter.refreshfollowstatus(mStoryData,nStoryData);

            if(followstatus.equals("1")){
                Toast.makeText(getActivity(), "sucessfully followed the story", Toast.LENGTH_SHORT).show();
            }
        }


        public void stopProgress() {

            progress.setProgress(100);
            progress.setVisibility(View.GONE);
        }


    public void startAdaptersecondcall(List<StoryDataMain> oListFromJson) {
        nStoryData = oListFromJson;
        mStoryfeedAdapter.totalRefreshData(mStoryData,nStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

    }

    public void startAdapterAfterPagination(List<StoryDataMain> oListFromJson){
        nStoryData = oListFromJson;
        mStoryfeedAdapter.RefreshDataPagination(mStoryData,nStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);


    }

}
