package com.rudbase.mikeissaev.rudbase;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Lvl2Activity extends AppCompatActivity {
    public String lvl2FileName;
    final ArrayList<String> arrayListName = new ArrayList<String>();
    final ArrayList<String> arrayListTel = new ArrayList<String>();
    ListView lvl2List;
    final String LOG_TAG = "myLogs";
    private Tracker mTracker;
    SharedPreferences sp;
    Integer getMyTheme;
    String catStrSel;
    String pageName;

    //--------------------------------Тема приложения-------------------------------------------------
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
        setContentView(R.layout.activity_lvl2);
        //-----------------------------Определение mTracker----------------------------------
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        setTitle(R.string.main_title);
//--------------------------------Получаем категорию-------------------------------------------------
        String getCat = getIntent().getStringExtra("catText");
        lvl2FileName = getIntent().getStringExtra("catTrans");
        setTitle(getCat);

        lvl2List = (ListView) findViewById(R.id.lvl2List);
        arrayListName.removeAll(arrayListName);
        readFileName();
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListName);
        lvl2List.setAdapter(adapter);
        lvl2List.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                catStrSel = arrayListName.get(position).toString();
                readFileTel();
              //------------------------------------Отправка статистики-------------------------------------------------
                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("Подкатегория")
                        .setAction(pageName +" > " + catStrSel)
                        .build());
                Log.d(LOG_TAG,"Выбрана подкатегория: " + pageName +" > " + catStrSel );

//------------------------------Создание диалога и клик по пункту----------------------------------------------------------
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(Lvl2Activity.this);
                builderSingle.setTitle(catStrSel);
                if ((arrayListTel.size()) != 1) {
                    arrayListTel.remove(0);
                }
                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Lvl2Activity.this, android.R.layout.simple_list_item_1, arrayListTel);
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri call = Uri.parse("tel:" + arrayListTel.get(which));
                        Intent intent = new Intent(Intent.ACTION_DIAL, call);
                        startActivity(intent);
                        Log.d(LOG_TAG, "Tel: " + arrayListTel.get(which).toString());
                    }
                });

                builderSingle.show();


            }
        });

    }
    //---------------------------Чтение файла для создания списка----------------------------------------------------------

    void readFileName() {
        try {
            // открываем поток для чтения
            AssetManager assetManager = getBaseContext().getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open(lvl2FileName));
            BufferedReader br = new BufferedReader(istream);
            String str;
            int i = 0;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                String readText[] = str.split(":");
                Log.d(LOG_TAG, "readText[i]= " + readText[0] + " Line: " + i);
                arrayListName.add(i, readText[0]);
                i++;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "На данный момент категория пуста :(", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //---------------------------Чтение файла для создания Дилога----------------------------------------------------------
    void readFileTel() {
        try {
            // открываем поток для чтения
            AssetManager assetManager = getBaseContext().getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open(lvl2FileName));
            BufferedReader br = new BufferedReader(istream);
            String str;
            int i = 0;
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                String readText[] = str.split(":");
                int pv = readText.length;
                arrayListTel.removeAll(arrayListTel);
                if (readText[0].equals(catStrSel)) {
                    while (i != pv) {
                        arrayListTel.add(i, readText[i]);
                        Log.d(LOG_TAG, "readText[i]= " + readText[i] + " Position: " + i);
                        i++;
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "На данный момент категория пуста :(", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume() {
        pageName = (String) getTitle();
        Log.i(LOG_TAG, "Открыт экран: " + pageName);
        mTracker.setScreenName("Экран: " + pageName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        super.onResume();
    }
}
