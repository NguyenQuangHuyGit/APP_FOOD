package com.example.appfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.model.Bill;
import com.example.appfood.model.Food;

import java.text.DecimalFormat;
import java.util.List;

public class historyItemAdapter extends RecyclerView.Adapter<historyItemAdapter.ViewHolder>{

    private Context context;

    private List<Bill> arrayList;

    public historyItemAdapter(Context context, List<Bill> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public historyItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull historyItemAdapter.ViewHolder holder, int position) {
        Bill bill = arrayList.get(position);
        holder.txtDate.setText(bill.getDate());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtTotal.setText(bill.getTotalBill());
        holder.txtCount.setText(String.valueOf(bill.getCount()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtCount, txtTotal, txtDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtCount = (TextView) view.findViewById(R.id.txtCount);
            txtTotal = (TextView) view.findViewById(R.id.txtTotal);
            txtDate = (TextView) view.findViewById(R.id.txtDate);
        }
    }
}
