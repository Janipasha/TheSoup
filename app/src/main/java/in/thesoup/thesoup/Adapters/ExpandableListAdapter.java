package in.thesoup.thesoup.Adapters;

/**
 * Created by Jani on 09-06-2017.
 */

import android.graphics.Color;
import android.os.Build;
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

import com.squareup.picasso.Picasso;

import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
import in.thesoup.thesoup.R;

import static android.media.CamcorderProfile.get;
import static in.thesoup.thesoup.R.layout.articleslist;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mcontext;
    private List<Substories> _listDataHeader; // header titles
    // child data in format of header title, child title
    private List<Articles> _listDataChild;
    private String storyColour;
    //private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<Substories> listDataHeader,String storyColour) {
        this.mcontext = context;
        this._listDataHeader = listDataHeader;
        this.storyColour = storyColour;
    }

    public void refreshData(List<Substories> substories){
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
    public View getChildView (int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

         ImageView imageView;
         TextView articleTitle, date, newsource;
         WebView wView;




        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(articleslist, null);
        }

        imageView = (ImageView) convertView.findViewById(R.id.article_image);
        articleTitle = (TextView) convertView.findViewById(R.id.article_title);
        date = (TextView) convertView.findViewById(R.id.Date);
        newsource = (TextView) convertView.findViewById(R.id.source_name);

        _listDataChild= _listDataHeader.get(groupPosition).getArticles();

        String imageUrl = _listDataChild.get(childPosition).getImageUrl();
        String ArticleTitle = _listDataChild.get(childPosition).getArticletitle();
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


        articleTitle.setText(articleTitlehtml);
        newsource.setText(newsSource);
        Picasso.with(mcontext).load(imageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(80, 80).into(imageView);
        date.setText(time+", "+Date+" "+month+" "+year);


        //date.setOnClickListener(this);
        //imageView.setOnClickListener(this);
        //articleTitle.setOnClickListener(this);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataHeader.get(groupPosition).getArticles()
                .size();
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
         ImageView mImageView,ReadImageblur;
        TextView mSubstory, mNumber_of_articles, mviewMore, mDate, mMonth, mYear;
        View leftline,rightline,topline;




        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mcontext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.substory_new, null);
        }

        mImageView = (ImageView) convertView.findViewById(R.id.main_image_story);
        mSubstory = (TextView) convertView.findViewById(R.id.substory_title_story);
        mNumber_of_articles = (TextView) convertView.findViewById(R.id.number_of_articles_story);
        mDate = (TextView) convertView.findViewById(R.id.date_story);
        leftline=(View)convertView.findViewById(R.id.leftline1);
        rightline= (View)convertView.findViewById(R.id.rightline1);
        topline =(View)convertView.findViewById(R.id.cardline1);
        ReadImageblur = (ImageView)convertView.findViewById(R.id.main_image_story_blur);

        if(storyColour!=null&&!storyColour.isEmpty()){

            leftline.setBackgroundColor(Color.parseColor("#"+storyColour));
            rightline.setBackgroundColor(Color.parseColor("#"+storyColour));
            topline.setBackgroundColor(Color.parseColor("#"+storyColour));
            mNumber_of_articles.setTextColor(Color.parseColor("#"+storyColour));

        }

        ReadImageblur.setVisibility(View.GONE);


        final Substories mSubstories = _listDataHeader.get(groupPosition);

        if(mSubstories.getReadStatus().equals("1")){
            ReadImageblur.setVisibility(View.VISIBLE);
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

        Log.i("SubstoryImage", SubstoryImage);
        Log.i("Time", Time);
       // Log.i("StoryTitle", StoryTitle);
        Log.i("substoryTitle", substoryTitle);

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

        if(isExpanded){
            mNumber_of_articles.setText(String.valueOf(NumberofArticles) + " ARTICLES"+" COLLAPSE");
        }else {
            mNumber_of_articles.setText(String.valueOf(NumberofArticles) + " ARTICLES"+" EXPAND");
        }


        mDate.setText(time+", "+Date+" "+month+" "+year);
        //((StoryViewHolder) holder).mMonth.setText(month);
        //((StoryViewHolder) holder).mYear.setText(year);
        mSubstory.setText(substoryTitlehtml);
        Picasso.with(mcontext).load(SubstoryImage).centerCrop().placeholder(R.drawable.placeholder).resize(400, 400).into(mImageView);


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
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = timeFormat.parse(string);
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

        SimpleDateFormat monthFormat2 = new SimpleDateFormat("hh:mm a");

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