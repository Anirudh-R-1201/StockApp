package com.example.mapp_project.datahelpers;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.mapp_project.R;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class OptionsHelper {
    private TextInputEditText stockPriceInput, stockAmountInput, averagePriceInput, calculateNewAverageInput, calculateWantedAverageInput;
    private AppCompatButton calculateNewAverageButton, calculateWantedAverageButton;
    private final Context context;
    private final View fragmentView;
    public OptionsHelper(Context context,View fragmentView) {
        this.context = context;
        this.fragmentView = fragmentView;
    }

    public void initAveragePriceFields(){
        if(fragmentView != null){
            stockPriceInput = fragmentView.findViewById(R.id.stockPriceInput);
            stockAmountInput = fragmentView.findViewById(R.id.stockAmountInput);
            averagePriceInput = fragmentView.findViewById(R.id.averagePriceInput);

            calculateNewAverageInput = fragmentView.findViewById(R.id.calculateNewAverageInput);
            calculateNewAverageButton = fragmentView.findViewById(R.id.calculateNewAverageButton);

            calculateWantedAverageInput = fragmentView.findViewById(R.id.calculateWantedAverageInput);
            calculateWantedAverageButton = fragmentView.findViewById(R.id.calculateWantedAverageButton);
            calculateWantedAverageButton.setOnClickListener(v -> calculateWantedAveragePrice());
            calculateNewAverageButton.setOnClickListener(v-> calculateNewAveragePrice());
        }
    }

    private boolean checkViews(){
        return  stockPriceInput != null && stockAmountInput != null &&
                averagePriceInput != null && calculateNewAverageInput != null &&
                calculateNewAverageButton != null && calculateWantedAverageInput != null &&
                calculateWantedAverageButton != null;
    }

    private Double getInputValue(TextInputEditText input){
        String value = Objects.requireNonNull(input.getText()).toString();
        if(value.isEmpty()){
            return null;
        }
        return Double.parseDouble(input.getText().toString());

    }

    public void calculateNewAveragePrice(){
        if(checkViews()){
            Double stockPrice = getInputValue(stockPriceInput);
            Double stockAmount = getInputValue(stockAmountInput);
            Double averagePrice = getInputValue(averagePriceInput);

            Double moneyAmount = getInputValue(calculateNewAverageInput);
            if(stockPrice == null || stockAmount==null||averagePrice==null || moneyAmount == null){
                setToast(context.getString(R.string.missing_value), Toast.LENGTH_SHORT);
                return;
            }

            double stockAmountToGet = moneyAmount / stockPrice;
            double bottomDivider = stockAmountToGet + stockAmount;
            double topDivider = averagePrice * stockAmount + stockAmountToGet * stockPrice;

            BigDecimal bd = new BigDecimal(topDivider/bottomDivider).setScale(2, RoundingMode.HALF_UP);
            setToast(context.getString(R.string.new_avg_would_be)+" "+ bd.toString(), Toast.LENGTH_LONG);
        }
    }

    private void setToast(String text,int length){
        Toast toast = Toast.makeText(context, text, length);
        toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    public void calculateWantedAveragePrice(){
        if(checkViews()){
            Double stockPrice = getInputValue(stockPriceInput);
            Double stockAmount = getInputValue(stockAmountInput);
            Double averagePrice = getInputValue(averagePriceInput);

            Double newAveragePrice = getInputValue(calculateWantedAverageInput);
            if(stockPrice == null || stockAmount==null||averagePrice==null || newAveragePrice == null){
                setToast(context.getString(R.string.missing_value),Toast.LENGTH_SHORT);
                return;
            }

            if(newAveragePrice.equals(stockPrice)){
                setToast(context.getString(R.string.not_possible),Toast.LENGTH_LONG);
                return;
            }
            double bottomDivider = newAveragePrice - stockPrice;
            double topDivider = (-1 * stockAmount) * (newAveragePrice - averagePrice);

            BigDecimal amount = new BigDecimal(topDivider/bottomDivider).setScale(2, RoundingMode.HALF_UP);
            BigDecimal moneyAmount = new BigDecimal(amount.doubleValue()*stockPrice).setScale(2, RoundingMode.HALF_UP);
            if(amount.doubleValue()<0){
                setToast(context.getString(R.string.not_possible),Toast.LENGTH_LONG);
                return;
            }
            setToast(context.getString(R.string.should_buy_info)+" "+amount.toString()+" "+moneyAmount.toString()+"₹", Toast.LENGTH_LONG);
        }
    }
}
