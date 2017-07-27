package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import in.thesoup.thesoup.Activities.DetailsActivity;
//import in.thesoup.thesoup.Activities.MainActivity;
//import in.thesoup.thesoup.Application.AnalyticsApplication;
import in.thesoupstoriesnews.thesoup.Activities.DetailsActivity;
import in.thesoupstoriesnews.thesoup.GSONclasses.FeedGSON.StoryData;
//import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

import static android.R.attr.format;


public class StoryFeedAdapter extends RecyclerView.Adapter<StoryFeedAdapter.DataViewHolder> {

    private List<StoryData> StoryDataList;
    private Context context;
    private int clickposition, fragmenttag;
    private String clickStoryId, clickStoryName;
    //private AnalyticsApplication application;
    //private Tracker mTracker;
    private SharedPreferences pref;
    private FirebaseAnalytics mFirebaseAnalytics;


    public StoryFeedAdapter(List<StoryData> Datalist, Context context, int fragmenttag) {
        this.StoryDataList = Datalist;
        this.context = context;
        this.fragmenttag = fragmenttag;
        //CVIPUL Analytics
        this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        //End Analytics
    }

    public void refreshData(List<StoryData> Datalist) {
        //this.StoryDataList = Datalist;
        StoryDataList.addAll(Datalist);
        notifyDataSetChanged();
    }

    public void totalRefreshData(List<StoryData> Datalist) {
        this.StoryDataList = Datalist;
        notifyDataSetChanged();
    }

    public void refreshfollowstatus(List<StoryData> Datalist) {
        this.StoryDataList = Datalist;

        notifyDataSetChanged();

    }


    public class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storyTitle, substoryTitle, date, seeall, viewmore, numberOfArticles,countSubstories,readstatustext;
        public ImageView imageView, ReadImageBlur,circle,pluscircle,filtericon;
        public View leftline, leftLine1, leftline2, bottomline;
        public Button mButton;
        public ImageButton tickfollow;
        public RelativeLayout toplayout,followlayout;
        public LinearLayout linearLayout;


        public DataViewHolder(View itemView) {
            super(itemView);


            ReadImageBlur = (ImageView) itemView.findViewById(R.id.readstatus_story);
            readstatustext = (TextView) itemView.findViewById(R.id.readstatus_text_story);
            storyTitle = (TextView) itemView.findViewById(R.id.Story_title);
            substoryTitle = (TextView) itemView.findViewById(R.id.substory_title);
            date = (TextView) itemView.findViewById(R.id.date);
            /*month = (TextView) itemView.findViewById(R.id.month);
            year = (TextView) itemView.findViewById(R.id.year);*/
            numberOfArticles = (TextView) itemView.findViewById(R.id.number_of_articles);
            mButton = (Button) itemView.findViewById(R.id.follow_button_image);
           // categoryname = (TextView) itemView.findViewById(R.id.categoryname);
            leftline = (View) itemView.findViewById(R.id.leftline);
            leftLine1 = (View) itemView.findViewById(R.id.leftline1);
            leftline2 = (View) itemView.findViewById(R.id.leftline2);
            //cardline = (View) itemView.findViewById(R.id.cardline);
            bottomline = (View) itemView.findViewById(R.id.bottomline);
            circle = (ImageView)itemView.findViewById(R.id.circle);
            pluscircle = (ImageView)itemView.findViewById(R.id.pluscircle);
            seeall = (TextView)itemView.findViewById(R.id.seeall);
            viewmore = (TextView)itemView.findViewById(R.id.view_more);
            imageView = (ImageView) itemView.findViewById(R.id.main_image);
            tickfollow = (ImageButton) itemView.findViewById(R.id.tickmark_follow);
            tickfollow.setVisibility(View.GONE);
            toplayout = (RelativeLayout)itemView.findViewById(R.id.toplayout);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearlayoutStory);
            filtericon = (ImageView)itemView.findViewById(R.id.filtericon);
            countSubstories = (TextView)itemView.findViewById(R.id.count_substories);
            followlayout = (RelativeLayout)itemView.findViewById(R.id.button_follow);

            readstatustext.setVisibility(View.GONE);
           ReadImageBlur.setVisibility(View.GONE);


            imageView.setOnClickListener(this);
            tickfollow.setOnClickListener(this);

            storyTitle.setOnClickListener(this);


