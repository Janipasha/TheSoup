package in.thesoupstoriesnews.thesoup.Adapters;

/**
 * Created by Jani on 09-06-2017.
 */

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoupstoriesnews.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoupstoriesnews.thesoup.R;

import static android.media.CamcorderProfile.get;
import static in.thesoupstoriesnews.thesoup.R.id.showmore;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mcontext;
    private List<Substories> _listDataHeader; // header titles
    // child data in format of header title, child title
    private List<Articles> _listDataChild;
    private String storyColour;
    //private HashMap<String, List<String>> _listDataChild;
    //CVIPUL Analytics
    private FirebaseAnalytics mFirebaseAnalytics;
    private ChildHolder childHolder = new ChildHolder();
    //End Analytics

    public ExpandableListAdapter(Context context, List<Substories> listDataHeader, String storyColour) {
        this.mcontext = context;
        this._listDataHeader = listDataHeader;
        this.storyColour = storyColour;
        //CVIPUL Analytics
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //End Analytics
    }

    public void refreshData(List<Substories> substories) {
        _listDataHeader.addAll(substories);
        notifyDataSetChanged();
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataHeader.get(groupPosition).getArticles()
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        // ImageView imageView;
        //TextView articleTitle, date, newsource,read;
        //WebView wView;


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.getarticles, parent, false);

            childHolder = new ChildHolder();
            convertView.setTag(childHolder);
        }

        childHolder.horizontalListView = (RecyclerView) convertView.findViewById(R.id.list_articles);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mcontext, LinearLayoutManager.HORIZONTAL, false);
        childHolder.horizontalListView.setLayoutManager(layoutManager);

        View bottomlineArticle = (View) convertView.findViewById(R.id.bottomline_article);
        TextView showmore = (TextView) convertView.findViewById(R.id.showmore);
        showmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childHolder.horizontalListView.smoothScrollToPosition(_listDataHeader.get(groupPosition).getArticles().size() - 1);
            }
        });
        if (storyColour != null && !storyColour.isEmpty()) {
            showmore.setTextColor(Color.parseColor("#" + storyColour));
            bottomlineArticle.setBackgroundColor(Color.parseColor("#" + storyColour));
        } else {
            showmore.setTextColor(Color.parseColor("#0000000"));
        }

        showmore.setText(String.valueOf(_listDataHeader.get(groupPosition).getArticles().size() + " Articles SHOW MORE"));

        ViewGroup.MarginLayoutParams marginLayoutParams =
                (ViewGroup.MarginLayoutParams) childHolder.horizontalListView.getLayoutParams();
        marginLayoutParams.setMargins(20, 30, 20, 0);
        childHolder.horizontalListView.setLayoutParams(marginLayoutParams);

        HorizontalArticleAdapter articlesListAdapter = new HorizontalArticleAdapter(mcontext, _listDataHeader.get(groupPosition).getArticles(), storyColour, _listDataHeader.get(groupPosition).getSubstoryId());
        childHolder.horizontalListView.setAdapter(articlesListAdapter);

        return convertView;

        //imageView = (ImageView) convertView.findViewById(R.id.logo);
        //articleTitle = (TextView) convertView.findViewById(R.id.article_title);
        //date = (TextView) convertView.findViewById(R.id.Date);
        //newsource = (TextView) convertView.findViewById(R.id.source_name);
        //read = (TextView)convertView.findViewById(R.id.read);

        //if(storyColour!=null&&!storyColour.isEmpty()){
        //read.setTextColor(Color.parseColor("#"+storyColour));
        // }else {
        // read.setTextColor(Color.parseColor("#000000"));

