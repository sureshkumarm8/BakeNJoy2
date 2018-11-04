package com.suresh.bakenjoy2.adaptor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.suresh.bakenjoy2.DetailsActivity;
import com.suresh.bakenjoy2.Fragments.VideoInstructionsFragment;
import com.suresh.bakenjoy2.R;
import com.suresh.bakenjoy2.Utils.Constants;
import com.suresh.bakenjoy2.datamodel.MainRecipes;
import com.suresh.bakenjoy2.datamodel.Step;

import java.util.ArrayList;

public class StepRecyclerAdapter extends RecyclerView.Adapter<StepRecyclerAdapter.ViewHolder>{


    private final ArrayList<Step> mStepList;
    private final Context mContext;
    private final MainRecipes mRecipe;

    public StepRecyclerAdapter(ArrayList<Step> stepList, Context context, MainRecipes recipe) {
        mStepList = stepList;
        mContext = context;
        mRecipe = recipe;
    }

    @NonNull
    @Override
    public StepRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.details_card_list_item, parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepRecyclerAdapter.ViewHolder holder, int position) {
        final Step step = mStepList.get(position);
        holder.mStepText.setText(step.getShortDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mStepArrow.setVisibility(View.VISIBLE);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.STEP, step);
                bundle.putParcelable(Constants.RECIPE, mRecipe);

                VideoInstructionsFragment videosInstructionFragment = new VideoInstructionsFragment();
                videosInstructionFragment.setArguments(bundle);

                android.support.v4.app.FragmentManager fragmentManager =((DetailsActivity)mContext).getSupportFragmentManager();
                if(fragmentManager != null) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.details_frame_layout, videosInstructionFragment)
                            .addToBackStack(Constants.INSTRUCTIONS_FRAGMENT)
                            .commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView mStepText;
        TextView mStepArrow;
        ViewHolder(View itemView) {
            super(itemView);
           mStepText = itemView.findViewById(R.id.list_recipe_step_tv);
           mStepArrow = itemView.findViewById(R.id.list_recipe_step_arrow);
        }
    }
}
