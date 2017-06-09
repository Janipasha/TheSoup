package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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

//import in.thesoup.thesoup.Activities.ArticleWebViewActivity;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.Activities.ArticleWebViewActivity;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.R;


public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    private List<Articles> articles;
    private Context mcontext;
    // private AnalyticsApplication application;
    //private Tracker mTracker;
    private SharedPreferences pref;


    public ArticlesAdapter(List<Articles> articles, Context mcontext) {
        this.articles = articles;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.articleslist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imageUrl = articles.get(position).getImageUrl();
        String articleTitle = articles.get(position).getArticletitle();
        String articleTitlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle, Html.FROM_HTML_MODE_LEGACY));

        } else {

            articleTitlehtml = String.valueOf(Html.fromHtml(articleTitle));
        }
        String newsSource = articles.get(position).getSourceName();
        String Time = articles.get(position).getTime();

        String time = null;
        try{
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


        holder.articleTitle.setText(articleTitlehtml);
        holder.newsource.setText(newsSource);
        Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80, 80).into(holder.imageView);
        holder.date.setText(time+", "+Date+" "+month+" "+year);

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView articleTitle, date, newsource;
        private WebView wView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.article_image);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            date = (TextView) itemView.findViewById(R.id.Date);
            newsource = (TextView) itemView.findViewById(R.id.source_name);

           date.setOnClickListener(this);
            imageView.setOnClickListener(this);
            articleTitle.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //application = AnalyticsApplication.getInstance();
            //mTracker = application.getDefaultTracker();
            pref = PreferenceManager.getDefaultSharedPreferences(mcontext);

          /*  if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

                application.sendEvent(mTracker, SoupContract.CLICK, SoupContract.CLICK_SOURCES, SoupContract.ARTICLES_PAGE);

            }else{

                application.sendEventUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_SOURCES, SoupContract.ARTICLES_PAGE
                        ,pref.getString(SoupContract.FB_ID,null),
                        pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

            }*/

            int mposition = getAdapterPosition();

            String ArticleURL = articles.get(mposition).getUrl();

            Intent intent = new Intent(mcontext, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL", ArticleURL);
            mcontext.startActivity(intent);


            // TODO: add intents for chrome browser
        }
    }

    public String timeFormat(String string) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = timeFormat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("hh:mm");

        return monthFormat2.format(date);


    }


    public String monthFomrat(String string) throws ParseException {
        SimpleDateFormat monthformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = monthformat.parse(string);

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("MMM");

        return monthFormat2.format(date);


    }

    public String yearFomrat(String string) throws ParseException {
        SimpleDateFormat yearformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = yearformat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat yearFormat2 = new SimpleDateFormat("yyyy");

        return yearFormat2.format(date);


    }


    public String DateFomrat(String string) throws ParseException {
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateformat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd");

        return dateFormat2.format(date);


    }
}
