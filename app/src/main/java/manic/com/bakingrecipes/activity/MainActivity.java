package manic.com.bakingrecipes.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import manic.com.bakingrecipes.R;
import manic.com.bakingrecipes.adapter.RecipeAdapter;
import manic.com.bakingrecipes.model.Recipe;
import manic.com.bakingrecipes.rest.RecipeApiService;
import manic.com.bakingrecipes.util.IdlingResource;
import manic.com.bakingrecipes.widget.DisplayIngredientsService;
import manic.com.bakingrecipes.util.GV;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = "MainActivity";

    @Nullable
    private IdlingResource mIdlingResource;

    private static Retrofit retrofit = null;
    @BindView(R.id.recipe_rv)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar pb;
    private RecipeAdapter mRecipeAdapter;

    private List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        mRecipeAdapter = new RecipeAdapter(this, this);
        mRecyclerView.setAdapter(mRecipeAdapter);
        mRecyclerView.setLayoutManager(layoutManager);

        getResponse();
    }

    public void getResponse() {
        pb.setVisibility(View.VISIBLE);
        OkHttpClient client = new OkHttpClient();
        IdlingResource.registerOkHttp(client);
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(getString(R.string.baseUrl))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
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
        GV.currentRecipe = recipe;
        DisplayIngredientsService.startActionUpdateIngredientsWidgets(this);
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(getString(R.string.recipe), recipe);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null){
           // mIdlingResource = new IdlingResource();
        }
        return mIdlingResource;
    }
}
