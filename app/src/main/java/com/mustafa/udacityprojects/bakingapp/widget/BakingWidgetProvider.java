/**
 * Copyright (C) 2018 Mustafa Kabaktepe
 */

package com.mustafa.udacityprojects.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.mustafa.udacityprojects.bakingapp.R;
import com.mustafa.udacityprojects.bakingapp.activity.RecipeActivity;
import com.mustafa.udacityprojects.bakingapp.service.BakingWidgetService;

import static com.mustafa.udacityprojects.bakingapp.service.BakingWidgetService.ACTION_RECIPE;

/**
 * Implementation of App Widget functionality.
 */
public class BakingWidgetProvider extends AppWidgetProvider {

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                        int appWidgetId, String title, String description) {

        Intent intent = new Intent(context, RecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_widget);
        views.setTextViewText(R.id.appwidget_recipe_title, title);
        views.setTextViewText(R.id.appwidget_ingredients, description);

        views.setOnClickPendingIntent(R.id.appwidget_ingredients, pendingIntent);

        Intent refreshIntent = new Intent(context, BakingWidgetService.class);
        refreshIntent.setAction(ACTION_RECIPE);

        views.setOnClickPendingIntent(R.id.appwidget_refresh_button,
                PendingIntent.getService(context, 0, refreshIntent, 0));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    /**
     * Updates al the widgets with new data.
     *
     * @param context          the context.
     * @param appWidgetManager the appWidgetManager.
     * @param appWidgetIds     the ids of the widgets.
     * @param title            the title of the selected recipe.
     * @param description      the description of the selected recipe.
     */
    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           int[] appWidgetIds, String title, String description) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, title, description);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        BakingWidgetService.startNextRecipe(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

