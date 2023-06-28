package com.example.appfood.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.model.Cart;
import com.example.appfood.model.Food;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class cardItemAdapter extends RecyclerView.Adapter<cardItemAdapter.ViewHolder>{
    private Context context;

    private List<Food> arrayList;

    private Cart cart;

    public cardItemAdapter(Context context, List<Food> arrayList, Cart cart) {
        this.context = context;
        this.arrayList = arrayList;
        this.cart = cart;
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
        Context context = holder.itemView.getContext();
        Food food = arrayList.get(position);
        holder.txtName.setText(food.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPrice.setText(decimalFormat.format(Double.parseDouble(food.getPrice()))+"đ");
        holder.imgItem.setImageResource(food.getImage());
        holder.txtCount.setText(String.valueOf(cart.getItems().get(food.getId())));
        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                int id = holder.getAdapterPosition();
                Food food = arrayList.get(id);
                int amount = Integer.parseInt(holder.txtCount.getText().toString());
                if(amount==1){
                    arrayList.remove(id);
                    cart.removeFromCart(food.getId());
                    notifyDataSetChanged();
                }else{
                    cart.fixItem(food.getId(),amount-1);
                    holder.txtCount.setText(String.valueOf(amount-1));
                }
            }
        });
        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Food food = arrayList.get(holder.getAdapterPosition());
                int amount = Integer.parseInt(holder.txtCount.getText().toString());
                cart.fixItem(food.getId(),amount+1);
                holder.txtCount.setText(String.valueOf(amount+1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView txtName, txtPrice;
        private final ImageView imgItem;
        private final TextView txtCount;
        private final ImageButton btnPlus;
        private final ImageButton btnMinus;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtName = (TextView) view.findViewById(R.id.txtNameCart);
            txtPrice = (TextView) view.findViewById(R.id.txtPriceCart);
            imgItem = (ImageView) view.findViewById(R.id.imgCart);
            txtCount = (TextView) view.findViewById(R.id.txtCount);
            btnMinus = (ImageButton) view.findViewById(R.id.btnMinus);
            btnPlus = (ImageButton) view.findViewById(R.id.btnPlus);
        }
    }
}
