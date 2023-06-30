package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appfood.R;
import com.example.appfood.Session.SessionCart;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.database.FoodDBHelper;
import com.ncorti.slidetoact.SlideToActView;

import java.util.ArrayList;

public class NotifyActivity extends AppCompatActivity {
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        setVariable();
        setEvent();
    }

    private void setVariable(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
    }
    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyActivity.super.onBackPressed();
            }
        });
    }
}