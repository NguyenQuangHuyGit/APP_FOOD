package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appfood.R;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.User;

public class AccountActivity extends AppCompatActivity {
    private FoodDBHelper foodDBHelper;
    private ImageButton btnBack;
    SessionUser sessionUser;
    private TextView tvName, tvAddress, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setVariable();
        setEvent();

        foodDBHelper = new FoodDBHelper(this);
        int userId = Integer.parseInt(sessionUser.getUserId());

        User user = foodDBHelper.getUserById(userId);
        if (user != null) {
            String name = user.getName();
            String address = user.getAddress();
            String email = user.getEmail();
            String phone = user.getPhone();

            tvName = findViewById(R.id.tvName);
            tvAddress = findViewById(R.id.tvAddress);
            tvEmail = findViewById(R.id.tvEmail);
            tvPhone = findViewById(R.id.tvPhone);

            tvName.setText("name");
            tvAddress.setText("address");
            tvEmail.setText("email");
            tvPhone.setText("phone");
        }


    }
    private void setVariable(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        sessionUser = new SessionUser(this);

    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountActivity.super.onBackPressed();
            }
        });
    }


    public static class PredictionItemActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_prediction_item);
        }
    }
}