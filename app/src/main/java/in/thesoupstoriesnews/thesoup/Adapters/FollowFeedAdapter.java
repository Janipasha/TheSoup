package in.thesoupstoriesnews.thesoup.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.DetailsActivity;
import in.thesoupstoriesnews.thesoup.Activities.NavigationActivity;
import in.thesoupstoriesnews.thesoup.Fragments.FollowingFragment;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoupstoriesnews.thesoup.GSONclasses.FollowingGSON.StoryDataFollowing;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsClick;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilswithTokenFollow;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

import static android.R.attr.category;
import static android.R.attr.targetActivity;


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


    public FollowFeedAdapter(List<StoryDataFollowing> Datalist, Context context, int fragmenttag,String value) {
        this.StoryDataList = Datalist;
        this.context = context;
        this.value = value;
        this.fragmenttag = fragmenttag;
        //CVIPUL Analytics
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //End Analytics
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
                intent.putExtra("category", category);
                intent.putExtra("hex_colour", storyColor);



                NavigationActivity activity = (NavigationActivity)context;
                FollowingFragment fragment = (FollowingFragment) activity.getFragment(2);
                fragment.startActivityForResult(intent,36);


                //TODO: Tap_add new updates to this event
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "following_screen");
                mparams.putString("category", "tap");
                mFirebaseAnalytics.logEvent("tap_following_collection", mparams);


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
                Fragment f = activity.getFragment(2);
                ((FollowingFragment)f).NetworkCallFollowing("latest_update");

                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "following_screen");
                mparams.putString("category", "tap");
                if(StoryDataList!=null){
                    mparams.putString("count_subscribed",String.valueOf(StoryDataList.size()));
                }
                mFirebaseAnalytics.logEvent("tap_sortby_latestupdate", mparams);



            }

            if(view==newlyfollowed){
                NavigationActivity activity = (NavigationActivity)context;
                Fragment f = activity.getFragment(2);
                ((FollowingFragment)f).NetworkCallFollowing("newly_followed");

                Bundle mparams = new Bundle();
                mparams.putString("screen_name", "following_screen");
                mparams.putString("category", "tap");
                if(StoryDataList!=null){
                    mparams.putString("count_subscribed",String.valueOf(StoryDataList.size()));
                }
                mFirebaseAnalytics.logEvent("tap_sortby_newlyadded", mparams);


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

            final StoryDataFollowing mStoryData = StoryDataList.get(position - 1);

            ((DataViewHolder) holder).numberunseenlayout.setVisibility(View.VISIBLE);

            String storyName = mStoryData.getStoryName();
            storyName = gethtmlString(storyName);
            ((DataViewHolder) holder).categoryfollow.setText(storyName);

         if(mStoryData.getCategoryName()!=null&&!mStoryData.getCategoryName().isEmpty()){
             String categoryName = mStoryData.getCategoryName();
             int filter = getDrawable(categoryName);

             ((DataViewHolder)holder).followfilterImage.setImageResource(filter);

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
            if (filter.equals("Politics & Governments")) {
                return R.drawable.icons8_parliament_filled;

            } else if (filter.equals("Judiciary")) {
                return R.drawable.icons8_scales;

            } else if (filter.equals("Development & Growth Indicators")) {
                return R.drawable.icons8_city_filled;

            } else if (filter.equals("Diplomacy, Defence, War & Terrorism")) {
                return R.drawable.icons8_assault_rifle_filled;
            } else if (filter.equals("Law Enforcement, Crime & Accidents")) {
                return R.drawable.icons8_police_badge_filled;
            } else if (filter.equals("Religion & Philosophy")) {
                return R.drawable.icons8_pray_filled;
            } else if (filter.equals("Human Rights, Social Work & Activism")) {
                return R.drawable.icons8_strike_filled;
            } else if (filter.equals("Sports, Games & Video Gaming")) {
                return R.drawable.icons8_tennis_ball_filled;
            } else if (filter.equals("Entertainment, Lifestyle & Culture")) {
                return R.drawable.icons8_movie_projector_filled;
            } else if (filter.equals("Environment & Nature")) {
                return R.drawable.icons8_earth_element_filled;
            } else if (filter.equals("Healthcare & Medicine")) {
                return R.drawable.icons8_heart_with_pulse_filled;
            } else if (filter.equals("Science & Technology")) {
                return R.drawable.icons8_rocket_filled;
            } else if (filter.equals("Business & Finance")) {
                return R.drawable.icons8_bullish_filled;
            } else if (filter.equals("Work, Career & Skills")) {
                return R.drawable.icons8_university_filled;
            }

            return 0;


        }


    }


