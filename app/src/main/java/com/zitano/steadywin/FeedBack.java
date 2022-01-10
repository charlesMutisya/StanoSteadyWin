package com.zitano.steadywin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FeedBack extends AppCompatActivity {
    Button btnSubmit;
    EditText txtFeed, txtEmail;
    String email;
    private static final String TAG = "FeedbackLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = getSharedPreferences("com.zitano.steadywin.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);

        if (isNightModeEnabled())
        {
            setTheme(R.style.darkTheme);
            Log.i(TAG, "Night mode has been enabled");
        }
        else
        {
            setTheme(R.style.AppTheme);
            Log.i(TAG, "Night mode has not been enabled");
        }
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setContentView(R.layout.activity_feed_back);

        btnSubmit = findViewById(R.id.btnfeed);
        txtFeed= findViewById(R.id.txtFeed);
        txtEmail= findViewById(R.id.txtEmail);
        email= txtEmail.getText().toString();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email != null){
                    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "footatips@gmail.com", null));
                    //intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"Steady Win Feedback");
                    intent.putExtra(Intent.EXTRA_TEXT, txtFeed.getText());
                    startActivity(Intent.createChooser(intent, "Send using"));

                } else {
                    Toast.makeText(FeedBack.this, "please provide your email address", Toast.LENGTH_SHORT).show();
                }

                }

        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    private SharedPreferences mSharedPref;
    private boolean isNightModeEnabled() {
        return  mSharedPref.getBoolean("settings", false);
    }

}
