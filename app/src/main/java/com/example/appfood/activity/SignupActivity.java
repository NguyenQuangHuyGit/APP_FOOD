package com.example.appfood.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.databinding.ActivitySignupBinding;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SignupActivity extends AppCompatActivity {
    private FoodDBHelper db;
    private ActivitySignupBinding binding;
    private EditText etName, etAddress, etEmail, etPhone, etPassword, etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new FoodDBHelper(this);

        etName = findViewById(R.id.signup_name);
        etAddress = findViewById(R.id.signup_address);
        etEmail = findViewById(R.id.signup_email);
        etPhone = findViewById(R.id.signup_phone);
        etPassword = findViewById(R.id.signup_password);
        etConfirmPassword = findViewById(R.id.signup_confirm);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    String name = binding.signupName.getText().toString().trim();
                    String address = binding.signupAddress.getText().toString().trim();
                    String email = binding.signupEmail.getText().toString().trim();
                    String phone = binding.signupPhone.getText().toString().trim();
                    String password = binding.signupPassword.getText().toString().trim();
                    String confirmPassword = binding.signupConfirm.getText().toString().trim();

                    if(name.equals("")||address.equals("")||email.equals("")||phone.equals("")||password.equals("")||confirmPassword.equals(""))
                        Toast.makeText(SignupActivity.this, "Dữ liệu không được để trống. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                    else{
                        if(password.equals(confirmPassword)){
                            Boolean checkPhone = db.checkPhone(phone);
                            Boolean checkEmail = db.checkEmail(email);
                            if(checkEmail == false){
                                if(checkPhone == false){
                                    Intent intent = new Intent(SignupActivity.this,VerificationActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name",name);
                                    bundle.putString("address",address);
                                    bundle.putString("email",email);
                                    bundle.putString("phone",phone);
                                    bundle.putString("password",password);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(SignupActivity.this, "Số điện thoại đã được đăng ký. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(SignupActivity.this, "Email đã được đăng ký. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignupActivity.this, "Mật khẩu nhập lại không đúng. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        binding.loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private boolean validateData() {
        String name = etName.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên !");
            return false;
        }

        if (address.isEmpty()) {
            etAddress.setError("Vui lòng nhập địa chỉ !");
            return false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Vui lòng nhập email !");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Vui lòng nhập email hợp lệ !");
            return false;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Vui lòng nhập số điện thoại !");
            return false;
        } else if (!Pattern.matches("[0-9]+", phone)) {
            etPhone.setError("Vui lòng nhập số điện thoại hợp lệ !");
            return false;
        }else if (phone.length() < 10) {
            etPhone.setError("Số điện thoại không đúng định dạng !");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Vui lòng nhập mật khẩu !");
            return false;
        } else if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự !");
            return false;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("Vui lòng nhập lại mật khẩu !");
            return false;
        } else if (!confirmPassword.equals(password)) {
            etConfirmPassword.setError("Mật khẩu nhập lại không khớp !");
            return false;
        }
        return true;
    }
}














