package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.User;

public class AccountActivity extends AppCompatActivity {
    private FoodDBHelper db;
    private ImageButton btnBack;
    private Button btnUpdate;
    SessionUser sessionUser;
    private TextView tvName, tvAddress, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setVariable();
        setEvent();

        db = new FoodDBHelper(this);
        int userId = Integer.parseInt(sessionUser.getUserId());

        tvName = findViewById(R.id.tvName);
        tvAddress = findViewById(R.id.tvAddress);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        btnUpdate = findViewById(R.id.button_update);

        User user = db.getUserById(userId);
        if (user != null) {
            String name = user.getName();
            String address = user.getAddress();
            String email = user.getEmail();
            String phone = user.getPhone();

            tvName.setText(name);
            tvAddress.setText(address);
            tvEmail.setText(email);
            tvPhone.setText(phone);
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = tvName.getText().toString().trim();
                String address = tvAddress.getText().toString().trim();
                String phone = tvPhone.getText().toString().trim();
                String email = tvEmail.getText().toString().trim();

                if(name.equals("")||address.equals("")||email.equals("")||phone.equals("")){
                    Toast.makeText(AccountActivity.this, "Dữ liệu không được để trống", Toast.LENGTH_SHORT).show();
                }else {
                    Boolean checkPhone = db.checkPhone(phone);
                    Boolean checkEmail = db.checkEmail(email);
                    if(checkEmail == false){
                        if (checkPhone == false){
                            Boolean update = db.updateUser(userId,name,address,email,phone);
                            if(update==true){
                                Toast.makeText(AccountActivity.this, "Cập nhật thông tin tài khoản thành công !", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AccountActivity.this, "Cập nhật thông tin tài khoản không thành công !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else {
                        Toast.makeText(AccountActivity.this, "Email đã được đăng ký. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });






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