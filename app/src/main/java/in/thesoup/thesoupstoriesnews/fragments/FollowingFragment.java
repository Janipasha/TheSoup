package in.thesoup.thesoupstoriesnews.fragments;

import android.content.Intent;
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

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.EndlessRecyclerView;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.adapters.FollowFeedAdapter;
import in.thesoup.thesoupstoriesnews.gsonclasses.FollowingGSON.StoryDataFollowing;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilswithTokenFollow;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtil;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;

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
        private FirebaseAnalytics mFirebaseAnalytics;
        private String unread = "";
        private SwipeRefreshLayout swipeRefreshLayout;
        private CleverTapAPI cleverTap;
        private int seenStatus = 0;
        private  String followunseen = "";



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
                    if(activity!=null){

                        HashMap<String,Object> aparams = new HashMap<>();
                        aparams.put("screen_name", "following_screen");
                        aparams.put("category", "tap");
                        cleverTap.event.push("tap_startfollowing",aparams);
                        activity.gotoFragment(0);
                    }
                }
            });
            warningImage = (ImageView)RootView.findViewById(R.id.attention_drawable);
            //CVIPUL Analytics
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());
            try {
                cleverTap = CleverTapAPI.getInstance(getActivity());
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }


            if(pref.getString(SoupContract.TOTAL_REFRESH,null)!=null&&!pref.getString(SoupContract.TOTAL_REFRESH,null).isEmpty()){
                totalrefresh = pref.getString(SoupContract.TOTAL_REFRESH,null);

            }


            params = new HashMap<>();

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

            return RootView;
        }

        public void NetworkCallFollowing(String value_fromAdapter) {
            value= value_fromAdapter;

            totalrefresh = "1";
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("sortby",value);
            params.put("page", "0");


            Log.e("following story page",params.get("page"));
            //  Log.d("auth_token1", pref.getString(SoupContract.AUTH_TOKEN, null));
            if(swipeRefreshLayout.isRefreshing()){
                progress.setVisibility(View.GONE);

            }else{
                progress.setVisibility(View.VISIBLE);

            }


            progress.setProgress(0);
            NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
            networkutilsToken.getFeedFollow(1, totalrefresh);

        }

        private void loadNextDataFromApi(int offset) {
            totalrefresh="0";
            String Page = String.valueOf(offset);
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("page", Page);
            params.put("sortby",value);
            Log.e("following story page",params.get("page"));

            Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            progress.setVisibility(View.VISIBLE);
            progress.setProgress(0);

            NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
            networkutilsToken.getFeedFollow(1, totalrefresh);


        }

        public void Nofollowers() {

            StoryView.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            // warningImage.setVisibility(View.VISIBLE);
            gotodiscover.setVisibility(View.VISIBLE);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }

        public void startAdapter(List<StoryDataFollowing> nStoryData,String followunseen) {

            this.followunseen = followunseen;

            mStoryfeedAdapter.refreshData(nStoryData,value);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

            if(getActivity()!=null){
                ((NavigationActivity)getActivity()).NumberofUnread(followunseen);
            }

            Log.d("mStoryData startAdapter", String.valueOf(mStoryData.size()));
        }

        public void startRefreshAdapter(List<StoryDataFollowing> nStoryData,String followunseen) {
            this.followunseen = followunseen;
            mStoryData = nStoryData;
            mStoryfeedAdapter.totalRefreshData(nStoryData,value);
            progress.setProgress(100);
            progress.setVisibility(View.GONE);

            swipeRefreshLayout.setRefreshing(false);

            if(getActivity()!=null){
                ((NavigationActivity)getActivity()).NumberofUnread(followunseen);
            }



            Log.d("mStoryData refresh", String.valueOf(nStoryData.size()));
        }



        public void stopProgress() {
            progress.setProgress(100);
            progress.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
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

               HashMap<String,Object> aparams = new HashMap<>();
                aparams.put("screen_name", "following_screen");
                aparams.put("category", "screen_view");

                if(mStoryData!=null){
                mparams.putString("count_subscribed",String.valueOf(mStoryData.size()));
                    aparams.put("count_subscribed",String.valueOf(mStoryData.size()));
                }
                mFirebaseAnalytics.logEvent("viewed_screen_following", mparams);
                cleverTap.event.push("viewed_screen_following", aparams);


                PrefUtil prefUtil = new PrefUtil(getActivity());

                totalrefresh = prefUtil.getTotalRefresh();
                Log.d("totalRefresh Discover", totalrefresh);

                if (totalrefresh.equals("1")) {

                    StoryView.setVisibility(View.VISIBLE);
                    mTextView.setVisibility(View.GONE);
                    // warningImage.setVisibility(View.VISIBLE);
                    gotodiscover.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);

                    params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                    params.put("page", "0");
                    params.put("sortby", value);
                    progress.setVisibility(View.VISIBLE);
                    progress.setProgress(0);
                    Log.e("following story page",params.get("page"));

                    for (String name : params.keySet()) {
                        String key = name;
                        String value = params.get(key);
                        Log.d("param values", key + " " + value);
                    }


                    Log.d("filter", filter);

                    Log.d(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));

                    NetworkUtilswithTokenFollow networkutilsToken = new NetworkUtilswithTokenFollow(getActivity(), params);
                    networkutilsToken.getFeedFollow(1, totalrefresh);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==36){
              if(resultCode==1){
                  int position = 39897;
                  String storyId = data.getStringExtra("storyId");
                  for(int i=0;i<mStoryData.size();i++){
                    if(mStoryData.get(i).getStoryId().equals(storyId)){
                        position = i;
                    }
                  }
                  if(position!=39897){
                      mStoryData.remove(position);
                      if(mStoryData.size()==0){

                          StoryView.setVisibility(View.GONE);
                          mTextView.setVisibility(View.VISIBLE);
                          // warningImage.setVisibility(View.VISIBLE);
                          gotodiscover.setVisibility(View.VISIBLE);
                          progress.setProgress(100);
                          progress.setVisibility(View.GONE);
                          swipeRefreshLayout.setRefreshing(false);

                          if(getActivity()!=null){
                              NavigationActivity activity = (NavigationActivity) getActivity();
                              activity.NumberofUnread("0");
                          }

                          startRefreshAdapter(mStoryData,followunseen);
                      }else{
                          startRefreshAdapter(mStoryData,followunseen);
                      }

                  }
              }
        }
    }

    @Override
        public void onRefresh () {
            seenStatus = 0;
            scrollListener.changeoffset(0);
            swipeRefreshLayout.setRefreshing(true);
        StoryView.setVisibility(View.VISIBLE);
        mTextView.setVisibility(View.GONE);
        // warningImage.setVisibility(View.VISIBLE);
        gotodiscover.setVisibility(View.GONE);
        progress.setProgress(100);
        progress.setVisibility(View.GONE);
            NetworkCallFollowing(value);


        }

    public void goToTop(){
        if(StoryView!=null){
            LinearLayoutManager manager =(LinearLayoutManager)StoryView.getLayoutManager();
            int firstvisibleitemposition = manager.findFirstVisibleItemPosition();
            if(firstvisibleitemposition!=0){
                StoryView.scrollToPosition(2);
                StoryView.smoothScrollToPosition(0);
            }else{
                seenStatus = 0;
                scrollListener.changeoffset(0);
                swipeRefreshLayout.setRefreshing(true);
                StoryView.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.GONE);
                // warningImage.setVisibility(View.VISIBLE);
                gotodiscover.setVisibility(View.GONE);
                progress.setProgress(100);
                progress.setVisibility(View.GONE);
                NetworkCallFollowing(value);

            }
        }
    }

        public interface Badgeonfilter {
            void NumberofUnread(String num_unread);
        }


    }


