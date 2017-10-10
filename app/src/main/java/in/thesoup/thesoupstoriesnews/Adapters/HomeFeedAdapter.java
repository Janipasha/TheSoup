package in.thesoup.thesoupstoriesnews.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.thesoup.thesoupstoriesnews.R;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.Activities.ArticleWebViewActivity;
import in.thesoup.thesoupstoriesnews.Activities.ArticlesActivity;
import in.thesoup.thesoupstoriesnews.Activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.Activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.Activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.GSONclasses.FeedGSONMain.SubstoriesMain;
import in.thesoup.thesoupstoriesnews.NetworkCalls.NetworkUtilsClick;
import in.thesoup.thesoupstoriesnews.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;

import static android.R.attr.filter;
import static android.R.attr.src;
import static android.media.CamcorderProfile.get;
import static com.uxcam.c.d.c;

/**
 * Created by Jani on 08-09-2017.
 */

public class HomeFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_MIDDLE = 2;


    private List<StoryDataMain> StoryDataList, nStoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName, followcount, followUpdateCount;
    //private AnalyticsApplication application;
    //private Tracker mTracker;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;


    public HomeFeedAdapter(List<StoryDataMain> Datalist, List<StoryDataMain> nStoryDataList, Context context, int fragmenttag) {
        this.StoryDataList = Datalist;
        this.nStoryDataList = nStoryDataList;
        this.context = context;
        this.fragmenttag = fragmenttag;
        //CVIPUL Analytics
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //End Analytics
    }

    public void refreshData(List<StoryDataMain> Datalist, List<StoryDataMain> nStoryDataList, String followcount, String followUpdateCount) {
        this.StoryDataList = Datalist;
        this.nStoryDataList = nStoryDataList;
        this.followcount = followcount;
        this.followUpdateCount = followUpdateCount;
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<StoryDataMain> Datalist, List<StoryDataMain> nStoryDataList) {
        this.StoryDataList = Datalist;
        this.nStoryDataList = nStoryDataList;
        notifyDataSetChanged();
    }

    public void RefreshDataPagination(List<StoryDataMain> Datalist, List<StoryDataMain> lStoryDataList) {
        this.StoryDataList = Datalist;
        nStoryDataList.addAll(lStoryDataList);
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryDataMain> Datalist, List<StoryDataMain> nStoryDataList) {
        this.StoryDataList = Datalist;
        this.nStoryDataList = nStoryDataList;

        notifyDataSetChanged();

    }


    public class StoryDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView storyTitle, viewfullstory, update1, time1, substoryTitle1, update2, time2, substoryTitle2, update3, time3, source1, source2,
                source3, source1time, source2time, source3time, articletitle1, articletitle2, articletitle3, bottomtextshowmore,
                bottomtextnumarticles, readstatus;
        public ImageView followicon, filterImage, heroimage, sourceimage1, sourceimage2, sourceimage3,
                circle1, circle2, circle3, seenImage;

        public RelativeLayout secondtopstorylayout, thirdtopstorylayout, topstorylayout;

        public LinearLayout firstarticle, secondarticle, thirdarticle, followlayout, shareicon;

        public View sideline1, sideline2, sideline3, sideline4, sideline5, sideline6, bottomline, bottomline1, bottomline2;

        public StoryDataViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView) itemView.findViewById(R.id.story_name);
            substoryTitle1 = (TextView) itemView.findViewById(R.id.substory_title1);
            substoryTitle2 = (TextView) itemView.findViewById(R.id.substory_title2);
            viewfullstory = (TextView) itemView.findViewById(R.id.view_full_story);
            update1 = (TextView) itemView.findViewById(R.id.update1);
            update2 = (TextView) itemView.findViewById(R.id.update2);
            update3 = (TextView) itemView.findViewById(R.id.update3);
            time1 = (TextView) itemView.findViewById(R.id.time1);
            time2 = (TextView) itemView.findViewById(R.id.time2);
            time3 = (TextView) itemView.findViewById(R.id.time3);
            substoryTitle1 = (TextView) itemView.findViewById(R.id.substory_title1);
            substoryTitle2 = (TextView) itemView.findViewById(R.id.substory_title2);
            source1 = (TextView) itemView.findViewById(R.id.source1);
            source2 = (TextView) itemView.findViewById(R.id.source2);
            source3 = (TextView) itemView.findViewById(R.id.source3);
            source1time = (TextView) itemView.findViewById(R.id.source1_time);
            source2time = (TextView) itemView.findViewById(R.id.source2_time);
            source3time = (TextView) itemView.findViewById(R.id.source3_time);
            articletitle1 = (TextView) itemView.findViewById(R.id.article_title1);
            articletitle2 = (TextView) itemView.findViewById(R.id.article_title2);
            articletitle3 = (TextView) itemView.findViewById(R.id.article_title3);
            bottomtextshowmore = (TextView) itemView.findViewById(R.id.bottomtext_showall);
            bottomtextnumarticles = (TextView) itemView.findViewById(R.id.bottomtext_numarticles);
            readstatus = (TextView) itemView.findViewById(R.id.readstatus_text_story);

            followicon = (ImageView) itemView.findViewById(R.id.followicon);
            filterImage = (ImageView) itemView.findViewById(R.id.filter_image);
            heroimage = (ImageView) itemView.findViewById(R.id.heroimage);
            seenImage = (ImageView) itemView.findViewById(R.id.readstatus_story);
            shareicon = (LinearLayout) itemView.findViewById(R.id.shareicon);
            sourceimage1 = (ImageView) itemView.findViewById(R.id.sourceImage1);
            sourceimage2 = (ImageView) itemView.findViewById(R.id.sourceImage2);
            sourceimage3 = (ImageView) itemView.findViewById(R.id.sourceImage3);
            circle1 = (ImageView) itemView.findViewById(R.id.circle1);
            circle2 = (ImageView) itemView.findViewById(R.id.circle2);
            circle3 = (ImageView) itemView.findViewById(R.id.circle3);

            bottomline = (View) itemView.findViewById(R.id.bottom_line);
            bottomline1 = (View) itemView.findViewById(R.id.bottom_line1);
            bottomline2 = (View) itemView.findViewById(R.id.bottom_line2);

            sideline1 = (View) itemView.findViewById(R.id.sideline1);
            sideline2 = (View) itemView.findViewById(R.id.sideline2);
            sideline3 = (View) itemView.findViewById(R.id.sideline3);
            sideline4 = (View) itemView.findViewById(R.id.sideline4);
            sideline5 = (View) itemView.findViewById(R.id.sideline5);
            sideline6 = (View) itemView.findViewById(R.id.sideline6);

            secondtopstorylayout = (RelativeLayout) itemView.findViewById(R.id.second_topstory_layout);
            thirdtopstorylayout = (RelativeLayout) itemView.findViewById(R.id.third_topstory_layout);

            firstarticle = (LinearLayout) itemView.findViewById(R.id.firstarticle);
            secondarticle = (LinearLayout) itemView.findViewById(R.id.secondarticle);
            thirdarticle = (LinearLayout) itemView.findViewById(R.id.thirdarticle);
            followlayout = (LinearLayout) itemView.findViewById(R.id.follow_icon_layout);
            topstorylayout = (RelativeLayout) itemView.findViewById(R.id.topstory_layout);

            firstarticle.setOnClickListener(this);
            secondarticle.setOnClickListener(this);
            thirdarticle.setOnClickListener(this);

            bottomtextshowmore.setOnClickListener(this);
            bottomtextshowmore.setOnClickListener(this);

            shareicon.setOnClickListener(this);

            storyTitle.setOnClickListener(this);
            viewfullstory.setOnClickListener(this);

            followlayout.setOnClickListener(this);
            readstatus.setOnClickListener(this);
            seenImage.setOnClickListener(this);

            heroimage.setOnClickListener(this);

            topstorylayout.setOnClickListener(this);
            secondtopstorylayout.setOnClickListener(this);
            thirdtopstorylayout.setOnClickListener(this);

            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int mposition = getAdapterPosition() - 1;

            if (view == bottomtextnumarticles || view == bottomtextshowmore || view == heroimage || view == seenImage || view == readstatus) {

                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    bottomclickHandle(mStoryData);

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", mStoryData.getStoryIdMain());
                    params.put("type", "stories");
                    NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                    try {
                        performClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (view == heroimage) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "from_your_followed");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_cover", nparams);
                        mFirebaseAnalytics.logEvent("viewed_modal_entity", nparams);

                    } else if (view == bottomtextshowmore || view == bottomtextnumarticles) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "from_your_followed");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_showall", nparams);
                        mFirebaseAnalytics.logEvent("viewed_modal_entity", nparams);

                    }
                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    bottomclickHandle(mStoryData);

                    if (view == heroimage) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "stories_for_you");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_cover", nparams);
                        mFirebaseAnalytics.logEvent("viewed_modal_entity", nparams);
                    } else if (view == bottomtextshowmore || view == bottomtextnumarticles) {

                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "stories_for_you");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_showall", nparams);
                        mFirebaseAnalytics.logEvent("viewed_modal_entity", nparams);


                    }

                }


            }

            if (view == firstarticle) {
                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();

                    articleClickHandle(mStoryData, 0);

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", mStoryData.getStoryIdMain());
                    params.put("type", "stories");
                    NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                    try {
                        performClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "from_your_followed");
                    nparams.putString("position_card_entity", String.valueOf(1));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    articleClickHandle(mStoryData, 0);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "stories_for_you");
                    nparams.putString("position_card_entity", String.valueOf(1));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);
                }
            }

            if (view == secondarticle) {
                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", mStoryData.getStoryIdMain());
                    params.put("type", "stories");
                    NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                    try {
                        performClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    articleClickHandle(mStoryData, 1);
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "from_your_followed");
                    nparams.putString("position_card_entity", String.valueOf(2));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    articleClickHandle(mStoryData, 1);
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "stories_for_you");
                    nparams.putString("position_card_entity", String.valueOf(2));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);
                }
            }

            if (view == thirdarticle) {
                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", mStoryData.getStoryIdMain());
                    params.put("type", "stories");
                    NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                    try {
                        performClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    articleClickHandle(mStoryData, 2);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "from_your_followed");
                    nparams.putString("position_card_entity", String.valueOf(3));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    articleClickHandle(mStoryData, 2);
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "stories_for_you");
                    nparams.putString("position_card_entity", String.valueOf(3));
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);
                }

            }

            if (view == viewfullstory || view == storyTitle || view == topstorylayout || view == secondtopstorylayout || view == thirdtopstorylayout) {
                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();

                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", mStoryData.getStoryIdMain());
                    params.put("type", "stories");
                    NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                    try {
                        performClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    if (view == viewfullstory||view == storyTitle||view ==topstorylayout) {

                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "from_your_followed");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_viewstory", nparams);

                        goToDetails(mStoryData,"0");

                    }

                    if (view == secondtopstorylayout) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "from_your_followed");
                        nparams.putString("position_card_old", String.valueOf(1));
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_old", nparams);

                        goToDetails(mStoryData,"2");

                    }

                    if (view == thirdtopstorylayout) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "from_your_followed");
                        nparams.putString("position_card_old", String.valueOf(2));
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_old", nparams);

                        goToDetails(mStoryData,"1");

                    }

                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();


                    if (view == viewfullstory||view ==storyTitle||view==topstorylayout) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "stories_for_you");
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_viewstory", nparams);
                        goToDetails(mStoryData,"0");

                    }

                    if (view == secondtopstorylayout) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "stories_for_you");
                        nparams.putString("position_card_old", String.valueOf(1));
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_old", nparams);
                        goToDetails(mStoryData,"2");
                    }

                    if (view == thirdtopstorylayout) {
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "home_screen");
                        nparams.putString("category", "tap"); //(only if possible)
                        nparams.putString("position_card_collection", String.valueOf(mposition));
                        nparams.putString("name_section", "stories_for_you");
                        nparams.putString("position_card_old", String.valueOf(2));
                        nparams.putString("story_title", StoryTitle);
                        nparams.putString("story_id", StoryID);
                        mFirebaseAnalytics.logEvent("tap_card_old", nparams);
                        goToDetails(mStoryData,"1");
                    }
                }
            }

            if (view == followicon || view == followlayout) {

                if (mposition < StoryDataList.size()) {
                    clickposition = mposition;
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    clickStoryId = StoryDataList.get(mposition).getStoryIdMain();
                    clickStoryName = StoryDataList.get(mposition).getStoryNameMain();
                    followstory(mStoryData);


                } else if (mposition > StoryDataList.size()) {
                    clickposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(clickposition);
                    clickStoryId = nStoryDataList.get(clickposition).getStoryIdMain();
                    clickStoryName = nStoryDataList.get(clickposition).getStoryNameMain();
                    clickposition = mposition;
                    followstory(mStoryData);


                }
            }

            if (view == shareicon) {
                //Todo 1(priority): this has to show error if StoryDataList is empty . cant see storydatalist.size() on empty array

                if (mposition < StoryDataList.size()) {
                    StoryDataMain mStoryData = StoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    shareclick(mStoryData);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "conversion"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "from_your_followed");
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_shareicon", nparams);


                } else if (mposition > StoryDataList.size()) {
                    mposition = mposition - StoryDataList.size() - 1;
                    StoryDataMain mStoryData = nStoryDataList.get(mposition);
                    String StoryTitle = mStoryData.getStoryNameMain();
                    String StoryID = mStoryData.getStoryIdMain();
                    shareclick(mStoryData);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "conversion"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("name_section", "stories_for_you");
                    nparams.putString("story_title", StoryTitle);
                    nparams.putString("story_id", StoryID);
                    mFirebaseAnalytics.logEvent("tap_shareicon", nparams);
                }


            }


        }
    }

    public void shareclick(StoryDataMain mStoryData) {
        String URL = "http://thesoup.in/share/" + mStoryData.getStoryIdMain() + "/" + mStoryData.getSubstoryId();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Share via");
        i.putExtra(Intent.EXTRA_TEXT, URL);
        context.startActivity(Intent.createChooser(i, "Share via"));

    }

    public void followstory(StoryDataMain mStoryData) {

        String mfollowstatus = mStoryData.getFollowstatus();
        pref = PreferenceManager.getDefaultSharedPreferences(context);


        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "home_screen"); // "myfeed / discover"
        mparams.putString("collection_id", clickStoryId);
        mparams.putString("collection_name", clickStoryName);
        mparams.putString("position_card_collection", String.valueOf(clickposition));
        //Todo 2 : add collection location once the size of empty array is sorted
        mparams.putString("category", "conversion");


        if (mfollowstatus.equals("") || mfollowstatus.equals("0") || TextUtils.isEmpty(mfollowstatus)) {

            //Analytics
            mFirebaseAnalytics.logEvent("tap_add", mparams);
            HashMap<String, String> params = new HashMap<>();

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);
            NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
            followrequest.followRequest(clickposition, fragmenttag);


        } else if (mfollowstatus.equals("1")) {

            //Analytics
            mFirebaseAnalytics.logEvent("tap_remove", mparams);
            HashMap<String, String> params = new HashMap<>();
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);

            Log.d("Check mStorydata size", String.valueOf(StoryDataList.size()));

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

            unFollowrequest.unFollowRequest(clickposition, fragmenttag);


        }
    }


    public void goToDetails(StoryDataMain mStoryData,String cardnumber) {
        String StoryId = mStoryData.getStoryIdMain();
        String storytitle = mStoryData.getStoryNameMain();
        String hex_colour = mStoryData.getHexColor();
        String category = mStoryData.getCategoryName();
        String categoryId = mStoryData.getCategoryId();


        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("story_id", StoryId);
        intent.putExtra("storytitle", storytitle);
        intent.putExtra("followstatus", "0");
        intent.putExtra("fragmenttag", fragmenttag);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("hex_colour", hex_colour);
        intent.putExtra("cardnumber",cardnumber);
        context.startActivity(intent);
    }

    public void articleClickHandle(StoryDataMain mStoryData, int i) {

        String storyColor = mStoryData.getHexColor();
        if (mStoryData.getSubstories().size() == 3) {
            String articleUrl = mStoryData.getSubstories().get(2).getArticlesMain().get(i).getUrl();
            String SubstoryId = mStoryData.getSubstories().get(2).getSubstory_id();


            Intent intent = new Intent(context, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL", articleUrl);
            intent.putExtra("substory_id", SubstoryId);
            intent.putExtra("storycolor", storyColor);
            context.startActivity(intent);

        } else if (mStoryData.getSubstories().size() == 2) {

            String articleUrl = mStoryData.getSubstories().get(1).getArticlesMain().get(i).getUrl();
            String SubstoryId = mStoryData.getSubstories().get(1).getSubstory_id();


            Intent intent = new Intent(context, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL", articleUrl);
            intent.putExtra("substory_id", SubstoryId);
            intent.putExtra("storycolor", storyColor);
            context.startActivity(intent);

        } else if (mStoryData.getSubstories().size() == 1) {

            String articleUrl = mStoryData.getSubstories().get(0).getArticlesMain().get(i).getUrl();
            String SubstoryId = mStoryData.getSubstories().get(0).getSubstory_id();


            Intent intent = new Intent(context, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL", articleUrl);
            intent.putExtra("substory_id", SubstoryId);
            intent.putExtra("storycolor", storyColor);
            context.startActivity(intent);
        }
    }

    public void bottomclickHandle(StoryDataMain mStoryData) {
        String StoryId = mStoryData.getStoryIdMain();
        String storyTitle = mStoryData.getStoryNameMain();
        String storyColor = mStoryData.getHexColor();

        if (mStoryData.getSubstories().size() == 3) {

            List<ArticlesMain> mArticles = mStoryData.getSubstories().get(2).getArticlesMain();
            Intent intent = new Intent(context, ArticlesActivity.class);
            intent.putExtra("ARTICLELIST", (Serializable) mArticles);
            intent.putExtra("StoryTitle", storyTitle);
            intent.putExtra("substory_id", mStoryData.getSubstoryId());
            intent.putExtra("story_color", storyColor);
            context.startActivity(intent);

            if (mArticles.size() > 3) {


            }/*else{
                Intent intent = new Intent(context,ArticleWebViewActivity.class);
                intent.putExtra("ArticleURL", mArticles.get(0).getUrl());
                intent.putExtra("substory_id", mStoryData.getSubstories().get(2).getSubstory_id());
                intent.putExtra("storycolor", storyColor);
                context.startActivity(intent);
            }*/
        } else if (mStoryData.getSubstories().size() == 2) {

            List<ArticlesMain> mArticles = mStoryData.getSubstories().get(1).getArticlesMain();
            Intent intent = new Intent(context, ArticlesActivity.class);
            intent.putExtra("ARTICLELIST", (Serializable) mArticles);
            intent.putExtra("StoryTitle", storyTitle);
            intent.putExtra("substory_id", mStoryData.getSubstoryId());
            intent.putExtra("story_color", storyColor);
            context.startActivity(intent);

            if (mArticles.size() > 3) {


            }

            /*else{
                Intent intent = new Intent(context,ArticleWebViewActivity.class);
                intent.putExtra("ArticleURL", mArticles.get(0).getUrl());
                intent.putExtra("substory_id", mStoryData.getSubstories().get(1).getSubstory_id());
                intent.putExtra("storycolor", storyColor);
                context.startActivity(intent);
            }*/
        } else if (mStoryData.getSubstories().size() == 1) {
            List<ArticlesMain> mArticles = mStoryData.getSubstories().get(0).getArticlesMain();
            Intent intent = new Intent(context, ArticlesActivity.class);
            intent.putExtra("ARTICLELIST", (Serializable) mArticles);
            intent.putExtra("StoryTitle", storyTitle);
            intent.putExtra("substory_id", mStoryData.getSubstoryId());
            intent.putExtra("story_color", storyColor);
            context.startActivity(intent);

            if (mArticles.size() > 3) {


            }/*else{
                Intent intent = new Intent(context,ArticleWebViewActivity.class);
                intent.putExtra("ArticleURL", mArticles.get(0).getUrl());
                intent.putExtra("substory_id", mStoryData.getSubstories().get(0).getSubstory_id());
                intent.putExtra("storycolor", storyColor);
                context.startActivity(intent);
            }*/
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView hellouser, managehome, followedtext, followedstoriesmessage, followedseeall, numberfollowingupdates, fromyourfollowedText;

        public LinearLayout notificationNumberLayout;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            hellouser = (TextView) itemView.findViewById(R.id.hello_user);
            managehome = (TextView) itemView.findViewById(R.id.manage_home);
            followedtext = (TextView) itemView.findViewById(R.id.followedtext);
            followedstoriesmessage = (TextView) itemView.findViewById(R.id.follwedstories_message);
            followedseeall = (TextView) itemView.findViewById(R.id.followedseeall);
            numberfollowingupdates = (TextView) itemView.findViewById(R.id.numberfollowingupdates);
            fromyourfollowedText = (TextView) itemView.findViewById(R.id.fromyourfollowedtext);

            notificationNumberLayout = (LinearLayout) itemView.findViewById(R.id.notificationNumber);

            managehome.setOnClickListener(this);
            followedseeall.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == managehome) {

                pref = PreferenceManager.getDefaultSharedPreferences(context);


                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                if (pref.getString("filter_count", null) != null) {
                    nparams.putString("count_category", pref.getString("filter_count", null));
                }
                mFirebaseAnalytics.logEvent("tap_managehome", nparams);

                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("resetfilter", "0");
                context.startActivity(intent);
            }

            if (view == followedseeall) {

                Bundle pparams = new Bundle();
                pparams.putString("screen_name", "home_screen");
                pparams.putString("category", "tap"); //(only if possible)
                pparams.putString("name_section", "from_your_followed");
                mFirebaseAnalytics.logEvent("tap_seeall", pparams);

                NavigationActivity activity = (NavigationActivity) context;
                activity.gotoFragment(2);

            }
        }
    }

    public class MiddleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView Moreforyou, moreseeAll;

        public MiddleViewHolder(View itemView) {
            super(itemView);

            Moreforyou = (TextView) itemView.findViewById(R.id.moreforyou);
            moreseeAll = (TextView) itemView.findViewById(R.id.moreseeall);
        }

        @Override
        public void onClick(View view) {
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bigcard_main, parent, false);
            return new StoryDataViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_home, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_MIDDLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_middle, parent, false);
            return new MiddleViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        } else if (position == StoryDataList.size() + 1) {
            return TYPE_MIDDLE;
        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.e("Position", String.valueOf(position));


        if (holder instanceof StoryDataViewHolder) {


            ((StoryDataViewHolder) holder).seenImage.setVisibility(View.GONE);
            ((StoryDataViewHolder) holder).readstatus.setVisibility(View.GONE);

            ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).thirdtopstorylayout.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).firstarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).secondarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).thirdarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).bottomtextshowmore.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).bottomtextnumarticles.setVisibility(View.VISIBLE);


            position = position - 1;


            if (position < StoryDataList.size()) {
                StoryDataMain mStoryData = StoryDataList.get(position);
                String followstatus = StoryDataList.get(position).getFollowstatus();

                fillUI((StoryDataViewHolder) holder, position, mStoryData);
                followstatus(followstatus, (StoryDataViewHolder) holder);
            } else if (position > StoryDataList.size()) {
                position = position - StoryDataList.size() - 1;

                String followstatus = nStoryDataList.get(position).getFollowstatus();
                followstatus(followstatus, (StoryDataViewHolder) holder);
                StoryDataMain mStoryData = nStoryDataList.get(position);

                fillUI((StoryDataViewHolder) holder, position, mStoryData);


            }
        } else if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).hellouser.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).followedtext.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).managehome.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).followedseeall.setVisibility(View.VISIBLE);
            ((HeaderViewHolder) holder).followedstoriesmessage.setVisibility(View.VISIBLE);

            ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.GONE);
            ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.INVISIBLE);

            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

            String hello = "Hello ";

            if (pref.getString(SoupContract.FIRST_NAME, null) != null && !pref.getString(SoupContract.FIRST_NAME, null).isEmpty()) {

                String userName = pref.getString(SoupContract.FIRST_NAME, null);
                hello = "Hello " + userName;
            }

            ((HeaderViewHolder) holder).hellouser.setText(hello);
            ((HeaderViewHolder) holder).managehome.setText("Manage Home");

            if (followcount != null && !followcount.isEmpty()) {
                ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.VISIBLE);


                if (followUpdateCount != null && !followUpdateCount.isEmpty()) {

                    if (followUpdateCount.equals("0") && followcount.equals("0")) {
                        ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.GONE);

                        String browsetext = context.getString(R.string.browsetext);
                        Spannable ss1 = new SpannableString(browsetext);
                        ss1.setSpan(new RelativeSizeSpan(1.1f), 37, 48, 0);
                        ss1.setSpan(new StyleSpan(Typeface.BOLD), 37, 48, 0);
                        ((HeaderViewHolder) holder).followedstoriesmessage.setText(ss1);
                        ((HeaderViewHolder) holder).followedseeall.setVisibility(View.GONE);
                        ((HeaderViewHolder) holder).fromyourfollowedText.setText("You are not following any story");
                        ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.VISIBLE);

                    } else if (Integer.valueOf(followUpdateCount) > 0) {
                        ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.VISIBLE);
                        ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.GONE);
                        ((HeaderViewHolder) holder).followedstoriesmessage.setText(followUpdateCount + " of your followed stories got updated " +
                                "while you were away");

                        if (Integer.valueOf(followUpdateCount) < 10) {
                            ((HeaderViewHolder) holder).numberfollowingupdates.setText("0" + followUpdateCount);
                        } else {
                            ((HeaderViewHolder) holder).numberfollowingupdates.setText(followUpdateCount);
                        }

                    } else if (Integer.valueOf(followUpdateCount) == 0) {
                        ((HeaderViewHolder) holder).fromyourfollowedText.setText(R.string.followingtextstatus);
                        ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.VISIBLE);
                        ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.GONE);
                        ((HeaderViewHolder) holder).followedstoriesmessage.setText(R.string.followingtexthome);

                    }

                }

            } else if (followUpdateCount != null && !followUpdateCount.isEmpty()) {
                if (followUpdateCount.equals("0")) {
                    ((HeaderViewHolder) holder).followedstoriesmessage.setText(R.string.browsetext);
                    ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.GONE);
                    ((HeaderViewHolder) holder).fromyourfollowedText.setText("You are not following any story");
                    ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.VISIBLE);

                } else if (Integer.valueOf(followUpdateCount) > 0) {
                    ((HeaderViewHolder) holder).notificationNumberLayout.setVisibility(View.VISIBLE);
                    ((HeaderViewHolder) holder).fromyourfollowedText.setVisibility(View.GONE);
                    ((HeaderViewHolder) holder).followedstoriesmessage.setText(followUpdateCount + " of your followed stories got updated " +
                            "while you were away");
                    if (Integer.valueOf(followUpdateCount) < 10) {
                        ((HeaderViewHolder) holder).numberfollowingupdates.setText("0" + followUpdateCount);
                    } else {
                        ((HeaderViewHolder) holder).numberfollowingupdates.setText(followUpdateCount);
                    }
                }
            }


            ((HeaderViewHolder) holder).followedtext.setText("New in Followed");
            ((HeaderViewHolder) holder).followedseeall.setText("See All");

        } else if (holder instanceof MiddleViewHolder) {
            ((MiddleViewHolder) holder).Moreforyou.setVisibility(View.VISIBLE);

            ((MiddleViewHolder) holder).Moreforyou.setText("More Stories For You");
            ((MiddleViewHolder) holder).moreseeAll.setText("See All");
        }

    }

    private void followstatus(String followstatus, StoryDataViewHolder holder) {
        if (followstatus != null && !followstatus.isEmpty()) {
            if (followstatus.equals("1")) {
                ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_ok);
            } else if (followstatus.equals("0")) {
                ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_plus_filled);
            }
        } else {
            ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_plus_filled);
        }
    }


    private void fillUI(StoryDataViewHolder holder, int position, StoryDataMain mStoryData) {
        String storytitle = mStoryData.getStoryNameMain();
        Log.e("Story ID", mStoryData.getStoryIdMain());

        if (mStoryData.getReadStatus() != null && !mStoryData.getReadStatus().isEmpty()) {

            ((StoryDataViewHolder) holder).readstatus.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).seenImage.setVisibility(View.VISIBLE);

        }


        ((StoryDataViewHolder) holder).storyTitle.setText(storytitle);

        String countSubstories = mStoryData.getNumberSubstories();

        String Readstatus = mStoryData.getReadStatus();

        if (mStoryData.getCategoryName() != null && !mStoryData.getCategoryName().isEmpty()) {
            // holder.categoryname.setVisibility(View.VISIBLE);
            String category = mStoryData.getCategoryName();
            String Id = mStoryData.getCategoryId();

            int filter = getDrawable(Id);

            if (filter != 0) {
                ((StoryDataViewHolder) holder).filterImage.setImageResource(filter);
                ((StoryDataViewHolder) holder).filterImage.setColorFilter(Color.parseColor("#cbffffff"));
            }

        }


        if (mStoryData.getHexColor() != null && !mStoryData.getHexColor().isEmpty()) {

            // holder.categoryname.setVisibility(View.VISIBLE);
            String color = mStoryData.getHexColor();
            ((StoryDataViewHolder) holder).circle1.setColorFilter(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).circle2.setColorFilter(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).circle3.setColorFilter(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline1.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline2.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline3.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline4.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline5.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).sideline6.setBackgroundColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).filterImage.setColorFilter(Color.parseColor("#" + color));
            //holder.categoryname.setTextColor(Color.parseColor("#" + mStoryData.getCategoryColour()));
            ((StoryDataViewHolder) holder).viewfullstory.setTextColor(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).followicon.setColorFilter(Color.parseColor("#" + color));
            ((StoryDataViewHolder) holder).bottomline.setBackgroundColor(Color.parseColor("#33" + color));
            ((StoryDataViewHolder) holder).bottomline1.setBackgroundColor(Color.parseColor("#33" + color));
            ((StoryDataViewHolder) holder).bottomline2.setBackgroundColor(Color.parseColor("#33" + color));
            ((StoryDataViewHolder) holder).bottomtextshowmore.setTextColor(Color.parseColor("#" + color));
        }

        if (mStoryData.getSubstories().size() == 3) {

            List<SubstoriesMain> Substories = mStoryData.getSubstories();

            String update1 = "Update " + Substories.get(0).getUpdateNumber();
            String update2 = "Update " + Substories.get(1).getUpdateNumber();
            String update3 = "Update " + Substories.get(2).getUpdateNumber();

            ((StoryDataViewHolder) holder).update1.setText(update1);
            ((StoryDataViewHolder) holder).update2.setText(update2);
            ((StoryDataViewHolder) holder).update3.setText(update3);

            String time1 = Substories.get(0).getTopArticleDate();
            String time2 = Substories.get(1).getTopArticleDate();
            String time3 = Substories.get(2).getTopArticleDate();

            time1 = getTime(time1);
            time2 = getTime(time2);
            time3 = getTime(time3);

            ((StoryDataViewHolder) holder).time1.setText(time1);
            ((StoryDataViewHolder) holder).time2.setText(time2);
            ((StoryDataViewHolder) holder).time3.setText(time3);

            String substorytitle1 = Substories.get(0).getSubStoryName();
            String substorytitle2 = Substories.get(1).getSubStoryName();

            substorytitle1 = gethtmlString(substorytitle1);
            substorytitle2 = gethtmlString(substorytitle2);

            ((StoryDataViewHolder) holder).substoryTitle1.setText(substorytitle1);
            ((StoryDataViewHolder) holder).substoryTitle2.setText(substorytitle2);

            fillarticles(mStoryData, (StoryDataViewHolder) holder, position, 2);
            fillImage(mStoryData, (StoryDataViewHolder) holder, position, 2);

        }

        if (mStoryData.getSubstories().size() == 2) {

            ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(View.GONE);

            List<SubstoriesMain> Substories = mStoryData.getSubstories();

            String update1 = "Update " + Substories.get(0).getUpdateNumber();
            String update2 = "Update " + Substories.get(1).getUpdateNumber();

            ((StoryDataViewHolder) holder).update2.setText(update1);
            ((StoryDataViewHolder) holder).update3.setText(update2);

            String time1 = Substories.get(0).getTopArticleDate();
            String time2 = Substories.get(1).getTopArticleDate();

            time1 = getTime(time1);
            time2 = getTime(time2);

            ((StoryDataViewHolder) holder).time2.setText(time1);
            ((StoryDataViewHolder) holder).time3.setText(time2);

            String substorytitle1 = Substories.get(0).getSubStoryName();

            substorytitle1 = gethtmlString(substorytitle1);

            ((StoryDataViewHolder) holder).substoryTitle2.setText(substorytitle1);

            fillarticles(mStoryData, ((StoryDataViewHolder) holder), position, 1);
            fillImage(mStoryData, ((StoryDataViewHolder) holder), position, 1);
        }


        if (mStoryData.getSubstories().size() == 1) {

            ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(View.GONE);
            ((StoryDataViewHolder) holder).thirdtopstorylayout.setVisibility(View.GONE);

            List<SubstoriesMain> Substories = mStoryData.getSubstories();

            String update1 = "Update " + Substories.get(0).getUpdateNumber();

            ((StoryDataViewHolder) holder).update3.setText(update1);

            String time1 = Substories.get(0).getTopArticleDate();

            time1 = getTime(time1);
            ((StoryDataViewHolder) holder).time3.setText(time1);

            fillarticles(mStoryData, ((StoryDataViewHolder) holder), position, 0);
            fillImage(mStoryData, ((StoryDataViewHolder) holder), position, 0);
        }
    }

    private void fillImage(StoryDataMain mStoryData, StoryDataViewHolder holder, int position, int i) {

        String ImageUrl = mStoryData.getSubstories().get(i).getTopArticleImage();

        if (ImageUrl != null && !ImageUrl.isEmpty()) {

            Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.heroimage);

        } else {
            holder.heroimage.setImageResource(R.drawable.background_splash);
        }
    }

    public void fillarticles(StoryDataMain mStoryData, StoryDataViewHolder holder, int position, int i) {

        if (mStoryData.getSubstories().get(i).getArticlesMain().size() >= 3) {

            List<ArticlesMain> Articles = mStoryData.getSubstories().get(i).getArticlesMain();

            String source1 = Articles.get(0).getsourceName();
            String source2 = Articles.get(1).getsourceName();
            String source3 = Articles.get(2).getsourceName();

            holder.source1.setText(source1);
            holder.source2.setText(source2);
            holder.source3.setText(source3);

            String source1time = Articles.get(0).getPubDate();
            String source2time = Articles.get(1).getPubDate();
            String source3time = Articles.get(2).getPubDate();

            source1time = getTimedateformat(source1time);
            source2time = getTimedateformat(source2time);
            source3time = getTimedateformat(source3time);

            holder.source1time.setText(source1time);
            holder.source2time.setText(source2time);
            holder.source3time.setText(source3time);


            String source1icon = Articles.get(0).getSourceThumb();
            String source2icon = Articles.get(1).getSourceThumb();
            String source3icon = Articles.get(2).getSourceThumb();

            String articleTitle1 = Articles.get(0).getTitle();
            String articleTitle2 = Articles.get(1).getTitle();
            String articleTitle3 = Articles.get(2).getTitle();

            articleTitle1 = gethtmlString(articleTitle1);
            articleTitle2 = gethtmlString(articleTitle2);
            articleTitle3 = gethtmlString(articleTitle3);

            holder.articletitle1.setText(articleTitle1);
            holder.articletitle2.setText(articleTitle2);
            holder.articletitle3.setText(articleTitle3);

            int numberofArticles = mStoryData.getSubstories().get(i).getArticlesMain().size();
            numberofArticles = numberofArticles - 3;
            if (numberofArticles != 0) {
                String bottomtext = String.valueOf(numberofArticles) + " More Articles In This Update.";
                holder.bottomtextnumarticles.setText(bottomtext);
                holder.bottomtextshowmore.setText("SHOW ALL");
            }

            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.background_splash);
            }


            if (source3icon != null && !source3icon.isEmpty()) {
                Picasso.with(context).load(source3icon).placeholder(R.drawable.placeholder).into(holder.sourceimage3);
            } else {
                holder.sourceimage3.setImageResource(R.drawable.background_splash);
            }

            if (mStoryData.getSubstories().get(i).getArticlesMain().size() == 3) {

                holder.bottomtextshowmore.setVisibility(View.GONE);
                holder.bottomtextnumarticles.setVisibility(View.GONE);
            }


        } else if (mStoryData.getSubstories().get(i).getArticlesMain().size() == 2) {

            List<ArticlesMain> Articles = mStoryData.getSubstories().get(i).getArticlesMain();

            String source1 = Articles.get(0).getsourceName();
            String source2 = Articles.get(1).getsourceName();

            holder.source1.setText(source1);
            holder.source2.setText(source2);


            String source1time = Articles.get(0).getPubDate();
            String source2time = Articles.get(1).getPubDate();

            source1time = getTimedateformat(source1time);
            source2time = getTimedateformat(source2time);

            holder.source1time.setText(source1time);
            holder.source2time.setText(source2time);

            String source1icon = Articles.get(0).getSourceThumb();
            String source2icon = Articles.get(1).getSourceThumb();

            String articleTitle1 = Articles.get(0).getTitle();
            String articleTitle2 = Articles.get(1).getTitle();

            articleTitle1 = gethtmlString(articleTitle1);
            articleTitle2 = gethtmlString(articleTitle2);

            holder.articletitle1.setText(articleTitle1);
            holder.articletitle2.setText(articleTitle2);

            holder.bottomtextshowmore.setVisibility(View.GONE);
            holder.bottomtextnumarticles.setVisibility(View.GONE);

            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.background_splash);
            }

            holder.thirdarticle.setVisibility(View.GONE);

        } else if (mStoryData.getSubstories().get(i).getArticlesMain().size() == 1) {

            List<ArticlesMain> Articles = mStoryData.getSubstories().get(i).getArticlesMain();

            String source1 = Articles.get(0).getsourceName();
            holder.source1.setText(source1);


            String source1time = Articles.get(0).getPubDate();
            source1time = getTimedateformat(source1time);
            holder.source1time.setText(source1time);

            String source1icon = Articles.get(0).getSourceThumb();

            String articleTitle1 = Articles.get(0).getTitle();

            articleTitle1 = gethtmlString(articleTitle1);

            holder.articletitle1.setText(articleTitle1);


            holder.bottomtextshowmore.setVisibility(View.GONE);
            holder.bottomtextnumarticles.setVisibility(View.GONE);


            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
            }

            holder.thirdarticle.setVisibility(View.GONE);
            holder.secondarticle.setVisibility(View.GONE);
        }
    }

    public String gethtmlString(String title) {

        String titlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            titlehtml = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));

        } else {
            titlehtml = String.valueOf(Html.fromHtml(title));
        }

        return titlehtml;
    }


    @Override
    public int getItemCount() {

        //Log.d("tech memw",StoryDataList.size() +" ");
        return StoryDataList.size() + nStoryDataList.size() + 2;
    }

    public String timeFormat(String string) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse(string);

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("HH:mm a");

        return monthFormat2.format(date);


    }


    public String monthFomrat(String string) throws ParseException {
        SimpleDateFormat monthformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = monthformat.parse(string);

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("MMM");

        return monthFormat2.format(date);


    }

    public String yearFomrat(String string) throws ParseException {
        SimpleDateFormat yearformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = yearformat.parse(string);

        SimpleDateFormat yearFormat2 = new SimpleDateFormat("yyyy");

        return yearFormat2.format(date);


    }


    public String DateFomrat(String string) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = dateformat.parse(string);


        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");

        return dateFormat2.format(date);


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

    public String getTime(String timeString) {

        String Time = timeString;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());


        Date date1 = null;
        Date date2 = null;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date1 = format.parse(Time);
            date2 = format.parse(currentTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long Difference = date2.getTime() - date1.getTime();


        int i = (int) (Difference / 86400000);

        if (i < 1) {

            int m = (int) (Difference / 3600000);

            if (m > 1) {
                return String.valueOf(m) + " hours ago";
            } else {
                return "1 hour ago";
            }

        } else {

            String time = null;
            try {
                time = timeFormat(Time);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            String month = null;
            try {
                month = monthFomrat(Time);
            } catch (ParseException e) {
                e.printStackTrace();
                Log.d("Not valid time", Time);
            }

            // Log.d("Month", month);


            String Date = null;
            try {
                Date = DateFomrat(Time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String year = null;
            try {
                year = yearFomrat(Time);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return time + ", " + Date + " " + month + " " + year;

        }

    }

    public String getTimedateformat(String timeString) {

        String Time = timeString;

        String time = null;
        try {
            time = timeFormat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String month = null;
        try {
            month = monthFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Not valid time", Time);
        }

        // Log.d("Month", month);


        String Date = null;
        try {
            Date = DateFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String year = null;
        try {
            year = yearFomrat(Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return time + ", " + Date + " " + month + " " + year;

    }
}

