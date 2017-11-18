package in.thesoup.thesoupstoriesnews.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.clevertap.android.sdk.CleverTapAPI;
import com.clevertap.android.sdk.exceptions.CleverTapMetaDataNotFoundException;
import com.clevertap.android.sdk.exceptions.CleverTapPermissionsNotSatisfied;
import com.uxcam.UXCam;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.HashMap;

import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsSplash;
import in.thesoup.thesoupstoriesnews.networkcalls.NetworkUtilsVersionCheck;
import in.thesoup.thesoupstoriesnews.preferencesfbauth.PrefUtil;
import in.thesoup.thesoupstoriesnews.R;

/**
 * Created by Jani on 28-04-2017.
 */

public class SplashActivity extends AppCompatActivity {

	private FirebaseAnalytics mFirebaseAnalytics;
    private CleverTapAPI cleverTap;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        try {
            cleverTap = CleverTapAPI.getInstance(getApplicationContext());
        } catch (CleverTapMetaDataNotFoundException e) {
            e.printStackTrace();
        } catch (CleverTapPermissionsNotSatisfied cleverTapPermissionsNotSatisfied) {
            cleverTapPermissionsNotSatisfied.printStackTrace();
        }


        UXCam.startWithKey(getString(R.string.uxcam_id));
        PrefUtil prefutil = new PrefUtil(this);
        if(prefutil.getEmail()!=null&&!prefutil.getEmail().isEmpty()){
            UXCam.tagUsersName(prefutil.getEmail());
        }
        Bundle mparams = new Bundle();
        mparams.putString("screen_name", "launcher_screen");
        mparams.putString("category", "screen_view");
        mFirebaseAnalytics.logEvent("viewed_screen_launcher",mparams);

        HashMap<String,Object> nparams = new HashMap<>();
        nparams.put("screen_name", "launcher_screen");
        nparams.put("category", "screen_view");
        cleverTap.event.push("viewed_screen_launcher",nparams);

        int versionCode=9;
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
            Log.d("version Code",String.valueOf(versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        HashMap<String,String> oparams = new HashMap<>();
        oparams.put("version",String.valueOf(versionCode));
        NetworkUtilsVersionCheck VersionApi = new NetworkUtilsVersionCheck(this,oparams);
        VersionApi.CheckVersion();

    }

    public void recommendUpdate(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("There is newer version of TheSoup available, click OK to update now");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.thesoup.thesoupstoriesnews"));
                                startActivity(intent);
                            }
                        });

        alertDialogBuilder.setNegativeButton("Remind Later",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: check whether this works, should give error
                if(SplashActivity.this!=null){
                    NetworkUtilsSplash fetchfilterdata = new NetworkUtilsSplash(SplashActivity.this);
                    fetchfilterdata.getFilters();
                }

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void forceUpdate(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("A major update of TheSoup is released, please update app to new version  ");
        alertDialogBuilder.setPositiveButton("Update",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=in.thesoup.thesoupstoriesnews"));
                        startActivity(intent);
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }



    public void ToMainActivity() {
        Intent intent = new Intent(this,PagerActivity.class);
        startActivity(intent);
        finish();
    }
}

