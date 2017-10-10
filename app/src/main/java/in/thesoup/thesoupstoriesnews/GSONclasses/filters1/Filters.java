package in.thesoup.thesoupstoriesnews.GSONclasses.filters1;

/**
 * Created by Jani on 25-06-2017.
 */

public class Filters {

    String id;
    String hex_colour;
    String name;
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
