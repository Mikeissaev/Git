package com.rudbase.mikeissaev.rudbase;


import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainList extends ListFragment {
    final String LOG_TAG = "myLogs";
    public String searchText = "";
    public String catText;
    public String catTrans;
    final ArrayList<String> arrayListTrans = new ArrayList<String>();
    final ArrayList<String> arrayListCat = new ArrayList<String>();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initList();
        setHasOptionsMenu(true);
        setEmptyText(getResources().getString(R.string.nofingSeach));


    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //----------------------------Поиск по всей фразе ------------------------------------
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }


            //---------------------------Поиск при добавлении символа ---------------------------
            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                initList();
                return false;

            }
        });

    }

    //---------------------------------------Клик по списку----------------------------------------
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        catText = l.getItemAtPosition(position).toString();
        readFileName();
        Intent intent = new Intent(getActivity(), Lvl2Activity.class);
        intent.putExtra("catText", catText);
        intent.putExtra("catTrans", catTrans);
        Log.d(LOG_TAG, "arrayListTrans: " + arrayListTrans.get(position).toString() + " catText: " + catText);
        startActivity(intent);
    }

    //-------------------------------------------Создание списка---------------------------------------------------------
    public void initList() {
        readCatName();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, arrayListCat);

        if (searchText.equals("")) {
            setListAdapter(adapter);
        } else {
            adapter.getFilter().filter(searchText);
            adapter.notifyDataSetChanged();
            setListAdapter(adapter);
        }
    }

    //----------------------------------------Опредиление имени файла для передачи Lvl2----------------------------------------------------
    void readFileName() {
        try {
            // открываем поток для чтения
            AssetManager assetManager = getActivity().getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open("category"));
            BufferedReader br = new BufferedReader(istream);
            String str;
            int i = 0;
            // читаем содержимое

            while ((str = br.readLine()) != null) {
                String readText[] = str.split(":");
                if (catText.equals(arrayListCat.get(i).toString())) {
                    catTrans = arrayListTrans.get(i).toString();
                }
                i++;


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //----------------------------------------Чтение и заполнение списка----------------------------------------------------------
    void readCatName() {
        try {
            // открываем поток для чтения
            AssetManager assetManager = getActivity().getAssets();
            InputStreamReader istream = new InputStreamReader(assetManager.open("category"));
            BufferedReader br = new BufferedReader(istream);
            String str;
            int i = 0;
            arrayListCat.removeAll(arrayListCat);
            arrayListTrans.removeAll(arrayListTrans);
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                String readText[] = str.split(":");
                arrayListCat.add(i, readText[0]);
                arrayListTrans.add(i, readText[1]);
                i++;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}



