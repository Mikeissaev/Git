package com.rudbase.mikeissaev.rudbase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;


public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sp;
    Integer getMyTheme;
    String theme;
    final String LOG_TAG = "myLogs";
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        super.onCreate(savedInstanceState);
        //-----------------------------Определение mTracker----------------------------------
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        //-----------------------------Отправка статистики----------------------------------
        String pageName = (String) getTitle();
        Log.i(LOG_TAG, "Открыт экран:: " + pageName);
        mTracker.setScreenName("Экран: " + pageName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        //---------------------------------Подключение фрагмента настроек-----------------------------------------
        getFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }


}
