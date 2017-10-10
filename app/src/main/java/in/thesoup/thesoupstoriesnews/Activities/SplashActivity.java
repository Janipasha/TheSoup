package in.thesoup.thesoupstoriesnews.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.uxcam.UXCam;

import com.google.firebase.analytics.FirebaseAnalytics;

import org.jsoup.Jsoup;

import java.io.IOException;

import in.thesoup.thesoupstoriesnews.NetworkCalls.NetworkUtilsSplash;
import in.thesoup.thesoupstoriesnews.PreferencesFbAuth.PrefUtil;

import static com.uxcam.c.d.p;

/**
 * Created by Jani on 28-04-2017.
 */

public class SplashActivity extends AppCompatActivity {
	// CVIPUL Analytics
	private FirebaseAnalytics mFirebaseAnalytics;
	// End Analytics
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        UXCam.startWithKey("dacc1dd6fa3d4fc");
        PrefUtil prefutil = new PrefUtil(this);
        if(prefutil.getEmail()!=null&&!prefutil.getEmail().isEmpty()){
            UXCam.tagUsersName(prefutil.getEmail());
        }
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "launcher_screen");
        mparams.putString("category", "screen_view");
        mFirebaseAnalytics.logEvent("viewed_screen_launcher",mparams);


        NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(this);
        fetchfilterdata.getFilters();

        //ToMainActivity();

       /* VersionChecker versionChecker = new VersionChecker();
        String versionname = "";
        try {
            versionname = versionChecker.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

      //  Log.d("version name",versionname);


    }


    public void ToMainActivity() {
        Intent intent = new Intent(this,PagerActivity.class);
        startActivity(intent);
        finish();
    }

    public class VersionChecker extends AsyncTask<String, String, String> {

        String newVersion;

        @Override
        protected String doInBackground(String... params) {

            try {
                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "in.thesoup.thesoupstoriesnews" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return newVersion;
        }

}
}
