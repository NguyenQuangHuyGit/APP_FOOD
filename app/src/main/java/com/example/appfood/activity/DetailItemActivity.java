package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appfood.R;
import com.example.appfood.Session.SessionCart;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Cart;
import com.example.appfood.model.Food;
import java.text.DecimalFormat;

public class DetailItemActivity extends AppCompatActivity {

    ImageView image;
    TextView txtName;
    TextView txtPrice;
    TextView txtDescription;

    TextView txtCount;
    ImageButton btnPlus;
    ImageButton btnMinus;

    Button btnAddCart;
    Intent intent;
    FoodDBHelper foodDB;
    Food food;
    ImageButton btnBack;
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    SessionCart sessionCart;

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
        btnMinus = (ImageButton) findViewById(R.id.btnMinus);
        btnPlus = (ImageButton) findViewById(R.id.btnPlus);
        txtCount = (TextView) findViewById(R.id.txtCount);
        btnAddCart = (Button) findViewById(R.id.btnAddCart);
        sessionCart = new SessionCart(this);
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
        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(txtCount.getText().toString());
                if(amount==1){
                    btnMinus.setEnabled(true);
                    btnMinus.setAlpha(1.f);
                }
                txtCount.setText(String.valueOf(++amount));
            }
        });
        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int amount = Integer.parseInt(txtCount.getText().toString());
                if (amount==2){
                    btnMinus.setAlpha(0.3f);
                    btnMinus.setEnabled(false);
                }
                txtCount.setText(String.valueOf(--amount));
            }
        });

        btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = intent.getExtras();
                int id = bundle.getInt("id");
                int amount = Integer.parseInt(txtCount.getText().toString());
                Cart cart = new Cart();
                cart.updateItem(id,amount);
                sessionCart.setCart(cart);
                Intent intent = new Intent(DetailItemActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}