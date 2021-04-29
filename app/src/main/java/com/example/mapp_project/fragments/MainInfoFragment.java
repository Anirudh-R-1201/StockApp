package com.example.mapp_project.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mapp_project.datahelpers.AppData;
import com.example.mapp_project.R;
import com.example.mapp_project.datahelpers.RecyclerAdapter;
import com.example.mapp_project.datahelpers.StockApi;
import com.example.mapp_project.datahelpers.StockData;
import com.example.mapp_project.interfaces.HelperCallback;
import com.example.mapp_project.interfaces.StockApiCallback;

import java.util.List;


public class MainInfoFragment extends Fragment {
    private AppData appData;
    private StockApi stockApi;
    private View fragmentView;
    private RecyclerAdapter infoAdapter;
    private RecyclerView infoRecyclerView;
    private ProgressBar spinner;
    public MainInfoFragment() {
    }

    public void initBackend(){
        appData = AppData.getInstance(getContext());
        stockApi = appData.getStockApi(getContext());
        initListViews();
        spinner = fragmentView.findViewById(R.id.infoProgress);
        spinner.setVisibility(View.INVISIBLE);
        if(appData.isFirstUpdate()){
            updateInfoBoxes(()->{});
            appData.setFirstUpdate(false);
        }
    }

    public static MainInfoFragment newInstance() {
        return new MainInfoFragment();
    }


    private RecyclerView setRecyclerSettings(RecyclerAdapter adapter){
        RecyclerView view = fragmentView.findViewById(R.id.infoRecyclerView);
        view.setNestedScrollingEnabled(true);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        return view;
    }
    public void initListViews(){
        infoAdapter = new RecyclerAdapter(getContext(), appData.getInfoData(), appData,-1,R.layout.info_box,null);
        infoRecyclerView = setRecyclerSettings(infoAdapter);
    }

    public void updateInfoBoxes(HelperCallback cb){
        spinner.setVisibility(View.VISIBLE);
        if(cb != null){
            stockApi.getFrontPageSymbols(new StockApiCallback() {
                @Override
                public void onSuccess(List<StockData> response, Context context) {
                    List<StockData> infoData = appData.getInfoData();
                    infoData.clear();
                    infoData.addAll(response);
                    infoAdapter.notifyDataSetChanged();
                    spinner.setVisibility(View.INVISIBLE);
                    finishCallback(cb);
                }
                @Override
                public void onError(VolleyError error, Context context) {
                    Toast.makeText(context,getString(R.string.error_msg),Toast.LENGTH_LONG).show();
                    spinner.setVisibility(View.INVISIBLE);
                    finishCallback(cb);
                }
            });
        }else{
            finishCallback(null);
            spinner.setVisibility(View.INVISIBLE);
        }
    }

    private void finishCallback(HelperCallback cb){
        if(cb != null){
            cb.onComplete();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_main_info, container, false);
        initBackend();
        return fragmentView;
    }
}