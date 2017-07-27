package in.thesoupstoriesnews.thesoup.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.EndlessRecyclerViewScrollListener;
import in.thesoupstoriesnews.thesoup.Activities.MainActivity;
import in.thesoupstoriesnews.thesoup.Adapters.MyFeedAdapter;
import in.thesoupstoriesnews.thesoup.Adapters.StoryFeedAdapter;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithToken;
import in.thesoupstoriesnews.thesoup.PreferencesFbAuth.PrefUtil;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

/**
 * Created by Jani on 22-05-2017.
 */

public class MyFeedFragment extends Fragment {

    private MyFeedAdapter mStoryfeedAdapter;
    private List<StoryData> mStoryData;
    private RecyclerView StoryView;
    private HashMap<String, String> params;
    private SharedPreferences pref;
    private TextView mTextView;
    private ProgressBar progress;
    private String totalrefresh="1";
    private Button gotodiscover;
    private ImageView warningImage;
    private String filter = "";
    private EndlessRecyclerViewScrollListener scrollListener;
	//CVIPUL Analytics
	private FirebaseAnalytics mFirebaseAnalytics;
	private int mLastFirstVisibleItem;
	private boolean mIsScrollingUp;
    private String unread = "";
	// Analytics end

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

        mTextView = (TextView) RootView.findViewById(R.id.warningtext);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		gotodiscover = (Button)RootView.findViewById(R.id.go_to_discover);
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
        mStoryfeedAdapter = new MyFeedAdapter(mStoryData, getActivity(), 1);
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
					mIsScrollingUp = false;
					scroll_direction = "scroll_screen_down";
				} else if (firstVisibleItem < mLastFirstVisibleItem) {
					mIsScrollingUp = true;
					scroll_direction = "scroll_screen_up";
				}
				mLastFirstVisibleItem = currentFirstVisibleItem;
				
				
				Bundle params = new Bundle();
				params.putString("screen_name", "myfeed_screen");
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

        params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
        params.put("myfeed", "1"); // 1 is the value required for getting myfeed
        params.put("page", "0");
        Log.d("auth_token1", pref.getString(SoupContract.AUTH_TOKEN, null));

        progress.setProgress(0);
        NetworkUtilswithToken networkutilsToken = new NetworkUtilswithToken(getActivity(), mStoryData, params);
        networkutilsToken.getFeed(1, totalrefresh);
		
		
		// CVIPUL Analytics
		// TODO : Verify view MyFeed screen
		Bundle mparams = new Bundle();
		mparams.putString("screen_name", "myfeed_screen");
		mparams.putString("category", "screen_view");
		mFirebaseAnalytics.logEvent("viewed_screen_myfeed",mparams);
		// 
		
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
        warningImage.setVisibility(View.VISIBLE);
        gotodiscover.setVisibility(View.VISIBLE);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);

    }

    public void startAdapter(List<StoryData> mStoryData,String num_unread) {
        mStoryfeedAdapter.refreshData(mStoryData);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);


        if(num_unread!=null&&!num_unread.isEmpty()){

            unread = num_unread;
            ((MainActivity)getActivity()).NumberofUnread(num_unread);

        }

                Log.d("mStoryData startAdapter", String.valueOf(mStoryData.size()));
    }

    public void startRefreshAdapter(List<StoryData> nStoryData, String num_unread) {
        mStoryData = nStoryData;
        mStoryfeedAdapter.totalRefreshData(nStoryData);


        if(num_unread!=null&&!num_unread.isEmpty()){
            unread = num_unread;
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
                    warningImage.setVisibility(View.GONE);
                    gotodiscover.setVisibility(View.GONE);
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
		
		// CVIPUL Analytics
		// TODO : Verify follow/unfollow event
		Bundle mparams = new Bundle();
		mparams.putString("screen_name", "myfeed_screen"); // "myfeed / discover"
		mparams.putString("collection_id", mStoryData.get(position).getStoryId());
		mparams.putString("follow_status", mStoryData.get(position).getFollowStatus());
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

        mStoryData.get(position).changeFollowStatus(followstatus);

        if(mStoryData.get(position).getReadStatus()!=null&&!mStoryData.get(position).getReadStatus().isEmpty()){
         if(mStoryData.get(position).getReadStatus().equals("0")){
             int i = Integer.valueOf(unread)-1;
             unread = String.valueOf(i);

             ((MainActivity)getActivity()).NumberofUnread(String.valueOf(i));

         }
        }else {
            int i = Integer.valueOf(unread)-1;
            unread = String.valueOf(i);

            ((MainActivity)getActivity()).NumberofUnread(String.valueOf(i));

        }



        mStoryData.remove(position);

        if(mStoryData.size()==0){

            StoryView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            warningImage.setVisibility(View.VISIBLE);
            gotodiscover.setVisibility(View.VISIBLE);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
        }



        mStoryfeedAdapter.refreshfollowstatus(mStoryData);
    }


    public void stopProgress() {
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    }

   public void filterApplied() {
   /*     StoryView.setVisibility(View.GONE);
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setText("Remove filters to see the stories you are following\n");
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
    */}

    public interface Badgeonfilter{
        void NumberofUnread(String num_unread);
    }




}
