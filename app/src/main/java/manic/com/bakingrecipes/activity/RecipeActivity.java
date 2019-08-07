package manic.com.bakingrecipes.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.fragment.DetailFragment;
import manic.com.bakingrecipes.fragment.MasterStepListFragment;
import manic.com.bakingrecipes.model.Ingredient;
import manic.com.bakingrecipes.model.Recipe;
import manic.com.bakingrecipes.model.Step;
import manic.com.bakingrecipes.util.GV;

public class RecipeActivity extends AppCompatActivity implements MasterStepListFragment.OnCardClickListener {

    private Recipe recipe;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(getString(R.string.recipe));
        if(null != recipe) {
            setTitle(recipe.getName());
            ingredientList = recipe.getIngredients();
            stepList = recipe.getSteps();
        }

        ArrayList<Step> steps = new ArrayList<>();
        steps.addAll(stepList);

        MasterStepListFragment msf = new MasterStepListFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(getString(R.string.step_key), steps);
        msf.setArguments(arguments);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLayout, msf).commit();


        if(findViewById(R.id.detail_container) != null){
            GV.twoPane = true;
        }

    }


    @Override
    public void onClick(Step step) {

        if(GV.twoPane){
            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.step_key),step);
            detailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment).commit();
        } else{
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(getString(R.string.step_key), step);
            intent.putExtra(getString(R.string.recipe_key), recipe);
            startActivity(intent);
        }
    }
}
