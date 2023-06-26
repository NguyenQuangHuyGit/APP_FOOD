package com.example.appfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appfood.R;
import com.example.appfood.adapter.foodItemAdapter;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    List<Food> arrayFood;
    RecyclerView listFood;
    DrawerLayout drawerLayout;
    foodItemAdapter adapter;
    FoodDBHelper foodDB;
    ViewFlipper viewFlipper;
    Toolbar toolbar;
    FloatingActionButton btnCart;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariable();
        setNavigation();
        setViewFlipper();
        setRecycleView();
        setEvent();
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
        foodDB = new FoodDBHelper(this);
        foodDB.insertFood();
        listFood = (RecyclerView) findViewById(R.id.listFood);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
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

    private void setEvent(){
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.btnLogin){
                    Toast toast = Toast.makeText(getApplicationContext(),"Login",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                if(itemId == R.id.btnRegister){
                    Toast toast = Toast.makeText(getApplicationContext(),"Register",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                if(itemId == R.id.btnAccount){
                    Toast toast = Toast.makeText(getApplicationContext(),"Account",Toast.LENGTH_SHORT);
                    toast.show();
                    return true;
                }
                if(itemId == R.id.btnShutdown){
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
}