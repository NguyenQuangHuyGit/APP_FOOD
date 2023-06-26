package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appfood.R;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Food;
import java.text.DecimalFormat;

public class DetailItemActivity extends AppCompatActivity {

    ImageView image;
    TextView txtName;
    TextView txtPrice;
    TextView txtDescription;
    Intent intent;
    FoodDBHelper foodDB;
    Food food;
    ImageButton btnBack;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_item);
        setVariable();
        getData();
        setText();
        setEvent();
    }

    private void getData(){
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        food = foodDB.getById(id);
    }

    private void setVariable(){
        image = (ImageView) findViewById(R.id.imgFood);
        txtName = (TextView) findViewById(R.id.nameFood);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDescription = (TextView) findViewById(R.id.txtAbout);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        foodDB = new FoodDBHelper(this);
    }

    @SuppressLint("SetTextI18n")
    private void setText(){
        image.setImageResource(food.getImage());
        txtName.setText(food.getName());
        txtPrice.setText(decimalFormat.format(Double.parseDouble(food.getPrice()))+"đ");
        txtDescription.setText(food.getDescription());
    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailItemActivity.super.onBackPressed();
            }
        });
    }
}