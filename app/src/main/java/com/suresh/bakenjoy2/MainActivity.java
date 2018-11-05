package com.suresh.bakenjoy2;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.suresh.bakenjoy2.Utils.Constants;
import com.suresh.bakenjoy2.Utils.JsonUtils;
import com.suresh.bakenjoy2.Utils.NetworkUtils;
import com.suresh.bakenjoy2.adaptor.MainRecipeAdaptor;
import com.suresh.bakenjoy2.datamodel.MainRecipes;

import java.io.IOException;
import java.util.ArrayList;

import static com.suresh.bakenjoy2.Utils.Constants.SHARED_PREFERENCES;
import static com.suresh.bakenjoy2.Utils.NetworkUtils.recipeUrl;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MainRecipeAdaptor adapter;
    private ArrayList<MainRecipes> mainRecipesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recipe_RV);
        recyclerView.setHasFixedSize(true);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
        mainRecipesList = new ArrayList<>();

        new getRecipes().execute();


    }

    private void storeData(Context context, ArrayList<MainRecipes> responseFromHttpUrl) {
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(responseFromHttpUrl);
        prefsEditor.putString(Constants.SHARED_PREFERENCES, json);
        prefsEditor.commit();
    }

    public class getRecipes extends AsyncTask<Void, Void, ArrayList<MainRecipes>>{

        @Override
        protected ArrayList<MainRecipes> doInBackground(Void... voids) {
            try {

                return JsonUtils.getRecipeList(NetworkUtils.getResponseFromHttpUrl(recipeUrl()));
            } catch(IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MainRecipes> recipes) {
            mainRecipesList = recipes;

            storeData(MainActivity.this, recipes);

            if( mainRecipesList != null){
                adapter = new MainRecipeAdaptor(mainRecipesList, MainActivity.this);
                recyclerView.setAdapter(adapter);
            }
            super.onPostExecute(recipes);
        }
    }
}
