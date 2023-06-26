package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.appfood.R;
import com.example.appfood.adapter.cardItemAdapter;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Food;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    RecyclerView listCart;
    List<Food> arrayFood;
    FoodDBHelper foodDB;
    cardItemAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setVariable();
        setRecycleView();
        setEvent();
    }

    private void setVariable(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        foodDB = new FoodDBHelper(this);
        foodDB.insertFood();
        listCart = (RecyclerView) findViewById(R.id.detailCartLayout);
    }

    private void setRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(CartActivity.this);
        listCart.setLayoutManager(layoutManager);
        listCart.setHasFixedSize(true);
        arrayFood = foodDB.getAllFoods();
        adapter = new cardItemAdapter(CartActivity.this,arrayFood);
        listCart.setAdapter(adapter);
    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity.super.onBackPressed();
            }
        });
    }
}