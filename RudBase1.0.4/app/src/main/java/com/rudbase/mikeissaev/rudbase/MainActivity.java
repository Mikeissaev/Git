package com.rudbase.mikeissaev.rudbase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    Integer getMyTheme;
    String theme;
    final String LOG_TAG = "myLogs";
    private Tracker mTracker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       //--------------------------Установка темы--------------------------------------------
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sp.getString("theme_list", "1");
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
        //--------------------------OnCreate---------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //-----------------------------Определение mTracker----------------------------------
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setTitle(R.string.main_title);

    }

    //------------------- Нажатие кнопки О приложении ----------------------
    public void onAbout(MenuItem item) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Главное меню")
                .setAction("О приложении")
                .build());

        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }
    //------------------- Нажатие кнопки Поделиться  ----------------------
    public void onShare(MenuItem item) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Главное меню")
                .setAction("Поделиться")
                .build());
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Приложение Полезные телефоны");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }
    //------------------- Нажатие кнопки Настройки ----------------------
    public void onSettings(MenuItem item) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("Главное меню")
                .setAction("Настройки")
                .build());
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        String pageName = (String) getTitle();
        Log.i(LOG_TAG, "Открыт экран:: " + pageName);
        mTracker.setScreenName("Экран: " + pageName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        String theme2 = sp.getString("theme_list", "1");
        if (theme2 != theme) {
            this.recreate();
        }
        super.onResume();
    }

}

