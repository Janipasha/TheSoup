package in.thesoupstoriesnews.thesoup.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

import in.thesoupstoriesnews.thesoup.Activities.FilterActivity;
import in.thesoupstoriesnews.thesoup.GSONclasses.filters1.Filters;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

/**
 * Created by Jani on 18-08-2017.
 */

public class FilterAdapterVersion extends RecyclerView.Adapter <RecyclerView.ViewHolder>{

     private static final int TYPE_HEADER = 0;
     private static final int TYPE_ITEM = 1;


        private List<Filters> datalist;
        private Context mcontext;
        //CVIPUL Analytics
        private FirebaseAnalytics mFirebaseAnalytics;

        private String resetfilter;
        //End Analytics

        public FilterAdapterVersion(List<Filters> datalist, Context context, String resetfilter){
            this.datalist = datalist;
            this.mcontext= context;
            this.resetfilter = resetfilter;
            //CVIPUL Analytics
            // TODO : Add the activity in place of "this"
            this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            //End Analytics
        }


        public void refreshAdapter(List<Filters> datalist){
            this.datalist = datalist;
            notifyDataSetChanged();
        }

        public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            public TextView heading;
            public TextView filterName;
            public RelativeLayout relativeLayout;
            public ImageView filterImage ,checkbox;

            public itemViewHolder(View itemView) {
                super(itemView);

                heading = (TextView)itemView.findViewById(R.id.heading);
                filterName = (TextView) itemView.findViewById(R.id.item1);
                relativeLayout = (RelativeLayout)itemView.findViewById(R.id.filterlayout);
                filterImage = (ImageView)itemView.findViewById(R.id.filter_image);
                checkbox = (ImageView)itemView.findViewById(R.id.checkbox_filter);

                relativeLayout.setOnClickListener(this);



            }

            @Override
            public void onClick(View view) {
                int mposition = getAdapterPosition();
                FilterActivity activity = (FilterActivity) mcontext;
                String Id = datalist.get(mposition-1).getId();

                if (datalist.get(mposition-1).getStatus().equals("1")) {

                    // CVIPUL Analytics
                    // TODO : Verify Tap to Remove on Filters screen
                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "filters_screen");
                    mparams.putString("category", "tap");
                    mparams.putString("name_filters_category", "" );
                    mparams.putString("count_filters_selected", "" );

                    mFirebaseAnalytics.logEvent("tap_filters_category_remove",mparams);
                    // End Analytics
                    datalist.get(mposition-1).changeStatus("0");

                    refreshAdapter(datalist);
                    activity.changeIDmapValue(Id,"0");

                } else {

                    // CVIPUL Analytics
                    // TODO : Verify Tap to Add on Filters screen
                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "filters_screen");
                    mparams.putString("category", "tap");
                    mparams.putString("name_filters_category", "" );
                    mparams.putString("count_filters_selected", "" );

                    mFirebaseAnalytics.logEvent("tap_filters_category_add",mparams);
                    // End Analytics

                    datalist.get(mposition-1).changeStatus("1");

                    refreshAdapter(datalist);

                    activity.changeIDmapValue(Id,"1");



                }
            }
        }



    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName,discoverfilter;
        public LinearLayout filterheadernormal;

        public HeaderViewHolder(View itemView) {
            super(itemView);

           userName= (TextView)itemView.findViewById(R.id.filter_heading);
            discoverfilter= (TextView)itemView.findViewById(R.id.discoverfilter);
            filterheadernormal = (LinearLayout)itemView.findViewById(R.id.filterheadernormal);

        }}


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_ITEM) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_version, parent, false);
                return new itemViewHolder(view);
            } else if (viewType == TYPE_HEADER) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_headerversion, parent, false);
                return new HeaderViewHolder(view);

            }
            throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
        }



    @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof itemViewHolder){


            ((itemViewHolder)holder).relativeLayout.setVisibility(View.GONE);
            ((itemViewHolder)holder).heading.setVisibility(View.GONE);

            String id = datalist.get(position-1).getId();
            String status = datalist.get(position-1).getStatus();
            String color = datalist.get(position-1).getHexColour();
            String name = datalist.get(position-1).getName();

            int filter = getDrawable(name);

            ((itemViewHolder)holder).filterImage.setImageResource(filter);
            ((itemViewHolder)holder).filterImage.setColorFilter(Color.parseColor("#"+color));

            if(id!=null&&!id.isEmpty()){
                ((itemViewHolder)holder).relativeLayout.setVisibility(View.VISIBLE);
                ((itemViewHolder)holder).filterName.setText(name);
                ((itemViewHolder)holder).filterName.setTextColor(Color.parseColor("#"+color));




                if(status!=null&&!status.isEmpty()){
                    if(status.equals("1")){
                        ((itemViewHolder) holder).checkbox.setImageResource(R.drawable.icons8_checked_checkbox);
                    }else if(status.equals("0")){
                        ((itemViewHolder) holder).checkbox.setImageResource(R.drawable.icons8_unchecked_checkbox);
                    }
                }
            }else {
                ((itemViewHolder)holder).heading.setVisibility(View.VISIBLE);
                ((itemViewHolder)holder).heading.setText(name);
            }


        } else if(holder instanceof HeaderViewHolder){

            ((HeaderViewHolder)holder).filterheadernormal.setVisibility(View.VISIBLE);
            ((HeaderViewHolder)holder).discoverfilter.setVisibility(View.GONE);

            if(resetfilter.equals("0")||resetfilter.isEmpty()){
                ((HeaderViewHolder)holder).filterheadernormal.setVisibility(View.VISIBLE);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
                String username = pref.getString(SoupContract.FIRST_NAME,null);

                ((HeaderViewHolder)holder).userName.setText("Hello "+username);
            }else if(resetfilter.equals("1")){
                ((HeaderViewHolder)holder).filterheadernormal.setVisibility(View.GONE);
                ((HeaderViewHolder)holder).discoverfilter.setVisibility(View.VISIBLE);



            }


        }

        }

    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;

        }

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

        @Override
        public int getItemCount() {
            return datalist.size()+1;
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


