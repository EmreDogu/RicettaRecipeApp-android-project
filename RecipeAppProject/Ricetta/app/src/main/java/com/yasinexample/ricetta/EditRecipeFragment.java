package com.yasinexample.ricetta;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//Emre Doğu
public class EditRecipeFragment extends Fragment {

    private static ArrayList<String> argsarraylist = new ArrayList<String>();
    private boolean value = false;
    private Button btnchangelink;
    private String mealtype;
    private String mealname;
    private String link;
    private String recipe;
    private String ingredients;
    private String calories;
    private String cookingtime;
    private String favouritestate;
    private String basketstate;
    private String delete;
    private EditText txtMealType;
    private EditText txtMealName;
    private EditText txtLink;
    private EditText txtRecipe;
    private EditText txtIngredients;
    private EditText txtCalories;
    private EditText txtCookingTime;
    private ImageView imgView;
    private ImageButton favourite_button;
    private ImageButton basket_button;
    private ImageButton delete_button;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static EditRecipeFragment newInstance(String type, String name, String link, String recipedesc, String ingredients, String calories, String time, String favourite, String basket, String delete) {
        EditRecipeFragment fragment = new EditRecipeFragment();
        Bundle args = new Bundle();
        argsarraylist.add(type);
        argsarraylist.add(name);
        argsarraylist.add(link);
        argsarraylist.add(recipedesc);
        argsarraylist.add(ingredients);
        argsarraylist.add(calories);
        argsarraylist.add(time);
        argsarraylist.add(favourite);
        argsarraylist.add(basket);
        argsarraylist.add(delete);
        args.putStringArrayList("RecipeList", argsarraylist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_recipe, container, false);

        favouritestate = (getArguments().getStringArrayList("RecipeList")).get(7);
        basketstate = (getArguments().getStringArrayList("RecipeList")).get(8);
        delete = (getArguments().getStringArrayList("RecipeList")).get(9);

        btnchangelink = (Button) view.findViewById(R.id.btnChangeLink);
        btnchangelink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = !value;
                txtLink.setVisibility(value ? View.VISIBLE : View.GONE);
                imgView.setVisibility(!value ? View.VISIBLE : View.INVISIBLE);
            }
        });

        basket_button = (ImageButton) view.findViewById(R.id.basket_button);
        if (basketstate.equals("true")) {
            basket_button.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
        }else {
            basket_button.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
        }

        basket_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(basketstate.equals("true")) {
                    basketstate = "false";
                    basket_button.setImageResource(R.drawable.ic_baseline_add_shopping_cart_24);
                }else {
                    basketstate = "true";
                    basket_button.setImageResource(R.drawable.ic_baseline_shopping_cart_24);
                }
            }
        });

        favourite_button = (ImageButton) view.findViewById(R.id.favourite_button);
        if (favouritestate.equals("true")) {
            favourite_button.setImageResource(R.drawable.ic_baseline_favorite_24);
        }else {
            favourite_button.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        favourite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouritestate.equals("true")) {
                    favouritestate = "false";
                    favourite_button.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }else {
                    favouritestate = "true";
                    favourite_button.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        });

        delete_button = (ImageButton) view.findViewById(R.id.delete_button);
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Emin misiniz?");
                builder.setMessage("Eğer evet butonuna basıp tarif sayfasından çıkış yaparsanız tarif kalıcı olarak silinecek fakat tekrar silme butonuna basıp iptali seçerseniz silme işlemini iptal edebilirsiniz.");

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete = "true";
                        delete_button.setImageResource(R.drawable.ic_baseline_delete_forever_24);
                    }
                });

                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delete = "false";
                        delete_button.setImageResource(R.drawable.ic_baseline_delete_outline_24);
                    }
                });
                AlertDialog ad  = builder.create();
                ad.show();
            }
        });

        mealtype = (getArguments().getStringArrayList("RecipeList")).get(0);
        mealname = (getArguments().getStringArrayList("RecipeList")).get(1);
        link = (getArguments().getStringArrayList("RecipeList")).get(2);
        recipe = (getArguments().getStringArrayList("RecipeList")).get(3);
        ingredients = (getArguments().getStringArrayList("RecipeList")).get(4);
        calories = (getArguments().getStringArrayList("RecipeList")).get(5);
        cookingtime = (getArguments().getStringArrayList("RecipeList")).get(6);

        getArguments().getStringArrayList("RecipeList").clear();

        imgView = view.findViewById(R.id.imgViewforAdd);
        txtMealType = view.findViewById(R.id.texttype);
        txtMealName = view.findViewById(R.id.textrecipename);
        txtLink = view.findViewById(R.id.textlink);
        txtRecipe = view.findViewById(R.id.textrecipe);
        txtIngredients = view.findViewById(R.id.textingredients);
        txtCalories = view.findViewById(R.id.textcalories);
        txtCookingTime = view.findViewById(R.id.textcookingtime);

        txtLink.setVisibility(View.GONE);

        int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        } else {
                DownloadTask task = new DownloadTask();
                String[] url = new String[1];
                url[0] = link;
                task.execute(url);
        }

        if (mealtype != null) {
            txtMealType.setText(mealtype);
        }
        if (mealname != null) {
            txtMealName.setText(mealname);
        }
        if (link != null) {
            txtLink.setText(link);
        }
        if (recipe != null) {
            txtRecipe.setText(recipe);
        }
        if (ingredients != null) {
            txtIngredients.setText(ingredients);
        }
        if (calories != null) {
            txtCalories.setText(calories);
        }
        if (cookingtime != null) {
            txtCookingTime.setText(cookingtime);
        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private Bitmap scaleBitmap(String imagePath) {
        if (imagePath == null) {
            return null;
        }
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        float w = image.getWidth();
        float h = image.getHeight();
        int W = 600;
        int H = (int) ((h * W) / w);
        Bitmap b = Bitmap.createScaledBitmap(image, W, H, false);
        return b;
    }

    private boolean downloadFile(String strUrl, String imagePath) {
        try {
            URL url = new URL(strUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(imagePath);
            byte data[] = new byte[1024];
            int count;

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            return true;

        } catch (Exception e) {
            Log.e("downloadFile", "can't download " + strUrl, e);
            return false;
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... url) {
            String filename = "temp.jpg";
            String imagePath = (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)).toString() + "/" + filename;
            downloadFile(url[0], imagePath);
            Boolean x = downloadFile(url[0], imagePath);
            if (x) {
                return scaleBitmap(imagePath);
            }
            return scaleBitmap(null);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    DownloadTask task = new DownloadTask();
                    String[] url = new String[1];
                    url[0] = link;
                    task.execute(url);
            } else {
                Toast.makeText(getActivity(), "Dosya erişimine izin verilmedi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getMealType() {
        return txtMealType.getText().toString();
    }
    public String getMealName() {
        return txtMealName.getText().toString();
    }
    public String getLink() {
        return txtLink.getText().toString();
    }
    public String getRecipe() {
        return txtRecipe.getText().toString();
    }
    public String getIngredients() {
        return txtIngredients.getText().toString();
    }
    public String getCalories() {
        return txtCalories.getText().toString();
    }
    public String getCookingTime() {
        return txtCookingTime.getText().toString();
    }
    public String getFavouriteState() {
        return favouritestate;
    }
    public String getBasketState() {
        return basketstate;
    }
    public String getDeleteState() { return delete; }
}