package com.suresh.bakenjoy2.adaptor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suresh.bakenjoy2.R;
import com.suresh.bakenjoy2.datamodel.Ingredients;

import java.util.ArrayList;

public class IngredientsAdaptor extends RecyclerView.Adapter<IngredientsAdaptor.ViewHolder> {
    private final Context mcontext;
    private ArrayList<Ingredients> mIngredientsList;


    public IngredientsAdaptor(ArrayList<Ingredients> ingredients, Context context) {
        this.mIngredientsList=ingredients;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public IngredientsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredient_card_item, viewGroup,false);
        return new  ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsAdaptor.ViewHolder viewHolder, int i) {
        Ingredients ingredients = mIngredientsList.get(i);
        viewHolder.ingredient_card_TV.setText(ingredients.getIngredient());
        viewHolder.quantity_card_TV.setText(String.valueOf( ingredients.getQuantity()));
        viewHolder.measure_card_TV.setText(ingredients.getMeasure());
    }

    @Override
    public int getItemCount() {
       return mIngredientsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ingredient_card_TV;
        TextView quantity_card_TV;
        TextView measure_card_TV;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredient_card_TV = itemView.findViewById(R.id.ingredient_card_tv);
            quantity_card_TV = itemView.findViewById(R.id.quantity_card_tv);
            measure_card_TV = itemView.findViewById(R.id.measure_card_tv);
        }
    }
}