            mButton.setOnClickListener(this);
            numberOfArticles.setOnClickListener(this);
            ReadImageBlur.setOnClickListener(this);
            substoryTitle.setOnClickListener(this);
            linearLayout.setOnClickListener(this);
            followlayout.setOnClickListener(this);


        }


        @Override
        public void onClick(View view) {


            int mposition = getAdapterPosition();
            String Storyname = StoryDataList.get(mposition).getStoryName();
            String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
            String mString = StoryDataList.get(mposition).getStoryId();
            String storytitle = StoryDataList.get(mposition).getStoryName();
            String hex_colour = StoryDataList.get(mposition).getCategoryColour();
           String category = StoryDataList.get(mposition).getCategoryName();


            //application = AnalyticsApplication.getInstance();
            //mTracker = application.getDefaultTracker();
            pref = PreferenceManager.getDefaultSharedPreferences(context);

            if (view == imageView || view == storyTitle || view == numberOfArticles ||view == linearLayout||view == substoryTitle||view ==ReadImageBlur) {

                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("story_id", mString);
                intent.putExtra("storytitle", storytitle);
                intent.putExtra("followstatus", mfollowstatus);
                intent.putExtra("fragmenttag", fragmenttag);
               intent.putExtra("category", category);
                intent.putExtra("hex_colour", hex_colour);

                // CVIPUL Analytics
                // TODO : Verify card_click event, add collection location if possible
                Bundle mparams = new Bundle();
                mparams.putString("screen_name", String.valueOf(fragmenttag)); // "myfeed / discover"
                mparams.putString("collection_id", mString);
                mparams.putString("collection_name", Storyname);
                mparams.putString("position_card_collection", String.valueOf(mposition));
                mparams.putString("follow_status", mfollowstatus);
                mparams.putString("category", "tap");
                mFirebaseAnalytics.logEvent("tap_card_cover", mparams);
                //


                context.startActivity(intent);

            } else if (view == mButton||view==tickfollow||view ==followlayout) {
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

        // CVIPUL Analytics
        // TODO : Verify follow event, add collection location if possible
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", String.valueOf(fragmenttag)); // "myfeed / discover"
        mparams.putString("collection_id", clickStoryId);
        mparams.putString("collection_name", clickStoryName);
        mparams.putString("position_card_collection", String.valueOf(clickposition));
        mparams.putString("category", "tap");
        //

        if (mfollowstatus.equals("") || mfollowstatus.equals("0") || TextUtils.isEmpty(mfollowstatus)) {

            //Analytics
            mFirebaseAnalytics.logEvent("tap_add", mparams);
            HashMap<String, String> params = new HashMap<>();

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);
            NetworkUtilsFollowUnFollow followrequest = new NetworkUtilsFollowUnFollow(context, params);
            followrequest.followRequest(clickposition, fragmenttag);


        } else if (mfollowstatus.equals("1")) {

            //Analytics
            mFirebaseAnalytics.logEvent("tap_remove", mparams);

            HashMap<String, String> params = new HashMap<>();

            params.put(SoupContract.AUTH_TOKEN, pref.getString(SoupContract.AUTH_TOKEN, null));
            params.put("story_id", clickStoryId);

            Log.d("Check mStorydata size", String.valueOf(StoryDataList.size()));

            NetworkUtilsFollowUnFollow unFollowrequest = new NetworkUtilsFollowUnFollow(context, params);

            unFollowrequest.unFollowRequest(clickposition, fragmenttag);


        }
    }


    @Override
    public StoryFeedAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover, parent, false);

        return new DataViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(StoryFeedAdapter.DataViewHolder holder, int position) {

        final StoryData mStoryData = StoryDataList.get(position);
        String followstatus = mStoryData.getFollowStatus();


        String storytitle = mStoryData.getStoryName();

        String countSubstories = mStoryData.getCountSubstories();

        String Readstatus = mStoryData.getReadStatus();

       holder.ReadImageBlur.setVisibility(View.GONE);
        holder.readstatustext.setVisibility(View.GONE);


        if (Readstatus != null && !Readstatus.isEmpty()) {
            if (Readstatus.equals("1")) {
               holder.ReadImageBlur.setVisibility(View.VISIBLE);
                holder.readstatustext.setVisibility(View.VISIBLE);

            }

        }

        holder.countSubstories.setText(countSubstories + " Older Updates");

        String substorytitle = mStoryData.getSubStoryName();
        String substorytitlehtml, storytitlehtml;
        if (Build.VERSION.SDK_INT >= 24) {
            substorytitlehtml = String.valueOf(Html.fromHtml(substorytitle, Html.FROM_HTML_MODE_LEGACY));
            storytitlehtml = String.valueOf(Html.fromHtml(storytitle, Html.FROM_HTML_MODE_LEGACY));

        } else {
            substorytitlehtml = String.valueOf(Html.fromHtml(mStoryData.getSubStoryName()));
            storytitlehtml = String.valueOf(Html.fromHtml(mStoryData.getStoryName()));
        }

        String Time = mStoryData.getTime();
        String Num_of_articles = mStoryData.getNumArticle();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(new Date());



        Log.d("time-current",mStoryData.getStoryName()+"/n"+currentTime);
        Log.d("time-current1",mStoryData.getStoryName()+"/n"+Time);


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
               holder.date.setText(String.valueOf(m)+" hours ago");
           }else{
               holder.date.setText("1 hour ago");
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

            holder.date.setText(time + ", " + Date + " " + month + " " + year);

        }



        Log.d("follow", storytitle + followstatus);

        // Log.d("followstatus",followstatus);


        String ImageUrl;

        if (mStoryData.getImageUrl().isEmpty()) {
            ImageUrl = mStoryData.getArticleImageUrl();

        } else {
            ImageUrl = mStoryData.getImageUrl();

        }

        holder.storyTitle.setText(storytitlehtml);
        holder.substoryTitle.setText(substorytitlehtml);
        if (ImageUrl != null && !ImageUrl.isEmpty()) {

            Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.imageView);

        } else {
            holder.imageView.setImageResource(R.drawable.background_splash);
        }


        //holder.month.setText(month);
        //holder.year.setText(year);
        holder.numberOfArticles.setText(Num_of_articles + " Articles");

        if (mStoryData.getCategoryName() != null && !mStoryData.getCategoryName().isEmpty()) {
           // holder.categoryname.setVisibility(View.VISIBLE);
            String category = mStoryData.getCategoryName();

            int filter = getDrawable(category);

            if(filter!=0){
                holder.filtericon.setImageResource(filter);
                holder.filtericon.setColorFilter(Color.parseColor("#cbffffff"));
            }

            //holder.categoryname.setText(mStoryData.getCategoryName());
        }else{
            //holder.categoryname.setVisibility(View.INVISIBLE);
        }


        if (followstatus.equals("1")) {
            holder.mButton.setText("FOLLOWING");
            holder.mButton.setTextColor(Color.parseColor("#9b9b9b"));
            holder.mButton.setVisibility(View.GONE);
            holder.tickfollow.setVisibility(View.VISIBLE);

            Typeface face = Typeface.createFromAsset(context.getAssets(),
                    "fonts/proxima-nova-bold.otf");
            holder.mButton.setTypeface(face);
        } else if (followstatus.equals("0")) {
            holder.mButton.setVisibility(View.VISIBLE);
            holder.mButton.setText("Follow");
            holder.mButton.setTextColor(Color.parseColor("#ffffff"));
            holder.tickfollow.setVisibility(View.GONE);

            if (mStoryData.getCategoryColour() != null && !mStoryData.getCategoryColour().isEmpty()) {
                //holder.mButton.setBackgroundColor(Color.parseColor("#" + mStoryData.getCategoryColour()));

            }else {
                //holder.mButton.setBackgroundColor((Color.parseColor("#000000")));
            }


        } else if (TextUtils.isEmpty(followstatus)) {
            holder.mButton.setVisibility(View.VISIBLE);
            holder.mButton.setText("Follow");
            holder.mButton.setTextColor(Color.parseColor("#ffffff"));
            holder.tickfollow.setVisibility(View.GONE);

            if (mStoryData.getCategoryColour() != null && !mStoryData.getCategoryColour().isEmpty()) {
               // holder.mButton.setBackgroundColor(Color.parseColor("#" + mStoryData.getCategoryColour()));

            }else {
               // holder.mButton.setBackgroundColor((Color.parseColor("#000000")));
            }

        }


        if (mStoryData.getCategoryColour() != null && !mStoryData.getCategoryColour().isEmpty()) {
           // holder.categoryname.setVisibility(View.VISIBLE);
            String color = mStoryData.getCategoryColour();
            holder.pluscircle.setColorFilter(Color.parseColor("#"+color));
            holder.circle.setColorFilter(Color.parseColor("#"+color));
            holder.toplayout.setBackgroundColor(Color.parseColor("#"+color));
            holder.leftline2.setBackgroundColor(Color.parseColor("#"+color));
            holder.leftLine1.setBackgroundColor(Color.parseColor("#"+color));
            holder.leftline.setBackgroundColor(Color.parseColor("#"+color));
            holder.bottomline.setBackgroundColor(Color.parseColor("#"+"80"+color));
            //holder.categoryname.setTextColor(Color.parseColor("#" + mStoryData.getCategoryColour()));
            holder.seeall.setTextColor(Color.parseColor("#" + mStoryData.getCategoryColour()));
            holder.viewmore.setTextColor(Color.parseColor("#"+color));
        } else {
            //holder.categoryname.setVisibility(View.INVISIBLE);
            holder.numberOfArticles.setTextColor(Color.parseColor("#000000"));
        }


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
}
