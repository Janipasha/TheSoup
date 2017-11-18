package in.thesoup.thesoupstoriesnews.adapters;

/**
 * Created by Jani on 11-11-2017.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;
import in.thesoup.thesoupstoriesnews.activities.ArticlesActivity;
import in.thesoup.thesoupstoriesnews.activities.CategoryActivity;
import in.thesoup.thesoupstoriesnews.activities.DetailsActivity;
import in.thesoup.thesoupstoriesnews.activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.activities.NavigationActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.StoryDataMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.SubstoriesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.ArticlesMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.StoryDataMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.SubstoriesMainHome;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsClick;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsFollowUnFollow;

import static android.R.attr.category;
import static android.view.View.GONE;
import static in.thesoup.thesoupstoriesnews.R.id.circle1;
import static in.thesoup.thesoupstoriesnews.R.id.followicon;
import static in.thesoup.thesoupstoriesnews.R.id.sideline1;
import static in.thesoup.thesoupstoriesnews.R.id.source2;
import static in.thesoup.thesoupstoriesnews.R.id.time2;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SMALLTEXT = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_FOLLOW_CARD = 2;
    private static final int TYPE_HERO_CARD = 3;
    private static final int TYPE_DOUBLE = 4;
    private static final int TYPE_CATEGORY_DISCOVER = 5;
    private static final int TYPE_EMPTY = 7;
    private static final int TYPE_TOP_STORIES = 8;


    private List<StoryDataMainHome> StoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;


    public HomeAdapter(List<StoryDataMainHome> Datalist, Context context, int fragmenttag) {
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

    public void refreshData(List<StoryDataMainHome> Datalist) {
        //this.StoryDataList = Datalist;
        StoryDataList.addAll(Datalist);
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<StoryDataMainHome> Datalist) {
        this.StoryDataList = Datalist;
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryDataMainHome> Datalist) {
        this.StoryDataList = Datalist;

        notifyDataSetChanged();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HERO_CARD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_large_card_nov,parent,false);
            return new StoryDataViewHolder(view);
        }if(viewType==TYPE_DOUBLE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_small_card_nov,parent,false);
            return new DoubleDataViewHolder(view);
        }if(viewType==TYPE_SMALLTEXT){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_headerversion,parent,false);
            return new SmallTextViewHolder(view);
        }if(viewType==TYPE_HEADER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_middle,parent,false);
            return new SectionHeaderViewHolder(view);
        }if(viewType==TYPE_FOLLOW_CARD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_card_nov,parent,false);
            return new FollowingCardViewHolder(view);
        }if(viewType==TYPE_CATEGORY_DISCOVER){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_button_nov,parent,false);
            return new DiscoverButtonViewHolder(view);
        }if(viewType==TYPE_EMPTY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty,parent,false);
            return  new EmptyViewHolder(view);}
        if(viewType==TYPE_TOP_STORIES){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_topstory_nov,parent,false);
            return  new TopStoriesViewHolder(view);
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

        if(holder instanceof FollowingCardViewHolder){
            followCardFill((FollowingCardViewHolder)holder,position);
        }

        if(holder instanceof DiscoverButtonViewHolder){
            DiscoverButtonFill((DiscoverButtonViewHolder)holder,position);
        }

        if(holder instanceof SectionHeaderViewHolder){
            SectioHeaderFill((SectionHeaderViewHolder)holder,position);
        }

        if(holder instanceof SmallTextViewHolder){
            SmallTextFill((SmallTextViewHolder)holder,position);
        }

        if(holder instanceof TopStoriesViewHolder){
            TopStoriesFill((TopStoriesViewHolder)holder,position);
        }



    }

    private void TopStoriesFill(TopStoriesViewHolder holder, int position) {

            holder.seen.setVisibility(GONE);
            holder.seenImage.setVisibility(GONE);

        if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
            holder.seen.setVisibility(View.VISIBLE);
            holder.seenImage.setVisibility(View.VISIBLE);
        }

            List<ArticlesMainHome> mArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();

            String StoryTitle = StoryDataList.get(position).getStoryNameMain();
            String articleText1 = mArticles.get(0).getTitle();
            String source = mArticles.get(0).getsourceName();
            String time = StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();

            time = getTime(time);

            String ImageUrl = mArticles.get(0).getImageUrl();
            String followstatus = StoryDataList.get(position).getFollowstatus();

            StoryTitle = gethtmlString(StoryTitle);
            articleText1 = gethtmlString(articleText1);

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

                Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.heroImage);

            } else {
                Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.heroImage);
            }


            holder.storyTitle.setText(StoryTitle);
            holder.articleTitle.setText(articleText1);
            holder.source.setText(source);
            holder.numarticles.setText(numArticles);
            holder.time.setText(time);
            holder.share.setText("SHARE");

            if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

                String color = StoryDataList.get(position).getHexColor();
                holder.filterIcon.setColorFilter(Color.parseColor("#" + color));
                holder.followIcon.setColorFilter(Color.parseColor("#" + color));
                holder.bottomLine.setBackgroundColor(Color.parseColor("#33" + color));
                holder.shareIcon.setColorFilter(Color.parseColor("#" + color));
                holder.share.setTextColor(Color.parseColor(("#"+color)));
                holder.numarticles.setTextColor(Color.parseColor("#"+color));
                holder.viewfullstory.setTextColor(Color.parseColor("#"+color));
            }








    }

    private void SmallTextFill(SmallTextViewHolder holder, int position) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);

        if(pref.getString(SoupContract.FIRST_NAME,null)!=null&&!pref.getString(SoupContract.FIRST_NAME,null).isEmpty()){
            holder.helloUser.setText("Hello "+pref.getString(SoupContract.FIRST_NAME,null));
        }

        holder.UserText.setVisibility(View.VISIBLE);
        holder.ManageHome.setText("Manage Home");
        String usertext = gethtmlString(StoryDataList.get(position).getStoryNameMain());
        holder.UserText.setText(usertext);
    }

    private void SectioHeaderFill(SectionHeaderViewHolder holder, int position) {
       String storyname =  StoryDataList.get(position).getStoryNameMain();
        holder.number.setVisibility(View.GONE);
        holder.section.setText(storyname);
        holder.seeAll.setText("See All");
        if(StoryDataList.get(position).getNotif()!=null&&!StoryDataList.get(position).getNotif().isEmpty()){

            if(!StoryDataList.get(position).getNotif().equals("0")){

            holder.number.setVisibility(View.VISIBLE);
            holder.numberfollowingupdates.setText(StoryDataList.get(position).getNotif());}
        }
    }

    private void DiscoverButtonFill(DiscoverButtonViewHolder holder, int position) {

        holder.section.setText(StoryDataList.get(position).getStoryNameMain());
        String extratext = gethtmlString(StoryDataList.get(position).getExtraText());
        holder.extratext.setText(extratext);
        if (StoryDataList.get(position).getCategoryId()!= null && !StoryDataList.get(position).getCategoryId().isEmpty()) {
            String Id = StoryDataList.get(position).getCategoryId();

            int filter = getDrawable(Id);

            if (filter != 0) {
                holder.filtericon.setImageResource(filter);
                holder.filtericon.setColorFilter(Color.parseColor("#cbffffff"));
            }

        }

        if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

            String color = StoryDataList.get(position).getHexColor();
            GradientDrawable odrawable = (GradientDrawable) holder.buttonLayout.getBackground();
            odrawable.setStroke(2,Color.parseColor("#"+color));

          //  holder.buttonLayout.setBackgroundResource(odrawable);
            holder.filtericon.setColorFilter(Color.parseColor("#" + color));
            holder.section.setTextColor(Color.parseColor("#"+color));
        }



    }

    private void followCardFill(FollowingCardViewHolder holder, int position) {


        holder.seen.setVisibility(GONE);
        holder.seenImage.setVisibility(GONE);

        if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
            holder.seen.setVisibility(View.VISIBLE);
            holder.seenImage.setVisibility(View.VISIBLE);
        }


        List<ArticlesMainHome> mArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();

            String StoryTitle = StoryDataList.get(position).getStoryNameMain();
            String articleText1 = mArticles.get(0).getTitle();
            String source = mArticles.get(0).getsourceName();
            String time =StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();

            time = getTime(time);

            String ImageUrl = mArticles.get(0).getImageUrl();
            String followstatus = StoryDataList.get(position).getFollowstatus();

            StoryTitle = gethtmlString(StoryTitle);
            articleText1 = gethtmlString(articleText1);

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

                Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.heroImage);

            } else {
                Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.heroImage);
            }


            holder.storyTitle.setText(StoryTitle);
            holder.articleTitle.setText(articleText1);
            holder.source.setText(source);
            holder.numarticles.setText(numArticles);
            holder.time.setText(time);
            holder.share.setText("SHARE");

            if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

                String color = StoryDataList.get(position).getHexColor();
                holder.filterIcon.setColorFilter(Color.parseColor("#" + color));
                holder.followIcon.setColorFilter(Color.parseColor("#" + color));
                holder.bottomLine.setBackgroundColor(Color.parseColor("#33" + color));
                holder.shareIcon.setColorFilter(Color.parseColor("#" + color));
                holder.share.setTextColor(Color.parseColor(("#"+color)));
                holder.numarticles.setTextColor(Color.parseColor("#"+color));
                holder.viewfullstory.setTextColor(Color.parseColor("#"+color));
            }






    }

    private void largeCardFill(StoryDataViewHolder holder, int position) {

        holder.seenImage.setVisibility(GONE);
        holder.seen.setVisibility(GONE);

        if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
            holder.seenImage.setVisibility(View.VISIBLE);
            holder.seen.setVisibility(View.VISIBLE);
        }


        List<ArticlesMainHome> mArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();

        String StoryTitle = StoryDataList.get(position).getStoryNameMain();
        String articleText1 = mArticles.get(0).getTitle();
        String source = mArticles.get(0).getsourceName();
        String time =StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();

        time = getTime(time);

        String ImageUrl = mArticles.get(0).getImageUrl();
        String followstatus = StoryDataList.get(position).getFollowstatus();

        StoryTitle = gethtmlString(StoryTitle);
        articleText1 = gethtmlString(articleText1);

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


        holder.storyTitle.setText(StoryTitle);
        holder.articleTitle.setText(articleText1);
        holder.source.setText(source);
        holder.numarticles.setText(numArticles);
        holder.time.setText(time);
        holder.share.setText("SHARE");

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


        List<SubstoriesMainHome> mSubstories = StoryDataList.get(position).getSubstories();

        holder.seen1.setVisibility(GONE);
        holder.seen2.setVisibility(GONE);
        holder.seenImage1.setVisibility(GONE);
        holder.seenimage2.setVisibility(GONE);

        if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
            holder.seen1.setVisibility(View.VISIBLE);
            holder.seenImage1.setVisibility(View.VISIBLE);
        }



        if(mSubstories.size()==1){

            holder.bottomcardlayout.setVisibility(GONE);
            List<ArticlesMainHome> mFirstArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();
            ArticlesMainHome FirstArticle = mFirstArticles.get(0);

            String time1 =StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();
            time1 = getTime(time1);
            String source1 = FirstArticle.getsourceName();
            String StoryTitle = StoryDataList.get(position).getStoryNameMain();
            StoryTitle = gethtmlString(StoryTitle);
            holder.storyTitle.setText(StoryTitle);

            String articleText1 = FirstArticle.getTitle();
            articleText1 = gethtmlString(articleText1);
            String imageUrl1 =FirstArticle.getImageUrl();
            String numberArticle1 = String.valueOf(mFirstArticles.size())+" Articles -";
            holder.source1.setText(source1);
            holder.articleTitle1.setText(articleText1);
            holder.time1.setText(time1);
            holder.share1.setText("SHARE");
            holder.numArticles1.setText(numberArticle1);
            if (imageUrl1 != null && !imageUrl1.isEmpty()) {

                Picasso.with(context).load(imageUrl1).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.articleImage1);

            } else {
                Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.articleImage1);
            }

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

            if (StoryDataList.get(position).getHexColor() != null && !StoryDataList.get(position).getHexColor().isEmpty()) {

                String color = StoryDataList.get(position).getHexColor();
                holder.circle1.setColorFilter(Color.parseColor("#" + color));
                holder.sideLine1.setBackgroundColor(Color.parseColor("#" + color));
                holder.sideline4.setBackgroundColor(Color.parseColor("#" + color));
                holder.filterIcon.setColorFilter(Color.parseColor("#" + color));
                holder.followIcon.setColorFilter(Color.parseColor("#" + color));
                holder.bottomLine1.setBackgroundColor(Color.parseColor("#33" + color));
                holder.shareIcon1.setColorFilter(Color.parseColor("#" + color));
                holder.share1.setTextColor(Color.parseColor(("#"+color)));
                holder.numArticles1.setTextColor(Color.parseColor("#"+color));
                holder.viewfullstory.setTextColor(Color.parseColor("#"+color));
            }

        }else {

            if(StoryDataList.get(position).getSubstories().get(1).getReadStatus()!=null&&!StoryDataList.get(position).getSubstories().get(1).getReadStatus().isEmpty()){
                holder.seen2.setVisibility(View.VISIBLE);
                holder.seenimage2.setVisibility(View.VISIBLE);
            }


            holder.bottomcardlayout.setVisibility(View.VISIBLE);
            List<ArticlesMainHome> mFirstArticles = StoryDataList.get(position).getSubstories().get(0).getArticlesMain();
            ArticlesMainHome FirstArticle = mFirstArticles.get(0);

            String time1 = StoryDataList.get(position).getSubstories().get(0).getTopArticleDate();
            time1 = getTime(time1);
            String source1 = FirstArticle.getsourceName();

            String articleText1 = FirstArticle.getTitle();
            articleText1 = gethtmlString(articleText1);
            String imageUrl1 =FirstArticle.getImageUrl();
            String numberArticle1 = String.valueOf(mFirstArticles.size())+" Articles -";
            String StoryTitle = StoryDataList.get(position).getStoryNameMain();
            StoryTitle = gethtmlString(StoryTitle);
            holder.storyTitle.setText(StoryTitle);
            holder.source1.setText(source1);
            holder.articleTitle1.setText(articleText1);
            holder.time1.setText(time1);
            holder.share1.setText("SHARE");
            holder.numArticles1.setText(numberArticle1);
            if (imageUrl1 != null && !imageUrl1.isEmpty()) {

                Picasso.with(context).load(imageUrl1).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.articleImage1);

            } else {
                Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.articleImage1);
            }


            List<ArticlesMainHome> mSecondArticles = StoryDataList.get(position).getSubstories().get(1).getArticlesMain();
            ArticlesMainHome SecondArticle = mSecondArticles.get(0);
            String time2 = StoryDataList.get(position).getSubstories().get(1).getTopArticleDate();
            time2 = getTime(time2);
            String source2 = SecondArticle.getsourceName();
            String articleText2 = SecondArticle.getTitle();
            articleText2 = gethtmlString(articleText2);
            String imageUrl2 = SecondArticle.getImageUrl();
            String numberArticles2 = String.valueOf(mSecondArticles.size())+" Articles -";
            holder.articleTitle2.setText(articleText2);
            holder.time2.setText(time2);
            holder.source2.setText(source2);
            holder.share2.setText("SHARE");
            holder.numArticles2.setText(numberArticles2);
            if (imageUrl2 != null && !imageUrl2.isEmpty()) {

                Picasso.with(context).load(imageUrl2).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.articleImage2);

            } else {
                Picasso.with(context).load(R.drawable.ic_sample).fit().centerCrop().into(holder.articleImage2);
            }

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

    }


    @Override
    public int getItemCount() {
        return StoryDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (StoryDataList != null) {
            if (StoryDataList.get(position).getType() == String.valueOf(TYPE_SMALLTEXT)) {
                return TYPE_SMALLTEXT;
            } else if (StoryDataList.get(position).getType() == String.valueOf(TYPE_HEADER)) {
                return TYPE_HEADER;
            } else if (StoryDataList.get(position).getType() == String.valueOf(TYPE_FOLLOW_CARD)) {
                return TYPE_FOLLOW_CARD;
            } else if (StoryDataList.get(position).getType() == String.valueOf(TYPE_HERO_CARD)) {
                return TYPE_HERO_CARD;
            } else if (StoryDataList.get(position).getType() == String.valueOf(TYPE_DOUBLE)) {
                return TYPE_DOUBLE;
            } else if (StoryDataList.get(position).getType() == String.valueOf(TYPE_CATEGORY_DISCOVER)) {
                return TYPE_CATEGORY_DISCOVER;

            }else if(StoryDataList.get(position).getType()==String.valueOf(TYPE_EMPTY)){
                return TYPE_EMPTY;
            } else if(StoryDataList.get(position).getType()==String.valueOf(TYPE_TOP_STORIES)){
                return TYPE_TOP_STORIES;
            }


        }
            return 0;

    }

    private class FollowingCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storyTitle, time, source, seen, share, articleTitle,numarticles,viewfullstory;

        public ImageView followIcon, heroImage, seenImage, shareIcon, filterIcon,circle;

        public RelativeLayout storyLayout;

        public RelativeLayout followIconLayout;

        public View sideLine, sideLine1, bottomLine;

        public FollowingCardViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView)itemView.findViewById(R.id.story_name);
            time = (TextView)itemView.findViewById(R.id.following_card_time);
            source = (TextView)itemView.findViewById(R.id.follow_card_source);
            share = (TextView)itemView.findViewById(R.id.sharetext);
            articleTitle = (TextView)itemView.findViewById(R.id.article_text);
            followIcon = (ImageView)itemView.findViewById(R.id.followicon);
            heroImage = (ImageView)itemView.findViewById(R.id.article_image);
            shareIcon = (ImageView)itemView.findViewById(R.id.share_icon);
            numarticles = (TextView)itemView.findViewById(R.id.num_articles);
            filterIcon = (ImageView)itemView.findViewById(R.id.filter_image);
            seen = (TextView)itemView.findViewById(R.id.readstatus_text_story_bottom);
            seenImage = (ImageView)itemView.findViewById(R.id.readstatus_story_bottom);

            storyLayout = (RelativeLayout)itemView.findViewById(R.id.topstory_layout);
            bottomLine = (View)itemView.findViewById(R.id.bottom_line);
            followIconLayout = (RelativeLayout) itemView.findViewById(R.id.follow_icon_layout);
            viewfullstory = (TextView)itemView.findViewById(R.id.view_full_story);

            itemView.setOnClickListener(this);
            followIconLayout.setOnClickListener(this);
            followIcon.setOnClickListener(this);
            storyLayout.setOnClickListener(this);
            shareIcon.setOnClickListener(this);
            share.setOnClickListener(this);

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

        @Override
        public void onClick(View view)  {

            int position = getAdapterPosition();

            String substoryId = StoryDataList.get(position).getSubstories().get(0).getSubstory_id();
            String storyColor = StoryDataList.get(position).getHexColor();
            String StoryId = StoryDataList.get(position).getStoryIdMain();
            String storytitle = StoryDataList.get(position).getStoryNameMain();
            String mfollowstatus = StoryDataList.get(position).getFollowstatus();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String substoryname = StoryDataList.get(position).getSubstories().get(0).getSubStoryName();


            if(view==itemView){

                HashMap<String, String> params = new HashMap<>();
                params.put("id", StoryDataList.get(position).getStoryIdMain());
                params.put("type", "stories");
                params.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
                NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                try {
                    performClick.sendClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);

                List<ArticlesMainHome> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(0).getArticlesMain();
                Intent intent = new Intent(context, ArticlesActivity.class);
                intent.putExtra("ARTICLELISTHOME",(Serializable)mArticles);
                intent.putExtra("substory_id",substoryId);
                intent.putExtra("story_color",storyColor);
               context.startActivity(intent);

            }

            if(view ==storyLayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("type_card","followcard");
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

                StoryDataMainHome mStoryData = StoryDataList.get(position);

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "conversion"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("type_card","followingcard");
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
    }

    private class SmallTextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView helloUser ,ManageHome,UserText;

        public SmallTextViewHolder(View itemView) {
            super(itemView);

            helloUser = (TextView)itemView.findViewById(R.id.filter_heading);
            ManageHome = (TextView)itemView.findViewById(R.id.manage_home);
            UserText = (TextView)itemView.findViewById(R.id.filtertoptext);

            ManageHome.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view==ManageHome){
                pref = PreferenceManager.getDefaultSharedPreferences(context);


                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)

                HashMap<String,Object> aparams = new HashMap<>();
                aparams.put("screen_name", "home_screen");
                aparams.put("category", "tap"); //(only if possible)


                if (pref.getString("filter_count", null) != null) {
                    nparams.putString("count_category", pref.getString("filter_count", null));
                    aparams.put("count_category", pref.getString("filter_count", null));

                }

                mFirebaseAnalytics.logEvent("tap_section_managehome", nparams);
                cleverTap.event.push("tap_section_managehome", aparams);

                Intent intent = new Intent(context, FilterActivity.class);
                intent.putExtra("resetfilter", "0");
                context.startActivity(intent);
            }
        }
    }

    private class SectionHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView section,seeAll,numberfollowingupdates;
        private LinearLayout number;


        public SectionHeaderViewHolder(View itemView) {
            super(itemView);

            section = (TextView)itemView.findViewById(R.id.moreforyou);
            seeAll = (TextView) itemView.findViewById(R.id.moreseeall);
            number = (LinearLayout)itemView.findViewById(R.id.notificationNumber);
            numberfollowingupdates = (TextView)itemView.findViewById(R.id.numberfollowingupdates);

            seeAll.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if(view==seeAll||view==itemView){
                int position = getAdapterPosition();
                String categoryId = StoryDataList.get(position).getCategoryId();
                String category = StoryDataList.get(position).getStoryNameMain();



                if(StoryDataList.get(position).getNotif()!=null){
                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "home_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("section_name",category);
                    oparams.put("position_card_collection", String.valueOf(position));
                    cleverTap.event.push("tap_section_seeall", oparams);

                    NavigationActivity activity = (NavigationActivity) context;
                    if(activity!=null){
                        activity.gotoFragment(1);
                    }

                }else{

                    HashMap<String, Object> oparams = new HashMap<>();
                    oparams.put("screen_name", "home_screen");
                    oparams.put("category", "tap"); //(only if possible)
                    oparams.put("section_id",categoryId);
                    oparams.put("section_name",category);
                    oparams.put("position_card_collection", String.valueOf(position));
                    cleverTap.event.push("tap_section_seeall", oparams);

                    Intent intent = new Intent(context, CategoryActivity.class);
                    intent.putExtra("category_id",categoryId);
                    intent.putExtra("category",category);
                    context.startActivity(intent);

                }




            }

        }
    }

    private class DiscoverButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView section,extratext;
        private ImageView filtericon;
        private RelativeLayout buttonLayout;

        public DiscoverButtonViewHolder(View itemView) {
            super(itemView);

            section = (TextView)itemView.findViewById(R.id.section);
            filtericon = (ImageView)itemView.findViewById(R.id.filter_icon);
            extratext =(TextView)itemView.findViewById(R.id.extratext);
            buttonLayout = (RelativeLayout)itemView.findViewById(R.id.button_layout);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String category = StoryDataList.get(position).getStoryNameMain();
            if(view==itemView){


                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("section_id",categoryId);
                oparams.put("section_name",category);
                oparams.put("position_card_collection", String.valueOf(position));
                cleverTap.event.push("tap_card_morecategory", oparams);

                Intent intent = new Intent(context, CategoryActivity.class);
                intent.putExtra("category_id",categoryId);
                intent.putExtra("category",category);
                context.startActivity(intent);
            }
        }
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

            followIconLayout = (LinearLayout) itemView.findViewById(R.id.follow_icon_layout);
            viewfullstory = (TextView)itemView.findViewById(R.id.view_full_story);

            sideLine = (View)itemView.findViewById(R.id.sideline);
            sideLine1= (View)itemView.findViewById(sideline1);

            bottomLine = (View)itemView.findViewById(R.id.bottom_line);

            circle = (ImageView)itemView.findViewById(R.id.circle1);

            itemView.setOnClickListener(this);
            followIconLayout.setOnClickListener(this);
            followIcon.setOnClickListener(this);
            storyLayout.setOnClickListener(this);
            shareIcon.setOnClickListener(this);
            share.setOnClickListener(this);



        }

        @Override
        public void onClick(View view)  {

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
                nparams.putString("screen_name", "home_screen");
                nparams.putString("type_card","bigimage");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);

                List<ArticlesMainHome> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(0).getArticlesMain();
                Intent intent = new Intent(context, ArticlesActivity.class);
                intent.putExtra("ARTICLELISTHOME",(Serializable)mArticles);
                intent.putExtra("substory_id",substoryId);
                intent.putExtra("story_color",storyColor);
               context.startActivity(intent);

            }

            if(view ==storyLayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
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

                StoryDataMainHome mStoryData = StoryDataList.get(position);

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap");
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("category", "tap"); //(only if possible)
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
            mparams.putString("category", "tap");


            HashMap<String, Object> oparams = new HashMap<>();
            oparams.put("collection_id", clickStoryId);
            oparams.put("type_card","bigimage");
            oparams.put("collection_name", clickStoryName);
            oparams.put("position_card_collection", String.valueOf(clickposition));
            oparams.put("category", "tap");

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

        public TextView storyTitle, time1, time2, source1,seen1,seen2, share1,share2, articleTitle1,articleTitle2,source2,numArticles1,numArticles2,viewfullstory ;

        public ImageView followIcon,articleImage1,articleImage2, seenImage1,seenimage2, shareIcon1,shareIcon2, filterIcon,circle1,circle2;

        public RelativeLayout bottomcardlayout,topcardlayout,storylayout;

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
            viewfullstory = (TextView)itemView.findViewById(R.id.view_full_story);
            seen1= (TextView)itemView.findViewById(R.id.readstatus_text_story);
            seen2 = (TextView)itemView.findViewById(R.id.readstatus_text_story_bottom);
            seenImage1 = (ImageView)itemView.findViewById(R.id.readstatus_story);
            seenimage2 = (ImageView)itemView.findViewById(R.id.readstatus_story_bottom);

            filterIcon = (ImageView)itemView.findViewById(R.id.filter_image);

            sideLine1= (View)itemView.findViewById(sideline1);
            sideline2 = (View)itemView.findViewById(R.id.sideline2);
            sideline3 = (View)itemView.findViewById(R.id.sideline3);
            sideline4 = (View)itemView.findViewById(R.id.sideline4);
            storylayout = (RelativeLayout)itemView.findViewById(R.id.topstory_layout);

            bottomLine1 = (View)itemView.findViewById(R.id.bottom_line1);
            bottomline2 = (View)itemView.findViewById(R.id.bottom_line_2);
            topcardlayout = (RelativeLayout)itemView.findViewById(R.id.top_card);
            bottomcardlayout = (RelativeLayout) itemView.findViewById(R.id.bottom_card);
            followiconLayout = (LinearLayout)itemView.findViewById(R.id.follow_icon_layout);

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
        public void onClick(View view)  {
            int position = getAdapterPosition();
            StoryDataMainHome mStoryData = StoryDataList.get(position);

            String substoryId = StoryDataList.get(position).getSubstories().get(0).getSubstory_id();
            String storyColor = StoryDataList.get(position).getHexColor();
            String StoryId = StoryDataList.get(position).getStoryIdMain();
            String storytitle = StoryDataList.get(position).getStoryNameMain();
            String mfollowstatus = StoryDataList.get(position).getFollowstatus();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String substoryname = StoryDataList.get(position).getSubstories().get(0).getSubStoryName();

            if(view==topcardlayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("type_card","multicard");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
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
                nparams.putString("screen_name", "home_screen");
                nparams.putString("type_card","multicard");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
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
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
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
            StoryDataMainHome mStoryData = StoryDataList.get(position);

            Bundle nparams = new Bundle();
            nparams.putString("screen_name", "discover_screen");
            nparams.putString("category", "tap"); //(only if possible)
            nparams.putString("position_card_collection", String.valueOf(position));
            if(mStoryData.getSubstories().size()>1){
                nparams.putString("type_card","multicard");
            }else{
                nparams.putString("type_card","smallcard");
            }
            mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

            HashMap<String, Object> oparams = new HashMap<>();
            oparams.put("screen_name", "discover_screen");
            if(mStoryData.getSubstories().size()>1){
                oparams.put("type_card","multicard");
            }else{
                oparams.put("type_card","smallcard");
            }
            oparams.put("category", "conversion"); //(only if possible)
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


        public void goToArticles(StoryDataMainHome mStoryData, int articlePosition) {

            String substoryId = mStoryData.getSubstories().get(articlePosition).getSubstory_id();
            String storyColor = mStoryData.getHexColor();



            List<ArticlesMainHome> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(articlePosition).getArticlesMain();
            Intent intent = new Intent(context, ArticlesActivity.class);
            intent.putExtra("ARTICLELISTHOME",(Serializable)mArticles);
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
        } else if(filter.equals("1001")){
            return R.drawable.topstories;
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

            return Date + " " + month + " " + year;

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

    public String gethtmlString(String title) {

        String titlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            titlehtml = String.valueOf(Html.fromHtml(title, Html.FROM_HTML_MODE_LEGACY));

        } else {
            titlehtml = String.valueOf(Html.fromHtml(title));
        }

        return titlehtml;
    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(View view) {
            super(view);
        }
    }

    private class TopStoriesViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        public TextView storyTitle, time, source, share,seen, articleTitle, numarticles, viewfullstory;

        public ImageView followIcon, heroImage, seenImage, shareIcon, filterIcon, circle;

        public RelativeLayout storyLayout;

        public RelativeLayout followIconLayout;

        public View sideLine, sideLine1, bottomLine;

        public TopStoriesViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView) itemView.findViewById(R.id.story_name);
            time = (TextView) itemView.findViewById(R.id.following_card_time);
            source = (TextView) itemView.findViewById(R.id.follow_card_source);
            share = (TextView) itemView.findViewById(R.id.sharetext);
            articleTitle = (TextView) itemView.findViewById(R.id.article_text);
            followIcon = (ImageView) itemView.findViewById(R.id.followicon);
            heroImage = (ImageView) itemView.findViewById(R.id.article_image);
            shareIcon = (ImageView) itemView.findViewById(R.id.share_icon);
            numarticles = (TextView) itemView.findViewById(R.id.num_articles);
            filterIcon = (ImageView) itemView.findViewById(R.id.filter_image);
            seen = (TextView)itemView.findViewById(R.id.readstatus_text_story_bottom);

            storyLayout = (RelativeLayout) itemView.findViewById(R.id.topstory_layout);
            seenImage = (ImageView)itemView.findViewById(R.id.readstatus_story_bottom);
            bottomLine = (View) itemView.findViewById(R.id.bottom_line);
            followIconLayout = (RelativeLayout) itemView.findViewById(R.id.follow_icon_layout);
            viewfullstory = (TextView) itemView.findViewById(R.id.view_full_story);

            itemView.setOnClickListener(this);
            followIconLayout.setOnClickListener(this);
            followIcon.setOnClickListener(this);
            storyLayout.setOnClickListener(this);
            shareIcon.setOnClickListener(this);
            share.setOnClickListener(this);

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

        @Override
        public void onClick(View view)  {

            int position = getAdapterPosition();

            String substoryId = StoryDataList.get(position).getSubstories().get(0).getSubstory_id();
            String storyColor = StoryDataList.get(position).getHexColor();
            String StoryId = StoryDataList.get(position).getStoryIdMain();
            String storytitle = StoryDataList.get(position).getStoryNameMain();
            String mfollowstatus = StoryDataList.get(position).getFollowstatus();
            String categoryId = StoryDataList.get(position).getCategoryId();
            String substoryname = StoryDataList.get(position).getSubstories().get(0).getSubStoryName();


            if(view==itemView){

                HashMap<String, String> params = new HashMap<>();
                params.put("id", StoryDataList.get(position).getStoryIdMain());
                params.put("type", "stories");
                params.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
                NetworkUtilsClick performClick = new NetworkUtilsClick(context, params);
                try {
                    performClick.sendClick();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                nparams.putString("story_id", StoryId);
                nparams.putString("story_title", storytitle);
                mFirebaseAnalytics.logEvent("tap_card_substory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("category", "tap"); //(only if possible)
                oparams.put("position_card_collection", String.valueOf(position));
                oparams.put("story_id", StoryId);
                oparams.put("story_title", storyTitle);
                oparams.put("substory_name",substoryname);
                oparams.put("substory_id",substoryId);
                cleverTap.event.push("tap_card_substory", oparams);

                List<ArticlesMainHome> mArticles = StoryDataList.get(getAdapterPosition()).getSubstories().get(0).getArticlesMain();
                Intent intent = new Intent(context, ArticlesActivity.class);
                intent.putExtra("ARTICLELISTHOME",(Serializable)mArticles);
                intent.putExtra("substory_id",substoryId);
                intent.putExtra("story_color",storyColor);
                context.startActivity(intent);

            }

            if(view ==storyLayout){

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "tap"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_viewfullstory", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("type_card","followcard");
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

                StoryDataMainHome mStoryData = StoryDataList.get(position);

                Bundle nparams = new Bundle();
                nparams.putString("screen_name", "home_screen");
                nparams.putString("category", "conversion"); //(only if possible)
                nparams.putString("position_card_collection", String.valueOf(position));
                mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                HashMap<String, Object> oparams = new HashMap<>();
                oparams.put("screen_name", "home_screen");
                oparams.put("type_card","followingcard");
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

    }
}






