package com.yasinexample.ricetta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

//Emre Doğu
public class RecipeAddingActivity extends AppCompatActivity implements RecipeFragment.OnAdminRecipeListInteractionListener {

    boolean displayEditor = false;
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

        if (!displayEditor) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.MealView, RecipeFragment.newInstance(), "list_recipes");
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.MealView, EditRecipeFragment.newInstance(editingRecipe.getType(), editingRecipe.getMealName(), editingRecipe.getLink(), editingRecipe.getRecipe(), editingRecipe.getIngredients(), editingRecipe.getCalories(), editingRecipe.getCookingTime(), editingRecipe.getFavouriteState(), editingRecipe.getBasketState(), "false"));
            ft.addToBackStack(null);
            ft.commit();
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listenerRegistration = db.collection(email).orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                Log.d("Email:", email);
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
                    if (recipes.get(i).getDate() == null) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_new).setVisible(!displayEditor);
        menu.findItem(R.id.action_close).setVisible(displayEditor);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        displayEditor = !displayEditor;
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.action_new:
                editingRecipe = createRecipe();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.MealView, EditRecipeFragment.newInstance("", "", "", "", "", "", "","false","false", "false"), "edit_recipe");
                ft.addToBackStack(null);
                ft.commit();
                return true;
            case R.id.action_close:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Recipe createRecipe() {
        Recipe newrecipe = new Recipe();
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
        String id = db.collection(email).document().getId();
        newrecipe.setId(id);
        newrecipe.setBasketState("false");
        newrecipe.setFavouriteState("false");
        return newrecipe;
    }

    @Override
    public void onAdminRecipeSelected(Recipe newrecipe) {
        editingRecipe = newrecipe;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MealView, EditRecipeFragment.newInstance(editingRecipe.getType(), editingRecipe.getMealName(), editingRecipe.getLink(), editingRecipe.getRecipe(), editingRecipe.getIngredients(), editingRecipe.getCalories(), editingRecipe.getCookingTime(), editingRecipe.getFavouriteState(), editingRecipe.getBasketState(), "false" ), "edit_recipe");
        ft.addToBackStack(null);
        ft.commit();

        displayEditor = !displayEditor;
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        EditRecipeFragment editRecipeFragment = (EditRecipeFragment) getSupportFragmentManager().findFragmentByTag("edit_recipe");
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
            String delete = editRecipeFragment.getDeleteState();
            if (delete == "true") {
                deleteRecipe(editingRecipe);
                Intent intent = new Intent(RecipeAddingActivity.this, RecipeAddingActivity.class);
                startActivity(intent);
            }
            saveRecipe(editingRecipe, mealtype, mealname, link, recipe, ingredients, calories, cookingtime, favouritestate, basketstate);
            Intent intent = new Intent(RecipeAddingActivity.this, RecipeAddingActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(RecipeAddingActivity.this, MainActivity.class);
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
            editingRecipe.setDate(new Timestamp(new Date()));
            Toast.makeText(RecipeAddingActivity.this, "Tarifiniz kaydedildi", Toast.LENGTH_LONG).show();
            db.collection(email).document(editingRecipe.getId()).set(editingRecipe);
        }
    }

    public void deleteRecipe(Recipe editingRecipe) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(email).document(editingRecipe.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RecipeAddingActivity.this, "Tarifiniz silindi", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        listenerRegistration.remove();
    }
}

