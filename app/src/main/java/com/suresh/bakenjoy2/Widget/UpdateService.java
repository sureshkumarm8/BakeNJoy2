package com.suresh.bakenjoy2.Widget;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.suresh.bakenjoy2.MainActivity;
import com.suresh.bakenjoy2.R;
import com.suresh.bakenjoy2.Utils.JsonUtils;
import com.suresh.bakenjoy2.Utils.NetworkUtils;
import com.suresh.bakenjoy2.adaptor.MainRecipeAdaptor;
import com.suresh.bakenjoy2.datamodel.Ingredients;
import com.suresh.bakenjoy2.datamodel.MainRecipes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.suresh.bakenjoy2.Utils.NetworkUtils.recipeUrl;

public class UpdateService extends Service {
    public UpdateService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
//                    startJob();
                    new getRecipesW().execute();
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        t.start();


        return super.onStartCommand(intent, flags, startId);
    }

//    private void startJob() {
//
//        // generates random number
//        Random random = new Random();
//        int randomInt = random.nextInt(100);
//        String lastUpdate = "R: "+randomInt;
//
//        // Reaches the view on widget and displays the number
//        RemoteViews view = new RemoteViews(getPackageName(), R.layout.bake_app_widget);
//        view.setTextViewText(R.id.appwidget_text, lastUpdate);
//        ComponentName theWidget = new ComponentName(this, BakeAppWidget.class);
//        AppWidgetManager manager = AppWidgetManager.getInstance(this);
//        manager.updateAppWidget(theWidget, view);
//    }

    @SuppressLint("StaticFieldLeak")
    public class getRecipesW extends AsyncTask<Void, Void, ArrayList<MainRecipes>> {

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
//            int num = 0 + (int)Math.random()*(3-0) ;
            int num = (int) (Math.random() * (3));
            RemoteViews view = new RemoteViews(getPackageName(), R.layout.bake_app_widget);
            ArrayList<Ingredients> ingredients = recipes.get(num).getIngredients();
            String data= "";
            for(Ingredients ingredients1: ingredients){
                data+= ingredients1.getIngredient() + "\n";
            }
            view.setTextViewText(R.id.appwidget_text, recipes.get(num).getName());
            view.setTextViewText(R.id.ingredient_text,data);
            ComponentName theWidget = new ComponentName(getApplicationContext(), BakeAppWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
            manager.updateAppWidget(theWidget, view);
            super.onPostExecute(recipes);
        }
    }
}
