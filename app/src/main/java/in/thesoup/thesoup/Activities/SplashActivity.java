package in.thesoup.thesoup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import in.thesoup.thesoup.NetworkCalls.NetworkUtilsFilters;

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


        NetworkUtilsFilters fetchfilterdata = new NetworkUtilsFilters(this);

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

