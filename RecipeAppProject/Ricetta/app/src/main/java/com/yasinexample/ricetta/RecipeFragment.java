package com.yasinexample.ricetta;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

//Emre Doğu
public class RecipeFragment extends Fragment {

    private OnAdminRecipeListInteractionListener mListener;
    RecyclerView recyclerView;

    public RecipeFragment(){}

    public static RecipeFragment newInstance() {
        return new RecipeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        if (view instanceof RecyclerView) {
            recyclerView  = (RecyclerView) view;
        }
        return  view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAdminRecipeListInteractionListener) {
            mListener = (OnAdminRecipeListInteractionListener)  context;
        } else {
            throw new RuntimeException(context.toString() + " must implement " + "OnAdminRecipeListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnAdminRecipeListInteractionListener {
        void onAdminRecipeSelected(Recipe item);
    }

    public void updateRecipe(List<Recipe> addr) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(getActivity(), addr, mListener));
    }
}