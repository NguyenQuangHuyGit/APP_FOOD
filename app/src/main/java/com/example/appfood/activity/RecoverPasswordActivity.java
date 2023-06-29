package com.example.appfood.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appfood.R;
import com.example.appfood.database.FoodDBHelper;
import com.example.appfood.model.User;

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

public class RecoverPasswordActivity extends AppCompatActivity {
    private Button buttonBack;
    private Button buttonSend;
    private EditText editTextEmail;
    private FoodDBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        db = new FoodDBHelper(this);

        buttonBack = findViewById(R.id.button_back);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSend = findViewById(R.id.send_button);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()){
                    String email = editTextEmail.getText().toString().trim();
                    Boolean checkEmail = db.checkEmail(email);
                    if (checkEmail == true){
                        User user = db.getUserByEmail(email);
                        String password = user.getPassword();
                        sendForgotPassword(email,password);
                        Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        Toast.makeText(RecoverPasswordActivity.this, "Yêu cầu gửi lại mật khẩu thành công !", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(RecoverPasswordActivity.this, "Email chưa được đăng ký. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean validateData() {
        String email = editTextEmail.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Vui lòng nhập email !");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Vui lòng nhập email hợp lệ !");
            return false;
        }

        return true;
    }

    private void sendForgotPassword(String email, String password){
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

            mimeMessage.setSubject("[Food HVH] Gửi lại mật khẩu");
            mimeMessage.setText("Xin chào quý khách, \n\nĐây là mật khẩu quý khách yêu cầu :"+ password +"\nQuý khách lên bảo mật thông tin mật khẩu!\n\nTrân trọng.");

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
}