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

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import in.thesoup.thesoup.Activities.DetailsActivity;
//import in.thesoup.thesoup.Activities.MainActivity;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoup.thesoup.Activities.DetailsActivity;
import in.thesoup.thesoup.GSONclasses.FeedGSON.StoryData;
import in.thesoup.thesoup.Activities.MainActivity;
//import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoup.thesoup.R;

import static in.thesoup.thesoup.R.id.month;

/**
 * Created by Jani on 07-04-2017.
 */

public class StoryFeedAdapter extends RecyclerView.Adapter<StoryFeedAdapter.DataViewHolder> {

    private List<StoryData> StoryDataList;
    private Context context;
    private int clickposition;
    private String clickStoryId,clickStoryName;
    //private AnalyticsApplication application;
    //private Tracker mTracker;
    private SharedPreferences pref;

    public StoryFeedAdapter(List<StoryData> Datalist, Context context) {
        this.StoryDataList = Datalist;
        this.context = context;
    }

    public void refreshData(List<StoryData> Datalist) {
        //this.StoryDataList = Datalist;
        StoryDataList.addAll(Datalist);
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryData> Datalist){
        this.StoryDataList = Datalist;

        notifyDataSetChanged();

    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storyTitle, substoryTitle, date, month, year, numberOfArticles;
        public ImageView imageView;
        public Button mButton;


        public DataViewHolder(View itemView) {
            super(itemView);

            storyTitle = (TextView) itemView.findViewById(R.id.Story_title);
            substoryTitle = (TextView) itemView.findViewById(R.id.substory_title);
            date = (TextView) itemView.findViewById(R.id.date);
            /*month = (TextView) itemView.findViewById(R.id.month);
            year = (TextView) itemView.findViewById(R.id.year);*/
            numberOfArticles = (TextView) itemView.findViewById(R.id.number_of_articles);
            mButton = (Button) itemView.findViewById(R.id.followbutton);

            imageView = (ImageView) itemView.findViewById(R.id.main_image);


            imageView.setOnClickListener(this);

            storyTitle.setOnClickListener(this);

            mButton.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {



            int mposition = getAdapterPosition();
            String Storyname = StoryDataList.get(mposition).getStoryName();
            String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
            String mString = StoryDataList.get(mposition).getStoryId();

            //application = AnalyticsApplication.getInstance();
            //mTracker = application.getDefaultTracker();
            pref = PreferenceManager.getDefaultSharedPreferences(context);




            if (view == imageView || view == storyTitle) {

               /* if (TextUtils.isEmpty(pref.getString("auth_token", null))) {

                     application.sendEventCollection(mTracker, SoupContract.CLICK, SoupContract.CLICK_COLLECTION, SoupContract.HOME_PAGE,Storyname,mString);

                }else{

                    application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_COLLECTION, SoupContract.HOME_PAGE
                            ,Storyname,mString,pref.getString(SoupContract.FB_ID,null),
                            pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

                }*/

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", mString);
                intent.putExtra("followstatus",mfollowstatus);



                context.startActivity(intent);


            } else if (view == mButton) {
                clickposition = mposition;
                clickStoryId = mString;
                clickStoryName = Storyname;

                followstory();
            }

        }

    }

    public void followstory() {


        Log.d("follow worked", clickStoryId);
        String mfollowstatus = StoryDataList.get(clickposition).getFollowStatus();




        if (mfollowstatus.equals("") || mfollowstatus.equals("0")) {





                //NetworkUtilsFollowUnFollow follow = new NetworkUtilsFollowUnFollow(context,mString);

                HashMap<String, String> params = new HashMap<>();

                params.put("auth_token", pref.getString("auth_token", null));
                params.put("story_id", clickStoryId);

                //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_FOLLOW, SoupContract.HOME_PAGE,clickStoryName,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null), pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

                NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
                followrequest.followRequest(clickposition);

                //followrequest.followRequest(clickposition);



        } else if (mfollowstatus.equals("1")) {

            //application.sendEventCollectionUser(mTracker, SoupContract.CLICK, SoupContract.CLICK_UNFOLLOW, SoupContract.HOME_PAGE,clickStoryName,String.valueOf(clickStoryId),pref.getString(SoupContract.FB_ID,null), pref.getString(SoupContract.FIRSTNAME,null)+pref.getString(SoupContract.LASTNAME,null));

            HashMap<String, String> params = new HashMap<>();

            params.put("auth_token", pref.getString("auth_token", null));
            params.put("story_id", clickStoryId);

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

            unFollowrequest.unFollowRequest(clickposition);




        }
    }


    @Override
    public StoryFeedAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_new, parent, false);

        return new DataViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(StoryFeedAdapter.DataViewHolder holder, int position) {

        final StoryData mStoryData = StoryDataList.get(position);

        String storytitle = mStoryData.getStoryName();

        String substorytitle = mStoryData.getSubStoryName();
        String substorytitlehtml,storytitlehtml ;
        if(Build.VERSION.SDK_INT >= 24){
            substorytitlehtml = String.valueOf(Html.fromHtml(substorytitle , Html.FROM_HTML_MODE_LEGACY));
            storytitlehtml = String.valueOf(Html.fromHtml(storytitle , Html.FROM_HTML_MODE_LEGACY));

        }else{
            substorytitlehtml =String.valueOf (Html.fromHtml( mStoryData.getSubStoryName()));
            storytitlehtml = String.valueOf (Html.fromHtml( mStoryData.getStoryName()));
        }

        String Time = mStoryData.getTime();
        String Num_of_articles = mStoryData.getNumArticle();
        String followstatus = mStoryData.getFollowStatus();

        Log.d("follow",storytitle+followstatus);

        // Log.d("followstatus",followstatus);

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


        String ImageUrl;

        if (mStoryData.getImageUrl().isEmpty()) {
            ImageUrl = mStoryData.getArticleImageUrl();

        } else {
            ImageUrl = mStoryData.getImageUrl();

        }

        holder.storyTitle.setText(storytitlehtml);
        holder.substoryTitle.setText(substorytitlehtml);
        Picasso.with(context).load(ImageUrl).centerCrop().placeholder(R.drawable.placeholder).resize(400,400).into(holder.imageView);
        holder.date.setText(time+", "+Date+" "+month+" "+year);
        //holder.month.setText(month);
        //holder.year.setText(year);
        holder.numberOfArticles.setText(Num_of_articles + " ARTICLES " +" EXPLORE");

        if (followstatus.equals("1")) {
            holder.mButton.setText("Following");
        } else if (followstatus.equals("0")) {
            holder.mButton.setText("Follow");
        }


    }

    @Override
    public int getItemCount() {


        //Log.d("tech memw",StoryDataList.size() +" ");
        return StoryDataList.size();
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
