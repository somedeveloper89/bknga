package com.mustafa.udacityprojects.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Supplies Recipes to the view.
 */
public class RecipeRecyclerViewAdapter
        extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.ViewHolder> {

    private List<Recipe> recipeList;

    /**
     * Constructor for initialization.
     *
     * @param recipes the list of recipes to show.
     */
    public RecipeRecyclerViewAdapter(List<Recipe> recipes) {
        recipeList = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.mTextView.setText(recipe.getName());

        Context context = holder.mCardView.getContext();
        if (!recipe.getImageUrl().equals("")) {
            Picasso.with(context).load(recipe.getImageUrl()).into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_name_text)
        TextView mTextView;
        @BindView(R.id.card_view)
        CardView mCardView;
        @BindView(R.id.recipe_image)
        ImageView recipeImage;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
