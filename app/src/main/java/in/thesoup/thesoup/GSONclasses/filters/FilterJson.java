package in.thesoup.thesoup.GSONclasses.filters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jani on 05-06-2017.
 */

public class FilterJson {

    List<Filterdata> Governance;

    @SerializedName("World Order & Security")
    List<Filterdata> World;

    List<Filterdata>Society;
    List<Filterdata>Leisure;

    List<Filterdata>Future;
    List<Filterdata>Professional;






    public List<Filterdata> getWorld (){
        return  World;
    }


    public List<Filterdata> getGovernance (){
        return  Governance;
    }


    public List<Filterdata> getSociety(){
        return Society;
    }


    public List<Filterdata> getLeisure (){
        return  Leisure;
    }


    public List<Filterdata>  getFuture (){
        return  Future;
    }


    public List<Filterdata> getProfessional (){
        return  Professional;
    }
}
