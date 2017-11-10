package in.thesoup.thesoupstoriesnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.ArticleWebViewActivity;
import in.thesoup.thesoupstoriesnews.activities.ArticlesActivity;
import in.thesoup.thesoupstoriesnews.activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.SubstoriesMain;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter2;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;

import static android.view.View.GONE;

/**
 * Created by Jani on 05-09-2017.
 */

public class StoryFeedAdapterMain extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;


    private List<StoryDataMain> StoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;


    public StoryFeedAdapterMain(List<StoryDataMain> Datalist, Context context, int fragmenttag) {
        this.StoryDataList = Datalist;
        this.context = context;
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

    public void refreshData(List<StoryDataMain> Datalist) {
        //this.StoryDataList = Datalist;
        StoryDataList.addAll(Datalist);
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<StoryDataMain> Datalist) {
        this.StoryDataList = Datalist;
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryDataMain> Datalist) {
        this.StoryDataList = Datalist;

        notifyDataSetChanged();

    }


    public class StoryDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView storyTitle1, viewfullstory, update1, time1, substoryTitle1, update2, time2, substoryTitle2, update3, time3, source1, source2,
                source3, source1time, source2time, source3time, articletitle1, articletitle2, articletitle3, bottomtextshowmore,
                bottomtextnumarticles, readstatus;

        public ImageView followicon, filterImage, heroimage, sourceimage1, sourceimage2, sourceimage3,
                circle1, circle2, circle3, seenImage;

        public RelativeLayout secondtopstorylayout, thirdtopstorylayout, topstorylayout;

        public LinearLayout firstarticle, secondarticle, thirdarticle, followlayout, shareicon;

        public View sideline1, sideline2, sideline3, sideline4, sideline5, sideline6, bottomline, bottomline1, bottomline2;

        public StoryDataViewHolder(View itemView) {
            super(itemView);

            storyTitle1 = (TextView) itemView.findViewById(R.id.story_name);
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
            shareicon = (LinearLayout) itemView.findViewById(R.id.shareicon);
            sourceimage1 = (ImageView) itemView.findViewById(R.id.sourceImage1);
            sourceimage2 = (ImageView) itemView.findViewById(R.id.sourceImage2);
            sourceimage3 = (ImageView) itemView.findViewById(R.id.sourceImage3);
            seenImage = (ImageView) itemView.findViewById(R.id.readstatus_story);
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
            topstorylayout = (RelativeLayout) itemView.findViewById(R.id.topstory_layout);

            firstarticle = (LinearLayout) itemView.findViewById(R.id.firstarticle);
            secondarticle = (LinearLayout) itemView.findViewById(R.id.secondarticle);
            thirdarticle = (LinearLayout) itemView.findViewById(R.id.thirdarticle);
            followlayout = (LinearLayout) itemView.findViewById(R.id.follow_icon_layout);

            firstarticle.setOnClickListener(this);
            secondarticle.setOnClickListener(this);
            thirdarticle.setOnClickListener(this);
            topstorylayout.setOnClickListener(this);

            bottomtextnumarticles.setOnClickListener(this);
            bottomtextshowmore.setOnClickListener(this);
            viewfullstory.setOnClickListener(this);

            followicon.setOnClickListener(this);

            shareicon.setOnClickListener(this);

            followlayout.setOnClickListener(this);

            storyTitle1.setOnClickListener(this);

            heroimage.setOnClickListener(this);
            seenImage.setOnClickListener(this);
            readstatus.setOnClickListener(this);

            secondtopstorylayout.setOnClickListener(this);
            thirdtopstorylayout.setOnClickListener(this);


            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            int mposition = getAdapterPosition() - 1;

            StoryDataMain mStoryData = StoryDataList.get(mposition);
            String StoryId = mStoryData.getStoryIdMain();
            String storyColor = mStoryData.getHexColor();
            String storyTitle = mStoryData.getStoryNameMain();
            String category = mStoryData.getCategoryName();
            String categoryId = mStoryData.getCategoryId();
            String substoryname ="";
            String substoryId="";
            if( mStoryData.getSubstories().size()==3){
                substoryname = mStoryData.getSubstories().get(2).getSubStoryName();
                substoryId = mStoryData.getSubstories().get(2).getSubStoryName();
            }else if (mStoryData.getSubstories().size()==2){
                substoryname = mStoryData.getSubstories().get(1).getSubStoryName();
                substoryId = mStoryData.getSubstories().get(1).getSubStoryName();
            }else if(mStoryData.getSubstories().size()==1){
                substoryname = mStoryData.getSubstories().get(0).getSubStoryName();
                substoryId = mStoryData.getSubstories().get(0).getSubStoryName();
            }


            if (view == bottomtextnumarticles || view == bottomtextshowmore || view == heroimage || view == readstatus || view == seenImage) {

                if (view == heroimage || view == seenImage || view == readstatus) {
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "discover_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("story_id", StoryId);
                    nparams.putString("story_Title", storyTitle);
                    mFirebaseAnalytics.logEvent("tap_card_cover", nparams);

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "discover_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("position_card_collection", String.valueOf(mposition));
                    oparams.put("story_id", StoryId);
                    oparams.put("story_Title", storyTitle);
                    oparams.put("substory_name",substoryname);
                    oparams.put("substory_id",substoryId);
                    cleverTap.event.push("tap_card_cover", oparams);

                }

                if (view == bottomtextnumarticles || view == bottomtextshowmore) {
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "discover_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("story_id", StoryId);
                    nparams.putString("story_Title", storyTitle);
                    mFirebaseAnalytics.logEvent("tap_showall", nparams);

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "discover_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("position_card_collection", String.valueOf(mposition));
                    oparams.put("story_id", StoryId);
                    oparams.put("story_Title", storyTitle);
                    oparams.put("substory_name",substoryname);
                    oparams.put("substory_id",substoryId);
                    cleverTap.event.push("tap_showall", oparams);

                }


                if (mStoryData.getSubstories().size() == 3) {
                    List<ArticlesMain> mArticles = mStoryData.getSubstories().get(2).getArticlesMain();
                    Intent intent = new Intent(context, ArticlesActivity.class);
                    intent.putExtra("ARTICLELIST", (Serializable) mArticles);
                    intent.putExtra("StoryTitle", storyTitle);
                    intent.putExtra("substory_id", mStoryData.getSubstoryId());
                    intent.putExtra("story_color", storyColor);
                    context.startActivity(intent);
                } else if (mStoryData.getSubstories().size() == 2) {

                    List<ArticlesMain> mArticles = mStoryData.getSubstories().get(1).getArticlesMain();
                    Intent intent = new Intent(context, ArticlesActivity.class);
                    intent.putExtra("ARTICLELIST", (Serializable) mArticles);
                    intent.putExtra("StoryTitle", storyTitle);
                    intent.putExtra("substory_id", mStoryData.getSubstoryId());
                    intent.putExtra("story_color", storyColor);
                    context.startActivity(intent);

                } else if (mStoryData.getSubstories().size() == 1) {
                    List<ArticlesMain> mArticles = mStoryData.getSubstories().get(0).getArticlesMain();
                    Intent intent = new Intent(context, ArticlesActivity.class);
                    intent.putExtra("ARTICLELIST", (Serializable) mArticles);
                    intent.putExtra("StoryTitle", storyTitle);
                    intent.putExtra("substory_id", mStoryData.getSubstoryId());
                    intent.putExtra("story_color", storyColor);
                    context.startActivity(intent);
                }
            }


            if (view == firstarticle) {

                //TODO: the article source and information
                articleClickHandle(mStoryData, 0);

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "discover_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(mposition));
                nparams.putString("position_card_entity", String.valueOf(1));
                mFirebaseAnalytics.logEvent("tap_entity", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "discover_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(mposition));
                oparams.put("position_card_entity", String.valueOf(1));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_entity", oparams);


            }

            if (view == secondarticle) {
                articleClickHandle(mStoryData, 1);
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "discover_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(mposition));
                nparams.putString("position_card_entity", String.valueOf(2));
                mFirebaseAnalytics.logEvent("tap_entity", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "discover_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(mposition));
                oparams.put("position_card_entity", String.valueOf(2));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_entity", oparams);

            }

            if (view == thirdarticle) {
                articleClickHandle(mStoryData, 2);
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "discover_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(mposition));
                nparams.putString("position_card_entity", String.valueOf(3));
                mFirebaseAnalytics.logEvent("tap_entity", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "discover_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(mposition));
                oparams.put("position_card_entity", String.valueOf(3));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_entity", oparams);

            }

            if (view == viewfullstory || view == storyTitle1 || view == secondtopstorylayout || view == thirdtopstorylayout || view == topstorylayout) {


                if (view == viewfullstory || view == storyTitle1 || view == topstorylayout) {

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    mFirebaseAnalytics.logEvent("tap_viewstory", nparams);

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "home_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("position_card_collection", String.valueOf(mposition));
                    oparams.put("substory_name",substoryname);
                    oparams.put("substory_id",substoryId);
                    cleverTap.event.push("tap_viewstory", oparams);


                    goToDetails(mStoryData, "0");

                }


                if (view == secondtopstorylayout) {

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "discover_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("position_card_old", String.valueOf(1));
                    mFirebaseAnalytics.logEvent("tap_card_old", nparams);

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "discover_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("position_card_collection", String.valueOf(mposition));
                    oparams.put("position_card_old", String.valueOf(1));
                    cleverTap.event.push("tap_card_old", oparams);

                    goToDetails(mStoryData, "2");

                }

                if (view == thirdtopstorylayout) {
                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "home_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection", String.valueOf(mposition));
                    nparams.putString("position_card_old", String.valueOf(2));
                    mFirebaseAnalytics.logEvent("tap_card_old", nparams);

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "home_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("position_card_collection", String.valueOf(mposition));
                    oparams.put("position_card_old", String.valueOf(2));
                    cleverTap.event.push("tap_card_old", oparams);

                    goToDetails(mStoryData, "1");

                }
            }

            if (view == followicon || view == followlayout) {
                clickposition = mposition;
                clickStoryId = StoryId;
                clickStoryName = storyTitle;

                followstory();
            }

            if (view == shareicon) {

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "discover_screen");
                nparams.putString("category", "conversion"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(mposition));
                mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "discover_screen");
                oparams.put("category", "conversion"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(mposition));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_shareicon", oparams);

                int n = StoryDataList.get(mposition).getSubstories().size();

                String URL = "http://thesoup.in/share/" + mStoryData.getStoryIdMain() + "/" + mStoryData.getSubstories().get(n-1).getSubstory_id()+"?utm_source=appshare&utm_medium="+mStoryData.getStoryIdMain()+"&utm_campaign="+ mStoryData.getSubstories().get(n-1).getSubstory_id();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share via");
                i.putExtra(Intent.EXTRA_TEXT, URL);
                context.startActivity(Intent.createChooser(i, "Share via"));


            }

        }

    }

    public void goToDetails(StoryDataMain mStoryData, String cardnumber) {
        String StoryId = mStoryData.getStoryIdMain();
        String storytitle = mStoryData.getStoryNameMain();
        String hex_colour = mStoryData.getHexColor();
        String category = mStoryData.getCategoryName();
        String categoryId = mStoryData.getCategoryId();
        String followstatus = mStoryData.getFollowstatus();


        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("story_id", StoryId);
        intent.putExtra("storytitle", storytitle);
        intent.putExtra("followstatus", followstatus);
        intent.putExtra("fragmenttag", fragmenttag);
        intent.putExtra("category_id", categoryId);
        intent.putExtra("hex_colour", hex_colour);
        intent.putExtra("cardnumber", cardnumber);
        context.startActivity(intent);
    }

    public void followstory() {

        String mfollowstatus = "";

        if (StoryDataList.get(clickposition).getFollowstatus() != null && !StoryDataList.get(clickposition).getFollowstatus().isEmpty()) {
            mfollowstatus = StoryDataList.get(clickposition).getFollowstatus();
        }

        pref = PreferenceManager.getDefaultSharedPreferences(context);


        Bundle mparams = new Bundle();
        mparams.putString("collection_id", clickStoryId);
        mparams.putString("collection_name", clickStoryName);
        mparams.putString("position_card_collection", String.valueOf(clickposition));
        mparams.putString("category", "conversion");


        HashMap<String, Object> oparams = new HashMap<>();
        oparams.put("collection_id", clickStoryId);
        oparams.put("collection_name", clickStoryName);
        oparams.put("position_card_collection", String.valueOf(clickposition));
        oparams.put("category", "conversion");

        if (mfollowstatus.equals("") || mfollowstatus.equals("0") || TextUtils.isEmpty(mfollowstatus)) {

            //Analytics
            mFirebaseAnalytics.logEvent("tap_add", mparams);
            cleverTap.event.push("tap_add", oparams);
            HashMap<String, String> params = new HashMap<>();

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);
            NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
            followrequest.followRequest(clickposition, fragmenttag);


        } else if (mfollowstatus.equals("1")) {


            mFirebaseAnalytics.logEvent("tap_remove", mparams);
            cleverTap.event.push("tap_remove", oparams);
            HashMap<String, String> params = new HashMap<>();
            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);

            Log.d("Check mStorydata size", String.valueOf(StoryDataList.size()));

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

            unFollowrequest.unFollowRequest(clickposition, fragmenttag);


        }
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

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout buttonRelative;

        public TextView buttonText;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            buttonRelative = (RelativeLayout) itemView.findViewById(R.id.discover_button);
            buttonText = (TextView) itemView.findViewById(R.id.filter_discover);


            buttonRelative.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            if (view == buttonRelative) {


                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "discover_screen");
                nparams.putString("category", "tap"); //(only if possible)


                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "discover_screen");
                oparams.put("category", "tap"); //(only if possible)

                NavigationActivity activity = (NavigationActivity) context;
                PrefUtilFilter2 prefUtil = new PrefUtilFilter2(activity);

                if (prefUtil.getFilterlistSize() != null && !prefUtil.getFilterlistSize().isEmpty()) {
                    nparams.putString("count_filter_category", prefUtil.getFilterlistSize());
                    oparams.put("count_filter_category", prefUtil.getFilterlistSize());
                } else {
                    nparams.putString("count_filter_category", "no categories");
                    oparams.put("count_filter_category", "no categories");
                }
                mFirebaseAnalytics.logEvent("tap_filters_expand", nparams);
                cleverTap.event.push("tap_filters_expand", oparams);

                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("resetfilter", "1");
                context.startActivity(intent);
            }

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bigcard_main, parent, false);
            return new StoryDataViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discoverheader, parent, false);
            return new HeaderViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

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


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof StoryDataViewHolder) {
            Log.e("story_id discover", StoryDataList.get(position - 1).getStoryIdMain());
            ((StoryDataViewHolder) holder).seenImage.setVisibility(GONE);
            ((StoryDataViewHolder) holder).readstatus.setVisibility(GONE);

            ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).thirdtopstorylayout.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).firstarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).secondarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).thirdarticle.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).bottomtextshowmore.setVisibility(View.VISIBLE);
            ((StoryDataViewHolder) holder).bottomtextnumarticles.setVisibility(View.VISIBLE);


            position = position - 1;
            final StoryDataMain mStoryData = StoryDataList.get(position);
            String followstatus = StoryDataList.get(position).getFollowstatus();

            if (mStoryData.getReadStatus() != null && !mStoryData.getReadStatus().isEmpty()) {
                ((StoryDataViewHolder) holder).seenImage.setVisibility(View.VISIBLE);
                ((StoryDataViewHolder) holder).readstatus.setVisibility(View.VISIBLE);
            }

            if (followstatus != null && !followstatus.isEmpty()) {
                if (followstatus.equals("1")) {
                    ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_ok);
                } else if (followstatus.equals("0")) {
                    ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_plus_filled);
                }
            } else {
                ((StoryDataViewHolder) holder).followicon.setImageResource(R.drawable.icons8_plus_filled);
            }


            String storytitle = mStoryData.getStoryNameMain();

            storytitle = gethtmlString(storytitle);

            ((StoryDataViewHolder) holder).storyTitle1.setText(storytitle);

            String countSubstories = mStoryData.getNumberSubstories();

            String Readstatus = mStoryData.getReadStatus();

            if (mStoryData.getCategoryId() != null && !mStoryData.getCategoryId().isEmpty()) {
                // holder.categoryname.setVisibility(View.VISIBLE);
                String id = mStoryData.getCategoryId();


                int filter = getDrawable(id);

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

            if (StoryDataList.get(position).getSubstories().size() == 3) {

                List<SubstoriesMain> Substories = StoryDataList.get(position).getSubstories();

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

                fillarticles((StoryDataViewHolder) holder, position, 2);
                fillImage((StoryDataViewHolder) holder, position, 2);

            }

            if (StoryDataList.get(position).getSubstories().size() == 2) {

                ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(GONE);

                List<SubstoriesMain> Substories = StoryDataList.get(position).getSubstories();

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

                fillarticles(((StoryDataViewHolder) holder), position, 1);
                fillImage(((StoryDataViewHolder) holder), position, 1);
            }


            if (StoryDataList.get(position).getSubstories().size() == 1) {

                ((StoryDataViewHolder) holder).secondtopstorylayout.setVisibility(GONE);
                ((StoryDataViewHolder) holder).thirdtopstorylayout.setVisibility(GONE);

                List<SubstoriesMain> Substories = StoryDataList.get(position).getSubstories();

                String update1 = "Update " + Substories.get(0).getUpdateNumber();

                ((StoryDataViewHolder) holder).update3.setText(update1);

                String time1 = Substories.get(0).getTopArticleDate();

                time1 = getTime(time1);
                ((StoryDataViewHolder) holder).time3.setText(time1);

                fillarticles(((StoryDataViewHolder) holder), position, 0);
                fillImage(((StoryDataViewHolder) holder), position, 0);
            }

        } else if (holder instanceof HeaderViewHolder) {

            String categories = "All Categories";
            int count = 0;

            String filterfirstname = "";

            pref = PreferenceManager.getDefaultSharedPreferences(context);

            NavigationActivity activity = (NavigationActivity) context;
            PrefUtilFilter prefFilter = new PrefUtilFilter(activity);
            filterfirstname = prefFilter.getFirstFiltername();

            String filterCount = prefFilter.getFilterlistSize();

            if (filterCount != null && !filterCount.isEmpty()) {
                count = Integer.valueOf(filterCount);
            }


            if (filterfirstname != null && !filterfirstname.isEmpty()) {

              /*  int i = 0;
                if(prefFilter.TotalFilterCount()!=null&&!prefFilter.TotalFilterCount().isEmpty()){
                    i= Integer.valueOf(prefFilter.TotalFilterCount());
                }

                Log.e("totalfiltercount",prefFilter.TotalFilterCount());

                if(i>0&&count==i){
                    categories = "All Categories";
                }else*/

                if (count > 1) {
                    categories = filterCount + " categories";


                } else if (count == 1) {
                    categories = filterfirstname;
                }

            }

            ((HeaderViewHolder) holder).buttonText.setText(categories);

        }


    }

    private void fillImage(StoryDataViewHolder holder, int position, int i) {

        String ImageUrl = StoryDataList.get(position).getSubstories().get(i).getTopArticleImage();

        if (ImageUrl != null && !ImageUrl.isEmpty()) {

            Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.heroimage);

        } else {
            holder.heroimage.setImageResource(R.drawable.ic_sample);
        }
    }

    public void fillarticles(StoryDataViewHolder holder, int position, int i) {

        if (StoryDataList.get(position).getSubstories().get(i).getArticlesMain().size() >= 3) {

            List<ArticlesMain> Articles = StoryDataList.get(position).getSubstories().get(i).getArticlesMain();

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


            int numberofArticles = StoryDataList.get(position).getSubstories().get(i).getArticlesMain().size();
            numberofArticles = numberofArticles - 3;
            if (numberofArticles > 0) {
                String bottomtext = String.valueOf(numberofArticles) + " More Articles In This Update.";
                holder.bottomtextnumarticles.setText(bottomtext);
                holder.bottomtextshowmore.setText("SHOW ALL");
            }

            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.ic_launcher);
            }


            if (source3icon != null && !source3icon.isEmpty()) {
                Picasso.with(context).load(source3icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage3);
            } else {
                holder.sourceimage3.setImageResource(R.drawable.ic_launcher);
            }


            if (StoryDataList.get(position).getSubstories().get(i).getArticlesMain().size() == 3) {
                holder.bottomtextshowmore.setVisibility(GONE);
                holder.bottomtextnumarticles.setVisibility(GONE);
            }


        } else if (StoryDataList.get(position).getSubstories().get(i).getArticlesMain().size() == 2) {

            List<ArticlesMain> Articles = StoryDataList.get(position).getSubstories().get(i).getArticlesMain();

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

            holder.bottomtextshowmore.setVisibility(GONE);
            holder.bottomtextnumarticles.setVisibility(GONE);

            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.ic_launcher);
            }

            holder.thirdarticle.setVisibility(GONE);

        } else if (StoryDataList.get(position).getSubstories().get(i).getArticlesMain().size() == 1) {

            List<ArticlesMain> Articles = StoryDataList.get(position).getSubstories().get(i).getArticlesMain();

            String source1 = Articles.get(0).getsourceName();
            holder.source1.setText(source1);


            String source1time = Articles.get(0).getPubDate();
            source1time = getTimedateformat(source1time);
            holder.source1time.setText(source1time);

            String source1icon = Articles.get(0).getSourceThumb();

            String articleTitle1 = Articles.get(0).getTitle();

            articleTitle1 = gethtmlString(articleTitle1);

            holder.articletitle1.setText(articleTitle1);


            holder.bottomtextshowmore.setVisibility(GONE);
            holder.bottomtextnumarticles.setVisibility(GONE);


            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }

            holder.thirdarticle.setVisibility(GONE);
            holder.secondarticle.setVisibility(GONE);
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
        return StoryDataList.size() + 1;
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

        Log.d("difference", String.valueOf(Difference));

        int i = (int) (Difference / 86400000);
        Log.d("kya be", String.valueOf(i));

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


