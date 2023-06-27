package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.Session.SessionUser;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    FoodDBHelper db;
    ActivityLoginBinding binding;
    SessionUser sessionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = new FoodDBHelper(this);
        sessionUser = new SessionUser(this);
        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = binding.loginPhone.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if(phone.equals("")||password.equals(""))
                    Toast.makeText(LoginActivity.this, "Bắt buộc phải nhập dữ liệu", Toast.LENGTH_SHORT).show();
                else{
                    int checkCredentials = db.checkPhonePassword(phone, password);
                    if(checkCredentials != 0){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        String userId = String.valueOf(checkCredentials);
                        sessionUser.setUserId(userId);
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Thông tin không đúng", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}