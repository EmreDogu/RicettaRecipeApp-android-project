package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

//Yasin Orta
public class AccountActivity extends AppCompatActivity {

    FirebaseAuth mAuth;

    Button btnLogOut;
    Button btndeleteacc;
    Button btnchangepass;
    TextView textViewUsername;
    EditText textpass;
    EditText textpassverify;

    ArrayList<UserName> usernames = new ArrayList<UserName>();
    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    ListenerRegistration listenerRegistration;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.account);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        return true;
                    case R.id.favourites:
                        startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));
                        return true;
                    case R.id.basket:
                        startActivity(new Intent(getApplicationContext(), BasketActivity.class));
                        return true;
                    case R.id.account:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        return true;
                }
                return false;
            }
        });

        btnLogOut = findViewById(R.id.btnLogout);
        btnchangepass = findViewById(R.id.btnchangepassword);
        btndeleteacc = findViewById(R.id.btndeleteacc);
        textViewUsername = findViewById(R.id.textViewUsername);
        textpass = findViewById(R.id.textpass);
        textpassverify = findViewById(R.id.textpassverify);
        mAuth = FirebaseAuth.getInstance();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listenerRegistration = db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.e("Firebase", "Error retrieving recipes");
                    return;
                }
                usernames.clear();
                for (QueryDocumentSnapshot doc : value) {
                    usernames.add(doc.toObject(UserName.class));
                }
                int i = usernames.size() - 1;
                while (i >= 0) {
                    if (usernames.get(i).getUserid().equals(user.getUid())) {
                        textViewUsername.setText(usernames.get(i).getFirstname() + " " + usernames.get(i).getLastname());
                    }
                    i--;
                }
            }
        });

        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            Toast.makeText(AccountActivity.this, "Başarıyla hesabınızdan çıkış yaptınız", Toast.LENGTH_LONG).show();
            startActivity(new Intent(AccountActivity.this, LoginActivity.class));
        });

        btnchangepass.setOnClickListener(view ->{
            if (textpass.getText().toString().equals(textpassverify.getText().toString()) && textpass.getText().toString().length() >= 6) {
                user.updatePassword(textpass.getText().toString());
                mAuth.signOut();
                Toast.makeText(AccountActivity.this, "Şifreniz başarıyla değişti", Toast.LENGTH_LONG).show();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
            }else{
                Toast.makeText(AccountActivity.this, "Yazdığınız şifreler eşleşmiyor, 6 harften küçük veya ikisi birden.", Toast.LENGTH_LONG).show();
            }
        });

        btndeleteacc.setOnClickListener(view ->{
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Emin misiniz?");
            builder.setMessage("Eğer evete basarsanız hesabınız kalıcı olarak silinecek.");

            builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    user.delete();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection(email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {

                            }else {
                                db.collection(email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        listenerRegistration = db.collection(email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                                                if (error != null) {
                                                    Log.e("Firebase", "Error retrieving recipes");
                                                    return;
                                                }
                                                recipes.clear();
                                                for (QueryDocumentSnapshot doc : value) {
                                                    recipes.add(doc.toObject(Recipe.class));
                                                }
                                                for (int i = 0;i< recipes.size();i++) {
                                                    db.collection(email).document(recipes.get(i).getId()).delete();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

                    db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                            }else {
                                db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        listenerRegistration = db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                if (error != null) {
                                                    Log.e("Firebase", "Error retrieving users");
                                                    return;
                                                }
                                                usernames.clear();
                                                for (QueryDocumentSnapshot doc : value) {
                                                    usernames.add(doc.toObject(UserName.class));
                                                }
                                                for (int i = 0;i< usernames.size();i++) {
                                                    db.collection("Users").document(usernames.get(i).getUserid()).delete();
                                                }
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });

                    mAuth.signOut();
                    Toast.makeText(AccountActivity.this, "Hesabınız başarıyla silinmiştir.", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                }
            });

            builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog ad  = builder.create();
            ad.show();
        });
    }
}