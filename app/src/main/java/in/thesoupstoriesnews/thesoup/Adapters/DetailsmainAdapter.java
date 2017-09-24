package in.thesoupstoriesnews.thesoup.Adapters;

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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.ArticleWebViewActivity;
import in.thesoupstoriesnews.thesoup.Activities.ArticlesActivity;
import in.thesoupstoriesnews.thesoup.Activities.FilterActivity;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.ArticlesMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.StoryDataMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.SubstoriesMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoupstoriesnews.thesoup.R;

import static in.thesoupstoriesnews.thesoup.R.id.firstarticle;
import static in.thesoupstoriesnews.thesoup.R.id.secondarticle;
import static in.thesoupstoriesnews.thesoup.R.id.sideline1;
import static in.thesoupstoriesnews.thesoup.R.id.sideline6;
import static in.thesoupstoriesnews.thesoup.R.id.thirdarticle;

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


    public DetailsmainAdapter(Context context, List<Substories> listDataHeader, String storyColour) {
        this.context = context;
        this.StoryDataList = listDataHeader;
        this.storyColour = storyColour;
        //CVIPUL Analytics
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //End Analytics
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
                    bottomtextnumarticles;

            public ImageView heroimage, shareicon, sourceimage1, sourceimage2, sourceimage3, circle3;


            public LinearLayout firstarticle,secondarticle, thirdarticle, articlelistmain;

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


                heroimage = (ImageView) itemView.findViewById(R.id.hero_image);
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
                bottommostline =(View)itemView.findViewById(R.id.bottommostline);

                firstarticle.setOnClickListener(this);
                secondarticle.setOnClickListener(this);
                thirdarticle.setOnClickListener(this);

                bottomtextnumarticles.setOnClickListener(this);
                bottomtextshowmore.setOnClickListener(this);
                shareicon.setOnClickListener(this);



                itemView.setOnClickListener(this);


            }

            @Override
            public void onClick(View view) {
                int mposition = getAdapterPosition();
                String storyColor = storyColour;

                if(view ==bottomtextnumarticles||view==bottomtextshowmore||view==heroimage){


                        List<Articles> mArticles = StoryDataList.get(getAdapterPosition()).getArticles();
                        Intent intent = new Intent(context, ArticlesActivity.class);
                        intent.putExtra("LISTARTICLES",(Serializable)mArticles);
                        intent.putExtra("story_color",storyColor);
                        context.startActivity(intent);

                    if(view==heroimage){
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "collection_screen");
                        nparams.putString("category", "tap");
                        nparams.putString("position_card_collection",String.valueOf(mposition));
                        mFirebaseAnalytics.logEvent("tap_card_cover", nparams);
                    }

                    if(view==bottomtextshowmore||view==bottomtextnumarticles){
                        Bundle nparams = new Bundle();
                        nparams.putString("screen_name", "collection_screen");
                        nparams.putString("category", "tap");
                        nparams.putString("position_card_collection",String.valueOf(mposition));
                        mFirebaseAnalytics.logEvent("tap_showall", nparams);

                    }
                  }


                if (view ==firstarticle){

                    String articleUrl = StoryDataList.get(mposition).getArticles().get(0).getUrl();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("storycolor", storyColor);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(1));
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);
                }

                if(view == secondarticle){
                    String articleUrl = StoryDataList.get(mposition).getArticles().get(1).getUrl();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("storycolor", storyColor);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(2));
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                }

                if(view == thirdarticle){
                    String articleUrl = StoryDataList.get(mposition).getArticles().get(2).getUrl();
                    Intent intent = new Intent(context, ArticleWebViewActivity.class);
                    intent.putExtra("ArticleURL", articleUrl);
                    intent.putExtra("storycolor", storyColor);
                    context.startActivity(intent);

                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "tap"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));
                    nparams.putString("position_card_entity",String.valueOf(2));
                    mFirebaseAnalytics.logEvent("tap_entity", nparams);

                }

                if(view==shareicon){


                    Bundle nparams = new Bundle();
                    nparams.putString("screen_name", "collection_screen");
                    nparams.putString("category", "conversion"); //(only if possible)
                    nparams.putString("position_card_collection",String.valueOf(mposition));;
                    mFirebaseAnalytics.logEvent("tap_shareicon", nparams);

                    String URL = "http://whatsonapp.info/share/"+StoryId+"/"+StoryDataList.get(mposition).getSubstoryId();
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

                holder.firstarticle.setVisibility(View.VISIBLE);
                holder.secondarticle.setVisibility(View.VISIBLE);
                holder.thirdarticle.setVisibility(View.VISIBLE);
                holder.bottomtextshowmore.setVisibility(View.VISIBLE);
                holder.bottomtextnumarticles.setVisibility(View.VISIBLE);

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
                holder.heroimage.setImageResource(R.drawable.background_splash);
            }


            fillarticles(holder,position);




        }

    public void fillarticles(StoryDataViewHolder holder, int position) {

        if (StoryDataList.get(position).getArticles().size() >= 3) {

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

            int numberofArticles = StoryDataList.get(position).getArticles().size();
            numberofArticles = numberofArticles -3;
            if(numberofArticles!=0){
                String bottomtext = String.valueOf(numberofArticles)+" More Articles In This Update.";
                holder.bottomtextnumarticles.setText(bottomtext);
                holder.bottomtextshowmore.setText("SHOW ALL");

            }

            if (source1icon != null && !source1.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
            }


            if (source2icon != null && !source2.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.background_splash);
            }


            if (source3icon != null && !source1.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage3);
            } else {
                holder.sourceimage3.setImageResource(R.drawable.background_splash);
            }

            if (StoryDataList.get(position).getArticles().size() >= 3) {

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

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.sideline.getLayoutParams();

            lp.addRule(RelativeLayout.ALIGN_BOTTOM,holder.articlelistmain.getId());

            holder.sideline.setLayoutParams(lp);

            if (source1icon != null && !source1.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
            }


            if (source2icon != null && !source2.isEmpty()) {
                Picasso.with(context).load(source2icon).placeholder(R.drawable.placeholder).into(holder.sourceimage2);
            } else {
                holder.sourceimage2.setImageResource(R.drawable.background_splash);
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

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.sideline.getLayoutParams();

            lp.addRule(RelativeLayout.ALIGN_BOTTOM,holder.articlelistmain.getId());


            holder.sideline.setLayoutParams(lp);



            if (source1icon != null && !source1.isEmpty()) {
                Picasso.with(context).load(source1icon).placeholder(R.drawable.placeholder).into(holder.sourceimage1);
            } else {
                holder.sourceimage1.setImageResource(R.drawable.background_splash);
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

            Log.d("difference",String.valueOf(Difference));

            int i = (int) (Difference/86400000);
            Log.d("kya be",String.valueOf(i));

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



