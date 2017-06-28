package in.thesoup.thesoup.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.thesoup.thesoup.NetworkCalls.NetworkUtilsSplash;

/**
 * Created by Jani on 28-04-2017.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(this);

        fetchfilterdata.getFilters();

        //getfilters();

        ToMainActivity();


    }


    public void ToMainActivity() {
        Intent intent = new Intent(this, PagerActivity.class);
        startActivity(intent);
        finish();
    }

}

