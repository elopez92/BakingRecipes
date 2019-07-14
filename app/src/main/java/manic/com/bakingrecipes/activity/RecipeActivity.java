package manic.com.bakingrecipes.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.fragment.MasterStepListFragment;
import manic.com.bakingrecipes.model.Ingredient;
import manic.com.bakingrecipes.model.Recipe;
import manic.com.bakingrecipes.model.Step;

public class RecipeActivity extends AppCompatActivity implements MasterStepListFragment.OnCardClickListener {

    private Recipe recipe;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_item);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(getString(R.string.recipe));
        if(null != recipe){
            ingredientList = recipe.getIngredients();
            stepList = recipe.getSteps();
        }

        ArrayList<Step> ingredients = new ArrayList<>(ingredientList.size());
        ingredients.addAll(stepList);

        ArrayList<Step> steps = new ArrayList<>(stepList.size());
        steps.addAll(stepList);

        Bundle ingredientFrag = new Bundle();
        ingredientFrag.putParcelableArrayList(getString(R.string.ingredient_list), ingredients);

        Bundle stepFrag = new Bundle();
        stepFrag.putParcelableArrayList(getString(R.string.step_list), steps);

        MasterStepListFragment msf = new MasterStepListFragment();
        msf.setArguments(stepFrag);
    }

    @Override
    public void onClick(int position) {

    }
}
