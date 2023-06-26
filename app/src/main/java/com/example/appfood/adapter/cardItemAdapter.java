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
import com.example.appfood.model.Food;

import java.text.DecimalFormat;
import java.util.List;

public class cardItemAdapter extends RecyclerView.Adapter<cardItemAdapter.ViewHolder>{
    private Context context;

    private List<Food> arrayList;

    public cardItemAdapter(Context context, List<Food> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_detail_cart, parent, false);
        return  new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull cardItemAdapter.ViewHolder holder, int position) {
        ViewHolder myViewHolder= (ViewHolder) holder;
        Food food = arrayList.get(position);
        holder.txtName.setText(food.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText(decimalFormat.format(Double.parseDouble(food.getPrice()))+"đ");
        holder.imgItem.setImageResource(food.getImage());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtName, txtPrice;
        private final ImageView imgItem;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtName = (TextView) view.findViewById(R.id.txtNameCart);
            txtPrice = (TextView) view.findViewById(R.id.txtPriceCart);
            imgItem = (ImageView) view.findViewById(R.id.imgCart);
        }
    }
}
