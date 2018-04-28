package com.mustafa.udacityprojects.bakingapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.fragment.RecipeStepDetailFragment;
import com.mustafa.udacityprojects.bakingapp.model.Recipe;
import com.mustafa.udacityprojects.bakingapp.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a list of Recipe Steps. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeStepDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeStepListActivity extends AppCompatActivity implements
        RecipeStepDetailFragment.StepNavigationListener{

    public static final String EXTRA_CURRENT_STEP_POSITION = "EXTRA_CURRENT_STEP_POSITION";

    private boolean mTwoPane;
    private Recipe mRecipe;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.recipestep_list)
    RecyclerView mRecyclerView;
    private SimpleItemRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipestep_list);
        ButterKnife.bind(this);

        mRecipe = null;
        int lastStepPosition = -1;

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RecipeActivity.EXTRA_RECIPE);
            lastStepPosition = savedInstanceState.getInt(EXTRA_CURRENT_STEP_POSITION);

        } else if (getIntent().hasExtra(RecipeActivity.EXTRA_RECIPE)) {
            mRecipe = getIntent().getParcelableExtra(RecipeActivity.EXTRA_RECIPE);
        }

        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipestep_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        assert mRecipe != null;

        mAdapter = new SimpleItemRecyclerViewAdapter(this, mRecipe, mTwoPane);

        mRecyclerView.setAdapter(mAdapter);

        // only applicable when in twopane mode
        if (lastStepPosition != -1 && mTwoPane) {
            mAdapter.loadStep(lastStepPosition);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RecipeActivity.EXTRA_RECIPE, mRecipe);
        outState.putInt(EXTRA_CURRENT_STEP_POSITION, mAdapter.getCurrentStepPosition());
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

    @Override
    public void onNextStep() {
        mAdapter.onNextStep();
    }

    @Override
    public void onPreviousStep() {
        mAdapter.onPreviousStep();
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements
            RecipeStepDetailFragment.StepNavigationListener{

        private final RecipeStepListActivity mParentActivity;
        private final List<Step> mRecipeStepDescriptions;
        private final boolean mTwoPane;
        private final Recipe mCurrentRecipe;

        private Step mSelectedStep;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedStep = (Step) view.getTag();
                if (mTwoPane) {
                    launchRecipeStepDetailFragmentWithStep();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeStepDetailActivity.class);
                    intent.putExtra(RecipeStepDetailActivity.EXTRA_CURRENT_RECIPE, mCurrentRecipe);
                    intent.putExtra(RecipeStepDetailFragment.EXTRA_SELECTED_STEP, mSelectedStep);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeStepListActivity parent,
                                      Recipe recipe, boolean twoPane) {
            mRecipeStepDescriptions = recipe.getSteps();
            mCurrentRecipe = recipe;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipestep_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Step currentStep = mRecipeStepDescriptions.get(position);

            holder.mIdView.setText(currentStep.getShortDescription());
            holder.itemView.setTag(currentStep);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mRecipeStepDescriptions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.header)
            TextView mIdView;

            ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        private int findIndexOfStep() {

            if (mSelectedStep != null) {
                for (Step step : mRecipeStepDescriptions) {
                    if (step.getId() == mSelectedStep.getId()) {
                        return mRecipeStepDescriptions.indexOf(step);
                    }
                }
            }

            return -1;
        }

        public int getCurrentStepPosition() {
            return findIndexOfStep();
        }

        public void loadStep(int position) {
            mSelectedStep = mRecipeStepDescriptions.get(position);
            launchRecipeStepDetailFragmentWithStep();
        }

        @Override
        public void onNextStep() {
            int currentPosition = findIndexOfStep();

            if (mRecipeStepDescriptions.size() - 1 > currentPosition) {
                mSelectedStep = mRecipeStepDescriptions.get(++currentPosition);
                launchRecipeStepDetailFragmentWithStep();
            } else {
                Toast.makeText(mParentActivity, "This is the last step of this recipe", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPreviousStep() {
            int currentPosition = findIndexOfStep();

            if (currentPosition > 0) {
                mSelectedStep = mRecipeStepDescriptions.get(--currentPosition);
                launchRecipeStepDetailFragmentWithStep();
            } else {
                Toast.makeText(mParentActivity, "This is the first step of this recipe", Toast.LENGTH_SHORT).show();
            }
        }

        private void launchRecipeStepDetailFragmentWithStep() {
            RecipeStepDetailFragment fragment = RecipeStepDetailFragment.newInstance(mSelectedStep);
            mParentActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipestep_detail_container, fragment).commit();
        }
    }
}
