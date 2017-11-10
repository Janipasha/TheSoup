package in.thesoup.thesoupstoriesnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.ArticleWebViewActivity;
import in.thesoup.thesoupstoriesnews.activities.ArticlesActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoupstoriesnews.R;

/**
 * Created by Jani on 08-09-2017.
 */

public class DetailsmainAdapter extends RecyclerView.Adapter<DetailsmainAdapter.StoryDataViewHolder> {


        private List<Substories> StoryDataList;
        private Context context;
        private int clickposition, fragmenttag;
        private SharedPreferences pref;
        private String storyColour,StoryId;
        private FirebaseAnalytics mFirebaseAnalytics;
        private CleverTapAPI clevertap;


    public DetailsmainAdapter(Context context, List<Substories> listDataHeader, String storyColour) {
        this.context = context;
        this.StoryDataList = listDataHeader;
        this.storyColour = storyColour;
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        try {
            this.clevertap = CleverTapAPI.getInstance(context);
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }
    }

    public void refreshData(List<Substories> substories,String StoryId) {
        StoryDataList.addAll(substories);
        this.StoryId = StoryId;
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<Substories> substories,String StoryId){
        this.StoryDataList = substories;
        this.StoryId = StoryId;
        notifyDataSetChanged();
    }

        public class StoryDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView  update3, time3, source1, source2, source3, source1time, source2time, source3time, articletitle1, articletitle2, articletitle3,bottomtextshowmore,
                    bottomtextnumarticles,readstatus;

            public ImageView heroimage, shareicon, sourceimage1, sourceimage2, sourceimage3, circle3,seenImage;

            public LinearLayout firstarticle,secondarticle, thirdarticle, articlelistmain,bottommostlinelayout;

            public View sideline6,bottommostline,sideline;