//        }



        /*_listDataChild= _listDataHeader.get(groupPosition).getArticles();

        String imageUrl = _listDataChild.get(childPosition).getSourceLogo();
        String ArticleTitle = _listDataChild.get(childPosition).getArticleTitle();
        String articleTitlehtml;

        if (Build.VERSION.SDK_INT >= 24) {
            articleTitlehtml = String.valueOf(Html.fromHtml(ArticleTitle, Html.FROM_HTML_MODE_LEGACY));

        } else {

            articleTitlehtml = String.valueOf(Html.fromHtml(ArticleTitle));
        }
        String newsSource = _listDataChild.get(childPosition).getSourceName();
        String Time = _listDataChild.get(childPosition).getTime();

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


        articleTitle.setText(articleTitlehtml);
        newsource.setText(newsSource);

        if (imageUrl != null && !imageUrl.isEmpty()) {

            Picasso.with(mcontext).load(imageUrl).placeholder(R.drawable.placeholder).resize(80, 80).centerCrop().into(imageView);

        } else {
            imageView.setImageResource(R.drawable.background_splash);
        }
       // Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80, 80).into(imageView);
        date.setText(time+", "+Date+" "+month+" "+year);


        //date.setOnClickListener(this);
        //imageView.setOnClickListener(this);
        //articleTitle.setOnClickListener(this);*/

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //return this._listDataHeader.get(groupPosition).getArticles().size();
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        ImageView mImageView, ReadImageblur, circle;

        TextView mSubstory, mNumber_of_articles, Readstatustext, mDate, mMonth, mYear;
        View leftline, leftline1, bottomline, topline;


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.detailscreen, null);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.main_image_story);
        mSubstory = (TextView) convertView.findViewById(R.id.substory_title_story);
        mNumber_of_articles = (TextView) convertView.findViewById(R.id.number_of_articles_story);
        mDate = (TextView) convertView.findViewById(R.id.date_story);
        leftline = (View) convertView.findViewById(R.id.leftline);
        leftline1 = (View) convertView.findViewById(R.id.leftline1);
        bottomline = (View) convertView.findViewById(R.id.bottomline);
        topline = (View) convertView.findViewById(R.id.topline);
        circle = (ImageView) convertView.findViewById(R.id.circle);
        topline.setVisibility(View.VISIBLE);
        leftline.setVisibility(View.VISIBLE);
        leftline1.setVisibility(View.VISIBLE);
        mNumber_of_articles.setVisibility(View.VISIBLE);
        bottomline.setVisibility(View.VISIBLE);


        if (groupPosition == 0) {
            topline.setVisibility(View.GONE);
        }

        if (groupPosition == _listDataHeader.size() - 1) {
            leftline.setVisibility(View.INVISIBLE);
            leftline1.setVisibility(View.INVISIBLE);

        }
        ReadImageblur = (ImageView) convertView.findViewById(R.id.readstatus_story);
        Readstatustext = (TextView) convertView.findViewById(R.id.readstatus_text_story);

        ReadImageblur.setVisibility(View.GONE);
        Readstatustext.setVisibility(View.GONE);


        if (storyColour != null && !storyColour.isEmpty()) {
            mNumber_of_articles.setTextColor(Color.parseColor("#" + storyColour));
            leftline.setBackgroundColor(Color.parseColor("#" + storyColour));
            leftline1.setBackgroundColor(Color.parseColor("#" + storyColour));
            bottomline.setBackgroundColor(Color.parseColor("#" + "80" + storyColour));
            topline.setBackgroundColor(Color.parseColor("#" + storyColour));
            circle.setColorFilter(Color.parseColor("#" + storyColour));

        } else {
            mNumber_of_articles.setTextColor(Color.parseColor("#000000"));
        }

        final Substories mSubstories = _listDataHeader.get(groupPosition);


        if (mSubstories.getReadStatus() != null && !mSubstories.getReadStatus().isEmpty()) {

            if (mSubstories.getReadStatus().equals("1")) {
                ReadImageblur.setVisibility(View.VISIBLE);
                Readstatustext.setVisibility(View.VISIBLE);
            }
        }

        String Time = mSubstories.getTime();
        String substoryTitle = mSubstories.getSubstoryName();
        String substoryTitlehtml;
        if (Build.VERSION.SDK_INT >= 24) {
            substoryTitlehtml = String.valueOf(Html.fromHtml(substoryTitle, Html.FROM_HTML_MODE_LEGACY));

        } else {

            substoryTitlehtml = String.valueOf(Html.fromHtml(substoryTitle));
        }

        int NumberofArticles = mSubstories.getNumberofArticles();
        String SubstoryImage = mSubstories.getSubstoryImageURL();

        // Log.i("SubstoryImage", SubstoryImage);
        Log.i("Time", Time);
        // Log.i("StoryTitle", StoryTitle);
        Log.i("substoryTitle", substoryTitle);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());


        Log.d("time-current", "/n" + currentTime);
        Log.d("time-current1", "/n" + Time);


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
                mDate.setText(String.valueOf(m) + " hours ago");
            } else {
                mDate.setText("1 hour ago");
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

            Log.d("Month", month);


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

            mDate.setText(Date + " " + month + " " + year);

        }


        if (isExpanded) {


            mNumber_of_articles.setVisibility(View.INVISIBLE);
            bottomline.setVisibility(View.INVISIBLE);
            // CVIPUL Analytics
            // TODO : Verify follow event, add collection location if possible
            Bundle mparams = new Bundle();
            mparams.putString("screen_name", "collection_screen"); // "myfeed / discover"
            mparams.putString("card_type", "subcollection");
            mparams.putString("subcollection_name", substoryTitle);
            mparams.putString("category", "conversion");
            mFirebaseAnalytics.logEvent("explore_subcollection", mparams);
            // End Analytics

        } else {
            mNumber_of_articles.setText(String.valueOf(NumberofArticles) + " Articles SEE ALL");
        }


        //((StoryViewHolder) holder).mMonth.setText(month);
        //((StoryViewHolder) holder).mYear.setText(year);
        mSubstory.setText(substoryTitlehtml);

        if (SubstoryImage != null && !SubstoryImage.isEmpty()) {
            Picasso.with(mcontext).load(SubstoryImage).placeholder(R.drawable.placeholder).into(mImageView);
        } else {
            mImageView.setImageResource(R.drawable.background_splash);
        }


        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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

    private static class ChildHolder {
        static RecyclerView horizontalListView;
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