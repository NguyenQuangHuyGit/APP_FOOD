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
                String user = binding.loginPhoneEmail.getText().toString();
                String password = binding.loginPassword.getText().toString();

                if(user.equals("")||password.equals(""))
                    Toast.makeText(LoginActivity.this, "Bắt buộc phải nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                else{
                    int checkPhone = db.checkPhonePassword(user, password);
                    int checkEmail = db.checkEmailPassword(user, password);
                    if(checkPhone != 0 || checkEmail != 0){
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        String id1 = String.valueOf(checkPhone);
                        String id2 = String.valueOf(checkEmail);
                        sessionUser.setUserId(id1);
                        sessionUser.setUserId(id2);
                        Intent intent  = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(LoginActivity.this, "Thông tin không đúng. Vui lòng kiểm tra lại!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        binding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                startActivity(intent);
            }
        });


    }
}