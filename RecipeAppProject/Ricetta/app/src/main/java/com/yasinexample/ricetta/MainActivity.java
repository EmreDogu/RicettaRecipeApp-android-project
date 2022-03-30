package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//Yasin Orta

public class MainActivity extends AppCompatActivity {

    Button btnmealtime;
    Button btnsearchdishes;
    Button btnrecipeadding;
    Button btnbasket;
    Button btnuseracc;
    Button btnfav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnmealtime = (Button) findViewById(R.id.btnmealtime);
        btnsearchdishes = (Button) findViewById(R.id.btnsearchdishes);
        btnrecipeadding = (Button) findViewById(R.id.btnrecipeadding);
        btnfav = (Button) findViewById(R.id.btnfav);
        btnbasket = (Button) findViewById(R.id.btnbasket);
        btnuseracc = (Button) findViewById(R.id.btnuseracc);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

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

        btnmealtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MealTimeActivity.class);
                startActivity(intent);
            }
        });
        btnsearchdishes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        btnrecipeadding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecipeAddingActivity.class);
                startActivity(intent);
            }
        });
        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(intent);
            }
        });
        btnbasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });
        btnuseracc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });
    }


}