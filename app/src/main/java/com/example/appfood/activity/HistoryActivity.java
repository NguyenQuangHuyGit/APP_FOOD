package com.example.appfood.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.adapter.historyItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Bill;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView listHistory;
    ArrayList<Bill> arrayList;
    SessionUser sessionUser;
    historyItemAdapter adapter;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setVariable();
        readData();
        setEvent();
    }

    private void setVariable(){
        sessionUser = new SessionUser(this);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        listHistory = (RecyclerView) findViewById(R.id.listHistory);
        db = FirebaseFirestore.getInstance();
    }


//    private void setRecycleView(){
//        arrayList = new ArrayList<Bill>();
//        adapter = new historyItemAdapter(this,arrayList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
//        listHistory.setLayoutManager(layoutManager);
//        listHistory.setHasFixedSize(true);
//        listHistory.setAdapter(adapter);
//        readData();
//    }

    private void setEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.super.onBackPressed();
            }
        });
    }

    private void readData() {
        db.collection("Bill")
                .get()
                    .addOnCompleteListener(task -> {
                       if(task.isSuccessful()){
                           ArrayList<Bill> arrayBill = new ArrayList<>();
                           for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                               String userId = String.valueOf(queryDocumentSnapshot.getLong("user_id"));
                               if(!userId.equals(sessionUser.getUserId())){
                                   continue;
                               }
                               Bill bill = new Bill();
                               bill.setTotalBill(queryDocumentSnapshot.getString("totalBill"));
                               bill.setId_user(Integer.parseInt(sessionUser.getUserId()));
                               Date date = queryDocumentSnapshot.getDate("date");
                               SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                               String formattedDate = dateFormat.format(date);
                               bill.setDate(formattedDate);
                               bill.setCount(Integer.parseInt(queryDocumentSnapshot.getString("count")));
                               arrayBill.add(bill);
                           }
                           adapter = new historyItemAdapter(this, arrayBill);
                           LinearLayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
                           listHistory.setLayoutManager(layoutManager);
                           listHistory.setHasFixedSize(true);
                           listHistory.setAdapter(adapter);
                       }
                    });
    }
}