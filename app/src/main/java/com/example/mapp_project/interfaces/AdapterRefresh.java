package com.example.mapp_project.interfaces;

import com.example.mapp_project.datahelpers.StockData;

public interface AdapterRefresh {
    void onFavouriteAddClicked(StockData stock);
    void onFavouriteRemoveClicked(StockData stock);
}
