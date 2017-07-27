package in.thesoupstoriesnews.thesoup.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Adapters.StoryFeedAdapter;
import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;
import me.toptas.fancyshowcase.FancyShowCaseView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class DiscoverFragment extends Fragment {

    private StoryFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private Button Discover, MyFeed;
    private EndlessRecyclerViewScrollListener scrollListener;
    private ProgressBar progress;
    private String totalrefresh="0";
    private Context context;
    private String filter="";
	//CVIPUL Analytics
	private FirebaseAnalytics mFirebaseAnalytics;
	private int mLastFirstVisibleItem;
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


        new FancyShowCaseView.Builder(getActivity())
                .customView(R.layout.overlay, null)
                .backgroundColor(Color.parseColor("#33000000"))
                .build()
                .show();

        //removing coma at the end

       if(filter!=null&&!filter.isEmpty()){
          filter = filter.substring(0,filter.length()-1);
        }



       if(pref.getString(SoupContract.TOTAL_REFRESH,null)!=null&&!pref.getString(SoupContract.TOTAL_REFRESH,null).isEmpty()){
        totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);}

        Log.d("totalRefresh Discover",totalrefresh);
        params = new HashMap<>();

       params.put("filters",filter);

        progress = (ProgressBar) RootView.findViewById(R.id.progressBar2);
        progress.setVisibility(View.GONE);

        mStoryData = new ArrayList<>();
        StoryView = (RecyclerView)RootView.findViewById(R.id.list_discover);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StoryView.setLayoutManager(layoutManager);
        mStoryfeedAdapter = new StoryFeedAdapter(mStoryData, getActivity(),0);
        StoryView.setAdapter(mStoryfeedAdapter);
        StoryView.setHasFixedSize(true);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            
			// CVIPUL Analytics
			// TODO : Verify the new function this is added to, also the correctness

			/*@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
                super.onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
				
				private String scroll_direction;
				if (firstVisibleItem > mLastFirstVisibleItem) {
					//mIsScrollingUp = false;
					scroll_direction = "scroll_screen_down";
				} else if (firstVisibleItem < mLastFirstVisibleItem) {
					//mIsScrollingUp = true;
					scroll_direction = "scroll_screen_up";
				}
				mLastFirstVisibleItem = currentFirstVisibleItem;
				
				
				Bundle params = new Bundle();
				params.putString("screen_name", "discover_screen");
				params.putString("scroll_direction", scroll_direction);
				mFirebaseAnalytics.logEvent("scroll",params);
			}*/
			// End Analytics
			
			@Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        StoryView.addOnScrollListener(scrollListener);


        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {
            params.put("page", "0");

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
            networkutilsToken.getFeed(0,totalrefresh);
        } else {
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", "0");
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);

            for (String name : params.keySet()) {
                String key = name;
                String value = params.get(key);
                Log.d("param values", key + " " + value);
            }

            Log.d("auth_token", pref.getString(SoupContract.AUTH_TOKEN, null));

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
            networkutilsToken.getFeed(0,totalrefresh);

        }



        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Semibolditalic.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
		
		// CVIPUL Analytics
		// TODO : Verify view Discover feed screen
		Bundle params = new Bundle();
		params.putString("screen_name", "discover_screen");
		params.putString("category", "screen_view");
		mFirebaseAnalytics.logEvent("viewed_screen_discover",params);
		// 

        return RootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
    }

    public void loadNextDataFromApi(int offset) {

        String Page = String.valueOf(offset);

        if (TextUtils.isEmpty(pref.getString(SoupContract.AUTH_TOKEN, null))) {


            params.put("page", Page);


            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


            networkutilsToken.getFeed(0,totalrefresh);

        } else {

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", Page);

            Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);

            NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);


            networkutilsToken.getFeed(0,totalrefresh);

        }


    }


    public void startAdapter(List<StoryData> mStoryData) {
        //
        //StoryView.setAdapter(mStoryfeedAdapter);
        mStoryfeedAdapter.refreshData(mStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);

    }

    public void startRefreshAdapter(List<StoryData> nStoryData){
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {

            if (getActivity() == null) {
                Log.d("context is null"," at fragment");
            }else {

                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover",totalrefresh);

                if (totalrefresh.equals("1")) {



                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);

                    for (String name : params.keySet()) {
                        String key = name;
                        String value = params.get(key);
                        Log.d("param values", key + " " + value);
                    }


                    Log.d("filter",filter);

                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
                    networkutilsToken.getFeed(0, totalrefresh);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString(SoupContract.TOTAL_REFRESH, "0");
                    editor.apply();

                    //prefUtil.saveTotalRefresh("0");

                }
            }
        }





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
		mparams.putString("collection_id", mStoryData.get(position).getStoryId());
		mparams.putString("follow_status", mStoryData.get(position).getFollowStatus());
		mparams.putString("collection_name", mStoryData.get(position).getStoryName());
		mparams.putString("category", "conversion");
        if(followstatus.equals("1")){
			mFirebaseAnalytics.logEvent("add",mparams);
		}
		else if(followstatus.equals("0")){
			mFirebaseAnalytics.logEvent("remove",mparams);
		}
		//End Analytics
		
		
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(SoupContract.TOTAL_REFRESH,"1");
        edit.apply();

        totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);



        mStoryData.get(position).changeFollowStatus(followstatus);
        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }


    public void stopProgress() {

        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }



}
