package in.thesoup.thesoupstoriesnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.fragments.FollowingFragment;
import in.thesoup.thesoupstoriesnews.gsonclasses.FollowingGSON.StoryDataFollowing;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsClick;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;


/**
 * Created by Jani on 06-09-2017.
 */

public class FollowFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private String value;
    private List<StoryDataFollowing> StoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName;
    //private AnalyticsApplication application;
    //private Tracker mTracker;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;


    public FollowFeedAdapter(List<StoryDataFollowing> Datalist, Context context, int fragmenttag,String value) {
        this.StoryDataList = Datalist;
        this.context = context;
        this.value = value;
        this.fragmenttag = fragmenttag;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        try {
            this.cleverTap = CleverTapAPI.getInstance(context);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
    }

    public void refreshData(List<StoryDataFollowing> Datalist,String value) {
        //this.StoryDataList = Datalist;
        StoryDataList.addAll(Datalist);
        this.value = value;
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<StoryDataFollowing> Datalist,String value) {
        this.StoryDataList = Datalist;
        this.value = value;
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryDataFollowing> Datalist) {
        this.StoryDataList = Datalist;

        notifyDataSetChanged();

    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView categoryfollow, numberfollowingupdate;
        public ImageView followfilterImage;
        public LinearLayout numberunseenlayout;
        public RelativeLayout followlayout;

        public DataViewHolder(View itemView) {
            super(itemView);

            categoryfollow = (TextView) itemView.findViewById(R.id.categoryfollow);
            numberfollowingupdate = (TextView) itemView.findViewById(R.id.numberfollowingupdates);
            followfilterImage = (ImageView) itemView.findViewById(R.id.filterimage_follow);
            numberunseenlayout = (LinearLayout) itemView.findViewById(R.id.notificationNumber);
            followlayout =(RelativeLayout) itemView.findViewById(R.id.follow_layout);

            followlayout.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {

            if(view==followlayout){
                int mposition =getAdapterPosition()-1;
                String StoryId=StoryDataList.get(mposition).getStoryId();
                String storyTitle = StoryDataList.get(mposition).getStoryName();
                String category = StoryDataList.get(mposition).getCategoryName();
                String storyColor = StoryDataList.get(mposition).getHexColor();
                String categoryId = StoryDataList.get(mposition).getCategoryId();

                HashMap<String, String> params = new HashMap<>();
                pref = PreferenceManager.getDefaultSharedPreferences(context);
                params.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
                params.put("id",StoryId);
                params.put("type","stories");

                NetworkUtilsClick Click = new NetworkUtilsClick(context,params);
                try {
                    Click.sendClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", StoryId);
                intent.putExtra("storytitle", storyTitle);
                intent.putExtra("followstatus","1");
                intent.putExtra("fragmenttag", fragmenttag);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("hex_colour", storyColor);



                NavigationActivity activity = (NavigationActivity)context;
                if(activity!=null){
                    FollowingFragment fragment = (FollowingFragment) activity.getFragment(2);
                    fragment.startActivityForResult(intent,36);
                }


                //TODO: Tap_add new updates to this event
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "following_screen");
                mparams.putString("category", "tap");
                mFirebaseAnalytics.logEvent("tap_following_collection", mparams);

                HashMap<String,Object> nparams = new HashMap<>();
                nparams.put("screen_name", "following_screen");
                nparams.put("category", "tap");
                cleverTap.event.push("tap_following_collection", nparams);


            }

        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


       public TextView latestfollow , newlyfollowed;


        public HeaderViewHolder(View itemView) {
            super(itemView);

            latestfollow = (TextView)itemView.findViewById(R.id.LatestUpdate);
            newlyfollowed =(TextView)itemView.findViewById(R.id.newlyfollowed);

            latestfollow.setOnClickListener(this);
            newlyfollowed.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view==latestfollow){
                NavigationActivity activity = (NavigationActivity)context;

                if(activity!=null){

                    Fragment f = activity.getFragment(2);
                    ((FollowingFragment)f).NetworkCallFollowing("latest_update");

                    HashMap<String,Object> nparams = new HashMap<>();
                    nparams.put("screen_name", "following_screen");
                    nparams.put("category", "tap");

                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "following_screen");
                    mparams.putString("category", "tap");
                    if(StoryDataList!=null){
                        mparams.putString("count_subscribed",String.valueOf(StoryDataList.size()));
                        nparams.put("count_subscribed",String.valueOf(StoryDataList.size()));
                    }
                    mFirebaseAnalytics.logEvent("tap_sortby_latestupdate", mparams);
                    cleverTap.event.push("tap_sortby_latestupdate", nparams);

                }

            }

            if(view==newlyfollowed){
                NavigationActivity activity = (NavigationActivity)context;
                if(activity!=null){
                    Fragment f = activity.getFragment(2);
                    ((FollowingFragment)f).NetworkCallFollowing("newly_followed");

                    HashMap<String,Object> nparams = new HashMap<>();
                    nparams.put("screen_name", "following_screen");
                    nparams.put("category", "tap");

                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "following_screen");
                    mparams.putString("category", "tap");
                    if(StoryDataList!=null){
                        mparams.putString("count_subscribed",String.valueOf(StoryDataList.size()));
                        nparams.put("count_subscribed",String.valueOf(StoryDataList.size()));
                    }
                    mFirebaseAnalytics.logEvent("tap_sortby_newlyadded", mparams);
                    cleverTap.event.push("tap_sortby_newlyadded", nparams);

                }

            }

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followedbody, parent, false);
            return new DataViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.followingheader, parent, false);
            return new HeaderViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof DataViewHolder) {

            Log.e("Story_id following",StoryDataList.get(position-1).getStoryId());

            final StoryDataFollowing mStoryData = StoryDataList.get(position - 1);

            ((DataViewHolder) holder).numberunseenlayout.setVisibility(View.VISIBLE);

            String storyName = mStoryData.getStoryName();
            storyName = gethtmlString(storyName);
            ((DataViewHolder) holder).categoryfollow.setText(storyName);

         if(mStoryData.getCategoryId()!=null&&!mStoryData.getCategoryId().isEmpty()){
             String Id = mStoryData.getCategoryId();
             int filter = getDrawable(Id);

             if(filter!=0){
                 ((DataViewHolder)holder).followfilterImage.setImageResource(filter);
             }

             if(mStoryData.getHexColor()!=null&&!mStoryData.getHexColor().isEmpty()){

                 String color = mStoryData.getHexColor();
                 ((DataViewHolder)holder).categoryfollow.setTextColor(Color.parseColor("#"+color));

                 ((DataViewHolder)holder).followfilterImage.setColorFilter(Color.parseColor("#"+color));
             }

         }



            if (mStoryData.getNumUnseen() != null && !mStoryData.getNumUnseen().isEmpty()) {

                String numUnseen = mStoryData.getNumUnseen();

                if(numUnseen.equals("0")){
                    ((DataViewHolder) holder).numberunseenlayout.setVisibility(View.GONE);
                }

                if(Integer.valueOf(numUnseen)<10){
                    ((DataViewHolder) holder).numberfollowingupdate.setText("0"+numUnseen);

                }else {
                    ((DataViewHolder)holder).numberfollowingupdate.setText(numUnseen);
                }

            } else {
                ((DataViewHolder) holder).numberunseenlayout.setVisibility(View.GONE);
            }

        } else if(holder instanceof HeaderViewHolder){

            if(value.equals("latest_update")){
                ((HeaderViewHolder)holder).latestfollow.setTypeface(null, Typeface.BOLD);
                ((HeaderViewHolder)holder).newlyfollowed.setTypeface(null,Typeface.NORMAL);
            }else if(value.equals("newly_followed")){

                ((HeaderViewHolder)holder).latestfollow.setTypeface(null, Typeface.NORMAL);
                ((HeaderViewHolder)holder).newlyfollowed.setTypeface(null,Typeface.BOLD);

            }
        }
    }

    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public String gethtmlString(String title){

        String titlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            titlehtml = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));

        } else {
            titlehtml =String.valueOf(Html.fromHtml(title));
        }

        return titlehtml;
    }


        @Override
        public int getItemCount() {


            //Log.d("tech memw",StoryDataList.size() +" ");
            return StoryDataList.size()+1;
        }

    public int getDrawable(String filter) {

        if (filter.equals("1")) {
            return R.drawable.politics;
        } else if (filter.equals("2")) {
            return R.drawable.diplomacy;

        } else if (filter.equals("3")) {
            return R.drawable.justice;

        } else if (filter.equals("4")) {
            return R.drawable.religion;
        } else if (filter.equals("5")) {
            return R.drawable.sports;
        } else if (filter.equals("7")) {
            return R.drawable.nature;
        } else if (filter.equals("9")) {
            return R.drawable.health;
        } else if (filter.equals("10")) {
            return R.drawable.science;
        } else if (filter.equals("11")) {
            return R.drawable.human_rights;
        } else if (filter.equals("12")) {
            return R.drawable.entertainment;
        } else if (filter.equals("15")) {
            return R.drawable.accident;
        } else if (filter.equals("16")) {
            return R.drawable.economy_business;
        } else if (filter.equals("17")) {
            return R.drawable.jobs;
        } else if (filter.equals("18")) {
            return R.drawable.education;
        } else if (filter.equals("19")) {
            return R.drawable.people;
        }

        return 0;


    }


    }


