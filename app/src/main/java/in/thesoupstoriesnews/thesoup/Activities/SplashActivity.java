package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsSplash;

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



        //CVIPUL Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "launcher_screen");
        mparams.putString("category", "screen_view");
        mFirebaseAnalytics.logEvent("viewed_screen_launcher",mparams);
        //Analytics End


        NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(this);

        fetchfilterdata.getFilters();

        ToMainActivity();



    }


    public void ToMainActivity() {
        Intent intent = new Intent(this, PagerActivity.class);
        startActivity(intent);
        finish();
    }

}

