package com.suresh.bakenjoy2.datamodel;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;

public class MainRecipes implements Parcelable {

    public long id;
    public String name;
    private ArrayList<Ingredients> ingredients;
    private ArrayList<Step> steps;
    private long servings;
    public String image;

    private MainRecipes(Parcel in) {
        id = in.readLong();
        name = in.readString();
        servings = in.readLong();
        image = in.readString();
        this.ingredients = in.readArrayList(getClass().getClassLoader());
        this.steps = in.readArrayList(getClass().getClassLoader());
    }

    public static final Creator<MainRecipes> CREATOR = new Creator<MainRecipes>() {
        @Override
        public MainRecipes createFromParcel(Parcel in) {
            return new MainRecipes(in);
        }

        @Override
        public MainRecipes[] newArray(int size) {
            return new MainRecipes[size];
        }
    };

    public MainRecipes() {

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredients> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public long getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeLong(servings);
        dest.writeString(image);
        dest.writeList(ingredients);
        dest.writeList(steps);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setServings(long servings) {
        this.servings = servings;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public void setIngredients(ArrayList<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }
}
