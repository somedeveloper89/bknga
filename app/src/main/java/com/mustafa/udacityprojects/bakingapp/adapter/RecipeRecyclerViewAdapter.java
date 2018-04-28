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

    private List<Recipe> mRecipeList;
    private Listener mListener;

    /**
     * Constructor for initialization.
     *
     * @param recipes the list of recipes to show.
     */
    public RecipeRecyclerViewAdapter(Listener listener, List<Recipe> recipes) {
        mListener = listener;
        mRecipeList = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Recipe recipe = mRecipeList.get(position);

        holder.mTextView.setText(recipe.getName());

        if (!recipe.getImageUrl().equals("")) {
            Picasso.get().load(recipe.getImageUrl()).into(holder.recipeImage);
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecipeClick(recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
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

    /**
     * Interface that notifies it's mListener(s).
     */
    public interface Listener {
        /**
         * Invoked when the user clicks a recipe.
         *
         * @param recipe The recipe that has been clicked.
         */
        void onRecipeClick(Recipe recipe);
    }

}
