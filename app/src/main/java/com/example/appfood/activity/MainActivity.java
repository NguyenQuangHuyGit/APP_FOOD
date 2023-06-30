package com.example.appfood.activity;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.Session.SessionCart;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Food;
import com.example.appfood.model.SMS;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Food> arrayFood;
    List<Integer> foodIds;
    RecyclerView listFood;
    DrawerLayout drawerLayout;
    foodItemAdapter adapter;
    FoodDBHelper foodDB;
    ViewFlipper viewFlipper;
    Toolbar toolbar;
    ImageButton btnNotify;
    LinearLayout location;
    FloatingActionButton btnCart;
    NavigationView navigationView;
    SessionUser sessionUser;
    SessionCart sessionCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestNotificationPermission();
        setVariable();
        setNavigation();
        setViewFlipper();
        setRecycleView();
        setEvent();
        setUser();
        setBtnCart();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        sessionUser.clear();
        sessionCart.clear();
    }

    private void setUser(){
        String userId = sessionUser.getUserId();
        if(userId != null){
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.btnLogin).setVisible(false);
            menu.findItem(R.id.btnRegister).setVisible(false);
            menu.findItem(R.id.btnLogout).setVisible(true);
            menu.findItem(R.id.btnAccount).setVisible(true);
            menu.findItem(R.id.btnHistory).setVisible(true);
        }
    }

    private void setRecycleView(){
        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,2);
        listFood.setLayoutManager(layoutManager);
        listFood.setHasFixedSize(true);
        arrayFood = foodDB.getAllFoods();
        adapter = new foodItemAdapter(MainActivity.this,arrayFood);
        listFood.setAdapter(adapter);
    }

    private void setVariable(){
        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        btnCart = (FloatingActionButton) findViewById(R.id.btnCart);
        location = (LinearLayout) findViewById(R.id.location);
        foodDB = new FoodDBHelper(this);
        foodDB.insertFood();
        listFood = (RecyclerView) findViewById(R.id.listFood);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        btnNotify = findViewById(R.id.notify_ic);
        sessionUser = new SessionUser(this);
        sessionCart = new SessionCart(this);
    }

    private void setNavigation(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.navigation_ic);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void setViewFlipper(){
        List<Integer> arrayList = new ArrayList<>();
        arrayList.add(R.drawable.ads1);
        arrayList.add(R.drawable.ads2);
        arrayList.add(R.drawable.ads3);
        arrayList.add(R.drawable.ads4);
        for(int i =0; i<arrayList.size(); i++){
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(arrayList.get(i));
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out);
        viewFlipper.setOutAnimation(slide_in);
        viewFlipper.setOutAnimation(slide_out);
    }

    private void setBtnCart(){
        if(sessionCart.getCart() == null){
            btnCart.setVisibility(View.INVISIBLE);
        }else{
            foodIds = sessionCart.getCart().getItemIds();
            if (foodIds.size() == 0) {
                btnCart.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void requestNotificationPermission() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Yêu cầu quyền gửi thông báo");
            builder.setIcon(R.drawable.app_logo);
            builder.setMessage("Chúng tôi muốn gửi thông báo cho bạn. Hãy cấp quyền đi mà!!");
            builder.setPositiveButton("Cấp quyền", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Open the app-specific notification settings screen
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, MainActivity.this.getPackageName());
                    MainActivity.this.startActivity(intent);
                }
            });
            builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    private void setEvent(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotifyActivity.class);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.btnLogin){
                    Intent login = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(login);
                    return true;
                }
                if(itemId == R.id.btnRegister){
                    Intent signup = new Intent(MainActivity.this, SignupActivity.class);
                    startActivity(signup);
                    return true;
                }
                if(itemId == R.id.btnAccount){
                    Intent account = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(account);
                    return true;
                }
                if(itemId == R.id.btnLogout){
                    sessionUser.clear();
                    sessionCart.clear();
                    recreate();
                    return true;
                }
                if(itemId == R.id.btnShutdown){
                    finishAffinity();
                    return true;
                }
                if(itemId == R.id.btnHistory){
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }
}