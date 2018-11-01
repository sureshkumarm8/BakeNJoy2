package com.suresh.bakenjoy2.Utils;



import com.suresh.bakenjoy2.datamodel.MainRecipes;
import com.suresh.bakenjoy2.datamodel.Step;
import com.suresh.bakenjoy2.datamodel.Ingredients;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static ArrayList<MainRecipes> recipeList;

    public static ArrayList<MainRecipes> getRecipeList(String json){

        //create a MainRecipes list, check if its clear when function starts as this will be called in the first load.
        recipeList = new ArrayList<>();
        if(recipeList.size() != 0){
            recipeList.clear();
        }

        //get the results from the jsonArray, create new movie objects for the results and add to recipe array list.
        try {
            JSONArray recipeArray = new JSONArray(json);
            for(int i = 0; i < recipeArray.length(); i++) {
                JSONObject recipeJSON = recipeArray.optJSONObject(i);
                MainRecipes newRecipe = getRecipe(recipeJSON);
                recipeList.add(newRecipe);
            }

        } catch(JSONException e) {
            e.printStackTrace();
        }

        return recipeList;
    }

    private static MainRecipes getRecipe(JSONObject recipe) {

        //create a new recipe object

        MainRecipes newRecipe = new MainRecipes();

        //create an arraylist of ingredients and an arraylist of steps to add to our recipe
        ArrayList<Ingredients> ingredientsList = new ArrayList<>();
        ArrayList<Step> stepsList = new ArrayList<>();

        //set the recipe variables
        newRecipe.setName(recipe.optString(Constants.NAME));
        newRecipe.setId(recipe.optLong(Constants.ID));
        newRecipe.setServings(recipe.optLong(Constants.SERVINGS));
        newRecipe.setImage(recipe.optString(Constants.IMAGE));

        //loop through ingredients array and create an ingredient object add to ingredients array
        JSONArray ingredientJSONArray = recipe.optJSONArray(Constants.INGREDIENTS);
        for(int i = 0; i < ingredientJSONArray.length() ; i++) {
            JSONObject ingredientsJSON = ingredientJSONArray.optJSONObject(i);
            Ingredients ingredients = new Ingredients();
            ingredients.setIngredient(ingredientsJSON.optString(Constants.INGREDIENT));
            ingredients.setMeasure(ingredientsJSON.optString(Constants.MEASURE));
            ingredients.setQuantity(ingredientsJSON.optLong(Constants.QUANTITY));
            ingredientsList.add(ingredients);
        }
        newRecipe.setIngredients(ingredientsList);

        //loop through steps array and create an step object add to step array
        JSONArray stepsJSONArray = recipe.optJSONArray(Constants.STEPS);
        for(int i = 0; i < stepsJSONArray.length() ; i++) {
            JSONObject stepJSON = stepsJSONArray.optJSONObject(i);
            Step step = new Step();
            step.setId(stepJSON.optInt(Constants.ID));
            step.setShortDescription(stepJSON.optString(Constants.SHORT_DESCR));
            step.setDescription(stepJSON.optString(Constants.DESCRIPTION));
            step.setVideoURL(stepJSON.optString(Constants.VIDEO_URL));
            step.setThumbnailURL(stepJSON.optString(Constants.THUMBNAIL_URL));
            stepsList.add(step);
        }
        newRecipe.setSteps(stepsList);

        return newRecipe;
    }

}
