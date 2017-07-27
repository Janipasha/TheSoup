package in.thesoupstoriesnews.thesoup;

/**
 * Created by Jani on 06-04-2017.
 */

public class SoupContract {

    //public static final String URL = "http://192.168.1.130/newsapp/api/get_story_feed";
    public static final String FOLLOWURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/follow";
    public static final String UNFOLLOWURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/unfollow";
    public static final String LOGINURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/login_android";
    public static final String STORYURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/get_story";
   public static final String URL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/get_story_feed";
   public static final String FILTERURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/get_categories";
   public static final String READURL = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/mark_as_read";
   public static final String FILTERURL2 = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/get_categories_android";
    public static final String AUTHVERIFY = "http://ec2-35-154-209-88.ap-south-1.compute.amazonaws.com/newsapp/api/auth";
    //analytics events
    public static final String TOTAL_REFRESH = "totalrefresh";
    public static final String PAGE_VIEW = "page_view";
    public static final String HOME_PAGE = "home_page";
    public static final String DISCOVER_VIEWED = "viewed_discover";
    public static final String MYFEED_VIEWED = "viewed_myfeed";
    public static final String CLICK_COLLECTION = "click_collection";
    public static final String CLICK_MYFEED = "click_myfeed";
    public static final String CLICK_DISCOVER = "click_discover";
    public static final String CLICK = "click";
    public static final String ARTICLES_VIEWED = "viewed_articles_page";
    public static final String FOLLOW = "follow";
    public static final String UNFOLLOW = "unfollow";
    public static final String COLLECTION_VIEWED = "viewed_collection";
    public static final String COLLECTION_PAGE = "collection_page";
    public static final String CLICK_SOURCES = "click_sources";

   public static final String FILTERJSON = "filterobject";

   public static final String FILTERPREF = "filterpref";


    //User info

    public static final String SOCIAL_ID = "social_id";
    public static final String SOCIAL_NAME = "social_name";
    public static final String SOCIAL_TOKEN = "social_token";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String FIRST_NAME = "first_name";
    public static final String LAST_NAME = "last_name";
    public static final String IMAGE_URL = "image_url";
    public static final String AGE_MIN = "age_min";
    public static final String AGE_MAX = "age_max";
    public static final String GENDER = "gender";
    public static final String EMAIL_ID ="email_id";
    public static final String DOB ="dob";

    public static final String ARTICLES_PAGE = "articles_page";
    public static final String LOGIN_PAGE = "login_page";
    public static final String LOGINPAGE_VIEWED = "viewed_login_page";
    public static final String ARTICLEWEB_PAGE = "article_web_page";
    public static final String ARTICLE_WEB_PAGE_VIEWED = "viewed_article_web_page";
    public static final String CLICK_FOLLOW = "click_follow";
    public static final String CLICK_UNFOLLOW = "click_unfollow";
    public static final String CONVERSION = "conversion";
    public static final String HOME_PAGE_DISCOVER = "home_page_discover";
    public static final String HOME_PAGE_MYFEED = "home_page_myfeed";
    public static final String CLICK_SUBCOLLECTION = "click_subcollection";
    public static final String FIREBASEID = "firebase_id";
    public static final int TIMEOUT_RETRY_TIME = 15000;


}
