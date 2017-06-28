package in.thesoup.thesoup.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.thesoup.thesoup.Activities.FilterActivity;
import in.thesoup.thesoup.GSONclasses.filters1.Filters;
import in.thesoup.thesoup.R;

/**
 * Created by Jani on 25-06-2017.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private List<Filters> datalist;
    private Context mcontext;

    public FilterAdapter(List<Filters> datalist, Context context){
        this.datalist = datalist;
        this.mcontext= context;
    }


    public void refreshAdapter(List<Filters> datalist){
        this.datalist = datalist;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView heading;
        public Button button;

        public ViewHolder(View itemView) {
            super(itemView);

            heading = (TextView)itemView.findViewById(R.id.heading);
            button = (Button)itemView.findViewById(R.id.item1);

            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int mposition = getAdapterPosition();
            FilterActivity activity = (FilterActivity) mcontext;
            String Id = datalist.get(mposition).getId();

            if (datalist.get(mposition).getStatus().equals("1")) {

                datalist.get(mposition).changeStatus("0");



                refreshAdapter(datalist);

                activity.changeIDmapValue(Id,"0");


            } else {
                datalist.get(mposition).changeStatus("1");

                refreshAdapter(datalist);

                activity.changeIDmapValue(Id,"1");



            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters1,parent,false);

        return new FilterAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.button.setVisibility(View.GONE);
        holder.heading.setVisibility(View.GONE);

        String id = datalist.get(position).getId();
        String status = datalist.get(position).getStatus();
        String color = datalist.get(position).getHexColour();
        String name = datalist.get(position).getName();

        if(id!=null&&!id.isEmpty()){
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText(name);


            if(status!=null&&!status.isEmpty()){
                if(status.equals("1")){
                    holder.button.setBackgroundColor(Color.parseColor("#"+color));
                    holder.button.setTextColor(Color.parseColor("#ffffff"));
                }else if(status.equals("0")){
                    holder.button.setBackgroundResource(R.drawable.buttonborder_filter);
                    holder.button.setTextColor(Color.parseColor("#000000"));
                }
            }
        }else {
            holder.heading.setVisibility(View.VISIBLE);
            holder.heading.setText(name);
        }

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }


}
