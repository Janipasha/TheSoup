package in.thesoupstoriesnews.thesoup.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;

import java.util.HashMap;

import in.thesoupstoriesnews.thesoup.NetworkCalls.NetworkUtilsLogin;
import in.thesoupstoriesnews.thesoup.R;
import in.thesoupstoriesnews.thesoup.SoupContract;

public class EmailActivity extends AppCompatActivity {

    private Button submitbutton;
    private EditText editText;
    private TextView name;
    private HashMap<String, String> params;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.email_layout);
        Intent intent = getIntent();
        if (getIntent().getSerializableExtra("params") != null) {
            params = (HashMap<String, String>) intent.getSerializableExtra("params");
        }
        editText = (EditText) findViewById(R.id.edittext);
        submitbutton = (Button) findViewById(R.id.submit);
        name = (TextView)findViewById(R.id.name);

        if(params.get(SoupContract.FIRST_NAME)!=null&&!params.get(SoupContract.FIRST_NAME).isEmpty()){
            name.setText(params.get(SoupContract.FIRST_NAME));
        }


    }

    public void Onclick(View v) {
        if (v == submitbutton) {
            String emailId = editText.getText().toString();

            if (emailId != null && !emailId.isEmpty()) {
                params.put(SoupContract.EMAIL_ID, emailId);
                NetworkUtilsLogin loginRequest = new NetworkUtilsLogin(EmailActivity.this, params);
                loginRequest.loginvolleyRequest();
            } else {
                Toast.makeText(this, "Enter a valid Email Id", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void main() {
        Intent intent = new Intent(EmailActivity.this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        LoginManager.getInstance().logOut();

    }
}

