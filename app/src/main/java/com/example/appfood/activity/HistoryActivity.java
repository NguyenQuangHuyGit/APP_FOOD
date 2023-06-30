package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.appfood.R;

public class HistoryActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView listHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setVariable();
        setEvent();
    }

    private void setVariable(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        listHistory = (RecyclerView) findViewById(R.id.listHistory);
    }

    private void setRecycleView(){

    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.super.onBackPressed();
            }
        });
    }
}