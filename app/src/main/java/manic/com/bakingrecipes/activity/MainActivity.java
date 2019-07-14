package manic.com.bakingrecipes.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.adapter.RecipeAdapter;
import manic.com.bakingrecipes.model.Recipe;
import manic.com.bakingrecipes.rest.RecipeApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = "MainActivity";

    private static Retrofit retrofit = null;
    @BindView(R.id.recipe_rv) RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar pb;
    private RecipeAdapter mRecipeAdapter;

    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        getResponse();
    }

    private void getResponse() {
        pb.setVisibility(View.VISIBLE);
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.baseUrl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        RecipeApiService api = retrofit.create(RecipeApiService.class);
        Call<List<Recipe>> call = api.getJson();

        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Log.d(TAG, "onResponse: " + response.body().get(0).getName());
                pb.setVisibility(View.INVISIBLE);
                recipeList = response.body();
                mRecipeAdapter.setRecipeList(response.body());

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }


    @Override
    public void onClick(Recipe recipe) {
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(getString(R.string.recipe), recipe);
        startActivity(intent);
    }
}
