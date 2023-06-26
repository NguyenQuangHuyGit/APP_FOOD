package com.example.appfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.activity.DetailItemActivity;
import com.example.appfood.interfaces.OnItemClickListener;
import com.example.appfood.model.Food;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class foodItemAdapter extends RecyclerView.Adapter<foodItemAdapter.ViewHolder> {

    private Context context;

    private List<Food> arrayList;

    public foodItemAdapter(Context context, List<Food> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_food_item, parent, false);
        return  new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull foodItemAdapter.ViewHolder holder, int position) {
        ViewHolder myViewHolder= (ViewHolder) holder;
        Food food = arrayList.get(position);
        holder.txtName.setText(food.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText(decimalFormat.format(Double.parseDouble(food.getPrice()))+"đ");
        holder.imgItem.setImageResource(food.getImage());
        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos, boolean isLongClick) {
                if (!isLongClick){
                    Intent intent = new Intent(context, DetailItemActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",arrayList.get(pos).getId());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView txtName, txtPrice;
        private final ImageView imgItem;
        private OnItemClickListener mListener;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtName = (TextView) view.findViewById(R.id.itemName);
            txtPrice = (TextView) view.findViewById(R.id.itemPrice);
            imgItem = (ImageView) view.findViewById(R.id.itemImg);
            view.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(view, position,false);
                }
            }
        }
    }
}
