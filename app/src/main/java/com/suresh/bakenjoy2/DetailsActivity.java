package com.suresh.bakenjoy2;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.suresh.bakenjoy2.Fragments.IngredientsFragment;
import com.suresh.bakenjoy2.Fragments.InstructionsFragment;
import com.suresh.bakenjoy2.Utils.Constants;

public class DetailsActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private Bundle mBundle;
    private boolean mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mFragmentManager = getSupportFragmentManager();
        mTabLayout = findViewById(R.id.tablet_layout) != null;

        if(savedInstanceState != null) {
            mBundle = savedInstanceState.getBundle(Constants.RECIPE_BUNDLE);
        }else {
            Intent sentIntent = getIntent();
            mBundle = sentIntent.getExtras();
        }

        if(mFragmentManager.findFragmentById(R.id.details_frame_layout) == null){
            if(mTabLayout) tabLayout();
            else phoneLayout();
        }

    }

    private void tabLayout() {
        if(mBundle != null){
            InstructionsFragment recipeMenuFragment = new InstructionsFragment();
            recipeMenuFragment.setArguments(mBundle);
            mFragmentManager.beginTransaction()
                    .add(R.id.tablet_menu, recipeMenuFragment, Constants.INSTRUCTIONS_FRAGMENT)
                    .commit();

            IngredientsFragment ingredientsDetailFragment = new IngredientsFragment();
            ingredientsDetailFragment.setArguments(mBundle);
            mFragmentManager.beginTransaction()
                    .replace(R.id.details_frame_layout, ingredientsDetailFragment, Constants.INGREDIENT_FRAGMENT)
                    .commit();
        }
    }

    private void phoneLayout() {
        if(mBundle != null){
            InstructionsFragment recipeMenuFragment = new InstructionsFragment();
            recipeMenuFragment.setArguments(mBundle);
            mFragmentManager.beginTransaction()
                    .add(R.id.details_frame_layout, recipeMenuFragment, Constants.INSTRUCTIONS_FRAGMENT)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mBundle != null){
            outState.putBundle(Constants.RECIPE_BUNDLE , mBundle);
        }
    }

}
