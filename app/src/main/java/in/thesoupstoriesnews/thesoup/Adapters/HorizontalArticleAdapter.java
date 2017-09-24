package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.ArticleWebViewActivity;
import in.thesoupstoriesnews.thesoup.Activities.DetailsActivity;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.R;

/**
 * Created by Jani on 15-07-2017.
 */

public class HorizontalArticleAdapter extends RecyclerView.Adapter<HorizontalArticleAdapter.ViewHolder> {

    private Context context;
    private List<Articles> articles;
    private String storyColor;
    private String SubstoryId;

    public HorizontalArticleAdapter(Context context,List<Articles> articles,String storyColor,String SubstoryId){
        this.context = context;
        this.articles= articles;
        this.storyColor = storyColor;
        this.SubstoryId =SubstoryId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = inflater.inflate(R.layout.articlelist, null, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.imageView = (ImageView) cardView.findViewById(R.id.logo);
        viewHolder.articleTitle = (TextView) cardView.findViewById(R.id.article_title);
        viewHolder.date = (TextView) cardView.findViewById(R.id.Date);
        viewHolder.newsource = (TextView) cardView.findViewById(R.id.source_name);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imageUrl =articles.get(position).getSourceLogo();
        String ArticleTitle =articles.get(position).getArticleTitle();
        String articleTitlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            articleTitlehtml = String.valueOf(Html.fromHtml(ArticleTitle, Html.FROM_HTML_MODE_LEGACY));

        } else {

            articleTitlehtml = String.valueOf(Html.fromHtml(ArticleTitle));
        }
        String newsSource =articles.get(position).getSourceName();
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

        if (imageUrl != null && !imageUrl.isEmpty()) {

            Picasso.with(context).load(imageUrl).placeholder(R.drawable.placeholder).resize(30, 30).centerCrop().into(holder.imageView);

        } else {
            holder.imageView.setImageResource(R.drawable.background_splash);
        }
        // Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80, 80).into(imageView);
        holder.date.setText(time+", "+Date+" "+month+" "+year);



    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView articleTitle, date, newsource;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.logo);
            articleTitle = (TextView) itemView.findViewById(R.id.article_title);
            date = (TextView) itemView.findViewById(R.id.Date);
            newsource = (TextView) itemView.findViewById(R.id.source_name);
            linearLayout =(LinearLayout)itemView.findViewById(R.id.articlelayout);

            linearLayout.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();

            String ArticleURL = articles.get(position).getUrl();



            Intent intent = new Intent(context, ArticleWebViewActivity.class);
            intent.putExtra("ArticleURL", ArticleURL);
            intent.putExtra("storycolor",storyColor);
           intent.putExtra("substory_id",SubstoryId);
            /*//CVIPUL Analytics
            intent.putExtra("collection_id", StoryId);
            intent.putExtra("follow_status", followStatus);
            intent.putExtra("collection_name", StoryTitle);
            intent.putExtra("category", "tap");
            intent.putExtra("id_entity", entity_id);
            intent.putExtra("type_entity", "article");
            intent.putExtra("name_entity_source", source);
            intent.putExtra("name_entity_title", entity_title);
            intent.putExtra("time_entity_publish_source", pub_time);*/
            //End Analytics
            context.startActivity(intent);


        }
    }

    public String timeFormat(String string) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = timeFormat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

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
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
