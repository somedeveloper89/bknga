/**
 * Copyright (C) 2018 Mustafa Kabaktepe
 */

package com.mustafa.udacityprojects.bakingapp.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.widget.BakingWidgetProvider;

/**
 * IntentService that supplies data to the BakingWidget.
 */
public class BakingWidgetService extends IntentService {
    public static final String ACTION_RECIPE =
            "com.mustafa.udacityprojects.bakingapp.widget" + ".action.recipe";
    private static final String TAG = "BakingWidgetService";

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     */
    public BakingWidgetService() {
        super(TAG);
    }

    /**
     * Starts the intent service with action ACTION_RECIPE.
     *
     * @param context the context.
     */
    public static void startNextRecipe(Context context) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_RECIPE);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();

            assert action != null;
            if (action.equals(ACTION_RECIPE)) {
                getRecipeWidgetDescription();
            }
        }
    }

    private void getRecipeWidgetDescription() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String titleKey = getApplicationContext().getResources()
                .getString(R.string.current_recipe_title);
        String descriptionKey = getApplicationContext().getResources()
                .getString(R.string.current_recipe_ingredient_description);
        String noRecipeTitle = getApplicationContext().getResources()
                .getString(R.string.no_recipe_selected);
        String noRecipeDescription = getApplicationContext().getResources()
                .getString(R.string.no_recipe_selected_instruction);
        String title = preferences.getString(titleKey, noRecipeTitle);
        String ingredientDescription = preferences.getString(descriptionKey, noRecipeDescription);

        handleBakingWidgetUpdate(title, ingredientDescription);
    }

    private void handleBakingWidgetUpdate(String title, String description) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, BakingWidgetProvider.class));

        BakingWidgetProvider
                .updateBakingWidgets(this, appWidgetManager, appWidgetIds, title, description);
    }
}
