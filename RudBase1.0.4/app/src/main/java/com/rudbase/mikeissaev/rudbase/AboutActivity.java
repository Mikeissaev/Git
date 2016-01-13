package com.rudbase.mikeissaev.rudbase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class AboutActivity extends AppCompatActivity {
    SharedPreferences sp;
    Integer getMyTheme;
    private Tracker mTracker;
    final String LOG_TAG = "myLogs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        String theme = sp.getString("theme_list", "1");
        switch (theme) {
            case "1":
                getMyTheme = R.style.AppTheme;
                break;
            case "2":
                getMyTheme = R.style.VKTheme;
                break;
            case "3":
                getMyTheme = R.style.GMTheme;
                break;
            case "4":
                getMyTheme = R.style.WATheme;
                break;
            case "5":
                getMyTheme = R.style.ORTheme;
                break;
            case "6":
                getMyTheme = R.style.DRTheme;
                break;
        }
        this.setTheme(getMyTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //-----------------------------Определение mTracker----------------------------------
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        String pageName = (String) getTitle();
        Log.i(LOG_TAG, "Открыт экран:: " + pageName);
        mTracker.setScreenName("Экран: " + pageName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void onVK(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://vk.com/mikeissaev"));
        startActivity(intent);
    }
    public void onOk(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ok.ru/profile/542994671073"));
        startActivity(intent);
    }

    public void onEmail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto:scr89shadow@gmail.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, "");
        intent.putExtra(Intent.EXTRA_SUBJECT, "[RudBase]");
        intent.putExtra(Intent.EXTRA_TEXT, " ");
        startActivity(intent);
    }


}
