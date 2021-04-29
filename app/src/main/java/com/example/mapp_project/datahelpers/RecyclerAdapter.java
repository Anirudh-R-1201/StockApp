package com.example.mapp_project.datahelpers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mapp_project.R;
import com.example.mapp_project.fragments.ChartFragment;
import com.example.mapp_project.interfaces.AdapterRefresh;
import com.google.gson.Gson;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private final List<StockData> stockList;
    private final Context context;
    private final AppData data;
    private final int viewId;
    private final AdapterRefresh refresh;
    private final Gson gson;
    private final int layoutToInflate;
    public RecyclerAdapter(Context ctx, List<StockData> stockList,AppData data,int viewId,int layoutToInflate, AdapterRefresh refresh){
        this.stockList = stockList;
        this.context = ctx;
        this.data = data;
        this.viewId = viewId;
        this.refresh = refresh;
        this.gson = new Gson();
        this.layoutToInflate = layoutToInflate;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(this.layoutToInflate,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StockData stock = stockList.get(position);
        if(this.layoutToInflate == R.layout.stock_row){
            handleStockRow(holder,stock);
        }else if(this.layoutToInflate == R.layout.info_box){
            handleInfoBox(holder,stock);
        }
    }

    private void handleStockRow(@NonNull ViewHolder holder,StockData stock){
        double change = stock.getPercentChange();
        if(change<0){
            holder.priceChange.setTextColor(context.getColor(R.color.red));
        }else{
            holder.priceChange.setTextColor(context.getColor(R.color.green));
        }
        holder.priceChange.setText(change+"%");
        holder.stockPrice.setText(String.valueOf(stock.getMarketPrice()));
        holder.stockName.setText(String.valueOf(stock.getName()));
        holder.stockTicker.setText(String.valueOf(stock.getSymbol()));

        if(this.viewId != R.id.favouriteRecyclerView && !stock.isFavourite()){
            if(data.isStockInFavouriteList(stock.getSymbol())){
                stock.setFavourite(true);
            }
        }

        holder.mainLayout.setOnClickListener(v->{
            String stockString = gson.toJson(stock);
            FragmentTransaction fragmentTransaction=((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            ChartFragment chartFragment = ChartFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("Stock",stockString);
            chartFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentContainer,chartFragment);
            fragmentTransaction.addToBackStack(AppData.CHART_FRAGMENT);
            fragmentTransaction.commit();
        });

        holder.favouriteStatus.setImageResource(stock.isFavourite()?R.drawable.ic_favourite:R.drawable.ic_not_favourite);
        holder.favouriteStatus.setOnClickListener(v -> {
            if(!data.isRefreshing()){
                int adapterPosition = holder.getAdapterPosition();
                StockData selectedStock = stockList.get(adapterPosition);
                if(selectedStock.isFavourite()){
                    if(selectedStock.getUuid() == null){
                        selectedStock.setFavourite(false);
                        notifyItemChanged(adapterPosition);
                    }else{
                        stockList.remove(adapterPosition);
                        notifyItemRemoved(adapterPosition);
                    }
                    refresh.onFavouriteRemoveClicked(selectedStock);

                }else{
                    selectedStock.setFavourite(true);
                    notifyItemChanged(adapterPosition);
                    refresh.onFavouriteAddClicked(selectedStock);
                }
            }
        });
    }

    private void handleInfoBox(@NonNull ViewHolder holder,StockData stock){
        double change = stock.getPercentChange();
        if(change<0){
            holder.priceChange.setTextColor(context.getColor(R.color.red));
        }else{
            holder.priceChange.setTextColor(context.getColor(R.color.green));
        }
        holder.priceChange.setText(change+"%");
        holder.stockPrice.setText(String.valueOf(stock.getMarketPrice()));
        holder.stockName.setText(String.valueOf(stock.formatInfoBoxText(stock.getSymbol())));

        holder.mainLayout.setOnClickListener(v->{
            String stockString = gson.toJson(stock);
            FragmentTransaction fragmentTransaction=((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            ChartFragment chartFragment = ChartFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("Stock",stockString);
            chartFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragmentContainer,chartFragment);
            fragmentTransaction.addToBackStack(AppData.CHART_FRAGMENT);
            fragmentTransaction.commit();
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView priceChange;
        final TextView stockPrice;
        final TextView stockName;
        final TextView stockTicker;
        final ImageView favouriteStatus;
        final ConstraintLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            priceChange = itemView.findViewById(R.id.priceChange);
            stockPrice = itemView.findViewById(R.id.stockPrice);
            stockName = itemView.findViewById(R.id.stockName);
            stockTicker = itemView.findViewById(R.id.stockTicker);
            favouriteStatus = itemView.findViewById(R.id.favouriteStatus);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
