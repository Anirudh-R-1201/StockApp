package com.example.mapp_project.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import com.example.mapp_project.datahelpers.AppData;
import com.example.mapp_project.datahelpers.BottomNavigationHandler;
import com.example.mapp_project.fragments.ChartFragment;
import com.example.mapp_project.fragments.MainFragment;
import com.example.mapp_project.fragments.OptionsFragment;
import com.example.mapp_project.R;
import com.example.mapp_project.fragments.SearchFragment;

import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private AppData appData;
    private BottomNavigationHandler bottomNavigationHandler;
    private boolean themeChanged = false;

    public void initBackend(){
        appData = AppData.getInstance(this);
        bottomNavigationHandler = new BottomNavigationHandler(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = AppData.getThemeSetting(AppData.getSettingFromPrefs(this,AppData.SELECTED_THEME));
        if(AppCompatDelegate.getDefaultNightMode() != theme){
            AppCompatDelegate.setDefaultNightMode(theme);
            recreate();
            themeChanged = true;
            return;
        }
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    private void setBottomNavigationItemSelected(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(currentFragment instanceof MainFragment || currentFragment instanceof ChartFragment) {
            bottomNavigationHandler.setSelectedItem(R.id.home);
        }else if (currentFragment instanceof SearchFragment) {
            bottomNavigationHandler.setSelectedItem(R.id.search);
        }else if (currentFragment instanceof OptionsFragment) {
            bottomNavigationHandler.setSelectedItem(R.id.settings);
        }
    }

    private int getCurrentFragmentsNavId(){
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(currentFragment instanceof MainFragment) {
            return R.id.home;
        }else if (currentFragment instanceof SearchFragment) {
            return R.id.search;
        }else if (currentFragment instanceof OptionsFragment) {
            return R.id.settings;
        }
        return -1;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBottomNavigationItemSelected();
        }

    @Override
    public void onStart() {
        super.onStart();
        if (!themeChanged) {
            initBackend();
            FragmentManager fragmentManager = getSupportFragmentManager();
            int id = R.id.home;
            if (fragmentManager.getBackStackEntryCount() == 0) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, MainFragment.newInstance()).commit();
            }else{
                id = getCurrentFragmentsNavId();
            }
            bottomNavigationHandler.initNavigation(R.id.bottomNav, id);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(appData != null){
            appData.setFirstUpdate(true);
            AppData.saveAppDataToSharedPrefs(this,appData,false);
        }
    }
}

