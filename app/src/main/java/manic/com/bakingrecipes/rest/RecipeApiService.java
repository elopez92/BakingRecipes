package manic.com.bakingrecipes.rest;

import java.util.List;

import manic.com.bakingrecipes.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeApiService {

    String RECIPE_JSON = "baking.json";

    @GET(RECIPE_JSON)
    Call<List<Recipe>> getJson();
}
