package com.yasinexample.ricetta;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

//Yasin Orta
public class BasketFragment extends Fragment {
    private TextView textViewCal;
    private TextView textViewCook;
    private TextView textViewTotalCal;
    private TextView textViewTotalCook;
    private Integer calories = 0;
    private Integer time = 0;

    public BasketFragment() {
    }

    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false);
        textViewTotalCal = view.findViewById(R.id.textViewTotalCal);
        textViewTotalCook = view.findViewById(R.id.textViewTotalCook);
        textViewCal = view.findViewById(R.id.textViewCal);
        textViewCook = view.findViewById(R.id.textViewCook);
        return view;
    }


    public void updateNumbers(List<Recipe> addr) {
        for (int i =0;i<addr.size();i++) {
            calories += Integer.parseInt(addr.get(i).getCalories());
            time += Integer.parseInt(addr.get(i).getCookingTime());
        }
        textViewCal.setText(calories.toString() + " Kalori");
        textViewCook.setText(time.toString() + " dakika");
        textViewTotalCal.setText("Toplam Kalori Miktarı:");
        textViewTotalCook.setText("Toplam Pişirme Süresi:");
    }
}