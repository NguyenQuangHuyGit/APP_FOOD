package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appfood.R;
import androidx.annotation.NonNull;

import com.example.appfood.database.FoodDBHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class VerificationActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText txtCode;
    String code;
    String phone;
    String phoneFormat;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    Intent intent;
    ImageButton btnSubmit;
    Button btnResend;
    Long timeout = 60L;
    TextView txtTimeout;
    ImageButton btnBack;
    FoodDBHelper db;
    String email;
    String address;
    String password;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        setVariable();
        sendSMS("+84"+phoneFormat,false);
        setEvent();
    }

    private void setVariable(){
        txtCode = (EditText) findViewById(R.id.txtCode);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        phone = bundle.getString("phone");
        name = bundle.getString("name");
        address = bundle.getString("address");
        email = bundle.getString("email");
        password = bundle.getString("password");
        phoneFormat = phone.substring(1);
        btnSubmit = (ImageButton) findViewById(R.id.btnSubmit);
        btnResend = (Button) findViewById(R.id.btnResend);
        txtTimeout = (TextView) findViewById(R.id.txtTimeout);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        db = new FoodDBHelper(this);
    }

    private void sendSMS(String phoneNumber, boolean isResend){
        startResendTimer();
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeout, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerificationActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    code = s;
                    resendingToken = forceResendingToken;
                    Toast.makeText(VerificationActivity.this, "Mã OTP xác nhận đã được gửi!", Toast.LENGTH_SHORT).show();
                }
            });
        if(isResend){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(resendingToken).build());
        }else{
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }

    private void signIn(PhoneAuthCredential phoneAuthCredential){
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Boolean insert = db.insertUser(name,address,email,phone,password);
                    if(insert){
                        Intent intent = new Intent(VerificationActivity.this,LoginActivity.class);
                        intent.putExtra("phone",phone);
                        startActivity(intent);
                        Toast.makeText(VerificationActivity.this, "Đăng kí tài khoản thành công", Toast.LENGTH_SHORT).show();
                        sendRegistrationSuccessEmail(email);
                    }else {
                        Toast.makeText(VerificationActivity.this, "Lỗi hệ thống vui lòng thử lại", Toast.LENGTH_SHORT).show();
                        VerificationActivity.super.onBackPressed();
                    }
                }else{
                    Toast.makeText(VerificationActivity.this, "Mã OTP không trùng khớp", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void startResendTimer(){
        btnResend.setEnabled(false);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                --timeout;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtTimeout.setText("Gửi lại sau "+timeout+"s");
                    }
                });
                if(timeout <= 0){
                    timeout = 60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btnResend.setEnabled(true);
                        }
                    });
                }
            }
        },0,1000);
    }

    private void sendRegistrationSuccessEmail(String email){
        try {
            String stringSenderEmail = "hungb.z98@gmail.com";
            String stringReceiverEmail = email;
            String stringPasswordSenderEmail = "xtnmyflmidrgxiif";
            String stringHost = "smtp.gmail.com";

            Properties properties = System.getProperties();
            properties.put("mail.smtp.host", stringHost);
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.ssl.enable", "true");
            properties.put("mail.smtp.auth", "true");

            javax.mail.Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
                }
            });

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(stringReceiverEmail));

            mimeMessage.setSubject("[Food HVH] Đăng ký tài khoản thành công!");
            mimeMessage.setText("Xin chào quý khách, \n\nChúc mừng quý khách đã đăng ký thành công tài khoản app food HVH. \nChúc quý khách có những bữa ăn ngon miệng!\n\nTrân trọng.");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mimeMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();

        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void setEvent(){
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = txtCode.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code,otp);
                signIn(credential);
            }
        });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS("+84"+phoneFormat,true);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificationActivity.super.onBackPressed();
            }
        });
    }
}