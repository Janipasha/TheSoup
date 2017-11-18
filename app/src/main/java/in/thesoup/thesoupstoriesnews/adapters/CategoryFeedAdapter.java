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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
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
import com.google.android.gms.plus.model.people.Person;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.activities.ArticleWebViewActivity;
import in.thesoup.thesoupstoriesnews.activities.ArticlesActivity;
import in.thesoup.thesoupstoriesnews.activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.SubstoriesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtilFilter2;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.media.CamcorderProfile.get;
import static android.view.View.GONE;
import static com.uxcam.c.d.m;
import static in.thesoup.thesoupstoriesnews.R.id.circle1;
import static in.thesoup.thesoupstoriesnews.R.id.firstarticle;
import static in.thesoup.thesoupstoriesnews.R.id.follow;
import static in.thesoup.thesoupstoriesnews.R.id.followicon;
import static in.thesoup.thesoupstoriesnews.R.id.sharetext;
import static in.thesoup.thesoupstoriesnews.R.id.sideline1;
import static in.thesoup.thesoupstoriesnews.R.id.time;

/**
 * Created by Jani on 11-11-2017.
 */

public class CategoryFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_DOUBLE = 1;


    private List<StoryDataMain> StoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;


    public CategoryFeedAdapter(List<StoryDataMain> Datalist, Context context, int fragmenttag) {
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

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_NORMAL){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_large_card_nov,parent,false);
            return new StoryDataViewHolder(view);
        }if(viewType==TYPE_DOUBLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_small_card_nov,parent,false);
            return new DoubleDataViewHolder(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof StoryDataViewHolder){
            largeCardFill((StoryDataViewHolder)holder,position);
        }

        if(holder instanceof DoubleDataViewHolder){
            doubleCardFill((DoubleDataViewHolder)holder,position);
        }

    }

    private void largeCardFill(StoryDataViewHolder holder, int position) {

        holder.seenImage.setVisibility(GONE);
        holder.seen.setVisibility(GONE);

        if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
            holder.seenImage.setVisibility(View.VISIBLE);
            holder.seen.setVisibility(View.VISIBLE);
        }

       List<SubstoriesMain> mSubstories = StoryDataList.get(position).getSubstories();
        List<ArticlesMain> mArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();

        String StoryTitle = StoryDataList.get(position).getStoryNameMain();
        String articleText1 = mArticles.get(0).getTitle();
        String source = mArticles.get(0).getsourceName();
        String time =StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();

        time = getTime(time);

        String ImageUrl = mArticles.get(0).getImageUrl();
        String followstatus = StoryDataList.get(position).getFollowstatus();

        StoryTitle = gethtmlString(StoryTitle);
        articleText1 = gethtmlString(articleText1);
        holder.articleTitle.setText(articleText1);

        String numArticles =  String.valueOf(mArticles.size()) + " Articles -";


        if (followstatus != null && !followstatus.isEmpty()) {
            if (followstatus.equals("1")) {
                holder.followIcon.setImageResource(R.drawable.icons8_ok);
            } else if (followstatus.equals("0")) {
                holder.followIcon.setImageResource(R.drawable.icons8_plus_filled);
            }
        } else {
            holder.followIcon.setImageResource(R.drawable.icons8_plus_filled);
        }

        if (StoryDataList.get(position).getCategoryName() != null && !StoryDataList.get(position).getCategoryName().isEmpty()) {
            String category = StoryDataList.get(position).getCategoryName();
            String Id = StoryDataList.get(position).getCategoryId();

            int filter = getDrawable(Id);

            if (filter != 0) {
                holder.filterIcon.setImageResource(filter);
                holder.filterIcon.setColorFilter(Color.parseColor("#cbffffff"));
            }

        }


        if (ImageUrl != null && !ImageUrl.isEmpty()) {

            Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.heroImage);

        } else {
            Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.heroImage);
        }


        holder.source.setText(source);
        holder.numarticles.setText(numArticles);
        holder.time.setText(time);
        holder.share.setText("SHARE");
        holder.storyTitle.setText(StoryTitle);

        if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

            String color = StoryDataList.get(position).getHexColor();
             holder.circle.setColorFilter(Color.parseColor("#" + color));
            holder.sideLine.setBackgroundColor(Color.parseColor("#" + color));
             holder.sideLine1.setBackgroundColor(Color.parseColor("#" + color));
            holder.filterIcon.setColorFilter(Color.parseColor("#" + color));
             holder.followIcon.setColorFilter(Color.parseColor("#" + color));
             holder.bottomLine.setBackgroundColor(Color.parseColor("#33" + color));
             holder.shareIcon.setColorFilter(Color.parseColor("#" + color));
            holder.share.setTextColor(Color.parseColor(("#"+color)));
            holder.numarticles.setTextColor(Color.parseColor("#"+color));
            holder.viewfullstory.setTextColor(Color.parseColor("#"+color));

        }



    }


    private void doubleCardFill(DoubleDataViewHolder holder, int position) {

        List<SubstoriesMain> mSubstories = StoryDataList.get(position).getSubstories();
        List<ArticlesMain> mFirstArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();
        List<ArticlesMain> mSecondArticles = StoryDataList.get(position).getSubstories().get(1).getArticlesMain();
        ArticlesMain FirstArticle = mFirstArticles.get(0);
        ArticlesMain SecondArticle = mSecondArticles.get(0);

        holder.seen1.setVisibility(GONE);
        holder.seen2.setVisibility(GONE);
        holder.seenImage1.setVisibility(GONE);
        holder.seenimage2.setVisibility(GONE);

      if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
          holder.seen1.setVisibility(View.VISIBLE);
          holder.seenImage1.setVisibility(View.VISIBLE);
      }

        if(StoryDataList.get(position).getSubstories().get(1).getReadStatus()!=null&&!StoryDataList.get(position).getSubstories().get(1).getReadStatus().isEmpty()){
            holder.seen2.setVisibility(View.VISIBLE);
            holder.seenimage2.setVisibility(View.VISIBLE);
        }


        String time1 =StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();
        String time2 = StoryDataList.get(position).getSubstories().get(1).getTopArticleDate();
        time1 = getTime(time1);
        time2 = getTime(time2);

        String source1 = FirstArticle.getsourceName();
        String source2 = SecondArticle.getsourceName();


        String articleText1 = FirstArticle.getTitle();
        articleText1 = gethtmlString(articleText1);
        String articleText2 = SecondArticle.getTitle();
        articleText2 = gethtmlString(articleText2);

        String imageUrl1 =FirstArticle.getImageUrl();
        String imageUrl2 = SecondArticle.getImageUrl();
        String numberArticle1 = String.valueOf(mFirstArticles.size())+" Articles -";
        String numberArticles2 = String.valueOf(mSecondArticles.size())+" Articles -";

        holder.articleTitle1.setText(articleText1);
        holder.articleTitle2.setText(articleText2);
        holder.time1.setText(time1);
        holder.time2.setText(time2);
        holder.source1.setText(source1);
        holder.source2.setText(source2);
        holder.share1.setText("SHARE");
        holder.share2.setText("SHARE");

        holder.numArticles1.setText(numberArticle1);
        holder.numArticles2.setText(numberArticles2);

        String StoryTitle = StoryDataList.get(position).getStoryNameMain();

        StoryTitle = gethtmlString(StoryTitle);
        holder.storyTitle.setText(StoryTitle);

        String followstatus = StoryDataList.get(position).getFollowstatus();

        if (followstatus != null && !followstatus.isEmpty()) {
            if (followstatus.equals("1")) {
                holder.followIcon.setImageResource(R.drawable.icons8_ok);
            } else if (followstatus.equals("0")) {
                holder.followIcon.setImageResource(R.drawable.icons8_plus_filled);
            }
        } else {
            holder.followIcon.setImageResource(R.drawable.icons8_plus_filled);
        }

        if (StoryDataList.get(position).getCategoryName() != null && !StoryDataList.get(position).getCategoryName().isEmpty()) {
            String category = StoryDataList.get(position).getCategoryName();
            String Id = StoryDataList.get(position).getCategoryId();

            int filter = getDrawable(Id);

            if (filter != 0) {
                holder.filterIcon.setImageResource(filter);
                holder.filterIcon.setColorFilter(Color.parseColor("#cbffffff"));
            }

        }

        if (imageUrl1 != null && !imageUrl1.isEmpty()) {

            Picasso.with(context).load(imageUrl1).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.articleImage1);

        } else {
            Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.articleImage1);
        }

        if (imageUrl2 != null && !imageUrl2.isEmpty()) {

            Picasso.with(context).load(imageUrl2).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.articleImage2);

        } else {
            Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.articleImage2);
        }


        if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

            String color = StoryDataList.get(position).getHexColor();
            holder.circle1.setColorFilter(Color.parseColor("#" + color));
            holder.circle2.setColorFilter(Color.parseColor("#" + color));

            holder.sideLine1.setBackgroundColor(Color.parseColor("#" + color));
            holder.sideline2.setBackgroundColor(Color.parseColor("#" + color));
            holder.sideline3.setBackgroundColor(Color.parseColor("#" + color));
            holder.sideline4.setBackgroundColor(Color.parseColor("#" + color));
            holder.filterIcon.setColorFilter(Color.parseColor("#" + color));
            holder.followIcon.setColorFilter(Color.parseColor("#" + color));
            holder.bottomLine1.setBackgroundColor(Color.parseColor("#33" + color));
            holder.bottomline2.setBackgroundColor(Color.parseColor("#33" + color));

            holder.shareIcon1.setColorFilter(Color.parseColor("#" + color));
            holder.shareIcon2.setColorFilter(Color.parseColor("#" + color));
            holder.share1.setTextColor(Color.parseColor(("#"+color)));
            holder.share2.setTextColor(Color.parseColor(("#"+color)));
            holder.numArticles1.setTextColor(Color.parseColor("#"+color));
            holder.numArticles2.setTextColor(Color.parseColor("#"+color));
            holder.viewfullstory.setTextColor(Color.parseColor("#"+color));
        }



    }


    @Override
    public int getItemCount() {
        return StoryDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(StoryDataList!=null){
            if(StoryDataList.get(position).getSubstories().size()==1){
                return TYPE_NORMAL;
            }else if(StoryDataList.get(position).getSubstories().size()>1){
                return TYPE_DOUBLE;
            }
        }
       return 0;
    }




    public class StoryDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView storyTitle, time, source, seen, share, articleTitle,numarticles,viewfullstory;

        public ImageView followIcon, heroImage, seenImage, shareIcon, filterIcon,circle;

        public RelativeLayout storyLayout;

        public LinearLayout followIconLayout;

        public View sideLine, sideLine1, bottomLine;

        public StoryDataViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView)itemView.findViewById(R.id.story_name);
            time = (TextView)itemView.findViewById(R.id.bigcard_time);
            source = (TextView)itemView.findViewById(R.id.source_name);
            seen = (TextView)itemView.findViewById(R.id.readstatus_text_story);
            share = (TextView)itemView.findViewById(R.id.sharetext);
            articleTitle = (TextView)itemView.findViewById(R.id.article_text);
            followIcon = (ImageView)itemView.findViewById(R.id.followicon);
            heroImage = (ImageView)itemView.findViewById(R.id.heroimage);
            seenImage = (ImageView)itemView.findViewById(R.id.readstatus_story);
            shareIcon = (ImageView)itemView.findViewById(R.id.shareicon);
            numarticles = (TextView)itemView.findViewById(R.id.num_articles);
            filterIcon = (ImageView)itemView.findViewById(R.id.filter_image);
            storyLayout = (RelativeLayout)itemView.findViewById(R.id.topstory_layout);
            followIconLayout = (LinearLayout)itemView.findViewById(R.id.follow_icon_layout);
            sideLine = (View)itemView.findViewById(R.id.sideline);
            sideLine1= (View)itemView.findViewById(sideline1);
            followIconLayout = (LinearLayout)itemView.findViewById(R.id.follow_icon_layout);
            viewfullstory = (TextView)itemView.findViewById(R.id.view_full_story);
            seen = (TextView)itemView.findViewById(R.id.readstatus_text_story);
            seenImage = (ImageView)itemView.findViewById(R.id.readstatus_story);

            bottomLine = (View)itemView.findViewById(R.id.bottom_line);

            circle = (ImageView)itemView.findViewById(R.id.circle1);

            itemView.setOnClickListener(this);
            storyLayout.setOnClickListener(this);
            followIconLayout.setOnClickListener(this);
            shareIcon.setOnClickListener(this);
            share.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            String substoryId = StoryDataList.get(position).getSubstories().get(0).getSubstory_id();
            String storyColor = StoryDataList.get(position).getHexColor();
            String StoryId = StoryDataList.get(position).getStoryIdMain();
            String storytitle = StoryDataList.get(position).getStoryNameMain();
            String mfollowstatus = StoryDataList.get(position).getFollowstatus();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String substoryname = StoryDataList.get(position).getSubstories().get(0).getSubStoryName();


            if(view==itemView){
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("type_card","bigimage");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);

                List<ArticlesMain> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(0).getArticlesMain();
                Intent intent = new Intent(context, ArticlesActivity.class);
                intent.putExtra("ARTICLELIST",(Serializable)mArticles);
                intent.putExtra("substory_id",substoryId);
                intent.putExtra("story_color",storyColor);
                context.startActivity(intent);

            }

            if(view ==storyLayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                oparams.put("type_card","bigimage");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_viewfullstory", oparams);

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", StoryId);
                intent.putExtra("storytitle", storytitle);
                intent.putExtra("followstatus",mfollowstatus);
                intent.putExtra("fragmenttag", fragmenttag);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("hex_colour",storyColor);
                context.startActivity(intent);
            }

            if(view == followIcon||view== followIconLayout){
                clickposition = position;
                clickStoryId = StoryId;
                clickStoryName = storytitle;

                followstory();
            }

            if(view ==shareIcon||view == share){

                StoryDataMain mStoryData = StoryDataList.get(position);

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("category", "conversion"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                oparams.put("type_card","bigimage");
                oparams.put("category", "conversion"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_share", oparams);

                String URL = "http://thesoup.in/share/" + mStoryData.getStoryIdMain() + "/" + mStoryData.getSubstories().get(0).getSubstory_id()+"?utm_source=appshare&utm_medium="+mStoryData.getStoryIdMain()+"&utm_campaign="+ mStoryData.getSubstories().get(0).getSubstory_id();
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Share via");
                i.putExtra(Intent.EXTRA_TEXT, URL);
                context.startActivity(Intent.createChooser(i, "Share via"));
            }


        }

        private void followstory() {
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
            oparams.put("type_card","bigimage");
            oparams.put("collection_name", clickStoryName);
            oparams.put("position_card_collection", String.valueOf(clickposition));
            oparams.put("category", "conversion");

            if (mfollowstatus.equals("") || mfollowstatus.equals("0") || TextUtils.isEmpty(mfollowstatus)) {

                //Analytics
                mFirebaseAnalytics.logEvent("tap_add", mparams);
                cleverTap.event.push("tap_follow", oparams);
                HashMap<String, String> params = new HashMap<>();

                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("story_id", clickStoryId);
                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
                followrequest.followRequest(clickposition, fragmenttag);


            } else if (mfollowstatus.equals("1")) {


                mFirebaseAnalytics.logEvent("tap_remove", mparams);
                cleverTap.event.push("tap_unfollow", oparams);
                HashMap<String, String> params = new HashMap<>();
                params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
                params.put("story_id", clickStoryId);

                Log.d("Check mStorydata size", String.valueOf(StoryDataList.size()));

                NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

                unFollowrequest.unFollowRequest(clickposition, fragmenttag);


            }
        }
    }

    private class DoubleDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView storyTitle, time1, time2, source1, seen1,seen2,share1,viewfullstory,share2, articleTitle1,articleTitle2,source2,numArticles1,numArticles2 ;

        public ImageView followIcon,articleImage1,articleImage2, seenImage1,seenimage2, shareIcon1,shareIcon2, filterIcon,circle1,circle2;

        public RelativeLayout topcardlayout,bottomcardlayout,storylayout;
        public LinearLayout followiconLayout;

        public View  sideLine1,sideline2,sideline3,sideline4, bottomLine1,bottomline2;


        public DoubleDataViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView)itemView.findViewById(R.id.story_name);
            time1 = (TextView)itemView.findViewById(R.id.time1);
            time2 = (TextView)itemView.findViewById(R.id.time2);
            source1 = (TextView)itemView.findViewById(R.id.source1);
            source2 = (TextView)itemView.findViewById(R.id.source2);
            share1 = (TextView)itemView.findViewById(R.id.sharetext1);
            share2 = (TextView)itemView.findViewById(R.id.sharetext_2);
            articleTitle1 = (TextView)itemView.findViewById(R.id.article_text1);
            articleTitle2 = (TextView)itemView.findViewById(R.id.article_text_2);
            followIcon = (ImageView)itemView.findViewById(followicon);
            articleImage1 = (ImageView)itemView.findViewById(R.id.article_image_1);
            articleImage2 = (ImageView)itemView.findViewById(R.id.article_image_2);
            shareIcon1 = (ImageView)itemView.findViewById(R.id.shareicon1);
            shareIcon2 = (ImageView)itemView.findViewById(R.id.shareicon2);
            numArticles1 = (TextView)itemView.findViewById(R.id.num_articles1);
            numArticles2 = (TextView) itemView.findViewById(R.id.num_articles_2);
            topcardlayout = (RelativeLayout)itemView.findViewById(R.id.top_card);
            bottomcardlayout = (RelativeLayout)itemView.findViewById(R.id.bottom_card);
            storylayout = (RelativeLayout)itemView.findViewById(R.id.topstory_layout);
            viewfullstory = (TextView)itemView.findViewById(R.id.view_full_story);
            seen1= (TextView)itemView.findViewById(R.id.readstatus_text_story);
            seen2 = (TextView)itemView.findViewById(R.id.readstatus_text_story_bottom);
            seenImage1 = (ImageView)itemView.findViewById(R.id.readstatus_story);
            seenimage2 = (ImageView)itemView.findViewById(R.id.readstatus_story_bottom);

            filterIcon = (ImageView)itemView.findViewById(R.id.filter_image);

            followiconLayout = (LinearLayout)itemView.findViewById(R.id.follow_icon_layout);

            sideLine1= (View)itemView.findViewById(sideline1);
            sideline2 = (View)itemView.findViewById(R.id.sideline2);
            sideline3 = (View)itemView.findViewById(R.id.sideline3);
            sideline4 = (View)itemView.findViewById(R.id.sideline4);

            bottomLine1 = (View)itemView.findViewById(R.id.bottom_line1);
            bottomline2 = (View)itemView.findViewById(R.id.bottom_line_2);

            circle1 = (ImageView)itemView.findViewById(R.id.circle1);
            circle2 = (ImageView)itemView.findViewById(R.id.circle2);

            topcardlayout.setOnClickListener(this);
            bottomcardlayout.setOnClickListener(this);
            storylayout.setOnClickListener(this);
            share1.setOnClickListener(this);
            share2.setOnClickListener(this);
            followIcon.setOnClickListener(this);
            followiconLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            StoryDataMain mStoryData = StoryDataList.get(position);

            String substoryId = StoryDataList.get(position).getSubstories().get(0).getSubstory_id();
            String storyColor = StoryDataList.get(position).getHexColor();
            String StoryId = StoryDataList.get(position).getStoryIdMain();
            String storytitle = StoryDataList.get(position).getStoryNameMain();
            String mfollowstatus = StoryDataList.get(position).getFollowstatus();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String substoryname = StoryDataList.get(position).getSubstories().get(0).getSubStoryName();

            if(view==topcardlayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("type_card","multicard");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                if(mStoryData.getSubstories().size()>1){
                    oparams.put("type_card","multicard");
                }else{
                    oparams.put("type_card","smallcard");
                }
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);

                goToArticles(mStoryData,0);

            }

            if(view==bottomcardlayout){
                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("type_card","multicard");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                nparams.putString("type_card","multicard");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);


                goToArticles(mStoryData,1);
            }

            if(view == storylayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "category_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "category_screen");
                if(mStoryData.getSubstories().size()>1){
                    oparams.put("type_card","multicard");
                }else{
                    oparams.put("type_card","smallcard");
                }
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_viewfullstory", oparams);



                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", StoryId);
                intent.putExtra("storytitle", storytitle);
                intent.putExtra("followstatus",mfollowstatus);
                intent.putExtra("fragmenttag", fragmenttag);
                intent.putExtra("category_id", categoryId);
                intent.putExtra("hex_colour",storyColor);
                context.startActivity(intent);
            }

            if(view == followIcon||view ==followiconLayout){
                clickposition = position;
                clickStoryId = StoryId;
                clickStoryName = storytitle;

                followstory();
            }

            if(view==share1){
                share(position,0);

            }

            if(view==share2){
                share(position,1);
            }

        }

        public void share(int position,int articlePosition){
            StoryDataMain mStoryData = StoryDataList.get(position);

            Bundle nparams = new Bundle();
            nparams.putString("screen_name", "discover_screen");
            nparams.putString("category", "conversion"); //(only if possible)
            nparams.putString("position_card_collection", String.valueOf(position));
            mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

            HashMap<String, Object> oparams = new HashMap<>();
            oparams.put("screen_name", "discover_screen");
            if(mStoryData.getSubstories().size()>1){
                oparams.put("type_card","multicard");
            }else{
                oparams.put("type_card","smallcard");
            }
            oparams.put("category", "tap"); //(only if possible)
            oparams.put("position_card_collection", String.valueOf(position));
            oparams.put("substory_name",mStoryData.getSubstories().get(articlePosition).getSubStoryName());
            oparams.put("substory_id",mStoryData.getSubstories().get(articlePosition).getSubStoryName());
            cleverTap.event.push("tap_share", oparams);

            String URL = "http://thesoup.in/share/" + mStoryData.getStoryIdMain() + "/" + mStoryData.getSubstories().get(articlePosition).getSubstory_id()+"?utm_source=appshare&utm_medium="+mStoryData.getStoryIdMain()+"&utm_campaign="+ mStoryData.getSubstories().get(articlePosition).getSubstory_id();
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Share via");
            i.putExtra(Intent.EXTRA_TEXT, URL);
            context.startActivity(Intent.createChooser(i, "Share via"));
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
            mparams.putString("category", "tap");


            HashMap<String, Object> oparams = new HashMap<>();
            oparams.put("collection_id", clickStoryId);
            oparams.put("collection_name", clickStoryName);
            if(StoryDataList.get(clickposition).getSubstories().size()>1){
                oparams.put("type_card","multicard");
            }else{
                oparams.put("type_card","smallcard");
            }
            oparams.put("position_card_collection", String.valueOf(clickposition));
            oparams.put("category", "tap");

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


        public void goToArticles(StoryDataMain mStoryData, int articlePosition) {

            String substoryId = mStoryData.getSubstories().get(articlePosition).getSubstory_id();
            String storyColor = mStoryData.getHexColor();

            List<ArticlesMain> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(articlePosition).getArticlesMain();
            Intent intent = new Intent(context, ArticlesActivity.class);
            intent.putExtra("ARTICLELIST",(Serializable)mArticles);
            intent.putExtra("substory_id",substoryId);
            intent.putExtra("story_color",storyColor);
            context.startActivity(intent);
        }
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

                    int n = (int) (Difference/1440000);

                    if(n>1){
                        return String.valueOf(n)+" minutes ago";
                    }

                    return String.valueOf(n)+" minute ago";


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

                return  Date + " " + month + " " + year;

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

            return  Date + " " + month + " " + year;

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


    }






