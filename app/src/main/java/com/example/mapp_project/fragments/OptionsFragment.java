package com.example.mapp_project.fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mapp_project.datahelpers.AppData;
import com.example.mapp_project.datahelpers.OptionsHelper;
import com.example.mapp_project.R;

import java.util.Objects;


public class OptionsFragment extends Fragment {
    private AppData appData;
    private Spinner spinner;
    private boolean spinnerClicked = false;
    private OptionsHelper optionsHelper;
    private View fragmentView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public void initBackend(){
        appData = AppData.getInstance(getContext());
        optionsHelper = new OptionsHelper(getContext(),fragmentView);
        optionsHelper.initAveragePriceFields();
        swipeRefreshLayout = Objects.requireNonNull(getActivity()).findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setEnabled(false);
    }

    public OptionsFragment() {}

    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_options, container, false);
        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
        initBackend();
        initThemeSpinner();
        initSettingSwitches();
    }

    private void setAppTheme(int selected){
        int theme = AppData.getThemeSetting(selected);
        AppData.setSettingToPrefs(Objects.requireNonNull(getContext()),AppData.SELECTED_THEME,selected);
        AppCompatDelegate.setDefaultNightMode(theme);
    }

    private void initThemeSpinner(){
        spinner = fragmentView.findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.theme_settings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(AppData.getSettingFromPrefs(Objects.requireNonNull(getContext()),AppData.SELECTED_THEME),false);
        spinner.setOnTouchListener((v, event) -> {
            spinnerClicked = true;
            return false;
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinnerClicked){
                    setAppTheme(position);
                    spinnerClicked = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initSettingSwitches(){}

    private void setSpinnerEnabled(boolean value){
        spinner.setClickable(value);
        spinner.setEnabled(value);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}