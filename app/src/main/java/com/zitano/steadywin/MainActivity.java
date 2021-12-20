package com.zitano.steadywin;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shrikanthravi.library.NightModeButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab_main, fab1_mail, fab2_share,fab3_rate;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    Boolean isOpen = false;
    NightModeButton nightModeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseMessaging.getInstance().subscribeToTopic("jackpot");

        ViewPager vp_pages= findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter=new FragmentAdaptrer(getSupportFragmentManager());
        vp_pages.setAdapter(pagerAdapter);

        TabLayout tbl_pages=  findViewById(R.id.tabs);
        tbl_pages.setupWithViewPager(vp_pages);
        Toolbar toolbar= findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab3_rate= findViewById(R.id.fab3);
        nightModeButton=findViewById(R.id.nightModeButton);

        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_closed);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_clock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_clock);
        fab_anticlock = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_rotate_anticlock);



        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOpen) {
                    fab3_rate.startAnimation(fab_close);
                    fab2_share.startAnimation(fab_close);
                    fab1_mail.startAnimation(fab_close);
                    fab_main.startAnimation(fab_anticlock);
                    fab3_rate.setClickable(false);
                    fab2_share.setClickable(false);
                    fab1_mail.setClickable(false);

                    isOpen = false;
                } else {
                    fab3_rate.startAnimation(fab_open);
                    fab2_share.startAnimation(fab_open);
                    fab1_mail.startAnimation(fab_open);
                    fab_main.startAnimation(fab_clock);
                    fab3_rate.setClickable(true);
                    fab2_share.setClickable(true);
                    fab1_mail.setClickable(true);
                    isOpen = true;
                }

            }
        });
        fab3_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        fab2_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hey, I have a linear winning graph thanks to Steady Win Football Prediction App. Download here https://play.google.com/store/apps/details?id=com.zitano.steadywin";
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " Steady Winning");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(sharingIntent);

            }
        });
        fab1_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent feedback = new Intent(view.getContext(), FeedBack.class);

                startActivity(feedback);

            }
        });
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                nightModeButton.setActivated(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                nightModeButton.setActivated(true);
                break;
        }
        nightModeButton.setOnSwitchListener(new NightModeButton.OnSwitchListener() {
            @Override
            public void onSwitchListener(boolean isNight) {
                if(isNight){
                    Toast.makeText(getApplicationContext(),"Night Mode On",Toast.LENGTH_SHORT).show();
                    setTheme(R.style.darkTheme);
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }

                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
                }
                else {
                    Toast.makeText(getApplicationContext(),"Night Mode Off",Toast.LENGTH_SHORT).show();
                    setTheme(R.style.AppTheme);
                    if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }

                    finish();
                    startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
                }
            }
        });
    }






    class  FragmentAdaptrer extends FragmentPagerAdapter {

        public FragmentAdaptrer(


                @NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new Tips();
                case 1:
                    return new DailyTips();
                case 2: return  new Livescore();

            }
            return new Tips();
        }

        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                //
                //Your tab titles
                //
                case 0:return "DailyTips";
                case 1: return "Jackpot guide";
                case 2: return "LiveScore";

                default:return "DailyTips";
            }
        }

}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}