package in.thesoup.thesoupstoriesnews.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;
import java.util.List;

import in.thesoup.thesoupstoriesnews.activities.FilterActivity;
import in.thesoup.thesoupstoriesnews.gsonclasses.filters1.Filters;
import in.thesoup.thesoupstoriesnews.R;
import in.thesoup.thesoupstoriesnews.SoupContract;

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
    private CleverTapAPI cleverTap;

        private String resetfilter;
        //End Analytics

        public FilterAdapterVersion(List<Filters> datalist, Context context, String resetfilter){
            this.datalist = datalist;
            this.mcontext= context;
            this.resetfilter = resetfilter;
            this.mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
            try {
                this.cleverTap = CleverTapAPI.getInstance(context);
            } catch (CleverTapMetaDataNotFoundException e) {
                e.printStackTrace();
            } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
                cleverTapPermissionsNotSatisfied.printStackTrace();
            }
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


                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "filters_screen");
                    mparams.putString("category", "tap");
                    mparams.putString("name_filters_category", "" );
                    mparams.putString("count_filters_selected", "" );
                    mFirebaseAnalytics.logEvent("tap_filters_category_remove",mparams);

                    HashMap<String,Object> nparams = new HashMap<>();
                    nparams.put("screen_name", "filters_screen");
                    nparams.put("category", "tap");
                    nparams.put("name_filters_category", "" );
                    nparams.put("count_filters_selected", "" );
                    cleverTap.event.push("tap_filters_category_remove",nparams);

                    datalist.get(mposition-1).changeStatus("0");

                    refreshAdapter(datalist);
                    activity.changeIDmapValue(Id,"0");

                } else {

                    Bundle mparams = new Bundle();
                    mparams.putString("screen_name", "filters_screen");
                    mparams.putString("category", "tap");
                    mparams.putString("name_filters_category", "" );
                    mparams.putString("count_filters_selected", "" );

                    mFirebaseAnalytics.logEvent("tap_filters_category_add",mparams);


                    HashMap<String,Object> nparams = new HashMap<>();
                    nparams.put("screen_name", "filters_screen");
                    nparams.put("category", "tap");
                    nparams.put("name_filters_category", "" );
                    nparams.put("count_filters_selected", "" );

                    cleverTap.event.push("tap_filters_category_add",nparams);





                    datalist.get(mposition-1).changeStatus("1");

                    refreshAdapter(datalist);

                    activity.changeIDmapValue(Id,"1");



                }
            }
        }



    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        public TextView userName,discoverfilter,filtertoptext;
        public LinearLayout filterheadernormal;

        public HeaderViewHolder(View itemView) {
            super(itemView);

           userName= (TextView)itemView.findViewById(R.id.filter_heading);
            discoverfilter= (TextView)itemView.findViewById(R.id.discoverfilter);
            filterheadernormal = (LinearLayout)itemView.findViewById(R.id.filterheadernormal);
            filtertoptext = (TextView)itemView.findViewById(R.id.filtertoptext);

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


            if(id!=null&&!id.isEmpty()){
                ((itemViewHolder)holder).relativeLayout.setVisibility(View.VISIBLE);
                ((itemViewHolder)holder).filterName.setText(name);
                ((itemViewHolder)holder).filterName.setTextColor(Color.parseColor("#"+color));

                int filter = getDrawable(id);

                ((itemViewHolder)holder).filterImage.setImageResource(filter);
                ((itemViewHolder)holder).filterImage.setColorFilter(Color.parseColor("#"+color));



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

            String toptext = mcontext.getString(R.string.categories_count);
            SpannableString ss1 = new SpannableString(toptext);
            ss1.setSpan(new StyleSpan(Typeface.BOLD),7,16,0);

            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#000000")),7,16,0);

            ss1.setSpan(new RelativeSizeSpan(1.1f),7,16,0);


            ((HeaderViewHolder)holder).filterheadernormal.setVisibility(View.VISIBLE);
            ((HeaderViewHolder)holder).discoverfilter.setVisibility(View.GONE);
            ((HeaderViewHolder)holder).filtertoptext.setText(ss1);

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

        if (filter.equals("1")) {
            return R.drawable.politics;


        } else if (filter.equals("2")) {
            return R.drawable.diplomacy;

        } else if (filter.equals("3")) {
            return R.drawable.justice;

        } else if (filter.equals("4")) {
            return R.drawable.religion;
        } else if (filter.equals("5")) {
            return R.drawable.sports;
        } else if (filter.equals("7")) {
            return R.drawable.nature;
        } else if (filter.equals("9")) {
            return R.drawable.health;
        } else if (filter.equals("10")) {
            return R.drawable.science;
        } else if (filter.equals("11")) {
            return R.drawable.human_rights;
        } else if (filter.equals("12")) {
            return R.drawable.entertainment;
        } else if (filter.equals("15")) {
            return R.drawable.accident;
        } else if (filter.equals("16")) {
            return R.drawable.economy_business;
        } else if (filter.equals("17")) {
            return R.drawable.jobs;
        } else if (filter.equals("18")) {
            return R.drawable.education;
        } else if (filter.equals("19")) {
            return R.drawable.people;
        }

        return 0;


    }


    }


