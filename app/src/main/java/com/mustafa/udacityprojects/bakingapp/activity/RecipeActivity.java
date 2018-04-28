package com.mustafa.udacityprojects.bakingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.adapter.RecipeRecyclerViewAdapter;
import com.mustafa.udacityprojects.bakingapp.model.Recipe;
import com.mustafa.udacityprojects.bakingapp.service.BakingAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeActivity extends AppCompatActivity implements Callback<List<Recipe>>, RecipeRecyclerViewAdapter.Listener {

    private static final String TAG = RecipeActivity.class.getSimpleName();

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    public static final String EXTRA_RECIPE = "EXTRA_RECIPE";

    @BindView(R.id.recipe_loading_progress)
    ProgressBar mProgressBar;
    @BindView(R.id.recipe_loading_textview)
    TextView mMessage;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    private RecipeRecyclerViewAdapter mRecipeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        setTitle(R.string.recipe_overview_title);
        ButterKnife.bind(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        retrieveRecipes();
    }

    public void retrieveRecipes() {
        loading();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        BakingAPI mBakingAPI = retrofit.create(BakingAPI.class);
        Call<List<Recipe>> call = mBakingAPI.getRecipes();
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        loadingFinished();

        if (response.isSuccessful()) {
            List<Recipe> recipeList = response.body();
            assert recipeList != null;

            for (Recipe recipe : recipeList) {
                Log.d(TAG, "onResponse: " + recipe.getName());
            }

            mRecipeRecyclerViewAdapter = new RecipeRecyclerViewAdapter(this, recipeList);
            mRecyclerView.setAdapter(mRecipeRecyclerViewAdapter);
        } else {
            Log.d(TAG, "onResponse: response not successful");
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        loadingFinished();
        Log.e(TAG, "onFailure: " + t.getMessage());
    }

    private void loading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mMessage.setVisibility(View.VISIBLE);
    }

    private void loadingFinished() {
        mProgressBar.setVisibility(View.GONE);
        mMessage.setVisibility(View.GONE);
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeStepListActivity.class);
        intent.putExtra(EXTRA_RECIPE, recipe);
        startActivity(intent);
    }
}
