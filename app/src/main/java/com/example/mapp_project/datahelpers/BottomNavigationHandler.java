package com.example.mapp_project.datahelpers;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mapp_project.R;
import com.example.mapp_project.fragments.ChartFragment;
import com.example.mapp_project.fragments.MainFragment;
import com.example.mapp_project.fragments.OptionsFragment;
import com.example.mapp_project.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class BottomNavigationHandler {
    final Context context;
    private BottomNavigationView bottomNavigationView;
    boolean selectedByCode = false;
    public BottomNavigationHandler(Context ctx) {
        this.context = ctx;
    }

    public void initNavigation(int viewId,int navId){
        if (!(context instanceof AppCompatActivity)) {
            return;
        }

        AppCompatActivity activity = (AppCompatActivity) context;
        Objects.requireNonNull(activity.getSupportActionBar()).hide();
        bottomNavigationView = activity.findViewById(viewId);
        if(navId !=-1){
            bottomNavigationView.setSelectedItemId(navId);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if(!selectedByCode){
                if(id == bottomNavigationView.getSelectedItemId()   && !(activity.getSupportFragmentManager().findFragmentById(R.id.fragmentContainer) instanceof ChartFragment)) {
                    return true;
                }else if (id == R.id.home){
                    navigateToFragment(MainFragment.newInstance(),AppData.MAIN_FRAGMENT,activity);
                    return true;
                }else if(id ==  R.id.search){
                    navigateToFragment(SearchFragment.newInstance(),AppData.SEARCH_FRAGMENT,activity);
                    return true;
                }else if (id == R.id.settings ){
                    navigateToFragment(OptionsFragment.newInstance(),AppData.OPTIONS_FRAGMENT,activity);
                    return true;
                }
            }else{
                selectedByCode = false;
                return true;
            }
            return false;
        });

    }

    private void navigateToFragment(Fragment fragment,String name, AppCompatActivity activity){
        FragmentTransaction fragmentTransaction=activity.getSupportFragmentManager().beginTransaction();
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,android.R.anim.fade_in,android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragmentContainer,fragment);
        fragmentTransaction.addToBackStack(name);
        fragmentTransaction.commit();

    }

    public void setSelectedItem(int navId){
        if(bottomNavigationView != null && bottomNavigationView.getSelectedItemId() != navId){
            selectedByCode = true;
            bottomNavigationView.setSelectedItemId(navId);
        }
    }
}
