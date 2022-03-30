package com.yasinexample.ricetta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

//Yasin Orta
public class RegisterActivity extends AppCompatActivity {

    TextInputEditText etRegFirstname;
    TextInputEditText etRegLastname;
    TextInputEditText etRegEmail;
    TextInputEditText etRegPassword;
    TextView tvLoginHere;
    Button btnRegister;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegFirstname = findViewById(R.id.etRegFirstname);
        etRegLastname = findViewById(R.id.etRegLastname);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPass);
        tvLoginHere = findViewById(R.id.tvLoginHere);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(view -> {
            String firstname = etRegFirstname.getText().toString();
            String lastname = etRegLastname.getText().toString();
            String email = etRegEmail.getText().toString();
            String password = etRegPassword.getText().toString();
            createUser(firstname, lastname, email, password);
        });

        tvLoginHere.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    public void createUser(String firstname, String lastname, String email, String password) {
        if (TextUtils.isEmpty(firstname)) {
            etRegEmail.setError("İsim boş olamaz");
            etRegEmail.requestFocus();
        } else if (TextUtils.isEmpty(lastname)) {
            etRegPassword.setError("Soy isim boş olamaz");
            etRegPassword.requestFocus();
        } else if (TextUtils.isEmpty(email)) {
            etRegPassword.setError("Email boş olamaz");
            etRegPassword.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            etRegPassword.setError("Şifre boş olamaz");
            etRegPassword.requestFocus();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        UserName username = new UserName(task.getResult().getUser().getUid(), firstname, lastname);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users").document(task.getResult().getUser().getUid()).set(username);
                        Toast.makeText(RegisterActivity.this, "Hesabınız başarıyla kaydedildi", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Registration Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}