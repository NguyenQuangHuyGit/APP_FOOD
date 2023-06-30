package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.appfood.R;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.adapter.historyItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Bill;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView listHistory;
    FoodDBHelper db;
    ArrayList<Bill> arrayList;
    SessionUser sessionUser;
    historyItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setVariable();
        setRecycleView();
        setEvent();
    }

    private void setVariable(){
        sessionUser = new SessionUser(this);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        listHistory = (RecyclerView) findViewById(R.id.listHistory);
        db = new FoodDBHelper(this);
        arrayList = db.getBillbyUserId(Integer.parseInt(sessionUser.getUserId()));
        adapter = new historyItemAdapter(this,arrayList);
    }

    private void setRecycleView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
        listHistory.setLayoutManager(layoutManager);
        listHistory.setHasFixedSize(true);
        listHistory.setAdapter(adapter);
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