package com.zitano.steadywin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.autolinklibrary.AutoLinkMode;
import com.luseen.autolinklibrary.AutoLinkOnClickListener;
import com.luseen.autolinklibrary.AutoLinkTextView;

public class PostDetails extends AppCompatActivity {

    DatabaseReference mRef;
    String postKey;
    TextView tvTitle, tvBody, tvTime;
    ImageView imgBody;
    ProgressDialog pd;
    String selection;
    AutoLinkTextView autoLinkTextView;
    AdView madview;
    private static final String TAG = "PostDetailsLog";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSharedPref = getSharedPreferences("com.zitano.steadywin.PREFERENCE_FILE_KEY",Context.MODE_PRIVATE);

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
        setContentView(R.layout.activity_post_details);
        postKey = getIntent().getExtras().getString("postkey");
        selection=getIntent().getExtras().getString("selection");
        tvBody =  findViewById(R.id.tvBody);
        tvTitle =  findViewById(R.id.tvTitle);
        tvTime =  findViewById(R.id.post_time);
        pd=new ProgressDialog(this);
        pd.setMessage("Loading...");
        pd.show();

        madview= findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        autoLinkTextView = findViewById(R.id.autoLinkrate);
        autoLinkTextView.addAutoLinkMode(AutoLinkMode.MODE_CUSTOM);
        autoLinkTextView.setCustomRegex("\\sHere\\b");


        autoLinkTextView.setAutoLinkText("Motivate us to continue giving you free winning tips by rating us five stars : Here");

        autoLinkTextView.setAutoLinkOnClickListener(new AutoLinkOnClickListener() {
            @Override
            public void onAutoLinkTextClick(AutoLinkMode autoLinkMode, String matchedText) {
                if (autoLinkMode == AutoLinkMode.MODE_CUSTOM)
                    try {
                        Intent RateIntent =
                                new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName()));
                        startActivity(RateIntent);
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"Unable to connect try again later...",
                                Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

            }
        });



        if (postKey != null){

            mRef = FirebaseDatabase.getInstance().getReference().child("jackpot").child(selection).child(postKey);

        }
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String title = dataSnapshot.child("title").getValue().toString();
                String body = dataSnapshot.child("body").getValue().toString();
                Long time = (Long) dataSnapshot.child("time").getValue();

                if(title!=null){
                    tvTitle.setText(title.toUpperCase());
                    pd.dismiss();
                } else {
                    Toast.makeText(PostDetails.this, "Check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                if (body != null) {
                    tvBody.setText(body);

                }
                if (time != null) {
                    setTime(time);
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onBackPressed() {

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menub, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        if (id == R.id.feedback){
            startActivity(new Intent(this, FeedBack.class));

        }else if (id == R.id.menu_share){
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Hey, I have a linear winning graph thanks to Steady Win Football Prediction App. Download here https://play.google.com/store/apps/details?id=com.zitano.steadywin";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Steady Winning");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(sharingIntent);
        }else if ( id== R.id.rate){
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }else  if (id== R.id.about){
            Uri uri = Uri.parse("https://winstedy.blogspot.com/2019/02/readMe.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }

        }else if (id == R.id.privacyP){
            Uri uri = Uri.parse("https://winstedy.blogspot.com/2018/12/steady-win-privacy-policy.html");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "Unable to find play store", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTime(Long time) {
        TextView txtTime = (TextView) findViewById(R.id.post_time);
        //long elapsedDays=0,elapsedWeeks = 0, elapsedHours=0,elapsedMin=0;
        long elapsedTime;
        long currentTime = System.currentTimeMillis();
        int elapsed = (int) ((currentTime - time) / 1000);
        if (elapsed < 60) {
            if (elapsed < 2) {
                txtTime.setText("Just Now");
            } else {
                txtTime.setText(elapsed + " sec ago");
            }
        } else if (elapsed > 604799) {
            elapsedTime = elapsed / 604800;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " week ago");
            } else {

                txtTime.setText(elapsedTime + " weeks ago");
            }
        } else if (elapsed > 86399) {
            elapsedTime = elapsed / 86400;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " day ago");
            } else {
                txtTime.setText(elapsedTime + " days ago");
            }
        } else if (elapsed > 3599) {
            elapsedTime = elapsed / 3600;
            if (elapsedTime == 1) {
                txtTime.setText(elapsedTime + " hour ago");
            } else {
                txtTime.setText(elapsedTime + " hours ago");
            }
        } else if (elapsed > 59) {
            elapsedTime = elapsed / 60;
            txtTime.setText(elapsedTime + " min ago");


        }

    }
    private SharedPreferences mSharedPref;
    private boolean isNightModeEnabled() {
        return  mSharedPref.getBoolean("settings", false);
    }
}
