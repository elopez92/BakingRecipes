package manic.com.bakingrecipes.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.Arrays;
import java.util.List;

import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.model.Ingredient;
import manic.com.bakingrecipes.util.GV;
import manic.com.bakingrecipes.widget.IngredientWidgetFactory;

public class RecipeRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientWidgetFactory(this.getApplicationContext(), intent);
    }
}

class IngredientWidgetFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private Ingredient[] ingredients;
    private List<Ingredient> ingredientList;

    public IngredientWidgetFactory(Context mContext, Intent intent) {
        this.mContext = mContext;
        if (GV.currentRecipe != null) {
            ingredientList = GV.currentRecipe.getIngredients();
            ingredients = new Ingredient[(ingredientList.size())];
            ingredients = ingredientList.toArray(new Ingredient[ingredientList.size()]);
        }
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        if(GV.currentRecipe!=null) {
            ingredientList = GV.currentRecipe.getIngredients();
            ingredients = new Ingredient[(GV.currentRecipe.getIngredients().size())];
            ingredients = ingredientList.toArray(new Ingredient[ingredientList.size()]);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients == null ? 0 : ingredients.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
        views.setTextViewText(R.id.ingredient_tv, ingredients[position].getIngredient());
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
