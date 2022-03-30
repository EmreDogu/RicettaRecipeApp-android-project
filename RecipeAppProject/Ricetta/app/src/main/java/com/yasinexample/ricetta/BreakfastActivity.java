package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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


//Emre Doğu
public class BreakfastActivity extends AppCompatActivity implements RecipeFragment.OnAdminRecipeListInteractionListener {

    ArrayList<Recipe> recipes = new ArrayList<Recipe>();
    Recipe editingRecipe;
    ListenerRegistration listenerRegistration;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

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
        ft.add(R.id.MealView, RecipeFragment.newInstance(), "list_recipes");
        ft.commit();

        checkCollection();
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
                    if (recipes.get(i).getType().equals("Kahvaltı")) {
                    } else {
                        recipes.remove(recipes.get(i));
                    }
                    i--;
                }
                RecipeFragment listFragment = (RecipeFragment) getSupportFragmentManager().findFragmentByTag("list_recipes");
                listFragment.updateRecipe(recipes);
            }
        });
    }

        @Override
        public void onAdminRecipeSelected (Recipe newrecipe){
            editingRecipe = newrecipe;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MealView, RecipeDescriptionFragment.newInstance(editingRecipe.getType(), editingRecipe.getMealName(), editingRecipe.getLink(), editingRecipe.getRecipe(), editingRecipe.getIngredients(), editingRecipe.getCalories(), editingRecipe.getCookingTime(), editingRecipe.getFavouriteState(), editingRecipe.getBasketState()), "show_recipe");
            ft.addToBackStack(null);
            ft.commit();
        }

        @Override
        public void onBackPressed () {
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
                Intent intent = new Intent(BreakfastActivity.this, BreakfastActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(BreakfastActivity.this, MealTimeActivity.class);
                startActivity(intent);
            }
        }

        private void saveRecipe (Recipe editingRecipe, String mealtype, String mealname, String link, String recipe, String ingredients, String calories, String cookingtime, String favouritestate, String basketstate){
            if (!editingRecipe.getFavouriteState().equals(favouritestate) || !editingRecipe.getBasketState().equals(basketstate)) {
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

        private void checkCollection() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.isEmpty()){
                        listenerRegistration = db.collection("recipes").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                    Recipe newrecipe = new Recipe();
                                    newrecipe.setId(recipes.get(i).getId());
                                    newrecipe.setType(recipes.get(i).getType());
                                    newrecipe.setMealName(recipes.get(i).getMealName());
                                    newrecipe.setLink(recipes.get(i).getLink());
                                    newrecipe.setRecipe(recipes.get(i).getRecipe());
                                    newrecipe.setIngredients(recipes.get(i).getIngredients());
                                    newrecipe.setCookingTime(recipes.get(i).getCookingTime());
                                    newrecipe.setCalories(recipes.get(i).getCalories());
                                    newrecipe.setFavouriteState(recipes.get(i).getFavouriteState());
                                    newrecipe.setBasketState(recipes.get(i).getBasketState());

                                    db.collection(email).document(newrecipe.getId()).set(newrecipe);
                                }
                            }
                        });
                    }
                }
            });
        }

        @Override
        protected void onStop () {
            super.onStop();
            listenerRegistration.remove();
        }
    }