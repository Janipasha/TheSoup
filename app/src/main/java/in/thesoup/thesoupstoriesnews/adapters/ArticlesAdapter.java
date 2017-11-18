package in.thesoup.thesoupstoriesnews.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.ArticleWebViewActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedGSONMain.ArticlesMain;
import in.thesoup.thesoupstoriesnews.gsonclasses.FeedHome.ArticlesMainHome;
import in.thesoup.thesoupstoriesnews.gsonclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoupstoriesnews.R;

/**
 * Created by Jani on 08-09-2017.
 */


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

        private List<ArticlesMain> articles;
        private List<Articles> nArticles;
    private  List<ArticlesMainHome> oArticles;
        private Context mcontext;
        private String StoryTitle , SubstoryId,storyColor;
        private CleverTapAPI cleverTap;

        private SharedPreferences pref;


        public ArticlesAdapter(List<ArticlesMain> articles,List<Articles> nArticles,List<ArticlesMainHome> oArticles,String storyColor,String SubstoryId, Context mcontext) {
            this.SubstoryId = SubstoryId;
            this.articles = articles;
            this.nArticles = nArticles;
            this.oArticles = oArticles;
            this.storyColor = storyColor;
            this.mcontext = mcontext;
            try {
                this.cleverTap = CleverTapAPI.getInstance(mcontext);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articles, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if(articles!=null){


                String imageUrl = articles.get(position).getSourceThumb();
                String articleTitle = articles.get(position).getTitle();
                String time = articles.get(position).getPubDate();

                time = getTimedateformat(time);
                String articleTitlehtml;



                if(Build.VERSION.SDK_INT >= 24){
                    articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle , Html.FROM_HTML_MODE_LEGACY));

                }else{

                    articleTitlehtml = String.valueOf (Html.fromHtml(articleTitle));
                }
                String newsSource = articles.get(position).getsourceName();

                holder.articleTitle.setText(articleTitlehtml);
                holder.newsource.setText(newsSource);
                if(imageUrl!=null&&!imageUrl.isEmpty()){
                    Picasso.with(mcontext).load(imageUrl).fit().centerCrop().placeholder(R.drawable.placeholder).into(holder.imageView);
                }else {
                    Picasso.with(mcontext).load(R.drawable.ic_sample).fit().centerCrop().into(holder.imageView);
                }
                holder.time.setText(time);



            } else if(nArticles!=null){

                String imageUrl = nArticles.get(position).getSourceLogo();
                String articleTitle = nArticles.get(position).getArticleTitle();
                String time = nArticles.get(position).getTime();

                time = getTimedateformat(time);
                String articleTitlehtml;



                if(Build.VERSION.SDK_INT >= 24){
                    articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle , Html.FROM_HTML_MODE_LEGACY));

                }else{

                    articleTitlehtml = String.valueOf (Html.fromHtml(articleTitle));
                }
                String newsSource = nArticles.get(position).getSourceName();

                holder.articleTitle.setText(articleTitlehtml);
                holder.newsource.setText(newsSource);
                if(imageUrl!=null&&!imageUrl.isEmpty()){
                    Picasso.with(mcontext).load(imageUrl).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.imageView);
                }else {
                    Picasso.with(mcontext).load(R.drawable.ic_sample).fit().centerCrop().into(holder.imageView);
                }
                holder.time.setText(time);


            }else if(oArticles!=null){

                String imageUrl = oArticles.get(position).getSourceThumb();
                String articleTitle = oArticles.get(position).getTitle();
                String time = oArticles.get(position).getPubDate();

                time = getTimedateformat(time);
                String articleTitlehtml;



                if(Build.VERSION.SDK_INT >= 24){
                    articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle , Html.FROM_HTML_MODE_LEGACY));

                }else{

                    articleTitlehtml = String.valueOf (Html.fromHtml(articleTitle));
                }
                String newsSource = oArticles.get(position).getsourceName();

                holder.articleTitle.setText(articleTitlehtml);
                holder.newsource.setText(newsSource);
                if(imageUrl!=null&&!imageUrl.isEmpty()){
                    Picasso.with(mcontext).load(imageUrl).placeholder(R.drawable.placeholder).fit().centerCrop().into(holder.imageView);
                }else {
                    Picasso.with(mcontext).load(R.drawable.ic_sample).fit().centerCrop().into(holder.imageView);
                }
                holder.time.setText(time);



            }

        }

        @Override
        public int getItemCount() {

            if(articles!=null){
                return articles.size();
            }else if(nArticles!=null){
                return nArticles.size();
            }else if(oArticles!=null){
                return oArticles.size();
            }

            return 0;

        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imageView;
            private TextView articleTitle, time, newsource;
            private WebView wView;

            public ViewHolder(View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.sourceImage1);
                articleTitle = (TextView) itemView.findViewById(R.id.article_title1);
                newsource = (TextView) itemView.findViewById(R.id.source1);
                time =(TextView)itemView.findViewById(R.id.source1_time);


                imageView.setOnClickListener(this);
                articleTitle.setOnClickListener(this);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {

                HashMap<String,Object> mparams = new HashMap<>();
                mparams.put("screen_name", "articles_screen");
                mparams.put("category", "tap"); //(only if possible)

                int mposition = getAdapterPosition();

                String ArticleURL="";
                if(articles!=null){
                     ArticleURL = articles.get(mposition).getUrl();
                    mparams.put("article_title",articles.get(mposition).getTitle());
                    mparams.put("article_source",articles.get(mposition).getsourceName());
                    mparams.put("article_publish_date",articles.get(mposition).getPubDate());
                }else if(nArticles!=null){
                    ArticleURL = nArticles.get(mposition).getUrl();
                    mparams.put("article_title",nArticles.get(mposition).getArticleTitle());
                    mparams.put("article_source",nArticles.get(mposition).getSourceName());
                    mparams.put("article_publish_date",nArticles.get(mposition).getTime());
                }else if(oArticles!=null){
                    ArticleURL = oArticles.get(mposition).getUrl();
                    mparams.put("article_title",oArticles.get(mposition).getTitle());
                    mparams.put("article_source",oArticles.get(mposition).getsourceName());
                    mparams.put("article_publish_date",oArticles.get(mposition).getPubDate());
                }

                cleverTap.event.push("tap_card_source",mparams);



                Intent intent = new Intent(mcontext, ArticleWebViewActivity.class);
                intent.putExtra("substory_id",SubstoryId);
                intent.putExtra("ArticleURL",ArticleURL);
                intent.putExtra("storycolor",storyColor);
                mcontext.startActivity(intent);
                

                // TODO: add intents for chrome browser
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

    public String getTimedateformat(String timeString) {

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

            return time+ " " + Date + " " + month + " " + year;

        }

    }
    }

