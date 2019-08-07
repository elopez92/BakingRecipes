package manic.com.bakingrecipes.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.List;

import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Ingredient;
import manic.com.bakingrecipes.util.GV;

public class DisplayIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_INGREDIETNS_WIDGETS = "manic.com.bakingrecipes.update_ingredients_widget";

    public DisplayIngredientsService() {
        super("DisplayIngredientsService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(ACTION_UPDATE_INGREDIETNS_WIDGETS.equals(action)){
                handleActionUpdateIngredientsWidgets();
            }
        }
    }

    public static void startActionUpdateIngredientsWidgets(Context context){
        if(GV.currentRecipe != null) {
            Intent intent = new Intent(context, DisplayIngredientsService.class);
            intent.setAction(ACTION_UPDATE_INGREDIETNS_WIDGETS);
            context.startService(intent);
        }
    }

    private void handleActionUpdateIngredientsWidgets() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.ingredients_lv_widget);

        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);
    }
}
