package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

//Emre Doğu
public class RecipeRecyclerViewAdapter extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> mValues;
    private RecipeFragment.OnAdminRecipeListInteractionListener mListener;
    private FragmentActivity activity;

    public RecipeRecyclerViewAdapter(FragmentActivity activity, List<Recipe> recipes, RecipeFragment.OnAdminRecipeListInteractionListener listener) {
        this.mValues = recipes;
        this.mListener = listener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String content = mValues.get(position).getMealName();
        String header = content.length() < 30 ? content : content.substring(0, 30);
        holder.mHeaderView.setText(header.replaceAll("\n", " "));
        if (activity instanceof RecipeAddingActivity) {
            holder.mDateView.setText((new SimpleDateFormat("yyyy-MM-dd")).format(mValues.get(position).getDate().toDate()));
        }
        holder.mView.setOnClickListener(view -> {
            if (mListener != null) {mListener.onAdminRecipeSelected(holder.mItem); }
        });
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.YELLOW);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mHeaderView;
        public final TextView mDateView;
        public Recipe mItem;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.mView = view;
            mHeaderView = view.findViewById(R.id.recipe_header);
            mDateView = view.findViewById(R.id.recipe_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mHeaderView.getText() + "'";
        }
    }
}