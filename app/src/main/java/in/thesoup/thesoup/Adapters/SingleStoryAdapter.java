package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import in.thesoup.thesoup.Activities.ArticlesActivity;
//import in.thesoup.thesoup.Activities.DetailsActivity;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.Activities.ArticlesActivity;
import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Articles;
import in.thesoup.thesoup.GSONclasses.SinglestoryGSON.Substories;
//import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.R;


public class SingleStoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<Substories> substories;
    private Context mcontext;
    private String storyTitle;
    private String followstatus;
    private String StoryTitle = "";
    private int clickposition,fragmenttag;
    private String clickStoryId;
    //private AnalyticsApplication application;
    private Tracker mTracker;
    private SharedPreferences pref;

    // private List<Substories> mSubstories;


    public SingleStoryAdapter(List<Substories> substories, String storyTitle, String followstatus, Context context, String StoryId,int fragmenttag) {
        this.substories = substories; //Check is this statement is valid
        this.mcontext = context;
        this.storyTitle = storyTitle;
        this.followstatus = followstatus;
        this.clickStoryId = StoryId;
        this.fragmenttag = fragmenttag;
    }

    public void refreshData(List<Substories> Datalist, String mStoryTitle) {
        this.storyTitle = mStoryTitle;
        substories.addAll(Datalist);
        notifyDataSetChanged();


    }

    public void refreshFollowStatus(String followstatus) {
        this.followstatus = followstatus;
        notifyDataSetChanged();

    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    public class StoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageView;
        private TextView mSubstory, mNumber_of_articles, mviewMore, mDate, mMonth, mYear;

        public StoryViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.main_image_story);
            mSubstory = (TextView) itemView.findViewById(R.id.substory_title_story);
            mNumber_of_articles = (TextView) itemView.findViewById(R.id.number_of_articles_story);
           // mviewMore = (TextView) itemView.findViewById(R.id.view_more_story);
            mDate = (TextView) itemView.findViewById(R.id.date_story);
            //mMonth = (TextView) itemView.findViewById(R.id.month_story);
            //mYear = (TextView) itemView.findViewById(R.id.year_story);


            mImageView.setOnClickListener(this);
            mSubstory.setOnClickListener(this);
            mNumber_of_articles.setOnClickListener(this);
            //mviewMore.setOnClickListener(this);
            mDate.setOnClickListener(this);
            //mMonth.setOnClickListener(this);
            //mYear.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            // application = AnalyticsApplication.getInstance();
            // mTracker = application.getDefaultTracker();
            pref = PreferenceManager.getDefaultSharedPreferences(mcontext);

            int nposition = getAdapterPosition();
            String StoryId = substories.get(nposition - 1).getSubstoryId();
            List<Articles> mArticles = substories.get(nposition - 1).getArticles();


          /*  if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

                application.sendEventCollection(mTracker, SoupContract.CLICK, SoupContract.CLICK_SUBCOLLECTION, SoupContract.COLLECTION_PAGE,storyTitle,StoryId);

            }else{

                application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_SUBCOLLECTION, SoupContract.COLLECTION_PAGE
                        ,storyTitle,StoryId,pref.getString(SoupContract.FB_ID,null),
                        pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

            }*/


            Intent intent = new Intent(mcontext, ArticlesActivity.class);

            intent.putExtra("ARTICLELIST", (Serializable) mArticles);
            intent.putExtra("StoryTitle", storyTitle);
            intent.putExtra("story_id", StoryId);
            mcontext.startActivity(intent);

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView Storytitle;
        private Button followbutton;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            Storytitle = (TextView) itemView.findViewById(R.id.story_title_header);
            followbutton = (Button) itemView.findViewById(R.id.followbutton_header);

            followbutton.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {


            clickposition = getAdapterPosition();

            followstory();

        }
    }


    public void followstory() {

        Log.d("follow worked", clickStoryId + "details");
        String mfollowstatus = followstatus;
        //AnalyticsApplication application = AnalyticsApplication.getInstance();
        //Tracker mTracker = application.getDefaultTracker();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);

        if (mfollowstatus.equals("") || mfollowstatus.equals("0")) {


            if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

                // application.sendEventCollection(mTracker, SoupContract.CLICK, SoupContract.CLICK_FOLLOW, SoupContract.COLLECTION_PAGE,storyTitle,String.valueOf(clickStoryId));


                DetailsActivity activity = (DetailsActivity) mcontext;

                activity.demo(clickStoryId);

            } else {

                //NetworkUtilsFollowUnFollow follow = new NetworkUtilsFollowUnFollow(context,mString);

                HashMap<String, String> params = new HashMap<>();

                params.put("auth_token", pref.getString("auth_token", null));
                params.put("story_id", clickStoryId);

                //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_FOLLOW, SoupContract.COLLECTION_PAGE,storyTitle,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null),pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(mcontext, params);


                followrequest.followRequest(clickposition,fragmenttag);


            }
        } else if (mfollowstatus.equals("1")) {

            //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_UNFOLLOW, SoupContract.COLLECTION_PAGE,storyTitle,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null),pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));


            HashMap<String, String> params = new HashMap<>();

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("story_id", clickStoryId);

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(mcontext, params);

            unFollowrequest.unFollowRequest(clickposition,fragmenttag);


        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.substory_new, parent, false);
            return new StoryViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.substory_header, parent, false);
            return new HeaderViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof HeaderViewHolder) {

            String storytitlehtml;

            if (Build.VERSION.SDK_INT >= 24) {
                storytitlehtml = String.valueOf(Html.fromHtml(storyTitle, Html.FROM_HTML_MODE_LEGACY));

            } else {

                storytitlehtml = String.valueOf(Html.fromHtml(storyTitle));
            }

            //StoryTitle = storyTitle;


            ((HeaderViewHolder) holder).Storytitle.setText(storytitlehtml);

            String nfollowstatus = followstatus;


            if (nfollowstatus.equals("1")) {
                ((HeaderViewHolder) holder).followbutton.setText("Following");
            } else if (nfollowstatus.equals("0")) {
                ((HeaderViewHolder) holder).followbutton.setText("Follow");
            }

        } else if (holder instanceof StoryViewHolder) {

            final Substories mSubstories = substories.get(position - 1);

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
            Log.i("StoryTitle", StoryTitle);
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
            ((StoryViewHolder) holder).mNumber_of_articles.setText(String.valueOf(NumberofArticles) + " ARTICLES"+" EXPLORE");
            ((StoryViewHolder) holder).mDate.setText(time+", "+Date+" "+month+" "+year);
            //((StoryViewHolder) holder).mMonth.setText(month);
            //((StoryViewHolder) holder).mYear.setText(year);
            ((StoryViewHolder) holder).mSubstory.setText(substoryTitlehtml);
            Picasso.with(mcontext).load(SubstoryImage).centerCrop().placeholder(R.drawable.placeholder).resize(400, 400).into(((StoryViewHolder) holder).mImageView);

        }

    }

    @Override
    public int getItemCount() {

        return substories.size() + 1;

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
       /* try {
            date = dateformat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Time sent is not valid",string);
        }*/

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
