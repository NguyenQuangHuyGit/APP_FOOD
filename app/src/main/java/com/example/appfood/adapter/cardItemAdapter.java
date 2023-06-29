package com.example.appfood.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.activity.CartActivity;
import com.example.appfood.activity.MainActivity;
import com.example.appfood.interfaces.TextViewChangeListener;
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

    private TextViewChangeListener listener;

    public cardItemAdapter(Context context, List<Food> arrayList, Cart cart) {
        this.context = context;
        this.arrayList = arrayList;
        this.cart = cart;
    }

    public void setTextViewChangeListener(TextViewChangeListener listener) {
        this.listener = listener;
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
                    if(arrayList.size()==0)
                        emptyCart();
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
        holder.txtCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int position = holder.getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onTextViewChanged(position, s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    private void emptyCart(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Giỏ hàng trống rồi!");
        builder.setMessage("Hãy chọn đồ ăn khác rồi quay lại nhé bạn yêu ơi !!!!!");
        builder.setIcon(R.drawable.app_logo);
        builder.setPositiveButton("Đi thôi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context,MainActivity.class);
                context.startActivity(intent);
            }
        });
        builder.show();
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
