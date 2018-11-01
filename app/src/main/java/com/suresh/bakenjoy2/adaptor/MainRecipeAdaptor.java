package com.suresh.bakenjoy2.adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.suresh.bakenjoy2.DetailsActivity;
import com.suresh.bakenjoy2.R;
import com.suresh.bakenjoy2.Utils.Constants;
import com.suresh.bakenjoy2.datamodel.MainRecipes;

import java.util.ArrayList;

public class MainRecipeAdaptor extends RecyclerView.Adapter<MainRecipeAdaptor.ViewHolder> {

    private final ArrayList<MainRecipes> mRecipeList;
    private final Context mContext;


    public MainRecipeAdaptor(ArrayList<MainRecipes> mainRecipesList, Context context) {
        this.mRecipeList=mainRecipesList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public MainRecipeAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_recipe_adaptor, viewGroup,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecipeAdaptor.ViewHolder viewHolder, int i) {

        final MainRecipes recipes = mRecipeList.get(i);

        viewHolder.mainRecipeTV.setText(recipes.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create a bundle which will contain the parcelable recipe object which we can pass to an intent
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.RECIPE, recipes);
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mainRecipeIV;
        TextView mainRecipeTV;
        RelativeLayout mainRecipeRL;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mainRecipeIV = itemView.findViewById(R.id.main_recipe_image);
            mainRecipeTV = itemView.findViewById(R.id.main_recipe_tv);
            mainRecipeRL = itemView.findViewById(R.id.main_recipe_RL);
        }
    }
}
