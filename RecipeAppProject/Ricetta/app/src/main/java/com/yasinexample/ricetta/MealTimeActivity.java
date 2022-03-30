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
public class MealTimeActivity extends AppCompatActivity {

    Button btnbreakfast;
    Button btnlunch;
    Button btndinner;
    Button btndeserts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_time);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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

        btnbreakfast = (Button) findViewById(R.id.btnbreakfast);
        btnlunch = (Button) findViewById(R.id.btnlunch);
        btndinner = (Button) findViewById(R.id.btndinner);
        btndeserts = (Button) findViewById(R.id.btndeserts);

        btnbreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTimeActivity.this,BreakfastActivity.class);
                startActivity(intent);
            }
        });
        btnlunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTimeActivity.this,LunchActivity.class);
                startActivity(intent);
            }
        });
        btndinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MealTimeActivity.this,DinnerActivity.class);
                startActivity(intent);
            }
        });
        btndeserts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MealTimeActivity.this, DessertsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MealTimeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}