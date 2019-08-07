package manic.com.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import java.util.List;

import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Ingredient;
import manic.com.bakingrecipes.util.GV;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = getListView(context);
        views.setTextViewText(R.id.widget_title_label, context.getString(R.string.widget_title_text, GV.currentRecipe.getName()));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getListView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);
        Intent intent = new Intent(context, RecipeRemoteViewsService.class);
        views.setRemoteAdapter(R.id.ingredients_lv_widget, intent);

        return views;
    }

    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        DisplayIngredientsService.startActionUpdateIngredientsWidgets(context);
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

