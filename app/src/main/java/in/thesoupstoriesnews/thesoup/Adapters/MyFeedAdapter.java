package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
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

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

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
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsClick;
import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsFollowUnFollow;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

public class MyFeedAdapter extends RecyclerView.Adapter<MyFeedAdapter.DataViewHolder> {

        private List<StoryData> StoryDataList;
        private Context context;
        private int clickposition, fragmenttag;
        private String clickStoryId, clickStoryName;
        //private AnalyticsApplication application;
        //private Tracker mTracker;
        private SharedPreferences pref;
        private FirebaseAnalytics mFirebaseAnalytics;


        public MyFeedAdapter(List<StoryData> Datalist, Context context, int fragmenttag) {
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
            public TextView storyTitle, substoryTitle, date, categoryname, readstatustext, numberOfArticles;
            public ImageView imageView, Readdot,filtericon;
            public CardView cardView;
            public View leftline, rightLine, cardline, bottomline;
            public Button mButton;


            public DataViewHolder(View itemView) {
                super(itemView);


                //ReadImageBlur = (ImageView) itemView.findViewById(R.id.readstatus_story);
                //readstatustext = (TextView) itemView.findViewById(R.id.readstatus_text_story);
                storyTitle = (TextView) itemView.findViewById(R.id.Story_title);
                substoryTitle = (TextView) itemView.findViewById(R.id.substory_title);
                date = (TextView) itemView.findViewById(R.id.date);
            /*month = (TextView) itemView.findViewById(R.id.month);
            year = (TextView) itemView.findViewById(R.id.year);*/
               // numberOfArticles = (TextView) itemView.findViewById(R.id.number_of_articles);
               // mButton = (Button) itemView.findViewById(R.id.follow_button_image);
                //categoryname = (TextView) itemView.findViewById(R.id.categoryname);
                //leftline = (View) itemView.findViewById(R.id.leftline);
                //rightLine = (View) itemView.findViewById(R.id.rightline);
                //cardline = (View) itemView.findViewById(R.id.cardline);
                //bottomline = (View) itemView.findViewById(R.id.bottomline);
                filtericon = (ImageView)itemView.findViewById(R.id.filtericon);

               // imageView = (ImageView) itemView.findViewById(R.id.filtericon);
                Readdot = (ImageView)itemView.findViewById(R.id.red_dot);
                Readdot.setVisibility(View.VISIBLE);
                cardView = (CardView)itemView.findViewById(R.id.myfeedcard);

                cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));

                //readstatustext.setVisibility(View.GONE);
                //ReadImageBlur.setVisibility(View.GONE);


               // imageView.setOnClickListener(this);

                storyTitle.setOnClickListener(this);


