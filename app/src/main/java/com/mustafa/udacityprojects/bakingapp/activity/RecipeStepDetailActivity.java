/**
 * Copyright (C) 2018 Mustafa Kabaktepe
 */

package com.mustafa.udacityprojects.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.Toast;

import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.fragment.RecipeStepDetailFragment;
import com.mustafa.udacityprojects.bakingapp.model.Recipe;
import com.mustafa.udacityprojects.bakingapp.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe Step detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeStepListActivity}.
 */
public class RecipeStepDetailActivity extends AppCompatActivity implements
        RecipeStepDetailFragment.StepNavigationListener {
    public static final String EXTRA_CURRENT_RECIPE = "EXTRA_CURRENT_RECIPE";
    private static final String EXTRA_CURRENT_STEP_POSITION = "EXTRA_CURRENT_STEP_POSITION";

    private Recipe mRecipe;
    private Step mCurrentStep;

    @BindView(R.id.detail_toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mRecipe = getIntent().getParcelableExtra(EXTRA_CURRENT_RECIPE);
            mCurrentStep = getIntent()
                    .getParcelableExtra(RecipeStepDetailFragment.EXTRA_SELECTED_STEP);

            launchRecipeStepDetailFragmentWithStep();
        } else {
            mRecipe = savedInstanceState.getParcelable(EXTRA_CURRENT_RECIPE);
            int currentStepPosition = savedInstanceState.getInt(EXTRA_CURRENT_STEP_POSITION);
            if (currentStepPosition != -1) {
                mCurrentStep = mRecipe.getSteps().get(currentStepPosition);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_CURRENT_RECIPE, mRecipe);
        outState.putInt(EXTRA_CURRENT_STEP_POSITION, findIndexOfStep());
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTitle(mRecipe.getName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private int findIndexOfStep() {
        for (Step step : mRecipe.getSteps()) {
            if (step.getId() == mCurrentStep.getId()) {
                return mRecipe.getSteps().indexOf(step);
            }
        }
        return -1;
    }

    @Override
    public void onNextStep() {
        int currentPosition = findIndexOfStep();

        if (mRecipe.getSteps().size() - 1 > currentPosition) {
            mCurrentStep = mRecipe.getSteps().get(++currentPosition);
            launchRecipeStepDetailFragmentWithStep();
        } else {
            Toast.makeText(this, "This is the last step of this recipe", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPreviousStep() {
        int currentPosition = findIndexOfStep();

        if (currentPosition > 0) {
            mCurrentStep = mRecipe.getSteps().get(--currentPosition);
            launchRecipeStepDetailFragmentWithStep();
        } else {
            Toast.makeText(this, "This is the first step of this recipe", Toast.LENGTH_SHORT).show();
        }
    }

    private void launchRecipeStepDetailFragmentWithStep() {
        RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(mCurrentStep);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.recipestep_detail_container, fragment).commit();
    }
}
