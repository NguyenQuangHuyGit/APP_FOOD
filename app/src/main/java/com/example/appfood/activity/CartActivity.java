package com.example.appfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.appfood.R;
import com.example.appfood.Session.SessionCart;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.adapter.cardItemAdapter;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.interfaces.TextViewChangeListener;
import com.example.appfood.model.Cart;
import com.example.appfood.model.Food;
import com.ncorti.slidetoact.SlideToActView;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements TextViewChangeListener {

    RecyclerView listCart;
    List<Food> arrayFood;
    FoodDBHelper foodDB;
    cardItemAdapter adapter;
    ImageButton btnBack;
    SessionCart sessionCart;
    Map<Integer, Integer> itemCarts;
    Cart cart;
    TextView txtCount;
    TextView txtTotal;
    TextView txtTotalBill;

    SlideToActView btnCheckOut;
    SessionUser sessionUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setVariable();
        getData();
        setRecycleView();
        setEvent();
        setText();
    }

    private void setVariable(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        foodDB = new FoodDBHelper(this);
        foodDB.insertFood();
        listCart = (RecyclerView) findViewById(R.id.detailCartLayout);
        sessionCart = new SessionCart(this);
        arrayFood = new ArrayList<>();
        txtCount = (TextView) findViewById(R.id.txtCount);
        txtTotal = (TextView) findViewById(R.id.txtTotal);
        txtTotalBill = (TextView) findViewById(R.id.txtTotalBill);
        btnCheckOut = (SlideToActView) findViewById(R.id.btnCheckOut);
        sessionUser = new SessionUser(this);
    }

    private void setRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        listCart.setLayoutManager(layoutManager);
        listCart.setHasFixedSize(true);
        adapter = new cardItemAdapter(CartActivity.this,arrayFood,cart);
        listCart.setAdapter(adapter);
        adapter.setTextViewChangeListener(this);
    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });

        btnCheckOut.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            Date date = new Date();
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                if(sessionUser.getUserId() == null){

                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setTitle("Xác nhận đặt hàng");
                builder.setMessage("Bạn có chắc chắn đặt hàng không?");
                builder.setIcon(R.drawable.app_logo);
                builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        foodDB.insertBill(txtTotal.getText().toString(), date,Integer.parseInt(txtCount.getText().toString()), Integer.parseInt(sessionUser.getUserId()));
                        int idBill = foodDB.getLastInsertId();
                        for(int i=0;i<arrayFood.size();i++){
                            int idFood = arrayFood.get(i).getId();
                            foodDB.insertDetailBill(idBill,idFood,itemCarts.get(idFood));
                        }
                        sessionCart.clear();
                        vibrate(CartActivity.this);
                        Intent intent = new Intent(CartActivity.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(CartActivity.this,"Đặt hàng thành công",Toast.LENGTH_SHORT);
                    }
                });
                builder.setNegativeButton("Thôi no rồi!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CartActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        btnCheckOut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(CartActivity.this, "Event", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
    public void getData(){
        cart = new Cart();
        cart = sessionCart.getCart();
        itemCarts = cart.getItems();
        Food food;
        List<Integer> itemIds = cart.getItemIds();
        for (int i = 0; i < itemIds.size(); i++) {
            food = foodDB.getById(itemIds.get(i));
            arrayFood.add(food);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setText(){
        int count = 0;
        for(int value : itemCarts.values()){
            count += value;
        }
        double totalPrice = 0;
        txtCount.setText(String.valueOf(count));
        for(int i=0;i<arrayFood.size();i++){
            count = itemCarts.get(arrayFood.get(i).getId());
            double temp = Double.parseDouble(arrayFood.get(i).getPrice());
            totalPrice += temp*count;
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotal.setText(decimalFormat.format(totalPrice)+"đ");
        double totalBill = 0;
        totalBill = totalPrice + 15000.0;
        txtTotalBill.setText(decimalFormat.format(totalBill)+"đ");
    }

    @Override
    public void onTextViewChanged(int position, String newText) {
        setText();
    }

    private void vibrate(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(1000);
            }
        }
    }

}