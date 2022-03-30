package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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
public class BasketActivity extends AppCompatActivity implements RecipeFragment.OnAdminRecipeListInteractionListener {

    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    FragmentContainerView a;
    Recipe editingRecipe;
    ListenerRegistration listenerRegistration;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.basket);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        onBackPressed();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.search:
                        onBackPressed();
                        startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                        return true;
                    case R.id.favourites:
                        onBackPressed();
                        startActivity(new Intent(getApplicationContext(), FavouritesActivity.class));
                        return true;
                    case R.id.basket:
                        onBackPressed();
                        startActivity(new Intent(getApplicationContext(), BasketActivity.class));
                        return true;
                    case R.id.account:
                        onBackPressed();
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        return true;
                }
                return false;
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.BasketView, RecipeFragment.newInstance(), "list_recipes");
        ft.add(R.id.BasketStuffView, BasketFragment.newInstance(), "basketlist_recipes");
        ft.commit();


        FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                int i = recipes.size() - 1;
                while (i >= 0) {
                    if (recipes.get(i).getBasketState().equals("true")) {
                    } else {
                        recipes.remove(recipes.get(i));
                    }
                    i--;
                }
                RecipeFragment listFragment = (RecipeFragment) getSupportFragmentManager().findFragmentByTag("list_recipes");
                BasketFragment basketlistFragment = (BasketFragment) getSupportFragmentManager().findFragmentByTag("basketlist_recipes");
                listFragment.updateRecipe(recipes);
                basketlistFragment.updateNumbers(recipes);
            }
        });
    }

    @Override
    public void onAdminRecipeSelected(Recipe newrecipe) {
        editingRecipe = newrecipe;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.BasketView, RecipeDescriptionFragment.newInstance(editingRecipe.getType(), editingRecipe.getMealName(), editingRecipe.getLink(), editingRecipe.getRecipe(), editingRecipe.getIngredients(), editingRecipe.getCalories(), editingRecipe.getCookingTime(), editingRecipe.getFavouriteState(), editingRecipe.getBasketState()), "show_recipe");
        ft.replace(R.id.BasketStuffView, BasketFragment.newInstance());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        RecipeDescriptionFragment editRecipeFragment = (RecipeDescriptionFragment) getSupportFragmentManager().findFragmentByTag("show_recipe");
        if (editRecipeFragment != null) {
            String mealtype = editRecipeFragment.getMealType();
            String mealname = editRecipeFragment.getMealName();
            String link = editRecipeFragment.getLink();
            String recipe = editRecipeFragment.getRecipe();
            String ingredients = editRecipeFragment.getIngredients();
            String calories = editRecipeFragment.getCalories();
            String cookingtime = editRecipeFragment.getCookingTime();
            String favouritestate = editRecipeFragment.getFavouriteState();
            String basketstate = editRecipeFragment.getBasketState();
            saveRecipe(editingRecipe, mealtype, mealname, link, recipe, ingredients, calories, cookingtime, favouritestate, basketstate);
            Intent intent = new Intent(BasketActivity.this, BasketActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(BasketActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void saveRecipe(Recipe editingRecipe, String mealtype, String mealname, String link, String recipe, String ingredients, String calories, String cookingtime, String favouritestate, String basketstate) {
        if (editingRecipe.getRecipe() == null || !editingRecipe.getType().equals(mealtype) || !editingRecipe.getMealName().equals(mealname) || !editingRecipe.getLink().equals(link) || !editingRecipe.getRecipe().equals(recipe) || !editingRecipe.getIngredients().equals(ingredients) || !editingRecipe.getCalories().equals(calories) || !editingRecipe.getCookingTime().equals(cookingtime) || !editingRecipe.getFavouriteState().equals(favouritestate) || !editingRecipe.getBasketState().equals(basketstate)) {
            editingRecipe.setType(mealtype);
            editingRecipe.setMealName(mealname);
            editingRecipe.setLink(link);
            editingRecipe.setRecipe(recipe);
            editingRecipe.setIngredients(ingredients);
            editingRecipe.setCalories(calories);
            editingRecipe.setCookingTime(cookingtime);
            editingRecipe.setFavouriteState(favouritestate);
            editingRecipe.setBasketState(basketstate);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection(email).document(editingRecipe.getId()).set(editingRecipe);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}

