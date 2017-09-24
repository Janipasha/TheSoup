package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.ArticleWebViewActivity;
import in.thesoupstoriesnews.thesoup.Activities.ArticlesActivity;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSONMain.ArticlesMain;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

/**
 * Created by Jani on 08-09-2017.
 */


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

        private List<ArticlesMain> articles;
    private List<Articles> nArticles;
        private Context mcontext;
    private String StoryTitle , storyId,storyColor;

        private SharedPreferences pref;


        public ArticlesAdapter(List<ArticlesMain> articles,List<Articles> nArticles,String storyColor, Context mcontext) {
            this.articles = articles;
            this.nArticles = nArticles;
            this.storyColor = storyColor;
            this.mcontext = mcontext;
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
                Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80,80).into(holder.imageView);
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
                Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80,80).into(holder.imageView);
                holder.time.setText(time);


            }

        }

        @Override
        public int getItemCount() {

            if(articles!=null){
                return articles.size();
            }else if(nArticles!=null){
                return nArticles.size();
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
            }

            @Override
            public void onClick(View view) {

                int mposition = getAdapterPosition();

                String ArticleURL="";
                if(articles!=null){
                     ArticleURL = articles.get(mposition).getUrl();
                }else if(nArticles!=null){
                    ArticleURL = nArticles.get(mposition).getUrl();
                }



                Intent intent = new Intent(mcontext, ArticleWebViewActivity.class);
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