               // mButton.setOnClickListener(this);
               // numberOfArticles.setOnClickListener(this);
                // ReadImageBlur.setOnClickListener(this);
                substoryTitle.setOnClickListener(this);
                cardView.setOnClickListener(this);


            }


            @Override
            public void onClick(View view) {


                int mposition = getAdapterPosition();
                String Storyname = StoryDataList.get(mposition).getStoryName();
                //String mfollowstatus = StoryDataList.get(mposition).getFollowStatus();
                String mString = StoryDataList.get(mposition).getStoryId();
                String storytitle = StoryDataList.get(mposition).getStoryName();
                String hex_colour = StoryDataList.get(mposition).getCategoryColour();
                String category = StoryDataList.get(mposition).getCategoryName();

                //application = AnalyticsApplication.getInstance();
                //mTracker = application.getDefaultTracker();
                pref = PreferenceManager.getDefaultSharedPreferences(context);

                if (view == storyTitle|| view == substoryTitle||view ==cardView) {


                    cardView.setCardBackgroundColor(Color.parseColor("#dddddd"));
                    Readdot.setVisibility(View.INVISIBLE);

                    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                    HashMap<String,String> params = new HashMap<>();
                    params.put(SoupContract.AUTH_TOKEN,pref.getString(SoupContract.AUTH_TOKEN,null));
                    params.put("id",StoryDataList.get(mposition).getNotifId());
                    params.put("type","notify");
                    NetworkUtilsClick networkUtilsClick = new NetworkUtilsClick(context,params);
                    try {
                        networkUtilsClick.sendClick();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("story_id", mString);
                    intent.putExtra("storytitle", storytitle);
                    intent.putExtra("followstatus", "1");
                    intent.putExtra("fragmenttag", fragmenttag);
                    intent.putExtra("category", category);
                    intent.putExtra("hex_colour", hex_colour);

                    // CVIPUL Analytics
                    // TODO : Verify card_click event, add collection location if possible
                    Bundle mparams = new Bundle();
                    // mparams.putString("screen_name", String.valueOf(fragmenttag)); // "myfeed / discover"
                    mparams.putString("collection_id", mString);
                    mparams.putString("collection_name", Storyname);
                    mparams.putString("position_card_collection", String.valueOf(mposition));
                    mparams.putString("follow_status", "1");
                    mparams.putString("category", "tap");
                    if (fragmenttag == 0) {
                        mparams.putString("label", "discover_screen");
                    } else if (fragmenttag == 1) {
                        mparams.putString("label", "notification_screen");
                    }
                    mFirebaseAnalytics.logEvent("tap_card_cover", mparams);
                    //


                    context.startActivity(intent);

                }

            }

        }




        @Override
        public MyFeedAdapter.DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myfeed, parent, false);

            return new DataViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(MyFeedAdapter.DataViewHolder holder, int position) {

            final StoryData mStoryData = StoryDataList.get(position);
            String followstatus = mStoryData.getFollowStatus();


            String storytitle = mStoryData.getStoryName();

            String Readstatus = mStoryData.getClicked();

            //holder.ReadImageBlur.setVisibility(View.GONE);
            //holder.readstatustext.setVisibility(View.GONE);

            holder.Readdot.setVisibility(View.VISIBLE);
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffffff"));



            if (Readstatus != null && !Readstatus.isEmpty()) {
                if (Readstatus.equals("1")) {
                    holder.Readdot.setVisibility(View.INVISIBLE);
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#dddddd"));
                   // holder.ReadImageBlur.setVisibility(View.VISIBLE);
                    //holder.readstatustext.setVisibility(View.VISIBLE);

                }

            }

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

                holder.date.setText(Date + " " + month + " " + year);

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
           /* if (ImageUrl != null && !ImageUrl.isEmpty()) {

                Picasso.with(context).load(ImageUrl).placeholder(R.drawable.placeholder).into(holder.imageView);

            } else {
                holder.imageView.setImageResource(R.drawable.background_splash);
            }
*/

            //holder.month.setText(month);
            //holder.year.setText(year);
            //holder.numberOfArticles.setText(Num_of_articles + "  SOURCES IN THIS ISSUE - VIEW ALL");

            if (mStoryData.getCategoryName() != null && !mStoryData.getCategoryName().isEmpty()) {

                String category = mStoryData.getCategoryName();

                int filter = getDrawable(category);

                if(filter!=0){
                    holder.filtericon.setImageResource(filter);
                    holder.filtericon.setColorFilter(Color.parseColor("#"+mStoryData.getCategoryColour()));
                }

                // holder.categoryname.setVisibility(View.VISIBLE);
                //holder.categoryname.setText(mStoryData.getCategoryName());
            }else{
                //holder.categoryname.setVisibility(View.INVISIBLE);
            }



            if (mStoryData.getCategoryColour() != null && !mStoryData.getCategoryColour().isEmpty()) {

                holder.storyTitle.setTextColor(Color.parseColor("#"+mStoryData.getCategoryColour()));


               // holder.categoryname.setVisibility(View.VISIBLE);
                //holder.categoryname.setTextColor(Color.parseColor("#" + mStoryData.getCategoryColour()));
               // holder.numberOfArticles.setTextColor(Color.parseColor("#" + mStoryData.getCategoryColour()));
            } else {
                //holder.categoryname.setVisibility(View.INVISIBLE);
                //holder.numberOfArticles.setTextColor(Color.parseColor("#000000"));
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