            public StoryDataViewHolder(View itemView) {
                super(itemView);




                update3 = (TextView) itemView.findViewById(R.id.update3);
                time3 = (TextView) itemView.findViewById(R.id.time3);
                source1 = (TextView) itemView.findViewById(R.id.source1);
                source2 = (TextView) itemView.findViewById(R.id.source2);
                source3 = (TextView) itemView.findViewById(R.id.source3);
                source1time = (TextView) itemView.findViewById(R.id.source1_time);
                source2time = (TextView) itemView.findViewById(R.id.source2_time);
                source3time = (TextView) itemView.findViewById(R.id.source3_time);
                articletitle1 = (TextView) itemView.findViewById(R.id.article_title1);
                articletitle2 = (TextView) itemView.findViewById(R.id.article_title2);
                articletitle3 = (TextView) itemView.findViewById(R.id.article_title3);
                bottomtextshowmore = (TextView)itemView.findViewById(R.id.bottomtext_showall) ;
                bottomtextnumarticles =(TextView)itemView.findViewById(R.id.bottomtext_numarticles);
                readstatus = (TextView)itemView.findViewById(R.id.readstatus_text_story);

                heroimage = (ImageView) itemView.findViewById(R.id.heroimage);
                seenImage = (ImageView)itemView.findViewById(R.id.readstatus_story);
                shareicon = (ImageView) itemView.findViewById(R.id.shareicon);
                sourceimage1 = (ImageView) itemView.findViewById(R.id.sourceImage1);
                sourceimage2 = (ImageView) itemView.findViewById(R.id.sourceImage2);
                sourceimage3 = (ImageView) itemView.findViewById(R.id.sourceImage3);
                circle3 = (ImageView) itemView.findViewById(R.id.circle3);


                sideline6 = (View)itemView.findViewById(R.id.sideline6);
                sideline = (View)itemView.findViewById(R.id.sideline);


                firstarticle = (LinearLayout)itemView.findViewById(R.id.firstarticle);
                secondarticle = (LinearLayout)itemView.findViewById(R.id.secondarticle);
                thirdarticle = (LinearLayout)itemView.findViewById(R.id.thirdarticle);
                articlelistmain = (LinearLayout)itemView.findViewById(R.id.article_list_main);
                bottommostlinelayout = (LinearLayout)itemView.findViewById(R.id.bottommostlinelayout);
                bottommostline =(View)itemView.findViewById(R.id.bottommostline);

                firstarticle.setOnClickListener(this);
                secondarticle.setOnClickListener(this);
                thirdarticle.setOnClickListener(this);

                bottomtextnumarticles.setOnClickListener(this);
                bottomtextshowmore.setOnClickListener(this);
                shareicon.setOnClickListener(this);

                heroimage.setOnClickListener(this);
                seenImage.setOnClickListener(this);
                readstatus.setOnClickListener(this);



                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View view) {
                int mposition = getAdapterPosition();
                String storyColor = storyColour;
                String substoryId = StoryDataList.get(mposition).getSubstoryId();
                String substoryname = StoryDataList.get(mposition).getSubstoryName();

                if(view ==bottomtextnumarticles||view==bottomtextshowmore||view==heroimage||view==seenImage||view==readstatus){


                        List<Articles> mArticles = StoryDataList.get(getAdapterPosition()).getArticles();
                        Intent intent = new Intent(context, ArticlesActivity.class);
                        intent.putExtra("LISTARTICLES",(Serializable)mArticles);
                         intent.putExtra("substory_id",substoryId);
                        intent.putExtra("story_color",storyColor);
                        context.startActivity(intent);


                    if(view==heroimage||view==seenImage||view==readstatus){
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "collection_screen");
                        nparams.putString("category", "tap");
                        nparams.putString("position_card_collection",String.valueOf(mposition));
                        nparams.putString("substory_id",substoryId);
                        mFirebaseAnalytics.logEvent("tap_card_cover", nparams);


                        HashMap<String,Object> aparams = new HashMap<>();
                        aparams.put("screen_name", "collection_screen");
                        aparams.put("category", "tap");
                        aparams.put("position_card_collection",String.valueOf(mposition));
                        aparams.put("substory_id",substoryId);
                        aparams.put("substory_name",substoryname);
                        clevertap.event.push("tap_card_cover",aparams);
                    }

                    if(view==bottomtextshowmore||view==bottomtextnumarticles){
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "collection_screen");
                        nparams.putString("category", "tap");
                        nparams.putString("substory_id",substoryId);
                        nparams.putString("position_card_collection",String.valueOf(mposition));
                        mFirebaseAnalytics.logEvent("tap_showall", nparams);

                        HashMap<String,Object> aparams = new HashMap<>();
                        aparams.put("screen_name", "collection_screen");
                        aparams.put("category", "tap");
                        aparams.put("position_card_collection",String.valueOf(mposition));
                        aparams.put("substory_id",substoryId);
                        aparams.put("substory_name",substoryname);
                        clevertap.event.push("tap_showall",aparams);

                    }
                  }


                if (view ==firstarticle){

                    String articleUrl = StoryDataList.get(mposition).getArticles().get(0).getUrl();
                    String articleTitle = StoryDataList.get(mposition).getArticles().get(0).getArticleTitle();
                    String articleSource = StoryDataList.get(mposition).getArticles().get(0).getSourceName();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("substory_id",substoryId);
                    intent.putExtra("storycolor", storyColor);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(1));
                    nparams.putString("article_title",articleTitle);
                    nparams.putString("substory_id",substoryId);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                    HashMap<String,Object> aparams = new HashMap<>();
                    aparams.put("screen_name", "collection_screen");
                    aparams.put("category", "tap"); //(only if possible)
                    aparams.put("position_card_collection",String.valueOf(mposition));
                    aparams.put("position_card_entity",String.valueOf(1));
                    aparams.put("article_title",articleTitle);
                    aparams.put("substory_id",substoryId);
                    aparams.put("substory_name",substoryname);
                    aparams.put("article_source",articleSource);
                    clevertap.event.push("tap_entity", aparams);

                }

                if(view == secondarticle){
                    String articleUrl = StoryDataList.get(mposition).getArticles().get(1).getUrl();
                    String articleTitle = StoryDataList.get(mposition).getArticles().get(1).getArticleTitle();
                    String articleSource = StoryDataList.get(mposition).getArticles().get(1).getSourceName();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("substory_id",substoryId);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("storycolor", storyColor);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(2));
                    nparams.putString("article_title",articleTitle);
                    nparams.putString("substory_id",substoryId);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                    HashMap<String,Object> aparams = new HashMap<>();
                    aparams.put("screen_name", "collection_screen");
                    aparams.put("category", "tap"); //(only if possible)
                    aparams.put("position_card_collection",String.valueOf(mposition));
                    aparams.put("position_card_entity",String.valueOf(2));
                    aparams.put("article_title",articleTitle);
                    aparams.put("substory_id",substoryId);
                    aparams.put("substory_name",substoryname);
                    aparams.put("article_source",articleSource);
                    clevertap.event.push("tap_entity", aparams);

                }

                if(view == thirdarticle){
                    String articleUrl = StoryDataList.get(mposition).getArticles().get(2).getUrl();
                    String articleTitle = StoryDataList.get(mposition).getArticles().get(2).getArticleTitle();
                    String articleSource = StoryDataList.get(mposition).getArticles().get(2).getSourceName();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("storycolor", storyColor);
                    intent.putExtra("substory_id",substoryId);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(3));
                    nparams.putString("substory_id",substoryId);
                    nparams.putString("article_title",articleTitle);
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                    HashMap<String,Object> aparams = new HashMap<>();
                    aparams.put("screen_name", "collection_screen");
                    aparams.put("category", "tap"); //(only if possible)
                    aparams.put("position_card_collection",String.valueOf(mposition));
                    aparams.put("position_card_entity",String.valueOf(3));
                    aparams.put("substory_id",substoryId);
                    aparams.put("article_title",articleTitle);
                    aparams.put("article_source",articleSource);
                    aparams.put("substory_name",substoryname);
                    clevertap.event.push("tap_entity", aparams);
                }

                if(view==shareicon){


                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "conversion"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));;
                    nparams.putString("substory_id",substoryId);
                    mFirebaseAnalytics.logEvent("tap_shareicon", nparams);


                    HashMap<String,Object> aparams = new HashMap<>();
                    aparams.put("screen_name", "collection_screen");
                    aparams.put("category", "conversion"); //(only if possible)
                    aparams.put("position_card_collection",String.valueOf(mposition));;
                    aparams.put("substory_id",substoryId);
                    aparams.put("substory_name",substoryname);
                    clevertap.event.push("tap_shareicon",aparams);

                    String URL =  "http://thesoup.in/share/"+StoryId+"/"+StoryDataList.get(mposition).getSubstoryId()+"?utm_source=appshare&utm_medium="+StoryId+"&utm_campaign="+ StoryDataList.get(mposition).getSubstoryId();;
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT,  "Share via");
                    i.putExtra(Intent.EXTRA_TEXT, URL);
                    context.startActivity(Intent.createChooser(i,"Share via"));


                }


            }

        }




        @Override
        public DetailsmainAdapter.StoryDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detailscard_main, parent, false);
                return new StoryDataViewHolder(view);
             }





        @Override
        public void onBindViewHolder(DetailsmainAdapter.StoryDataViewHolder holder, int position) {



            Log.e("story_Id details",StoryDataList.get(position).getSubstoryId());
            holder.sideline.setVisibility(View.VISIBLE);
                holder.seenImage.setVisibility(View.GONE);
                holder.readstatus.setVisibility(View.GONE);
                holder.firstarticle.setVisibility(View.VISIBLE);
                holder.secondarticle.setVisibility(View.VISIBLE);
                holder.thirdarticle.setVisibility(View.VISIBLE);
                holder.bottomtextshowmore.setVisibility(View.VISIBLE);
                holder.bottomtextnumarticles.setVisibility(View.VISIBLE);

            String readstatus = StoryDataList.get(position).getReadStatus();

            if(StoryDataList.get(position).getReadStatus()!=null&&!StoryDataList.get(position).getReadStatus().isEmpty()){
                holder.seenImage.setVisibility(View.VISIBLE);
                holder.readstatus.setVisibility(View.VISIBLE);
            }

            if (storyColour != null && !storyColour.isEmpty()) {
                holder.bottomtextshowmore.setTextColor(Color.parseColor("#"+storyColour));
                holder.sideline6.setBackgroundColor(Color.parseColor("#"+storyColour));
                holder.sideline.setBackgroundColor(Color.parseColor("#"+storyColour));
                holder.circle3.setColorFilter(Color.parseColor("#"+storyColour));
                holder.bottommostline.setBackgroundColor(Color.parseColor("#33"+storyColour));
            }

            if(position==StoryDataList.size()-1){
                holder.sideline.setVisibility(View.GONE);
            }



            String time = StoryDataList.get(position).getTime();
            time = getTime(time);

            holder.time3.setText(time);

            String ImageUrl = StoryDataList.get(position).getSubstoryImageURL();
            if (ImageUrl != null && !ImageUrl.isEmpty()) {

                Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.heroimage);

            } else {
                holder.heroimage.setImageResource(R.drawable.ic_sample);
            }


            fillarticles(holder,position);




        }

    public void fillarticles(StoryDataViewHolder holder, int position) {


        if (StoryDataList.get(position).getArticles().size() >= 3) {

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.bottommostline.getLayoutParams();
            lp.setMargins(0,65,0,0);
            holder.bottommostline.setLayoutParams(lp);

            RelativeLayout.LayoutParams Rp = (RelativeLayout.LayoutParams)holder.sideline.getLayoutParams();
            Rp.addRule(RelativeLayout.ALIGN_BOTTOM,holder.bottommostlinelayout.getId());
            holder.sideline.setLayoutParams(Rp);


            List<Articles> Articles = StoryDataList.get(position).getArticles();

            String source1 = Articles.get(0).getSourceName();
            String source2 = Articles.get(1).getSourceName();
            String source3 = Articles.get(2).getSourceName();

            holder.source1.setText(source1);
            holder.source2.setText(source2);
            holder.source3.setText(source3);

            String source1time = Articles.get(0).getTime();
            String source2time = Articles.get(1).getTime();
            String source3time = Articles.get(2).getTime();

            source1time = getTimedateformat(source1time);
            source2time = getTimedateformat(source2time);
            source3time = getTimedateformat(source3time);

            holder.source1time.setText(source1time);
            holder.source2time.setText(source2time);
            holder.source3time.setText(source3time);


            String source1icon = Articles.get(0).getSourceLogo();
            String source2icon = Articles.get(1).getSourceLogo();
            String source3icon = Articles.get(2).getSourceLogo();

            String articleTitle1 = Articles.get(0).getArticleTitle();
            String articleTitle2 = Articles.get(1).getArticleTitle();
            String articleTitle3 = Articles.get(2).getArticleTitle();

            articleTitle1 = gethtmlString(articleTitle1);
            articleTitle2 = gethtmlString(articleTitle2);
            articleTitle3 = gethtmlString(articleTitle3);

            holder.articletitle1.setText(articleTitle1);
            holder.articletitle2.setText(articleTitle2);
            holder.articletitle3.setText(articleTitle3);

            holder.bottomtextshowmore.setVisibility(View.GONE);
            holder.bottomtextnumarticles.setVisibility(View.GONE);

            int numberofArticles = StoryDataList.get(position).getArticles().size();
            numberofArticles = numberofArticles -3;
            if(numberofArticles!=0){
                String bottomtext = String.valueOf(numberofArticles)+" More Articles In This Update.";
                holder.bottomtextnumarticles.setText(bottomtext);
                holder.bottomtextshowmore.setText("SHOW ALL");

            }

            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.ic_launcher);
            }


            if (source3icon != null && !source3icon.isEmpty()) {
                Picasso.with(context).load(source3icon).placeholder(R.drawable.placeholder).into(holder.sourceimage3);
            } else {
                holder.sourceimage3.setImageResource(R.drawable.ic_launcher);
            }

            if (StoryDataList.get(position).getArticles().size() > 3) {

                holder.bottomtextshowmore.setVisibility(View.VISIBLE);
                holder.bottomtextnumarticles.setVisibility(View.VISIBLE);

            }

        } else if (StoryDataList.get(position).getArticles().size() == 2) {

            List<Articles> Articles = StoryDataList.get(position).getArticles();

            String source1 = Articles.get(0).getSourceName();
            String source2 = Articles.get(1).getSourceName();

            holder.source1.setText(source1);
            holder.source2.setText(source2);


            String source1time = Articles.get(0).getTime();
            String source2time = Articles.get(1).getTime();

            source1time = getTimedateformat(source1time);
            source2time = getTimedateformat(source2time);

            holder.source1time.setText(source1time);
            holder.source2time.setText(source2time);

            String source1icon = Articles.get(0).getSourceLogo();
            String source2icon = Articles.get(1).getSourceLogo();

            String articleTitle1 = Articles.get(0).getArticleTitle();
            String articleTitle2 = Articles.get(1).getArticleTitle();

            articleTitle1 = gethtmlString(articleTitle1);
            articleTitle2 = gethtmlString(articleTitle2);

            holder.articletitle1.setText(articleTitle1);
            holder.articletitle2.setText(articleTitle2);

            holder.bottomtextshowmore.setVisibility(View.GONE);
            holder.bottomtextnumarticles.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.bottommostline.getLayoutParams();

           lp.setMargins(0,0,0,0);

            holder.bottommostline.setLayoutParams(lp);

            RelativeLayout.LayoutParams Rp = (RelativeLayout.LayoutParams)holder.sideline.getLayoutParams();
            Rp.addRule(RelativeLayout.ALIGN_BOTTOM,holder.bottommostlinelayout.getId());
            holder.sideline.setLayoutParams(Rp);


            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }


            if (source2icon != null && !source2icon.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.ic_launcher);
            }

            holder.thirdarticle.setVisibility(View.GONE);

        } else if (StoryDataList.get(position).getArticles().size()== 1) {

            List<Articles> Articles = StoryDataList.get(position).getArticles();

            String source1 = Articles.get(0).getSourceName();
            holder.source1.setText(source1);


            String source1time = Articles.get(0).getTime();
            source1time = getTimedateformat(source1time);
            holder.source1time.setText(source1time);

            String source1icon = Articles.get(0).getSourceLogo();

            String articleTitle1 = Articles.get(0).getArticleTitle();

            articleTitle1 = gethtmlString(articleTitle1);

            holder.articletitle1.setText(articleTitle1);



            holder.bottomtextshowmore.setVisibility(View.GONE);
            holder.bottomtextnumarticles.setVisibility(View.GONE);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.bottommostline.getLayoutParams();

            lp.setMargins(0,0,0,0);

            holder.bottommostline.setLayoutParams(lp);

            RelativeLayout.LayoutParams Rp = (RelativeLayout.LayoutParams)holder.sideline.getLayoutParams();
            Rp.addRule(RelativeLayout.ALIGN_BOTTOM,holder.bottommostlinelayout.getId());
            holder.sideline.setLayoutParams(Rp);



            if (source1icon != null && !source1icon.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.ic_launcher);
            }

            holder.thirdarticle.setVisibility(View.GONE);
            holder.secondarticle.setVisibility(View.GONE);
        }
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
            return StoryDataList.size();
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


        public String getTime(String timeString){

            String Time = timeString;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(new Date());


            Date date1=null;
            Date date2 = null;

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date1 = format.parse(Time);
                date2 = format.parse(currentTime);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            long Difference = date2.getTime()-date1.getTime();

            int i = (int) (Difference/86400000);

            if(i<1){

                int m = (int)(Difference/3600000);

                if(m>1){
                    return String.valueOf(m)+" hours ago";
                }else{
                    return "1 hour ago";
                }

            }else{

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

        public String getTimedateformat(String timeString){

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



