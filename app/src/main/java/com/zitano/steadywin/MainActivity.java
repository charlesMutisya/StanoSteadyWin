package com.zitano.steadywin;

import android.app.UiModeManager;
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

import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchAnimListener;
import com.vimalcvs.switchdn.DayNightSwitchListener;
import com.zitano.steadywin.ui.main.ThemeSettings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
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

    private static final String TAG = "MainActivityLog";
    private FloatingActionButton fab_main, fab1_mail, fab2_share,fab3_rate;
    private Animation fab_open, fab_close, fab_clock, fab_anticlock;

    Boolean isOpen = false;
    DayNightSwitch nightModeButton;
    private UiModeManager uiModeManager;
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
        uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        fab_main = findViewById(R.id.fab);
        fab1_mail = findViewById(R.id.fab1);
        fab2_share = findViewById(R.id.fab2);
        fab3_rate= findViewById(R.id.fab3);
        nightModeButton=(DayNightSwitch)findViewById(R.id.switch_item);;

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
               // nightModeButton.setActivated(false);
                Log.i(TAG, "Dark mode has not been activated: ");
                setTheme(R.style.AppTheme);
                nightModeButton.setIsNight(false);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
               // nightModeButton.setActivated(true);
                Log.i(TAG, "Dark mode has been activated: ");
                setTheme(R.style.darkTheme);
                nightModeButton.setIsNight(true);
                break;
        }
        nightModeButton.setDuration(450);
       // nightModeButton.setIsNight(ThemeSettings.getInstance(this).nightMode);

        nightModeButton.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean isNight) {
                ThemeSettings.getInstance(MainActivity.this).nightMode = isNight;
                ThemeSettings.getInstance(MainActivity.this).refreshTheme();
            }
        });

        nightModeButton.setAnimListener(new DayNightSwitchAnimListener() {
            @Override
            public void onAnimEnd() {
                Intent intent = new Intent(MainActivity.this, MainActivity.this.getClass());
                intent.putExtras(getIntent());
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
            @Override
            public void onAnimValueChanged(float v) {

            }
            @Override
            public void onAnimStart() {
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

    public void NightModeON(){
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
    }

    public void NightModeOFF(){
        uiModeManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
    }
}