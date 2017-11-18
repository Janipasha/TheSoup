package in.thesoup.thesoupstoriesnews.fragments;

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

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.EndlessRecyclerView;
import in.thesoup.thesoupstoriesnews.adapters.HomeFeedAdapter;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSON.StoryData;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilswithTokenMain;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
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
        private EndlessRecyclerView scrollListener;
        private ProgressBar progress;
        private String totalrefresh="0";
        private Context context;
        private String filter="";
        private FirebaseAnalytics mFirebaseAnalytics;
        private SwipeRefreshLayout swipeRefreshLayout;
        private int mLastFirstVisibleItem;
        private MotionEvent motionEvent;
    private CleverTapAPI cleverTap;





    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View RootView = inflater.inflate(R.layout.getstorieslist,container,false);

            pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
        try {
            cleverTap = CleverTapAPI.getInstance(getActivity());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }

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


            scrollListener = new EndlessRecyclerView(layoutManager) {
                @Override
                public void onLoadMore(int current_page) {
                    loadNextDataFromApi(current_page);
                }

            };

            StoryView.addOnScrollListener(scrollListener);
            swipeRefreshLayout = (SwipeRefreshLayout)RootView.findViewById(R.id.container_discover);



         //   NetworkCall();


            CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                    .setDefaultFontPath("fonts/montserrat-bold.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build()
            );

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "Home_screen");
        mparams.putString("category", "screen_view");
        mFirebaseAnalytics.logEvent("viewed_screen_home", mparams);


        HashMap<String,Object> aparams = new HashMap<>();
        aparams.put("screen_name", "Home_screen");
        aparams.put("category", "screen_view");
        cleverTap.event.push("viewed_screen_home", aparams);


            swipeRefreshLayout.setOnRefreshListener(HomeFragment.this);


            return RootView;
        }

        public void NetworkCall(){



            if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
                params.put("page", "0");
                params.put("purpose","followed");


                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed2(0,totalrefresh);
            } else {
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("page", "0");
                params.put("purpose","followed");
                Log.e("home story page",params.get("page"));

                if(swipeRefreshLayout.isRefreshing()){
                    progress.setVisibility(View.GONE);

                }else{
                    progress.setVisibility(View.VISIBLE);

                }
                progress.setProgress(0);

                for (String name : params.keySet()) {
                    String key = name;
                    String value = params.get(key);
                    Log.d("param values", key + " " + value);
                }

                Log.d("auth_token", pref.getString(SoupContract.AUTH_TOKEN, null));

                NetworkUtilswithTokenMain networkutilsToken = new NetworkUtilswithTokenMain(getActivity(), mStoryData, params);
                networkutilsToken.getFeed2(0,totalrefresh);





            }
        }

        @Override
        public void onRefresh() {
            totalrefresh = "1";
            scrollListener.changeoffset(0);
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

            if(offset==0){
                NetworkCall();
            }else {
                if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {

                    params.put("page", Page);
                    params.put("purpose","foryou");

                    NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
                    networkUtilswithTokenMain.getFeed3(0,totalrefresh);

                } else {
                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", Page);
                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);

                    params.put("purpose","foryou");
                    Log.e("home story page",params.get("page"));

                    NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
                    networkUtilswithTokenMain.getFeed3(0,totalrefresh);

                }

            }

        }


        public void startAdapterfirsttime(List<StoryDataMain> mStoryData1,List<StoryDataMain> nStoryData,String followcount,String followUpdateCount) {
            //StoryView.setAdapter(mStoryfeedAdapter);
            mStoryData = mStoryData1;
            mStoryfeedAdapter.refreshData(mStoryData,nStoryData,followcount,followUpdateCount);


            swipeRefreshLayout.setRefreshing(false);

            params.put("purpose","foryou");

            Log.e("home story_id",params.get("page"));

            NetworkUtilswithTokenMain networkUtilswithTokenMain = new NetworkUtilswithTokenMain(getActivity(),nStoryData,params);
            networkUtilswithTokenMain.getFeed3(0,totalrefresh);


// Dispatch touch event to view



        }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

       if(isVisibleToUser) {


           if (getActivity() == null) {
               Log.d("context is null", " at fragment");
           } else {

               mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

               Bundle mparams = new Bundle();
               mparams.putString("screen_name", "Home_screen");
               mparams.putString("category", "screen_view");
               mFirebaseAnalytics.logEvent("viewed_screen_home", mparams);

               HashMap<String,Object> aparams = new HashMap<>();
               aparams.put("screen_name", "Home_screen");
               aparams.put("category", "screen_view");
               cleverTap.event.push("viewed_screen_home", aparams);


           }
       }
 /*
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
                    scrollListener.changeoffset(0);

                    //prefUtil.saveTotalRefresh("0");

                }
            }
        }*/
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

            SharedPreferences.Editor edit = pref.edit();
            edit.putString(SoupContract.TOTAL_REFRESH,"1");
            edit.apply();

            totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);

            HashMap<String,Object> aparams = new HashMap<>();

            if (position < mStoryData.size()) {
                StoryDataMain aStoryData = mStoryData.get(position);

             aStoryData.changeFollowStatus(followstatus);

                aparams.put("screen_name", "Home_screen"); // "myfeed / discover"
                aparams.put("collection_id", mStoryData.get(position).getStoryIdMain());
                aparams.put("collection_name", mStoryData.get(position).getStoryNameMain());
                aparams.put("category", "conversion");

            } else if (position > mStoryData.size()) {
                position = position - mStoryData.size() - 1;
                StoryDataMain aStoryData = nStoryData.get(position);

                aStoryData.changeFollowStatus(followstatus);

                aparams.put("screen_name", "Home_screen"); // "myfeed / discover"
                aparams.put("collection_id", nStoryData.get(position).getStoryIdMain());
                aparams.put("collection_name", nStoryData.get(position).getStoryNameMain());
                aparams.put("category", "conversion");
            }

            mStoryfeedAdapter.refreshfollowstatus(mStoryData,nStoryData);

            if(followstatus.equals("1")){
                cleverTap.event.push("add", aparams);
                Toast.makeText(getActivity(), "Now following the Story", Toast.LENGTH_SHORT).show();
            }else{
                cleverTap.event.push("remove", aparams);
            }
        }


        public void stopProgress() {

            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }


    public void startAdaptersecondcall(List<StoryDataMain> oListFromJson) {
        nStoryData= oListFromJson;
        mStoryfeedAdapter.totalRefreshData(mStoryData,oListFromJson);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);

    }

    public void startAdapterAfterPagination(List<StoryDataMain> oListFromJson){
        nStoryData.addAll(oListFromJson);
        mStoryfeedAdapter.RefreshDataPagination(mStoryData,oListFromJson);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);


    }

    public void goToTop(){
        if(StoryView!=null){
            LinearLayoutManager manager =(LinearLayoutManager)StoryView.getLayoutManager();
            int firstvisibleitemposition = manager.findFirstVisibleItemPosition();
            if(firstvisibleitemposition!=0){
                StoryView.scrollToPosition(2);
                StoryView.smoothScrollToPosition(0);
            }else{
                totalrefresh = "1";
                scrollListener.changeoffset(0);
                swipeRefreshLayout.setRefreshing(true);

                NetworkCall();

            }

        }
    }




}
