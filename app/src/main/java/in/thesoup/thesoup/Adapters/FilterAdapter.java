package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.thesoup.thesoup.Activities.FilterActivity;
import in.thesoup.thesoup.GSONclasses.filters.Filterdata;
import in.thesoup.thesoup.PreferencesFbAuth.PrefUtilFilter;
import in.thesoup.thesoup.R;

import static android.R.attr.data;
import static android.R.attr.packageNames;
import static android.media.CamcorderProfile.get;
import static android.os.Build.ID;

/**
 * Created by Jani on 07-06-2017.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {
    private List<List<Filterdata>> datalist;
    private Context mcontext;

    public FilterAdapter(List<List<Filterdata>> datalist,Context context){
        this.datalist = datalist;
        this.mcontext= context;
    }

    public void refreshAdapter(List<List<Filterdata>> datalist){
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mheading;
        public Button button1, button2, button3;




        public ViewHolder(View itemView) {
            super(itemView);

            mheading = (TextView) itemView.findViewById(R.id.heading);
            button1 = (Button) itemView.findViewById(R.id.item1);
            button2 = (Button) itemView.findViewById(R.id.item2);
            button3 = (Button) itemView.findViewById(R.id.item3);


            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);



        }

        @Override
        public void onClick(View view) {

            int mposition = getAdapterPosition();
            FilterActivity activity = (FilterActivity) mcontext;


            if (view == button1) {

                clickbutton(0, mposition);

            }

            if (view == button2) {
                clickbutton(1, mposition);
            }


            if (view == button3) {
                clickbutton(2, mposition);
            }


            // activity.updateFilter(mIDStorage);


            //I have to store this click in pref value have to check this prefvalue everytime the activity opens ,
            // have to store all filters locally

        }

        public void clickbutton(int i, int mposition) {
            String ID = datalist.get(mposition).get(i).getId();
            FilterActivity activity = (FilterActivity) mcontext;

            if (datalist.get(mposition).get(i).getStatus().equals("1")) {
                datalist.get(mposition).get(i).changeStatus("0");



                refreshAdapter(datalist);

                activity.changeIDmapValue(ID,"0");


            } else {
                datalist.get(mposition).get(i).changeStatus("1");

                refreshAdapter(datalist);

                activity.changeIDmapValue(ID,"1");



            }
        }
    }

    @Override
    public FilterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FilterAdapter.ViewHolder holder, int position) {

        final List<Filterdata> mfilterdatalist = datalist.get(position);
        holder.button3.setVisibility(View.GONE);

       if (position==0){
           holder.mheading.setText("GOVERNANCE");
       }else if (position==1){
           holder.mheading.setText("WORLD ORDER & SECURITY");
       } else if (position==2){
           holder.mheading.setText("SOCIETY");
       }else if (position==3){
           holder.mheading.setText("LIESURE");
       }else if (position==4){
           holder.mheading.setText("FUTURE");
       }else if (position==5){
           holder.mheading.setText("PROFESSIONAL");
       }

        if(mfilterdatalist.size()>2){

            holder.button3.setText(mfilterdatalist.get(2).getName());
            holder.button3.setVisibility(View.VISIBLE);

            if(mfilterdatalist.get(2).getStatus().equals("1")){
                holder.button3.setBackgroundColor(Color.parseColor("#"+mfilterdatalist.get(2).getHexColour()));
            }else if(mfilterdatalist.get(2).getStatus().equals("0")) {

                holder.button3.setBackgroundResource(R.drawable.buttonborder_filter);

            }
        }


        holder.button1.setText((mfilterdatalist.get(0).getName()));

        if(mfilterdatalist.get(0).getStatus().equals("1")){
            holder.button1.setBackgroundColor(Color.parseColor("#"+mfilterdatalist.get(0).getHexColour()));
        }else if(mfilterdatalist.get(0).getStatus().equals("0")){

            holder.button1.setBackgroundResource(R.drawable.buttonborder_filter);

        }



        holder.button2.setText(mfilterdatalist.get(1).getName());

        if(mfilterdatalist.get(1).getStatus().equals("1")){
            holder.button2.setBackgroundColor(Color.parseColor("#"+mfilterdatalist.get(1).getHexColour()));
        }else if(mfilterdatalist.get(1).getStatus().equals("0")){

            holder.button2.setBackgroundResource(R.drawable.buttonborder_filter);

        }







    }



    @Override
    public int getItemCount() {
        return datalist.size();
    }
}
