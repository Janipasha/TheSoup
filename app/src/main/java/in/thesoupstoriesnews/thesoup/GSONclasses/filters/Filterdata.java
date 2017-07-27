package in.thesoupstoriesnews.thesoup.GSONclasses.filters;

/**
 * Created by Jani on 05-06-2017.
 */

public class Filterdata {

    String id;
    String name;
    String hex_colour;
    String super_category;
    String status;

    public String getId(){
        return id;
    }

    public String getName(){
        return name;

    }

    public String getHexColour(){
        return hex_colour;
    }

    public String getStatus(){return  status;}

    public void changeStatus(String value){
        this.status = value;
    }
}
