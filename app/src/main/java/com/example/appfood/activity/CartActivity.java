package com.example.appfood.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
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
import com.example.appfood.model.Bill;
import com.example.appfood.model.Cart;
import com.example.appfood.model.DetailBill;
import com.example.appfood.model.Food;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ncorti.slidetoact.SlideToActView;

import java.security.Key;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
//            Date date = new Date();
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
                        writeData();
//                        foodDB.insertBill(txtTotal.getText().toString(), date,Integer.parseInt(txtCount.getText().toString()), Integer.parseInt(sessionUser.getUserId()));
//                        int idBill = foodDB.getLastInsertId();
//                        for(int i=0;i<arrayFood.size();i++){
//                            int idFood = arrayFood.get(i).getId();
//                            foodDB.insertDetailBill(idBill,idFood,itemCarts.get(idFood));
//                        }
                        sessionCart.clear();
                        vibrate(CartActivity.this);
                        Intent intent = new Intent(CartActivity.this,MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(CartActivity.this,"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                        sendNotification(CartActivity.this,"Đơn hàng đang được chuẩn bị. Nhớ lưu ý điện thoại nhé!");
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

    public static void sendNotification(Context context, String content) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setAction("NOTIFICATION_CLICK_ACTION");
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        // Create a notification builder with your custom content
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "appfood_chanel")
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle("Đặt hàng thành công rồi!")
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("appfood_chanel", "My App Channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = builder.build();
        notificationManager.notify(1, notification);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void writeData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date date = new Date();
        foodDB.insertBill(txtTotalBill.getText().toString(), date,Integer.parseInt(txtCount.getText().toString()), Integer.parseInt(sessionUser.getUserId()));
        int idBill = foodDB.getLastInsertIdBill();
        Map<String, Object> bill = new HashMap<>();
        bill.put("totalBill", txtTotalBill.getText().toString());
        bill.put("date", date);
        bill.put("count", txtCount.getText().toString());
        bill.put("user_id",Integer.parseInt(sessionUser.getUserId()));
        db.collection("Bill").document(String.valueOf(idBill)).set(bill);

        for(int i=0;i<arrayFood.size();i++){
            int idFood = arrayFood.get(i).getId();
            foodDB.insertDetailBill(idBill,idFood,itemCarts.get(idFood));
            int id = foodDB.getLastInsertIdDetailBill();
            Map<String, Object> detailBill = new HashMap<>();
            detailBill.put("id_food", idFood);
            detailBill.put("id_bill", idBill);
            detailBill.put("count", itemCarts.get(idFood));
            db.collection("DetailBill").document(String.valueOf(id)).set(detailBill);
        }
//        foodDB.insertBill(txtTotalBill.getText().toString(), date,Integer.parseInt(txtCount.getText().toString()), Integer.parseInt(sessionUser.getUserId()));
//        int idBill = foodDB.getLastInsertIdBill();
//        Bill bill = new Bill();
//        bill.setId_user(Integer.parseInt(sessionUser.getUserId()));
//        bill.setTotalBill(txtTotalBill.getText().toString());
//        bill.setDate(date.toString());
//        bill.setCount(Integer.parseInt(txtCount.getText().toString()));
//        mDatabase = FirebaseDatabase.getInstance("https://appfood-391114-default-rtdb.asia-southeast1.firebasedatabase.app/");
//        DatabaseReference billRef =  mDatabase.getReference("Bill");
//        billRef.child("id_bill").child(String.valueOf(idBill)).setValue(bill);
//        DatabaseReference detailbillRef =  mDatabase.getReference("DetailBill");
//        for(int i=0;i<arrayFood.size();i++){
//            int idFood = arrayFood.get(i).getId();
//            foodDB.insertDetailBill(idBill,idFood,itemCarts.get(idFood));
//            int id = foodDB.getLastInsertIdDetailBill();
//            DetailBill detailBill = new DetailBill();
//            detailBill.setId(idBill);
//            detailBill.setCount(itemCarts.get(idFood));
//            detailBill.setId_food(idFood);
//            detailbillRef.child(String.valueOf(id)).setValue(detailBill);
//        }
    }

}